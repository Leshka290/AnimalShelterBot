package com.skyteam.animalshelterbot.service;

import com.skyteam.animalshelterbot.model.Pet;
import com.skyteam.animalshelterbot.repository.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
@ContextConfiguration(classes = PetService.class)
public class PetServiceTests {

    @MockBean
    private PetRepository petRepository;

    private PetService petService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        petService = new PetService(petRepository);
    }

    @Test
    public void testCreatePet() {
        Pet pet = new Pet();
        pet.setNickName("Пушистик");

        petService.createPet(pet);

        verify(petRepository, times(1)).save(pet);
    }

    @Test
    public void testFindPetById() {
        Pet pet = new Pet();
        pet.setId(1L);
        when(petRepository.findById(1L)).thenReturn(java.util.Optional.of(pet));

        Pet foundPet = petService.findPet(1L);

        assertNotNull(foundPet);
        assertEquals(1L, foundPet.getId());
    }

    @Test
    public void testEditPet() {
        Pet pet = new Pet();
        pet.setId(1L);
        pet.setNickName("Толя");

        when(petRepository.save(pet)).thenReturn(pet);

        Pet editedPet = petService.editPet(pet);

        assertNotNull(editedPet);
        assertEquals("Толя", editedPet.getNickName());
    }

    @Test
    public void testDeletePet() {
        Long petId = 1L;

        petService.deletePet(petId);

        verify(petRepository, times(1)).deleteById(petId);
    }
}
