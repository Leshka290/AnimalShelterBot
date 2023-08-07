package com.skyteam.animalshelterbot.service;

import com.skyteam.animalshelterbot.model.Pet;
import com.skyteam.animalshelterbot.repository.PetRepository;
import org.springframework.stereotype.Service;

/**
 * Реализует CRUD операции с животными
 */
@Service
public class PetService {
    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    /**
     * Добавляет животное в БД
     * @param pet животное
     * @return животное
     */
    public Pet createPet(Pet pet) {
        return petRepository.save(pet);
    }

    /**
     * Находит животное в БД по id
     * @param id идентификатор животного в БД
     * @return животное
     */
    public Pet findPet(Long id) {
        return petRepository.findById(id).get();
    }

    /**
     * Редактирует животное в БД
     * @param pet животное
     * @return животное
     */
    public Pet editPet(Pet pet) {
        return petRepository.save(pet);
    }

    /**
     * Удаляет животное из БД по id
     * @param id идентификатор животного
     */
    public void deletePet(Long id) {
        petRepository.deleteById(id);
    }
}
