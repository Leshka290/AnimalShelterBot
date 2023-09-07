package com.skyteam.animalshelterbot.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Класс для создания БД с вопросами для волонтера со свойствами:
 * <p>
 * <b>id</b>,<b>question</b>,<b>chatId</b>
 * @author Kovach_DR
 */
@Entity(name = "questions")
@Data
@NoArgsConstructor
public class QuestionsForVolunteer {
    /**
     * Идентификатор вопроса в БД
     */
    @Id
    @GeneratedValue
    private Long id;
    /**
     * Вопрос пользователя волонтеру
     */
    private String question;
    /**
     * Идентификатор чата
     */
    private Long chatId;

    public QuestionsForVolunteer(String question, Long chatId) {
        this.question = question;
        this.chatId = chatId;
    }
}
