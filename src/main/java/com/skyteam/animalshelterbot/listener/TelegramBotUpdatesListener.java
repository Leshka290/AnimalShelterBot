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
import com.skyteam.animalshelterbot.model.Client;
import com.skyteam.animalshelterbot.model.QuestionsForVolunteer;
import com.skyteam.animalshelterbot.repository.ClientRepository;
import com.skyteam.animalshelterbot.repository.QuestionsForVolunteerRepository;
import com.skyteam.animalshelterbot.service.ClientService;
import com.skyteam.animalshelterbot.service.PetService;
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
    private final ClientRepository clientRepository;
    private final PetService petService;
    private final QuestionsForVolunteerRepository questionsForVolunteerRepository;


    public TelegramBotUpdatesListener(ClientRepository clientRepository, ClientService clientService, PetService petService, QuestionsForVolunteerRepository questionsForVolunteerRepository) {
        this.clientService = clientService;
        this.clientRepository = clientRepository;
        this.petService = petService;
        this.questionsForVolunteerRepository = questionsForVolunteerRepository;
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

//        if (matcherForAddPattern.matches()) {
//            PetType type = PetType.valueOf(matcherForAddPattern.group(1));
//            String nickName = matcherForAddPattern.group(5);
//            Sex sex = Sex.valueOf(matcherForAddPattern.group(3));
//            String breed = matcherForAddPattern.group(7);
//            Integer age = Integer.parseInt(matcherForAddPattern.group(9));
//            byte[] picture = matcherForAddPattern.group(11).getBytes();
//            Pet pet = new Pet(type,nickName,breed,sex,age,picture);
//            petService.createPet(pet);
//            sendMessage(chatId,"Животное добавлено в БД");
//        }

        switch (update.message().text()) {
            case "/start":

            case BUTTON_MAIN_MENU:
                processStartCommand(update);
                break;
            case BUTTON_CALL_VOLUNTEER:
                // Позвать волонтеру
//                callVolunteer(update);
                break;
            case BUTTON_CANCEL:
                cancelShareContact(update);
                break;
            default:
                questionsForVolunteerRepository.save(new QuestionsForVolunteer(update.message().text(), chatId));
                sendMessage(chatId, messagesBundle.getString( "MESSAGE_AFTER_YOUR_QUESTION"));

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
//                    saveAdoptionReport(chatId);
                    break;
                case BUTTON_SHARE_CONTACT_CALLBACK:
                    // Поделитесь своими контактными данными
                    sendButtonClickMessage(chatId, messagesBundle.getString( "BUTTON_SHARE_CONTACT_DETAILS"));
                    fillProfileMessage(chatId);
                    break;
                case BUTTON_INFO_SHELTER_CALLBACK:
                    // Информация о приюте
                    sendButtonClickMessage(chatId, messagesBundle.getString("BUTTON_INFO_SHELTER"));
//                    processGettingInformationAboutShelter(chatId);
                    break;
                case BUTTON_INFO_SECURITY_CALLBACK:
                    // Получение контактов службы безопасности
                    sendButtonClickMessage(chatId, messagesBundle.getString( "BUTTON_INFO_SECURITY"));
//                    processGettingInformationAboutSecurity(chatId);
                    break;
                case BUTTON_INFO_SAFETY_PRECAUTIONS_CALLBACK:
                    // Получение инструкций по технике безопасности
                    sendButtonClickMessage(chatId,  messagesBundle.getString("BUTTON_INFO_SAFETY_PRECAUTIONS"));
//                    processGettingInformationAboutSafetyPrecautions(chatId);
                    break;
                case BUTTON_RULES_MEETING_ANIMAL_CALLBACK:
                    // Инструкция как познакомиться с животным в первый раз
                    sendButtonClickMessage(chatId, messagesBundle.getString("BUTTON_RULES_MEETING_ANIMAL"));
//                    processInfoMeetingClick(chatId);
                    break;
                case BUTTON_DOCS_FOR_ADOPTION_CALLBACK:
                    // Список необходимых документов
                    sendButtonClickMessage(chatId, messagesBundle.getString("BUTTON_DOCS_FOR_ADOPTION"));
//                    processListOfDocsClick(chatId);
                    break;
                case BUTTON_RECOMMENDATIONS_FOR_TRANSPORT_CALLBACK:
                    // Рекомендации по перевозке животных
                    sendButtonClickMessage(chatId, messagesBundle.getString("BUTTON_RECOMMENDATIONS_FOR_TRANSPORT"));
//                    processTransportAnimal(chatId);
                    break;
                case BUTTON_ARRANGEMENT_FOR_PET_CALLBACK:
                    //  Устройство для молодого в доме
                    sendButtonClickMessage(chatId, messagesBundle.getString("BUTTON_ARRANGEMENT_FOR_PET"));
//                    processRecForLittle(chatId);
                    break;
                case BUTTON_ARRANGEMENT_FOR_ADULT_CALLBACK:
                    // Устройство для пожилого животного в доме
                    sendButtonClickMessage(chatId, messagesBundle.getString("BUTTON_ARRANGEMENT_FOR_ADULT"));
//                    processRecForAdult(chatId);
                    break;
                case BUTTON_ADVICES_FOR_DISABLED_PET_CALLBACK:
                    // Советы, как быть с животными-инвалидами
                    sendButtonClickMessage(chatId, messagesBundle.getString("BUTTON_ADVICES_FOR_DISABLED_PET"));
//                    processRecForDisable(chatId);
                    break;
                case BUTTON_ADVICES_FROM_KINOLOG_CALLBACK:
                    // Советы от кинолога
                    sendButtonClickMessage(chatId, messagesBundle.getString("BUTTON_ADVICES_FROM_KINOLOG"));
//                    processKinologAdvices(chatId);
                    break;
                case BUTTON_RECOMMENDED_KINOLOGS_CALLBACK:
                    // Список рекомендуемых кинологов
                    sendButtonClickMessage(chatId, messagesBundle.getString("BUTTON_RECOMMENDED_KINOLOGS"));
//                    processRecKinologs(chatId);
                    break;
                case BUTTON_REASONS_FOR_REFUSAL_CALLBACK:
                    // Причины, по которым мы можем вам отказать
                    sendButtonClickMessage(chatId, messagesBundle.getString("BUTTON_REASONS_FOR_REFUSAL"));
//                    processReasonsRefusal(chatId);
                    break;
                case BUTTON_CANCEL_SEND_REPORT_CALLBACK:
                    // Отменить отправку отчета
//                    cancelSendReport(chatId);
                    break;
            }
        }
    }

    /**
     * Создает кнопки выбора приютов.
     */
    private InlineKeyboardMarkup createButtonsPetTypeSelect() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(messagesBundle.getString("BUTTON_CAT_SHELTER")).callbackData(BUTTON_CAT_SHELTER_CALLBACK));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(messagesBundle.getString("BUTTON_DOG_SHELTER")).callbackData(BUTTON_DOG_SHELTER_CALLBACK));
        return inlineKeyboardMarkup;
    }

    /**
     * Создает кнопки основного меню при уже выбраном приюте
     */
    private InlineKeyboardMarkup createButtonsMenu() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(messagesBundle.getString("BUTTON_INFO_SHELTER")).callbackData(BUTTON_INFO_SHELTER_GENERAL_CALLBACK));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(messagesBundle.getString("BUTTON_HOW_ADOPT_ANIMAL_FROM_SHELTER")).callbackData(BUTTON_HOW_ADOPT_ANIMAL_FROM_SHELTER_CALLBACK));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(messagesBundle.getString("BUTTON_SUBMIT_PET_REPORT")).callbackData(BUTTON_SUBMIT_PET_REPORT_CALLBACK));

        return inlineKeyboardMarkup;
    }

    /**
     * Создает кнопки выбора информации о приюте.
     */
    private InlineKeyboardMarkup buttonsStartMenu() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(messagesBundle.getString("BUTTON_INFO_SHELTER")).callbackData(BUTTON_INFO_SHELTER_CALLBACK));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(messagesBundle.getString("BUTTON_INFO_SECURITY")).callbackData(BUTTON_INFO_SECURITY_CALLBACK));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(messagesBundle.getString("BUTTON_INFO_SAFETY_PRECAUTIONS")).callbackData(BUTTON_INFO_SAFETY_PRECAUTIONS_CALLBACK));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(messagesBundle.getString("BUTTON_SHARE_CONTACT_DETAILS")).callbackData(BUTTON_SHARE_CONTACT_CALLBACK));

        return inlineKeyboardMarkup;
    }

    /**
     * Создает кнопки при выборе приюта
     */
    private InlineKeyboardMarkup buttonsInfoShelter() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(messagesBundle.getString("BUTTON_RULES_MEETING_ANIMAL")).callbackData(BUTTON_RULES_MEETING_ANIMAL_CALLBACK));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(messagesBundle.getString("BUTTON_DOCS_FOR_ADOPTION")).callbackData(BUTTON_DOCS_FOR_ADOPTION_CALLBACK));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(messagesBundle.getString("BUTTON_RECOMMENDATIONS_FOR_TRANSPORT")).callbackData(BUTTON_RECOMMENDATIONS_FOR_TRANSPORT_CALLBACK));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(messagesBundle.getString("BUTTON_ARRANGEMENT_FOR_PET")).callbackData(BUTTON_ARRANGEMENT_FOR_PET_CALLBACK));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(messagesBundle.getString("BUTTON_ARRANGEMENT_FOR_ADULT")).callbackData(BUTTON_ARRANGEMENT_FOR_ADULT_CALLBACK));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(messagesBundle.getString("BUTTON_ADVICES_FOR_DISABLED_PET")).callbackData(BUTTON_ADVICES_FOR_DISABLED_PET_CALLBACK));
        if (petType.equals(DOG)) {
            inlineKeyboardMarkup.addRow(new InlineKeyboardButton(messagesBundle.getString("BUTTON_ADVICES_FROM_KINOLOG")).callbackData(BUTTON_ADVICES_FROM_KINOLOG_CALLBACK));
            inlineKeyboardMarkup.addRow(new InlineKeyboardButton(messagesBundle.getString("BUTTON_RECOMMENDED_KINOLOGS")).callbackData(BUTTON_RECOMMENDED_KINOLOGS_CALLBACK));
        }
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(messagesBundle.getString("BUTTON_REASONS_FOR_REFUSAL")).callbackData(BUTTON_REASONS_FOR_REFUSAL_CALLBACK));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(messagesBundle.getString("BUTTON_SHARE_CONTACT_DETAILS")).callbackData(BUTTON_SHARE_CONTACT_CALLBACK));
        return inlineKeyboardMarkup;
    }

    /**
     * Кнопки для отправки данных.
     */
    private InlineKeyboardMarkup createButtonsReport() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(messagesBundle.getString("BUTTON_REPORT_TEMPLATE")).callbackData(BUTTON_REPORT_TEMPLATE_CALLBACK));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(messagesBundle.getString("BUTTON_SEND_REPORT")).callbackData(BUTTON_SEND_REPORT_CALLBACK));
        return inlineKeyboardMarkup;
    }

    /**
     * Создает кнопки отправки данных пользователя.
     */
    private ReplyKeyboardMarkup requestContactKeyboardButton() {
        KeyboardButton keyboardButtonShare = new KeyboardButton(messagesBundle.getString("BUTTON_SHARE_CONTACT"));
        KeyboardButton keyboardButtonCancel = new KeyboardButton(messagesBundle.getString("BUTTON_CANCEL"));
        keyboardButtonShare.requestContact(true);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardButtonShare, keyboardButtonCancel);
        replyKeyboardMarkup.resizeKeyboard(true);
        return replyKeyboardMarkup;
    }

    /**
     * Создает главное меню.
     */
    private ReplyKeyboardMarkup mainMenuKeyboardButtons() {
        KeyboardButton keyboardButtonMain = new KeyboardButton(messagesBundle.getString("BUTTON_MAIN_MENU"));
        KeyboardButton keyboardButtonCall = new KeyboardButton(messagesBundle.getString("BUTTON_CALL_VOLUNTEER"));
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
     * Общая информация о приюте (1)
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

        message.replyMarkup(buttonsStartMenu());
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
        message.replyMarkup(buttonsInfoShelter());

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

        message.replyMarkup(createButtonsReport());
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
        message.replyMarkup(createButtonsReport());
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
        SendMessage message = new SendMessage(chatId, messagesBundle.getString("BUTTON_CANCEL_SHARE_CONTACT"));
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

