package com.skyteam.animalshelterbot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
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
 * @author leshka290
 *
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

    /**
     * Основной функционал телеграм-бота, отвечающий за прием и отправку сообщений пользователям.
     */
    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);

            if (!update.message().text().isEmpty()) {
                String messageText = update.message().text();
                long chatId = update.message().chat().id();
                Matcher matcher = pattern.matcher(messageText);
                switch (messageText) {
                    case ("/start"): {
                        startCommandReceived(chatId, update.message().chat().firstName());
                        break;
                    }
                    case ("/info_cat_shelter"): {
                        sendMessage(chatId, INFO_ABOUT_CAT_SHELTER);
                        break;
                    }
                    case ("/info_dog_shelter"): {
                        sendMessage(chatId, INFO_ABOUT_DOG_SHELTER);
                        break;
                    }
                    case ("/schedule_and_address_cat_shelter"): {
                        sendMessage(chatId, SCHEDULE_AND_ADDRESS_CAT_SHELTER);
                        break;
                    }
                    case ("/schedule_and_address_dog_shelter"): {
                        sendMessage(chatId, SCHEDULE_AND_ADDRESS_DOG_SHELTER);
                        break;
                    }
                    case ("/pass_information_cat_shelter"): {
                        sendMessage(chatId, PASS_INFORMATION_CAT_SHELTER);
                        break;
                    }
                    case ("/pass_information_dog_shelter"): {
                        sendMessage(chatId, PASS_INFORMATION_DOG_SHELTER);
                        break;
                    }
                    case ("/safety_information_cat_shelter"): {
                        sendMessage(chatId, SAFETY_INFORMATION_CAT_SHELTER);
                        break;
                    }
                    case ("/safety_information_dog_shelter"): {
                        sendMessage(chatId, SAFETY_INFORMATION_DOG_SHELTER);
                        break;
                    }
                    case ("/cats"): {
                        sendMessage(chatId, HELP_CATS_SHELTER);
                        break;
                    }
                    case ("/dogs"): {
                        sendMessage(chatId, HELP_DOGS_SHELTER);
                        break;
                    }
                    case ("/cat"): {
                        clientService.saveClientWithoutInfo(chatId, "cat");
                        break;
                    }
                    case ("/dog"): {
                        clientService.saveClientWithoutInfo(chatId, "dog");
                        break;
                    }
                    default: {
                        prepareAndSendMessage(chatId);

                        break;
                    }
                }
            }

        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;

    }

    /**
     * Создает кнопки выбора приюта или вызова волонтера.
     */
    private InlineKeyboardMarkup buttons1() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Cat").callbackData("/cat"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Dog").callbackData("/dog"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("volunteer").callbackData("/volunteer"));
        return inlineKeyboardMarkup;
    }

    /**
     * Метод отправляет приветственное сообщение пользователю и создаёт кнопки для выбора приюта.
     * @param chatId идентификатор чата
     * @param name имя пользователя
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
     * @param chatId идентификатор чата
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
     * @param chatId идентификатор чата
     */
    private void prepareAndSendMessage(Long chatId) {
        SendMessage message = new SendMessage(chatId, "Команда не распознана");
        executeMessage(message);
    }
}

