package com.skyteam.animalshelterbot.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
@Entity
public class DogClient extends Client {
    @Id
    @GeneratedValue
    private Long id;

    public DogClient() {}

    public DogClient(String firstName, String lastName, Long phoneNumber, Long chatId) {
        super(firstName, lastName, phoneNumber, chatId);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
