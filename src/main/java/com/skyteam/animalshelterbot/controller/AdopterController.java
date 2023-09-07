package com.skyteam.animalshelterbot.controller;

import com.skyteam.animalshelterbot.model.Adopter;
import com.skyteam.animalshelterbot.repository.AdopterRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "API для работы с усыновителями", description = "Содержит методы для создания усыновителей")
@RestController
public class AdopterController {
    private final AdopterRepository adopterRepository;

    public AdopterController(AdopterRepository adopterRepository) {
        this.adopterRepository = adopterRepository;
    }

    @PostMapping
    @Operation(summary = "Добавить нового владельца животного в БД",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Владелец добавлен в БД",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Adopter.class)
                            )
                    )
            })
    public Adopter createAdopter(@RequestBody Adopter adopter) {
        return adopterRepository.save(adopter);
    }
}
