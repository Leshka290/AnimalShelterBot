package com.skyteam.animalshelterbot.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VolunteerNotFoundException extends RuntimeException {
    public VolunteerNotFoundException(String description) {
        super(description);
    }
}
