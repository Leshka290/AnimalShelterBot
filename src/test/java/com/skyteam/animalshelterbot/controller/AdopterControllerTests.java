package com.skyteam.animalshelterbot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skyteam.animalshelterbot.model.Adopter;
import com.skyteam.animalshelterbot.repository.AdopterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
@ContextConfiguration(classes = AdopterController.class)
class AdopterControllerTest {

    @MockBean
    private AdopterRepository adopterRepository;

    @Autowired
    private AdopterController adopterController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adopterController).build();
    }

    @Test
    void testCreateAdopter() throws Exception {
        Adopter adopter = new Adopter();
        adopter.setId(1L);
        adopter.setFirstName("Danil");
        adopter.setLastName("Savinov");
        adopter.setUserName("danil_savinov");
        adopter.setChatId(123456L);
        adopter.setPhoneNumber("123456789");

        when(adopterRepository.save(any(Adopter.class))).thenReturn(adopter);

        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adopter)))
                        .andExpect(status().isOk());
    }
}
