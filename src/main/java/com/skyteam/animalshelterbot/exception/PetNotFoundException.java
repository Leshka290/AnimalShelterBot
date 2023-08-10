package com.skyteam.animalshelterbot.exception;

import lombok.Getter;

@Getter
public class PetNotFoundException extends RuntimeException{
    private long id;

    public PetNotFoundException(String message) {
        super(message);
    }
    public PetNotFoundException(long id) {
        this.id = id;
    }

}
