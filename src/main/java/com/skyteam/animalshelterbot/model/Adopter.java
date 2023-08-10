package com.skyteam.animalshelterbot.model;

import com.skyteam.animalshelterbot.listener.constants.AdopterStatus;
import com.skyteam.animalshelterbot.listener.constants.PetType;
import com.skyteam.animalshelterbot.listener.constants.ReportStatus;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Collection;

/**
 * Класс усыновителя приюта для собак со свойствами:
 * <p>
 * <b>id</b>,<b>chatId</b>,<b>firstName</b>,<b>lastName</b>,<b>userName</b>
 * ,<b>passport</b>,<b>age</b>,<b>phoneNumber</b>,<b>volunteerId</b>,<b>status</b>
 *
 * @author leshka290
 */
@Entity
@Data
public class Adopter {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * Идентификатор чата
     */
    private Long chatId;
    /**
     * Имя усыновителя
     */
    private String firstName;
    /**
     * Фамилия усыновителя
     */
    private String lastName;
    /**
     * Имя пользователя усыновителя
     */
    private String userName;
    /**
     * Номер паспорта усыновителя
     */
    private String passport;
    /**
     * Возраст усыновителя
     */
    private int age;
    /**
     * Телефонный номер усыновителя
     */
    private String phoneNumber;

    /**
     * Id волонтера, взаимодействующего с усыновителем
     */
    private Long volunteerId;
    /**
     * Тип животного усыновителя
     */
    private PetType petType;
    /**
     * Стадии усыновления
     */
    private AdopterStatus status;

    /**
     * Стадии отправки отчета
     */
    private ReportStatus reportStatus;

    @OneToMany(mappedBy = "adopterId")
    private Collection<Pet> pets;

    public Adopter(String firstName, String lastName, String username, String phoneNumber, long chatId, PetType peteType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = username;
        this.chatId = chatId;
        this.phoneNumber = phoneNumber;
        this.petType = peteType;
    }

    public Adopter() {

    }
}
