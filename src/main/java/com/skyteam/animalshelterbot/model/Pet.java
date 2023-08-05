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
    private String nickName;
    private PetType petType;
    private Sex sex;
    private byte[] picture;

    @ManyToOne
    @JoinColumn(name = "adopter_id")
    private Adopter adopterId;

}
