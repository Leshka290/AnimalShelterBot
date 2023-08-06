package com.skyteam.animalshelterbot.model;


import com.skyteam.animalshelterbot.listener.constants.PetType;
import com.skyteam.animalshelterbot.listener.constants.Sex;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "pets")
@Data
public class Pet {

    @Id
    @GeneratedValue
    private Long id;
    private PetType petType;
    private String nickName;
    private String breed;
    private Sex sex;
    private Integer age;
    private byte[] picture;

    @ManyToOne
    @JoinColumn(name = "adopter_id")
    private Adopter adopterId;

    public Pet(PetType petType, String nickName, String breed, Sex sex, Integer age) {
        this.petType = petType;
        this.nickName = nickName;
        this.breed = breed;
        this.sex = sex;
        this.age = age;
    }
}
