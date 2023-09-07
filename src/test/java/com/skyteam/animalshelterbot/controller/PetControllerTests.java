package com.skyteam.animalshelterbot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skyteam.animalshelterbot.listener.constants.PetType;
import com.skyteam.animalshelterbot.listener.constants.Sex;
import com.skyteam.animalshelterbot.model.Adopter;
import com.skyteam.animalshelterbot.model.Pet;
import com.skyteam.animalshelterbot.repository.AdopterRepository;
import com.skyteam.animalshelterbot.service.AdopterService;
import com.skyteam.animalshelterbot.service.PetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration(classes = PetController.class)
public class PetControllerTests {
    @MockBean
    private PetService petService;

    @Autowired
    private PetController petController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(petController).build();
    }

    @Test
    void testCreatePet() throws Exception {
        Pet pet = new Pet();
        pet.setId(1L);
        pet.setPetType(PetType.DOG);
        pet.setNickName("Гайка");
        pet.setBreed("Хаски");
        pet.setSex(Sex.MALE);
        pet.setAge(2);

        when(petService.createPet(any(Pet.class))).thenReturn(pet);

        mockMvc.perform(post("/pet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pet)))
                .andExpect(status().isOk());
    }
}
