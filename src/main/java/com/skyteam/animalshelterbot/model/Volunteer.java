package com.skyteam.animalshelterbot.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
/**
 * Класс волонтер со свойствами:
 * <p>
 * <b>id</b>,<b>firstName</b>,<b>lastName</b>,<b>phoneNumber</b>,<b>chatId</b>
 * @author Kostyura
 */
@Entity
@Data
@NoArgsConstructor
public class Volunteer {
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Имя волонтера
     */
    private String firstName;
    /**
     * Фамилия волонтера
     */
    private String lastName;
    /**
     * Телефонный номер волонтера
     */
    private Long phoneNumber;
    /**
     * Идентификатор чата
     */
    private Long chatId;
    /**
     * Идентификатор приюта
     */
    private Long shelterId;
}
