package com.skyteam.animalshelterbot.repository;

import com.skyteam.animalshelterbot.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

}
