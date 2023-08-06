package com.skyteam.animalshelterbot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import com.skyteam.animalshelterbot.listener.constants.PetType;
import com.skyteam.animalshelterbot.listener.constants.Sex;
import com.skyteam.animalshelterbot.model.Client;
import com.skyteam.animalshelterbot.model.Pet;
import com.skyteam.animalshelterbot.repository.ClientRepository;
import com.skyteam.animalshelterbot.repository.PetRepository;
import com.skyteam.animalshelterbot.service.ClientService;
import com.skyteam.animalshelterbot.service.PetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.skyteam.animalshelterbot.listener.constants.ConstantsButtons.*;
import static com.skyteam.animalshelterbot.listener.constants.PetType.CAT;
import static com.skyteam.animalshelterbot.listener.constants.PetType.DOG;

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

    private final TelegramBot telegramBot = new TelegramBot("BOT_TOKEN");

    private PetType petType;
    private final ClientService clientService;
    private final ClientRepository clientRepository;
    private final PetService petService;
    private final PetRepository petRepository;


    public TelegramBotUpdatesListener(ClientRepository clientRepository, ClientService clientService, PetService petService, PetRepository petRepository) {
        this.clientService = clientService;
        this.clientRepository = clientRepository;
        this.petService = petService;
        this.petRepository = petRepository;
    }

    /**
     * Регулярное выражение для распознавания вводимых пользователем данных и сохранением их в БД.
     */
    private final String regex = "([A-Z][a-z]+) ([A-Z][a-z]+) (\\d{3}-\\d{3}-\\d{4})";
    private final String regexForAddPet = "([a-zA-Z]{3})(\\s)([a-zA-Z]{4,6})(\\s)([a-zA-Z]+)(\\s)([a-zA-Z]+)(\\s)(\\d{1,2})";

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

        if (matcherForAddPattern.matches()) {
            PetType type = PetType.valueOf(matcherForAddPattern.group(1));
            String nickName = matcherForAddPattern.group(3);
            Sex sex = Sex.valueOf(matcherForAddPattern.group(5));
            String breed = matcherForAddPattern.group(7);
            int age = Integer.parseInt(matcherForAddPattern.group(9));
            byte[] picture = matcherForAddPattern.group(11).getBytes();
            Pet pet = new Pet(nickName, type, breed, sex, age, picture);
            petRepository.save(pet);
            petService.createPet(pet);
            sendMessage(chatId,"Животное добавлено в БД");
        }

        switch (messageText) {
            case "/start":
            case BUTTON_MAIN_MENU:
                processStartCommand(update);
                break;
            case BUTTON_CALL_VOLUNTEER:
                // Call a volunteer
//                callVolunteer(update);
                break;
            case BUTTON_CANCEL:
                cancelShareContact(update);
                break;
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
                    sendButtonClickMessage(chatId, BUTTON_CAT_SHELTER);
                    processCatShelterClick(chatId);
                    break;
                case BUTTON_DOG_SHELTER_CALLBACK:
                    sendButtonClickMessage(chatId, BUTTON_DOG_SHELTER);
                    processDogShelterClick(chatId);
                    break;
                case BUTTON_INFO_SHELTER_CALLBACK:
                    sendButtonClickMessage(chatId, BUTTON_WELCOME_INFO_CAT_SHELTER);
                    processInfoPetClick(chatId, update);
                    break;
                case BUTTON_SUBMIT_PET_REPORT_CALLBACK :
                    sendButtonClickMessage(chatId, BUTTON_SUBMIT_PET_REPORT);
                    processAdoptClick(chatId);
                    break;
                case BUTTON_SCHEDULE_AND_ADDRESS_SHELTER_CALLBACK:
                    sendButtonClickMessage(chatId, BUTTON_SCHEDULE_AND_ADDRESS_SHELTER);
                    break;
                case BUTTON_HOW_ADOPT_ANIMAL_FROM_SHELTER_CALLBACK:
                    sendButtonClickMessage(chatId, BUTTON_HOW_ADOPT_ANIMAL_FROM_SHELTER);
                    processAdoptClick(chatId);
                    break;
                case BUTTON_REPORT_TEMPLATE_CALLBACK:
                    sendButtonClickMessage(chatId, BUTTON_REPORT_TEMPLATE);
                    break;
                case BUTTON_PASS_INFORMATION_CAT_SHELTER_CALLBACK:
                    sendButtonClickMessage(chatId, messagesBundle.getString("PASS_INFORMATION_CAT_SHELTER"));
                    break;
                case BUTTON_SAFETY_INF_CAT_SHELTER_CALLBACK:
                    sendButtonClickMessage(chatId, messagesBundle.getString("SAFETY_INFORMATION_CAT_SHELTER"));
                    break;
            }
        }
    }

    /**
     * Создает кнопки выбора приютов.
     */
    private InlineKeyboardMarkup createButtonsPetTypeSelect() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_CAT_SHELTER).callbackData(BUTTON_CAT_SHELTER_CALLBACK));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_DOG_SHELTER).callbackData(BUTTON_DOG_SHELTER_CALLBACK));
        return inlineKeyboardMarkup;
    }

    /**
     * Создает кнопки основного меню при уже выбраном приюте
     */
    private InlineKeyboardMarkup createButtonsMenu() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_INFO_SHELTER).callbackData(BUTTON_INFO_SHELTER_CALLBACK));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_HOW_ADOPT_ANIMAL_FROM_SHELTER).callbackData(BUTTON_HOW_ADOPT_ANIMAL_FROM_SHELTER_CALLBACK));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_SUBMIT_PET_REPORT).callbackData(BUTTON_SUBMIT_PET_REPORT_CALLBACK));
        return inlineKeyboardMarkup;
    }

    /**
     * Создает кнопки выбора информации о приюте кошек.
     */
    private InlineKeyboardMarkup buttonsInfoShelter() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Для получения информации о приюте").callbackData(BUTTON_WELCOME_INFO_CAT_SHELTER_CALLBACK));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Для получения информации о расписании и адресе").callbackData(BUTTON_SCHEDULE_AND_ADDRESS_SHELTER_CALLBACK));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Информация о подаче заявки на пропуск").callbackData(BUTTON_PASS_INFORMATION_CAT_SHELTER_CALLBACK));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Информация о правилах безопасности на территории приюта").callbackData(BUTTON_SAFETY_INF_CAT_SHELTER_CALLBACK));
        return inlineKeyboardMarkup;
    }

    /**
     * Создает кнопки при выборе приюта
     */
    private InlineKeyboardMarkup buttonsStartMenu() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_INFO_SHELTER).callbackData(BUTTON_INFO_SHELTER_CALLBACK));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_HOW_ADOPT_ANIMAL_FROM_SHELTER).callbackData(BUTTON_HOW_ADOPT_ANIMAL_FROM_SHELTER_CALLBACK));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_SUBMIT_PET_REPORT).callbackData(BUTTON_SUBMIT_PET_REPORT_CALLBACK));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_CALL_VOLUNTEER).callbackData(BUTTON_CALL_VOLUNTEER_CALLBACK));
        return inlineKeyboardMarkup;
    }

    /**
     * Кнопки для отправки данных.
     */
    private InlineKeyboardMarkup createButtonsReport() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_REPORT_TEMPLATE).callbackData(BUTTON_REPORT_TEMPLATE_CALLBACK));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_SEND_REPORT).callbackData(BUTTON_SEND_REPORT_CALLBACK));
        return inlineKeyboardMarkup;
    }

    /**
     * Создает кнопки отправки данных пользователя.
     */
    private ReplyKeyboardMarkup requestContactKeyboardButton() {
        KeyboardButton keyboardButtonShare = new KeyboardButton(BUTTON_SHARE_CONTACT);
        KeyboardButton keyboardButtonCancel = new KeyboardButton(BUTTON_CANCEL);
        keyboardButtonShare.requestContact(true);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardButtonShare, keyboardButtonCancel);
        replyKeyboardMarkup.resizeKeyboard(true);
        return replyKeyboardMarkup;
    }

    /**
     * Создает главное меню.
     */
    private ReplyKeyboardMarkup mainMenuKeyboardButtons() {
        KeyboardButton keyboardButtonMain = new KeyboardButton(BUTTON_MAIN_MENU);
        KeyboardButton keyboardButtonCall = new KeyboardButton(BUTTON_CALL_VOLUNTEER);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardButtonMain, keyboardButtonCall);
        replyKeyboardMarkup.resizeKeyboard(true);
        return replyKeyboardMarkup;
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
     * Метод вызывается при выборе отправки данных пользователем
     */
    private void processAdoptClick(long chatId) {
        if (petType == null) {
            return;
        }
        String messageText = null;
        switch (petType) {
            case DOG:
                messageText = BUTTON_HOW_ADOPT_ANIMAL_FROM_DOG_SHELTER;
                break;
            case CAT:
                messageText = BUTTON_HOW_ADOPT_ANIMAL_FROM_CAT_SHELTER;
                break;
        }
        SendMessage message = new SendMessage(chatId, messageText);
        //createButtonsReport()- вместо этого метода необходимо добавить константы второго пункта
        message.replyMarkup(createButtonsReport());
        sendMessage(message);
    }

    /**
     * Метод вызывается при выборе получения информации о приюте
     */
    private void processInfoPetClick(long chatId, Update update) {
        if (petType == null) {
            return;
        }
        String messageText = null;
        switch (petType) {
            case DOG:
                messageText = BUTTON_WELCOME_INFO_DOG_SHELTER;
                break;
            case CAT:
                messageText = BUTTON_WELCOME_INFO_CAT_SHELTER;
                break;
        }
        SendMessage message = new SendMessage(chatId, messageText);
        message.replyMarkup(buttonsInfoShelter());

        sendMessage(message);
    }

    /**
     * Метод начального выбора приюта и вывод сообщения пользователю
     */
    private void processStartClick(long chatId) {
        if (petType == null) {
            return;
        }
        String messageText = null;
        switch (petType) {
            case DOG:
                messageText = BUTTON_WELCOME_INFO_DOG_SHELTER;
                break;
            case CAT:
                messageText = BUTTON_WELCOME_INFO_CAT_SHELTER;
                break;
        }

        SendMessage message = new SendMessage(chatId, messageText);
        message.replyMarkup(buttonsStartMenu());
        sendMessage(message);
    }

    /**
     * Метод вызывается при выборе приюта собак и сохранение столбца типа питомца Cat
     */
    private void processCatShelterClick(long chatId) {
        petType = CAT;
        saveClient(chatId, petType);
        sendStartMessage(chatId, messagesBundle.getString("CAT_SHELTER_WELCOME"));
    }

    /**
     * Метод вызывается при выборе приюта собак и сохранение столбца типа питомца Dog
     */
    private void processDogShelterClick(long chatId) {
        petType = DOG;
        saveClient(chatId, petType);
        sendStartMessage(chatId, messagesBundle.getString("DOG_SHELTER_WELCOME"));
    }

    /**
     * Функционал телеграм-бота, отвечающий за отмену отправки контактных данных.
     */
    private void cancelShareContact(Update update) {
        long chatId = update.message().chat().id();
        SendMessage message = new SendMessage(chatId, BUTTON_CANCEL_SHARE_CONTACT);
        sendMessage(message.replyMarkup(mainMenuKeyboardButtons()));
    }

    /**
     * Метод сохранения клиента (гость) в БД таблицу client
     */
    private void saveClient(long chatId, PetType lastPetType) {
        Client client = clientRepository.findByChatId(chatId);
        if (client == null) {
            client = new Client(chatId, lastPetType);
        } else {
            client.setLastPetType(lastPetType);
        }
        clientRepository.save(client);
    }

    /**
     * Метод выбора типа приюта
     */
    private void sendShelterTypeSelectMessage(long chatId) {
        SendMessage message = new SendMessage(chatId, messagesBundle.getString("SHELTER_TYPE_SELECT"));

        message.replyMarkup(createButtonsPetTypeSelect());
        sendMessage(message);
    }

    /**
     * Метод для создания стартогого меню и отправки сообщения
     */
    private void sendStartMessage(long chatId, String messageText) {
        SendMessage message = new SendMessage(chatId, messageText);

        message.replyMarkup(createButtonsMenu());
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

