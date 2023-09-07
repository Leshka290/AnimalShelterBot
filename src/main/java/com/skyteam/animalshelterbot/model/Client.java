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
}
