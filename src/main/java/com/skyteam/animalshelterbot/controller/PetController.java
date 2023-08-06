package com.skyteam.animalshelterbot.controller;

import com.skyteam.animalshelterbot.model.Pet;
import com.skyteam.animalshelterbot.repository.PetRepository;
import com.skyteam.animalshelterbot.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pet")
public class PetController {
    private final PetService petService;

    public PetController( PetService petService) {
        this.petService = petService;
    }

    @PostMapping
    @Operation(summary = "Добавить животное в БД")
    public Pet createPet(@RequestBody Pet pet) {
        return petService.createPet(pet);
    }
}
