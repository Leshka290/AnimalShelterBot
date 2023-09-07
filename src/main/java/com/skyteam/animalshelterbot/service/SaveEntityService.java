package com.skyteam.animalshelterbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.pengrad.telegrambot.response.SendResponse;
import com.skyteam.animalshelterbot.exception.ClientNotFoundException;
import com.skyteam.animalshelterbot.listener.TelegramBotUpdatesListener;
import com.skyteam.animalshelterbot.listener.constants.PetType;
import com.skyteam.animalshelterbot.model.Adopter;
import com.skyteam.animalshelterbot.model.Client;
import com.skyteam.animalshelterbot.model.Report.CatReport;
import com.skyteam.animalshelterbot.model.Report.DogReport;
import com.skyteam.animalshelterbot.model.Volunteer;
import com.skyteam.animalshelterbot.model.images.CatImage;
import com.skyteam.animalshelterbot.model.images.DogImage;
import com.skyteam.animalshelterbot.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static com.skyteam.animalshelterbot.listener.constants.PetType.CAT;
import static com.skyteam.animalshelterbot.listener.constants.PetType.DOG;

/**
 * Сервис сохранения сущностей в БД
 */
@Service
public class SaveEntityService {

    /**
     * Переменная для реализации логирования приложения.
     */
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private final TelegramBot telegramBot = new TelegramBot("BOT_TOKEN");

    private final VolunteerService volunteerService;
    private final ClientRepository clientRepository;
    private final AdopterRepository adopterRepository;
    private final CatReportRepository catReportRepository;
    private final DogReportRepository dogReportRepository;
    private final CatImageRepository catImageRepository;
    private final DogImageRepository dogImageRepository;

    private final MenuBuilderService menuBuilderService;

    /**
     * Подключение файла пропертис с сообщениями
     */
    private final ResourceBundle messagesBundle = ResourceBundle.getBundle("bot_messages");

    public SaveEntityService(VolunteerService volunteerService,
                             ClientRepository clientRepository, AdopterRepository adopterRepository,
                             CatReportRepository catReportRepository, DogReportRepository dogReportRepository,
                             CatImageRepository catImageRepository, DogImageRepository dogImageRepository,
                              MenuBuilderService menuBuilderService) {
        this.volunteerService = volunteerService;
        this.clientRepository = clientRepository;
        this.adopterRepository = adopterRepository;
        this.catReportRepository = catReportRepository;
        this.dogReportRepository = dogReportRepository;
        this.catImageRepository = catImageRepository;
        this.dogImageRepository = dogImageRepository;
        this.menuBuilderService = menuBuilderService;
    }

    private void sendMessage(SendMessage message) {
        SendResponse response = telegramBot.execute(message);
        if (response != null && !response.isOk()) {
            logger.warn("Message was not sent: {}, error code: {}", message, response.errorCode());
        }
    }

    /**
     * Метод сохранения клиента (гость) в БД таблицу client
     */
    public void saveClient(long chatId, PetType lastPetType) {
        Client client = clientRepository.findByChatId(chatId);
        if (client == null) {
            client = new Client(chatId, lastPetType);
        } else {
            client.setLastPetType(lastPetType);
        }
        clientRepository.save(client);
    }

    /**
     * Метод сохранения усыновителя
     */
    public void saveAdopter(Update update) {
        if (update.message().contact() != null) {
            String firstName = update.message().contact().firstName();
            String lastName = update.message().contact().lastName();
            String username = update.message().chat().username();
            String phone = update.message().contact().phoneNumber();
            long chatId = update.message().chat().id();

            Adopter adopter = adopterRepository.findByChatId(chatId);
            if (adopter == null) {
                Client client = clientRepository.findByChatId(chatId);
                if (client == null) {
                    throw new ClientNotFoundException(chatId);
                }
                adopter = new Adopter(firstName, lastName, username, phone, chatId, client.getLastPetType());
                adopterRepository.save(adopter);
                SendMessage message = new SendMessage(chatId, messagesBundle.getString("SAVE_ADOPTER_SUCCESS"));
                sendMessage(message.replyMarkup(menuBuilderService.mainMenuKeyboardButtons()));
            } else {
                SendMessage message = new SendMessage(chatId, "ADOPTER_ALREADY_EXISTS");
                sendMessage(message.replyMarkup(menuBuilderService.mainMenuKeyboardButtons()));
            }
        }
    }

