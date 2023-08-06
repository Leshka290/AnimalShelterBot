package com.skyteam.animalshelterbot.model;


import com.skyteam.animalshelterbot.listener.constants.PetType;
import com.skyteam.animalshelterbot.listener.constants.Sex;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "pets")
@Data
@NoArgsConstructor
public class Pet {

    @Id
    @GeneratedValue
    private Long id;
    private String nickName;
    private PetType petType;
    private String breed;
    private Sex sex;
    private int age;
    private byte[] picture;

    @ManyToOne
    @JoinColumn(name = "adopter_id")
    private Adopter adopterId;

    public Pet(String nickName, PetType petType, String breed, Sex sex, int age, byte[] picture) {
        this.nickName = nickName;
        this.petType = petType;
        this.breed = breed;
        this.sex = sex;
        this.age = age;
        this.picture = picture;
    }
}
