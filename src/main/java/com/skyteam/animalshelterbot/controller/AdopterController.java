package com.skyteam.animalshelterbot.controller;

import com.skyteam.animalshelterbot.model.Adopter;
import com.skyteam.animalshelterbot.repository.AdopterRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdopterController {
    private final AdopterRepository adopterRepository;

    public AdopterController(AdopterRepository adopterRepository) {
        this.adopterRepository = adopterRepository;
    }

    @PostMapping
    @Operation(summary = "Добавить нового владельца животного в БД")
    public Adopter createAdopter(@RequestBody Adopter adopter) {
        return adopterRepository.save(adopter);
    }
}
