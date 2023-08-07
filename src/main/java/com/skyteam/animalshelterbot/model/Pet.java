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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public PetType getPetType() {
        return petType;
    }

    public void setPetType(PetType petType) {
        this.petType = petType;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public Adopter getAdopterId() {
        return adopterId;
    }

    public void setAdopterId(Adopter adopterId) {
        this.adopterId = adopterId;
    }

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
