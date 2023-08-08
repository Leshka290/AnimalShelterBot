package com.skyteam.animalshelterbot.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClientNotFoundException extends RuntimeException {

    private final Long id;
}
