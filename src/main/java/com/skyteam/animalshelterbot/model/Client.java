package com.skyteam.animalshelterbot.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Абстрактный класс клиент приюта для кошек(или собак) со свойствами:
 * <p>
 * <b>firstName</b>,<b>lastName</b>,<b>phoneNumber</b>,<b>chatId</b>
 *
 * @author youcanwakemeup
 */

@Data
@NoArgsConstructor
public class Client {
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


    public Client(String firstName, String lastName, Long phoneNumber, Long chatId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.chatId = chatId;
    }

}
