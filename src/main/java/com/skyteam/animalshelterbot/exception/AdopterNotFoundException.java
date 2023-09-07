package com.skyteam.animalshelterbot.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdopterNotFoundException extends RuntimeException {

    private final Long id;
}
