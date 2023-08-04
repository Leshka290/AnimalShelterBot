package com.skyteam.animalshelterbot.exeptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClientNotFoundException extends RuntimeException {

    private final Long id;
}
