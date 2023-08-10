package com.skyteam.animalshelterbot.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class VolunteerNotFoundException extends RuntimeException {
    private long id;

    public VolunteerNotFoundException() {
        super();
    }

    public VolunteerNotFoundException(String message) {
        super(message);
    }

    public VolunteerNotFoundException(long id) {
        this.id = id;
    }
}