    /**
     * Метод сохранения отчета
     */
    public void saveAdoptionReport(long chatId) {
        Adopter adopterId = adopterRepository.findByChatId(chatId);
        LocalDate date = LocalDate.now();

        if (adopterId.getPetType().equals(DOG)) {
            DogReport adoptionReport = dogReportRepository.findDogReportByAdopterId(adopterId);

            if (adoptionReport == null) {
                adoptionReport = new DogReport(date, null, null, null);
                dogReportRepository.save(adoptionReport);
                SendMessage requestPhotoMessage = new SendMessage(chatId, messagesBundle.getString("PHOTO_WAITING_MESSAGE"));
                requestPhotoMessage.replyMarkup(menuBuilderService.createButtonsReport());
                sendMessage(requestPhotoMessage);
            } else {
                SendMessage message = new SendMessage(chatId, messagesBundle.getString("ADOPTION_REPORT_ALREADY_EXIST"));
                sendMessage(message);
            }
        }
        if (adopterId.getPetType().equals(CAT)) {
            CatReport adoptionReport = catReportRepository.findCatReportByAdopterId(adopterId);

            if (adoptionReport == null) {
                adoptionReport = new CatReport(date, null, null, null);
                catReportRepository.save(adoptionReport);
                SendMessage requestPhotoMessage = new SendMessage(chatId, messagesBundle.getString("PHOTO_WAITING_MESSAGE"));
                requestPhotoMessage.replyMarkup(menuBuilderService.createButtonsReport());
                sendMessage(requestPhotoMessage);
            } else {
                SendMessage message = new SendMessage(chatId, messagesBundle.getString("ADOPTION_REPORT_ALREADY_EXIST"));
                sendMessage(message);
            }
        }
    }

    /**
     * Метод фото в отчет
     */
    public void saveAdoptionReportPhoto(Update update) {
        long chatId = update.message().chat().id();
        Adopter adopter = adopterRepository.findByChatId(chatId);
//        Report adoptionReport;
        if (adopter.getPetType().equals(CAT)) {
            CatReport adoptionReport = catReportRepository.findCatReportByAdopterId(adopter);

            if (update.message().photo() != null) {
                byte[] image = getPhoto(update);
                CatImage images = new CatImage(image);
                catImageRepository.save(images);
                catReportRepository.save(adoptionReport);
                SendMessage savePhotoMessage = new SendMessage(chatId, messagesBundle.getString("PHOTO_SAVED_MESSAGE"));
                sendMessage(savePhotoMessage);
            }
        } else if (adopter.getPetType().equals(DOG)) {
            DogReport adoptionReport = dogReportRepository.findDogReportByAdopterId(adopter);

            if (update.message().photo() != null) {
                byte[] image = getPhoto(update);
                DogImage images = new DogImage(image);
                dogImageRepository.save(images);
                dogReportRepository.save(adoptionReport);
                SendMessage savePhotoMessage = new SendMessage(chatId, messagesBundle.getString("PHOTO_SAVED_MESSAGE"));
                sendMessage(savePhotoMessage);
            }
        }
    }

