package com.skyteam.animalshelterbot.repository;

import com.skyteam.animalshelterbot.model.images.CatImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatImageRepository extends JpaRepository<CatImage, Long> {
}
