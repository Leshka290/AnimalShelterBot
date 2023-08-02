package com.skyteam.animalshelterbot.model;

import com.skyteam.animalshelterbot.listener.constants.AdopterStatus;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class AdopterCat {

    @Id
    private Long chatId;
    private String firstName;
    private String lastName;
    private String userName;
    private String passport;
    private int age;
    private String phoneNumber;

    private Long volunteerId;

    private AdopterStatus status;

//    @OneToMany(mappedBy = "adopterId")
//    private Collection<Pet> pets;
}
