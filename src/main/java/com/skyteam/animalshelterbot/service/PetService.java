package com.skyteam.animalshelterbot.service;

import com.skyteam.animalshelterbot.model.Pet;
import com.skyteam.animalshelterbot.repository.PetRepository;
import org.springframework.stereotype.Service;

@Service
public class PetService {
    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public void createPet(Pet pet) {
        petRepository.save(pet);
    }

    public Pet findPet(Long id) {
        return petRepository.findById(id).get();
    }

    public Pet editPet(Pet pet) {
        return petRepository.save(pet);
    }

    public void deletePet(Long id) {
        petRepository.deleteById(id);
    }
}
