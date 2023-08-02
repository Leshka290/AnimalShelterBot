package com.skyteam.animalshelterbot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Класс клиент приюта для собак со свойствами:
 * <p>
 * <b>id</b>,<b>firstName</b>,<b>lastName</b>,<b>phoneNumber</b>,<b>chatId</b>
 * @author youcanwakemeup
 */
@Entity
@Data
@NoArgsConstructor
public class DogClient {

    public DogClient(String firstName, String lastName, Long phoneNumber, Long chatId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.chatId = chatId;
    }

    /**
     * Идентификатор по порядку генерируемый в БД.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Имя клиента
     */
    private String firstName;
    /**
     * Фамилия клиента
     */
    private String lastName;
    /**
     * Телефонный номер клиента
     */
    private Long phoneNumber;
    /**
     * Идентификатор чата
     */
    private Long chatId;
}
