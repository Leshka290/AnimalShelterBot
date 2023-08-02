package com.skyteam.animalshelterbot.repository;

import com.skyteam.animalshelterbot.model.AdopterDog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdopterDogRepository extends JpaRepository<AdopterDog, Long> {
}
