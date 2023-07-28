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

import static com.skyteam.animalshelterbot.listener.constants.ConstantsForBotMessages.*;

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
        String answer = "Добрый день, " + name + ". Выберите приют, из которого вы хотите взять питомца.\n/cats - Кошкин дом\n/dogs - Пёс хауз";

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

