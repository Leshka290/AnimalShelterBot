package com.skyteam.animalshelterbot.controller;

import com.skyteam.animalshelterbot.model.Adopter;
import com.skyteam.animalshelterbot.model.Pet;
import com.skyteam.animalshelterbot.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Tag(name = "API для работы с животными приюта", description = "Содержит методы для создания животных приюта")
@RestController
@RequestMapping("/pet")
public class PetController {
    private final PetService petService;

    public PetController( PetService petService) {
        this.petService = petService;
    }

    @PostMapping
    @Operation(summary = "Добавить животноe в БД",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Животное добавлено в БД",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pet.class)
                            )
                    )
            })
    public Pet createPet(@RequestBody Pet pet) {
        return petService.createPet(pet);
    }
}
