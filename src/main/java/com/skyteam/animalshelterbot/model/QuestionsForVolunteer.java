package com.skyteam.animalshelterbot.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "questions")
@Data
@NoArgsConstructor
public class QuestionsForVolunteer {
    @Id
    @GeneratedValue
    private Long id;
    private String question;
    private Long chatId;

    public QuestionsForVolunteer(String question, Long chatId) {
        this.question = question;
        this.chatId = chatId;
    }
}
