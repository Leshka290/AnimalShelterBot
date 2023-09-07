package com.skyteam.animalshelterbot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import com.skyteam.animalshelterbot.listener.constants.PetType;
import com.skyteam.animalshelterbot.listener.constants.ReportStatus;
import com.skyteam.animalshelterbot.model.Client;
import com.skyteam.animalshelterbot.model.Volunteer;
import com.skyteam.animalshelterbot.repository.*;
import com.skyteam.animalshelterbot.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.skyteam.animalshelterbot.listener.constants.ConstantsButtons.*;
import static com.skyteam.animalshelterbot.listener.constants.PetType.CAT;
import static com.skyteam.animalshelterbot.listener.constants.PetType.DOG;
import static com.skyteam.animalshelterbot.listener.constants.ReportStatus.*;

/**
 * Реализует функционал телеграм-бота.
 *
 * @author leshka290
 */
@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    /**
     * Переменная для реализации логирования приложения.
     */
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private final TelegramBot telegramBot = new TelegramBot("BOT_TOKEN");

    private PetType petType;
    private final ClientService clientService;
    private final VolunteerService volunteerService;
    private final ClientRepository clientRepository;
    private final AdopterRepository adopterRepository;
    private final CatReportRepository catReportRepository;
    private final DogReportRepository dogReportRepository;
    private final CatImageRepository catImageRepository;
    private final DogImageRepository dogImageRepository;
    private final AdopterService adopterService;
    private final MenuBuilderService menuBuilderService;
    private final SaveEntityService saveEntityService;

    public TelegramBotUpdatesListener(ClientService clientService, PetService petService, VolunteerService volunteerService,
                                      QuestionsForVolunteerRepository questionsForVolunteerRepository,
                                      ClientRepository clientRepository, AdopterRepository adopterRepository,
                                      CatReportRepository catReportRepository, DogReportRepository dogReportRepository,
                                      PetRepository petRepository, CatImageRepository catImageRepository,
                                      DogImageRepository dogImageRepository, AdopterService adopterService,
                                      MenuBuilderService menuBuilderService, SaveEntityService saveEntityService) {
        this.clientService = clientService;
        this.volunteerService = volunteerService;
        this.clientRepository = clientRepository;
        this.adopterRepository = adopterRepository;
        this.catReportRepository = catReportRepository;
        this.dogReportRepository = dogReportRepository;
        this.catImageRepository = catImageRepository;
        this.dogImageRepository = dogImageRepository;
        this.adopterService = adopterService;
        this.menuBuilderService = menuBuilderService;
        this.saveEntityService = saveEntityService;
    }

    /**
     * Регулярное выражение для распознавания вводимых пользователем данных и сохранением их в БД.
     */
    private final String regex = "([A-Z][a-z]+) ([A-Z][a-z]+) (\\d{3}-\\d{3}-\\d{4})";
    private final String regexForAddPet = "([a-zA-Z]{3})(\\s)([a-zA-Z]{4,6})(\\s)([a-zA-Z]+)(\\s)([a-zA-Z]+)(\\s)(\\d{1,2})(\\s)(\\w+)";

    private final Pattern pattern = Pattern.compile(regex);
    private final Pattern patternForAddPet = Pattern.compile(regexForAddPet);


    /**
     * Подключение файла пропертис с сообщениями
     */
    private final ResourceBundle messagesBundle = ResourceBundle.getBundle("bot_messages");


    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    private void sendMessage(SendMessage message) {
        SendResponse response = telegramBot.execute(message);
        if (response != null && !response.isOk()) {
            logger.warn("Message was not sent: {}, error code: {}", message, response.errorCode());
        }
    }

    /**
     * Основной функционал телеграм-бота, отвечающий за прием и отправку сообщений пользователям.
     */
    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {

            logger.info("Processing update: {}", update);

            if (update.message() != null) {
                processMessage(update);
            } else {
                processButtonClick(update);
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    /**
     * Функционал телеграм-бота, отвечающий отправку сообщений пользователям.
     */
    private void processMessage(Update update) {

        if (update.message().contact() != null) {
            saveEntityService.saveAdopter(update);
            return;
        }

        if (update.message().text() == null) {
            return;
        }

        String messageText = update.message().text();
        long chatId = update.message().chat().id();
        Matcher matcher = pattern.matcher(messageText);
        Matcher matcherForAddPattern = patternForAddPet.matcher(messageText);

        if (matcher.find()) {
            String name = matcher.group(1);
            String lastName = matcher.group(2);
            long phoneNumber = Long.parseLong(matcher.group(3));
            clientService.saveClientsInfo(name, lastName, phoneNumber, chatId);
        }

        ReportStatus reportStatus = adopterService.getUpdateStatus(chatId);
        if (reportStatus == WAITING_FOR_PET_PICTURE) {
            saveEntityService.saveAdoptionReportPhoto(update);
            adopterService.setUpdateStatus(chatId, WAITING_FOR_PET_DIET);
            return;
        }
        if (reportStatus == WAITING_FOR_PET_DIET) {
            saveEntityService.saveAdoptionReportDiet(update);
            adopterService.setUpdateStatus(chatId, WAITING_FOR_WELL_BEING);
            return;
        }
        if (reportStatus == WAITING_FOR_WELL_BEING) {
            saveEntityService.saveAdoptionReportWellBeing(update);
            adopterService.setUpdateStatus(chatId, WAITING_FOR_BEHAVIOR_CHANGE);
            return;
        }
        if (reportStatus == WAITING_FOR_BEHAVIOR_CHANGE) {
            saveEntityService.saveAdoptionReportBehaviorChange(update);
            adopterService.setUpdateStatus(chatId, DEFAULT);
            return;
        }

        switch (update.message().text()) {
            case "/start":

            case BUTTON_MAIN_MENU:
                processStartCommand(update);
                break;
            case BUTTON_CALL_VOLUNTEER:
                // Позвать волонтера
                saveEntityService.callVolunteer(update);
                break;
            case BUTTON_CANCEL:
                cancelShareContact(update);
                break;
//            default:
//                questionsForVolunteerRepository.save(new QuestionsForVolunteer(update.message().text(), chatId));
//                sendMessage(chatId, messagesBundle.getString("MESSAGE_AFTER_YOUR_QUESTION"));

        }
    }

    /**
     * Функционал телеграм-бота, отвечающий за нажатие кнопок пользователям.
     */
    private void processButtonClick(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        if (callbackQuery != null) {
            long chatId = callbackQuery.message().chat().id();
            switch (callbackQuery.data()) {

                case BUTTON_CAT_SHELTER_CALLBACK:
                    // Выбор приюта кошек
                    sendButtonClickMessage(chatId, messagesBundle.getString("BUTTON_CAT_SHELTER"));
                    processCatShelterClick(chatId);
                    break;
                case BUTTON_DOG_SHELTER_CALLBACK:
                    // Выбор приюта собак
                    sendButtonClickMessage(chatId, messagesBundle.getString("BUTTON_DOG_SHELTER_TEXT"));
                    processDogShelterClick(chatId);
                    break;
                case BUTTON_INFO_SHELTER_GENERAL_CALLBACK:
                    // Общая информация о приюте (1)
                    sendButtonClickMessage(chatId, messagesBundle.getString("BUTTON_INFO_SHELTER"));
                    processStartClick(chatId);
                    break;
                case BUTTON_HOW_ADOPT_ANIMAL_FROM_SHELTER_CALLBACK:
                    // Как завести собаку/кошку (2)
                    sendButtonClickMessage(chatId, messagesBundle.getString("BUTTON_HOW_ADOPT_ANIMAL_FROM_SHELTER"));
                    processInfoPetClick(chatId, update);
                    break;
                case BUTTON_SUBMIT_PET_REPORT_CALLBACK:
                    // Отправить отчет (3)
                    sendButtonClickMessage(chatId, messagesBundle.getString("BUTTON_SUBMIT_PET_REPORT"));
                    processAdoptClick(chatId);
                    break;
                //Шаблон отпарвки отчета
                case BUTTON_REPORT_TEMPLATE_CALLBACK:
                    SendMessage instructionMessage = new SendMessage(chatId, ADOPTION_REPORT_INSTRUCTION);
                    sendMessage(instructionMessage);
                    break;
                //Отправка отчета
                case BUTTON_SEND_REPORT_CALLBACK:
                    saveEntityService.saveAdoptionReport(chatId);
                    break;
                case BUTTON_SHARE_CONTACT_CALLBACK:
                    // Поделитесь своими контактными данными
                    sendButtonClickMessage(chatId, messagesBundle.getString("BUTTON_SHARE_CONTACT_DETAILS"));
                    fillProfileMessage(chatId);
                    break;
                case BUTTON_INFO_SHELTER_CALLBACK:
                    // Информация о приюте
                    sendButtonClickMessage(chatId, messagesBundle.getString("BUTTON_INFO_SHELTER"));
                    break;
                case BUTTON_INFO_SECURITY_CALLBACK:
                    // Получение контактов службы безопасности
                    sendButtonClickMessage(chatId, messagesBundle.getString("BUTTON_INFO_SECURITY"));
                    break;
                case BUTTON_INFO_SAFETY_PRECAUTIONS_CALLBACK:
                    // Получение инструкций по технике безопасности
                    sendButtonClickMessage(chatId, messagesBundle.getString("BUTTON_INFO_SAFETY_PRECAUTIONS"));
                    break;
                case BUTTON_RULES_MEETING_ANIMAL_CALLBACK:
                    // Инструкция как познакомиться с животным в первый раз
                    sendButtonClickMessage(chatId, messagesBundle.getString("BUTTON_RULES_MEETING_ANIMAL"));
                    break;
                case BUTTON_DOCS_FOR_ADOPTION_CALLBACK:
                    // Список необходимых документов
                    sendButtonClickMessage(chatId, messagesBundle.getString("BUTTON_DOCS_FOR_ADOPTION"));
                    break;
                case BUTTON_RECOMMENDATIONS_FOR_TRANSPORT_CALLBACK:
                    // Рекомендации по перевозке животных
                    sendButtonClickMessage(chatId, messagesBundle.getString("BUTTON_RECOMMENDATIONS_FOR_TRANSPORT"));
                    break;
                case BUTTON_ARRANGEMENT_FOR_PET_CALLBACK:
                    //  Устройство для молодого в доме
                    sendButtonClickMessage(chatId, messagesBundle.getString("BUTTON_ARRANGEMENT_FOR_PET"));
                    break;
                case BUTTON_ARRANGEMENT_FOR_ADULT_CALLBACK:
                    // Устройство для пожилого животного в доме
                    sendButtonClickMessage(chatId, messagesBundle.getString("BUTTON_ARRANGEMENT_FOR_ADULT"));
                    break;
                case BUTTON_ADVICES_FOR_DISABLED_PET_CALLBACK:
                    // Советы, как быть с животными-инвалидами
                    sendButtonClickMessage(chatId, messagesBundle.getString("BUTTON_ADVICES_FOR_DISABLED_PET"));
                    break;
                case BUTTON_ADVICES_FROM_KINOLOG_CALLBACK:
                    // Советы от кинолога
                    sendButtonClickMessage(chatId, messagesBundle.getString("BUTTON_ADVICES_FROM_KINOLOG"));
                    break;
                case BUTTON_RECOMMENDED_KINOLOGS_CALLBACK:
                    // Список рекомендуемых кинологов
                    sendButtonClickMessage(chatId, messagesBundle.getString("BUTTON_RECOMMENDED_KINOLOGS"));
                    break;
                case BUTTON_REASONS_FOR_REFUSAL_CALLBACK:
                    // Причины, по которым мы можем вам отказать
                    sendButtonClickMessage(chatId, messagesBundle.getString("BUTTON_REASONS_FOR_REFUSAL"));
                    break;
                case BUTTON_CANCEL_SEND_REPORT_CALLBACK:
                    // Отменить отправку отчета
                    break;
            }
        }
    }



    /**
     * Метод проверки пользователя в БД и если он там есть то пользователь начинает с выбраного приюта
     * Если пользователя нет, то он сначала выбирает приют
     */
    private void processStartCommand(Update update) {
        long chatId = update.message().chat().id();
        Client client = clientRepository.findByChatId(chatId);
        if (client == null) {
            sendShelterTypeSelectMessage(chatId);
        } else {
            petType = client.getLastPetType();
            if (petType == null) {
                sendShelterTypeSelectMessage(chatId);
                return;
            }
            switch (petType) {
                case DOG:
                    sendStartMessage(chatId, messagesBundle.getString("DOG_SHELTER_WELCOME"));
                    break;
                case CAT:
                    sendStartMessage(chatId, messagesBundle.getString("CAT_SHELTER_WELCOME"));
                    break;
                default:
                    sendShelterTypeSelectMessage(chatId);
            }
        }
    }

    /**
     * Общая информация о приюте (1)
     *
     * @param chatId
     */
    private void processStartClick(long chatId) {
        if (petType == null) {
            return;
        }
        String messageText = null;
        switch (petType) {
            case DOG:
                messageText = messagesBundle.getString("BUTTON_WELCOME_INFO_DOG_SHELTER");
                break;
            case CAT:
                messageText = messagesBundle.getString("BUTTON_WELCOME_INFO_CAT_SHELTER");
                break;
        }

        SendMessage message = new SendMessage(chatId, messageText);

        message.replyMarkup(menuBuilderService.buttonsStartMenu());
        sendMessage(message);
    }

    /**
     * Метод вызывается при выборе получения информации о приюте (2 этап)
     */
    private void processInfoPetClick(long chatId, Update update) {
        if (petType == null) {
            return;
        }
        String messageText = null;
        switch (petType) {
            case DOG:
                messageText = messagesBundle.getString("BUTTON_HOW_ADOPT_ANIMAL_FROM_DOG_SHELTER");
                break;
            case CAT:
                messageText = messagesBundle.getString("BUTTON_HOW_ADOPT_ANIMAL_FROM_CAT_SHELTER");
                break;
        }
        SendMessage message = new SendMessage(chatId, messageText);
        message.replyMarkup(menuBuilderService.buttonsInfoShelter());

        sendMessage(message);
    }

    /**
     * Метод вызывается при выборе отправки данных пользователем
     */
    private void processAdoptClick(long chatId) {
        if (petType == null) {
            return;
        }
        String messageText = null;
        switch (petType) {
            case DOG:
                messageText = messagesBundle.getString("BUTTON_HOW_ADOPT_ANIMAL_FROM_DOG_SHELTER");
                break;
            case CAT:
                messageText = messagesBundle.getString("BUTTON_HOW_ADOPT_ANIMAL_FROM_CAT_SHELTER");
                break;
        }
        SendMessage message = new SendMessage(chatId, messageText);

        message.replyMarkup(menuBuilderService.createButtonsReport());
        sendMessage(message);
    }


    /**
     * Метод для отправки отчета
     */
    private void processSubmitPetReport(long chatId) {
        if (petType == null) {
            return;
        }
        String messageText = null;
        switch (petType) {
            case DOG:
                messageText = messagesBundle.getString("BUTTON_SUBMIT_DOG_REPORT");
                break;
            case CAT:
                messageText = messagesBundle.getString("BUTTON_SUBMIT_CAT_REPORT");
                break;
        }

        SendMessage message = new SendMessage(chatId, messageText);
        message.replyMarkup(menuBuilderService.createButtonsReport());
        sendMessage(message);
    }

    /**
     * Метод вызывается при выборе приюта собак и сохранение столбца типа питомца Cat
     */
    private void processCatShelterClick(long chatId) {
        petType = CAT;
        saveEntityService.saveClient(chatId, petType);
        sendStartMessage(chatId, messagesBundle.getString("CAT_SHELTER_WELCOME"));
    }

    /**
     * Метод вызывается при выборе приюта собак и сохранение столбца типа питомца Dog
     */
    private void processDogShelterClick(long chatId) {
        petType = DOG;
        saveEntityService.saveClient(chatId, petType);
        sendStartMessage(chatId, messagesBundle.getString("DOG_SHELTER_WELCOME"));
    }

    /**
     * Функционал телеграм-бота, отвечающий за отмену отправки контактных данных.
     */
    private void cancelShareContact(Update update) {
        long chatId = update.message().chat().id();
        SendMessage message = new SendMessage(chatId, messagesBundle.getString("BUTTON_CANCEL_SHARE_CONTACT"));
        sendMessage(message.replyMarkup(menuBuilderService.mainMenuKeyboardButtons()));
    }


    /**
     * Метод выбора типа приюта
     */
    private void sendShelterTypeSelectMessage(long chatId) {
        SendMessage message = new SendMessage(chatId, messagesBundle.getString("SHELTER_TYPE_SELECT"));

        message.replyMarkup(menuBuilderService.createButtonsPetTypeSelect());
        sendMessage(message);
    }

    /**
     * Метод для создания стартогого меню и отправки сообщения
     */
    private void sendStartMessage(long chatId, String messageText) {
        SendMessage message = new SendMessage(chatId, messageText);

        message.replyMarkup(menuBuilderService.createButtonsMenu());
        sendMessage(message);
    }

    /**
     * Метод для отправки сообщения пользоваелю.
     *
     * @param chatId     идентификатор чата
     * @param textToSend текст сообщения для отправки пользователю
     */
    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage(chatId, textToSend);
        executeMessage(message);
    }

    /**
     * Отправляет техническое сообщение о том, что кнопка была нажата.
     * Можно отключить, если в этом нет необходимости.
     *
     * @param chatId  отправляет сообщение в этот чат
     * @param message само сообщение
     */
    private void sendButtonClickMessage(long chatId, String message) {
        sendMessage(new SendMessage(chatId, message));
    }

    /**
     * Отправляет сообщение пользователю о необходимости ввести контактные данные.
     * <p>
     * Используется метод {@code sendMessage(long chatId, String textToSend)}
     *
     * @param chatId идентификатор чата
     */
    private void fillProfileMessage(long chatId) {
        String message = "Введите данные в следующем формате: ИМЯ ФАМИЛИЯ НОМЕР";
        sendMessage(chatId, message);
    }

    private void executeMessage(SendMessage message) {
        telegramBot.execute(message);
    }

    /**
     * Отправляет пользователю сообщение о некорректности вводимых данных.
     *
     * @param chatId идентификатор чата
     */
    private void prepareAndSendMessage(Long chatId) {
        SendMessage message = new SendMessage(chatId, "Команда не распознана");
        executeMessage(message);
    }
}

