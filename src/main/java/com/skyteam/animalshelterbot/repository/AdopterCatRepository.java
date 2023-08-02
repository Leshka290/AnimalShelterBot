package com.skyteam.animalshelterbot.repository;

import com.skyteam.animalshelterbot.model.AdopterCat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdopterCatRepository extends JpaRepository<AdopterCat, Long> {
}
