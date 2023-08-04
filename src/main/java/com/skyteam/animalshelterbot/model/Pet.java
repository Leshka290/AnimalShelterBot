package com.skyteam.animalshelterbot.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "pets")
@Data
@NoArgsConstructor
public class Pet {
    @Id
    @GeneratedValue
    private Long id;
    private String type;
    private String gender;
    private String name;
    private String breed;
    private Integer age;

    public Pet(String type, String gender, String name, String breed, Integer age) {
        this.type = type;
        this.gender = gender;
        this.name = name;
        this.breed = breed;
        this.age = age;
    }
}
