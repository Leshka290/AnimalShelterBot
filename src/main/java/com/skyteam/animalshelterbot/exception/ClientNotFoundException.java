package com.skyteam.animalshelterbot.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ClientNotFoundException extends RuntimeException {

    private long id;
    public ClientNotFoundException(String message) {
        super(message);
    }
    public ClientNotFoundException(long id) {
        this.id = id;
    }
}
