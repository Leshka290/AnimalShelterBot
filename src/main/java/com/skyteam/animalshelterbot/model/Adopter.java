package com.skyteam.animalshelterbot.model;

import com.skyteam.animalshelterbot.listener.constants.AdopterStatus;
import lombok.Data;

import javax.persistence.Entity;
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
    private Long id;
    private Long chatId;
    private String firstName;
    private String lastName;
    private String userName;
    private String passport;
    private int age;
    private String phoneNumber;

    private Long volunteerId;

    private AdopterStatus status;

    @OneToMany(mappedBy = "adopterId")
    private Collection<Pet> pets;
}
