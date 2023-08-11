package com.skyteam.animalshelterbot.model;

import com.skyteam.animalshelterbot.listener.constants.PetType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Класс клиент приюта со свойствами:
 * <p>
 * <b>firstName</b>,<b>lastName</b>,<b>phoneNumber</b>,<b>chatId</b>
 *
 * @author youcanwakemeup
 */

@Data
@NoArgsConstructor
@Entity
public class Client {

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

    /**
     * Последний выбор приюта
     */
    private PetType lastPetType;


    public Client(Long chatId, PetType lastPetType) {
        this.chatId = chatId;
        this.lastPetType = lastPetType;
    }

    public Client(String firstName, String lastName, long phoneNumber, long chatId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.chatId = chatId;
    }
}
