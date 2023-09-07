package com.skyteam.animalshelterbot.model;


import com.skyteam.animalshelterbot.listener.constants.PetType;
import com.skyteam.animalshelterbot.listener.constants.Sex;
import lombok.*;

import javax.persistence.*;

/**
 * Класс описывающий сущность животного со свойствами:
 * <p>
 * <b>id</b>,<b>petType</b>,<b>nickName</b>,<b>breed</b>,<b>sex</b>,<b>age</b>,<b>picture</b>,<b>adopterId</b>
 * <p>
 * @author leshka290
 */
@Entity
@Table(name = "pets")
@Data
@NoArgsConstructor
public class Pet {
    /**
     * Идентификатор в БД
     */
    @Id
    @GeneratedValue
    private Long id;
    /**
     * Тип животного
     */
    private PetType petType;
    /**
     * Имя животного
     */
    private String nickName;
    /**
     * Порода животного
     */
    private String breed;
    /**
     * Пол животного
     */
    private Sex sex;
    /**
     * Возраст животного
     */
    private Integer age;
    /**
     * Фото животного
     */
    private byte[] picture;
    /**
     * Идентификатор усыновителя животного
     */
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
