package com.skyteam.animalshelterbot.repository;

import com.skyteam.animalshelterbot.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet,Long> {
}
