package com.skyteam.animalshelterbot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.skyteam.animalshelterbot.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private TelegramBot telegramBot = new TelegramBot("BOT_TOKEN");

    private final ClientService clientService;


    public TelegramBotUpdatesListener(TelegramBot telegramBot, ClientService clientService) {

        this.telegramBot = telegramBot;
        this.clientService = clientService;
    }

    String regex = "([A-Z][a-z]+) ([A-Z][a-z]+) (\\d{3}-\\d{3}-\\d{4})";

    Pattern pattern = Pattern.compile(regex);


    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

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
                    case ("/help"): {

                    }
                    case ("/cat"): {
                        clientService.saveClientWithoutInfo(chatId, "cat");
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

        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;

    }

    private void startCommandReceived(long chatId, String name) {
        String answer = "Привет " + name + ". Выбери, какой приют тебе нужен: /cat для кошачего и /dog для собачьего! Чтобы оставить данные, воспользуйтесь /profile";
        logger.info("Replied to user " + name);
        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage(chatId, textToSend);
        executeMessage(message);
    }

    private void fillProfileMessage(long chatId) {
        String message = "Введите данные в следующем формате: ИМЯ ФАМИЛИЯ НОМЕР";
        sendMessage(chatId, message);
    }

    private void executeMessage(SendMessage message) {
        telegramBot.execute(message);
    }

    private void prepareAndSendMessage(Long chatId) {
        SendMessage message = new SendMessage(chatId, "Команда не распознана");

        executeMessage(message);
    }
}

