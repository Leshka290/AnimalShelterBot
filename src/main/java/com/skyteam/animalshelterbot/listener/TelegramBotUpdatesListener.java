package com.skyteam.animalshelterbot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import com.skyteam.animalshelterbot.model.Client;
import com.skyteam.animalshelterbot.repository.DogClientRepository;
import com.skyteam.animalshelterbot.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.skyteam.animalshelterbot.listener.constants.ConstantsForBotMessages.*;

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
    private TelegramBot telegramBot = new TelegramBot("BOT_TOKEN");

    private final ClientService clientService;


    public TelegramBotUpdatesListener(ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * Регулярное выражение для распознавания вводимых пользователем данных и сохранением их в БД.
     */
    String regex = "([A-Z][a-z]+) ([A-Z][a-z]+) (\\d{3}-\\d{3}-\\d{4})";

    Pattern pattern = Pattern.compile(regex);


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

        String messageText = update.message().text();
        long chatId = update.message().chat().id();
        Matcher matcher = pattern.matcher(messageText);

        switch (update.message().text()) {
            case ("/start"): {
                startCommandReceived(chatId, update.message().chat().firstName());
                break;
            }
//            case ("/info_cat_shelter"): {
//                sendMessage(chatId, INFO_ABOUT_CAT_SHELTER);
//                break;
//            }
//            case ("/info_dog_shelter"): {
//                sendMessage(chatId, INFO_ABOUT_DOG_SHELTER);
//                break;
//            }
//            case ("/schedule_and_address_cat_shelter"): {
//                sendMessage(chatId, SCHEDULE_AND_ADDRESS_CAT_SHELTER);
//                break;
//            }
//            case ("/schedule_and_address_dog_shelter"): {
//                sendMessage(chatId, SCHEDULE_AND_ADDRESS_DOG_SHELTER);
//                break;
//            }
//            case ("/pass_information_cat_shelter"): {
//                sendMessage(chatId, PASS_INFORMATION_CAT_SHELTER);
//                break;
//            }
//            case ("/pass_information_dog_shelter"): {
//                sendMessage(chatId, PASS_INFORMATION_DOG_SHELTER);
//                break;
//            }
//            case ("/safety_information_cat_shelter"): {
//                sendMessage(chatId, SAFETY_INFORMATION_CAT_SHELTER);
//                break;
//            }
//            case ("/safety_information_dog_shelter"): {
//                sendMessage(chatId, SAFETY_INFORMATION_DOG_SHELTER);
//                break;
//            }
            case ("/cats"): {
                SendMessage message = new SendMessage(chatId, "Приют кошек");
                message.replyMarkup(buttonsInfoCats());
                sendMessage(message);
                break;
            }
            case ("/dogs"): {
                SendMessage message = new SendMessage(chatId, "Приют собак");
                message.replyMarkup(buttonsInfoDogs());
                sendMessage(message);
                break;
            }
            case ("/cat"): {
                clientService.saveClientWithoutInfo(chatId, "cat");
                logger.info("Replied to user " + chatId);
                break;
            }
            case ("/dog"): {
                clientService.saveClientWithoutInfo(chatId, "dog");
                break;
            }
            case ("/profile"): {
                fillProfileMessage(chatId);
                break;
            }
            default: {
                prepareAndSendMessage(chatId);
                break;
            }
        }
        if (matcher.find()) {
            String name = matcher.group(1);
            String lastName = matcher.group(2);
            long phoneNumber = Long.parseLong(matcher.group(3));
            clientService.saveClientsInfo(name, lastName, phoneNumber, chatId);
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
                case "info_cat_shelter":
                    sendMessage(chatId, INFO_ABOUT_CAT_SHELTER);
                    break;
                case "schedule_and_address_cat_shelter":
                    sendMessage(chatId, SCHEDULE_AND_ADDRESS_CAT_SHELTER);
                    break;
                case "pass_information_cat_shelter":
                    sendMessage(chatId, PASS_INFORMATION_CAT_SHELTER);
                    break;
                case "safety_information_cat_shelter":
                    sendMessage(chatId, SAFETY_INFORMATION_CAT_SHELTER);
                    break;
                case "info_dog_shelter":
                    sendMessage(chatId, INFO_ABOUT_DOG_SHELTER);
                    break;
                case "schedule_and_address_dog_shelter":
                    sendMessage(chatId, SCHEDULE_AND_ADDRESS_DOG_SHELTER);
                    break;
                case "pass_information_dog_shelter":
                    sendMessage(chatId, PASS_INFORMATION_DOG_SHELTER);
                    break;
                case "safety_information_dog_shelter":
                    sendMessage(chatId, SAFETY_INFORMATION_DOG_SHELTER);
                    break;
            }
        }
    }

    /**
     * Создает кнопки выбора информации о приюте кошек.
     */
    private InlineKeyboardMarkup buttonsInfoCats() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Для получения информации о приюте").callbackData("info_cat_shelter"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Для получения информации о расписании и адресе").callbackData("schedule_and_address_cat_shelter"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Информация о подаче заявки на пропуск").callbackData("pass_information_cat_shelter"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Информация о правилах безопасности на территории приюта").callbackData("safety_information_cat_shelter"));
        return inlineKeyboardMarkup;
    }

    /**
     * Создает кнопки выбора информации о приюте Собак.
     */
    private InlineKeyboardMarkup buttonsInfoDogs() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Для получения информации о приюте").callbackData("info_dog_shelter"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Для получения информации о расписании и адресе").callbackData("schedule_and_address_dog_shelter"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Информация о подаче заявки на пропуск").callbackData("pass_information_dog_shelter"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Информация о правилах безопасности на территории приюта").callbackData("safety_information_dog_shelter"));
        return inlineKeyboardMarkup;
    }

    /**
     * Метод отправляет приветственное сообщение пользователю и создаёт кнопки для выбора приюта.
     *
     * @param chatId идентификатор чата
     * @param name   имя пользователя
     */
    private void startCommandReceived(long chatId, String name) {
        String answer = "Привет " + name + ". Выбери, какой приют тебе нужен: /cats для кошачего и /dogs для собачьего! Чтобы оставить данные, воспользуйтесь /profile";
        logger.info("Replied to user " + name);
        SendMessage sendMessage = new SendMessage(chatId, answer);

        Keyboard keyboard = new ReplyKeyboardMarkup("/cats", "/dogs")
                .resizeKeyboard(true)
                .oneTimeKeyboard(true)
                .selective(true);
        sendMessage.replyMarkup(keyboard);
        sendMessage(chatId, answer);
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