    /**
     * Метод для получения фото
     */
    public byte[] getPhoto(Update update) {
        if (update.message().photo() != null) {
            PhotoSize[] photoSizes = update.message().photo();
            for (PhotoSize photoSize : photoSizes) {
                GetFile getFile = new GetFile(photoSize.fileId());
                GetFileResponse getFileResponse = telegramBot.execute(getFile);
                if (getFileResponse.isOk()) {
                    File file = getFileResponse.file();
                    try {
                        return telegramBot.getFileContent(file);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Метод для получения отчета о диете
     */
    public void saveAdoptionReportDiet(Update update) {
        long chatId = update.message().chat().id();
        Adopter adopter = adopterRepository.findByChatId(chatId);

//        Report adoptionReport;
        if (adopter.getPetType().equals(CAT)) {
            CatReport adoptionReport = catReportRepository.findCatReportByAdopterId(adopter);
            String diet = adoptionReport.getDiet();
            if (diet == null) {
                String newDiet = update.message().text();
                adoptionReport.setDiet(newDiet);
                catReportRepository.save(adoptionReport);
                SendMessage saveDietMessage = new SendMessage(chatId, messagesBundle.getString("DIET_SAVED"));
                sendMessage(saveDietMessage);
            }
        } else if (adopter.getPetType().equals(DOG)) {
            DogReport adoptionReport = dogReportRepository.findDogReportByAdopterId(adopter);
            String diet = adoptionReport.getDiet();
            if (diet == null) {
                String newDiet = update.message().text();
                adoptionReport.setDiet(newDiet);
                dogReportRepository.save(adoptionReport);
                SendMessage saveDietMessage = new SendMessage(chatId, messagesBundle.getString("DIET_SAVED"));
                sendMessage(saveDietMessage);
            }
        }
    }

    /**
     * Метод для сохранения отчета об изменениях в поведении
     */
    public void saveAdoptionReportBehaviorChange(Update update) {
        long chatId = update.message().chat().id();
        Adopter adopter = adopterRepository.findByChatId(chatId);
//        Report adoptionReport;
        if (adopter.getPetType().equals(CAT)) {
            CatReport adoptionReport = catReportRepository.findCatReportByAdopterId(adopter);
            String behaviorChane = adoptionReport.getBehavioralChanges();
            if (behaviorChane == null) {
                String newBehaviorChane = update.message().text();
                adoptionReport.setBehavioralChanges(newBehaviorChane);
                catReportRepository.save(adoptionReport);
                SendMessage saveBehaviorChangeMessage = new SendMessage(chatId, messagesBundle.getString("BEHAVIOR_CHANGE_SAVED"));
                sendMessage(saveBehaviorChangeMessage);
            }
        } else if (adopter.getPetType().equals(DOG)) {
            DogReport adoptionReport = dogReportRepository.findDogReportByAdopterId(adopter);
            String behaviorChane = adoptionReport.getBehavioralChanges();
            if (behaviorChane == null) {
                String newBehaviorChane = update.message().text();
                adoptionReport.setBehavioralChanges(newBehaviorChane);
                dogReportRepository.save(adoptionReport);
                SendMessage saveBehaviorChangeMessage = new SendMessage(chatId, messagesBundle.getString("BEHAVIOR_CHANGE_SAVED"));
                sendMessage(saveBehaviorChangeMessage);
            }
        }
    }

    /**
     * Метод для получения отчета о здоровье питомца
     */
    public void saveAdoptionReportWellBeing(Update update) {
        long chatId = update.message().chat().id();
        Adopter adopter = adopterRepository.findByChatId(chatId);

        if (adopter.getPetType().equals(CAT)) {
            CatReport adoptionReport = catReportRepository.findCatReportByAdopterId(adopter);
            String wellBeing = adoptionReport.getCommonDescriptionOfStatus();
            if (wellBeing == null) {
                String newWellBeing = update.message().text();
                adoptionReport.setCommonDescriptionOfStatus(newWellBeing);
                catReportRepository.save(adoptionReport);
                SendMessage saveWellBeingMessage = new SendMessage(chatId, messagesBundle.getString("WELL_BEING_SAVED"));
                sendMessage(saveWellBeingMessage);
            }
        } else if (adopter.getPetType().equals(DOG)) {
            DogReport adoptionReport = dogReportRepository.findDogReportByAdopterId(adopter);
            String wellBeing = adoptionReport.getCommonDescriptionOfStatus();
            if (wellBeing == null) {
                String newWellBeing = update.message().text();
                adoptionReport.setCommonDescriptionOfStatus(newWellBeing);
                dogReportRepository.save(adoptionReport);
                SendMessage saveWellBeingMessage = new SendMessage(chatId, messagesBundle.getString("WELL_BEING_SAVED"));
                sendMessage(saveWellBeingMessage);

            }
        }
    }

    /**
     * Метод вызова волонтера
     */
    public void callVolunteer(Update update) {
        String userId = ""; // client chat_id or username
        long chatId; // volunteer's chat_id
        userId += update.message().from().id();
        logger.info("UserId = {}", userId);
        Volunteer volunteer = volunteerService.findFreeVolunteer();
        if (volunteer == null) {
            chatId = Long.parseLong(userId);
            SendMessage message = new SendMessage(chatId, messagesBundle.getString("NO_VOLUNTEERS_TEXT"));
            sendMessage(message);
        } else {
            chatId = volunteer.getChatId();
            if (update.message().from().username() != null) {
                userId = "@" + update.message().from().username();
                SendMessage message = new SendMessage(chatId, String.format(messagesBundle.getString("CONTACT_TELEGRAM_USER"), userId));
                sendMessage(message);
            } else {
                SendMessage message = new SendMessage(chatId, String.format(messagesBundle.getString("CONTACT_TELEGRAM_ID"), userId));
                sendMessage(message);
            }
        }
    }
}
