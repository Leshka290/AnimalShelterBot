package com.skyteam.animalshelterbot.service;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.skyteam.animalshelterbot.listener.constants.PetType;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ResourceBundle;

import static com.skyteam.animalshelterbot.listener.constants.ConstantsButtons.*;
import static com.skyteam.animalshelterbot.listener.constants.PetType.DOG;

/**
 * Сервис создания кнопок меню
 */
@Service
@NoArgsConstructor
@AllArgsConstructor
@Generated
public class MenuBuilderService {

    private PetType petType;

    /**
     * Подключение файла пропертис с сообщениями
     */
    private final ResourceBundle messagesBundle = ResourceBundle.getBundle("bot_messages");

    /**
     * Создает кнопки выбора приютов.
     */
    public InlineKeyboardMarkup createButtonsPetTypeSelect() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(messagesBundle.getString("BUTTON_CAT_SHELTER")).callbackData(BUTTON_CAT_SHELTER_CALLBACK));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(messagesBundle.getString("BUTTON_DOG_SHELTER")).callbackData(BUTTON_DOG_SHELTER_CALLBACK));
        return inlineKeyboardMarkup;
    }

    /**
     * Создает кнопки основного меню при уже выбраном приюте
     */
    public InlineKeyboardMarkup createButtonsMenu() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(messagesBundle.getString("BUTTON_INFO_SHELTER")).callbackData(BUTTON_INFO_SHELTER_GENERAL_CALLBACK));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(messagesBundle.getString("BUTTON_HOW_ADOPT_ANIMAL_FROM_SHELTER")).callbackData(BUTTON_HOW_ADOPT_ANIMAL_FROM_SHELTER_CALLBACK));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(messagesBundle.getString("BUTTON_SUBMIT_PET_REPORT")).callbackData(BUTTON_SUBMIT_PET_REPORT_CALLBACK));

        return inlineKeyboardMarkup;
    }

    /**
     * Создает кнопки выбора информации о приюте.
     */
    public InlineKeyboardMarkup buttonsStartMenu() {
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
    public InlineKeyboardMarkup buttonsInfoShelter() {
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
    public InlineKeyboardMarkup createButtonsReport() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(messagesBundle.getString("BUTTON_REPORT_TEMPLATE")).callbackData(BUTTON_REPORT_TEMPLATE_CALLBACK));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(messagesBundle.getString("BUTTON_SEND_REPORT")).callbackData(BUTTON_SEND_REPORT_CALLBACK));
        return inlineKeyboardMarkup;
    }

    /**
     * Создает кнопки отправки данных пользователя.
     */
    public ReplyKeyboardMarkup requestContactKeyboardButton() {
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
    public ReplyKeyboardMarkup mainMenuKeyboardButtons() {
        KeyboardButton keyboardButtonMain = new KeyboardButton(messagesBundle.getString("BUTTON_MAIN_MENU"));
        KeyboardButton keyboardButtonCall = new KeyboardButton(messagesBundle.getString("BUTTON_CALL_VOLUNTEER"));
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardButtonMain, keyboardButtonCall);
        replyKeyboardMarkup.resizeKeyboard(true);
        return replyKeyboardMarkup;
    }
}
