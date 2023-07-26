package com.skyteam.animalshelterbot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot = new TelegramBot("BOT_TOKEN");


    public TelegramBotUpdatesListener() {

    }

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

                switch (messageText) {
                    case ("/start"): {

                        startCommandReceived(chatId, update.message().chat().firstName());
                        break;
                    }
                    case ("/help"): {

                    }
                    default: {
                        prepareAndSendMessage(chatId, "Команда не распознана");
                        break;
                    }
                }
            }

        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;

    }

    private void startCommandReceived(long chatId, String name) {
        String answer = "Привет " + name;

        logger.info("Replied to user " + name);

        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage(chatId, textToSend);

        executeMessage(message);
    }

    private void executeMessage(SendMessage message) {
        telegramBot.execute(message);
    }

    private void prepareAndSendMessage(Long chatId, String textToSend) {
        SendMessage message = new SendMessage(chatId, textToSend);

        executeMessage(message);
    }
}

