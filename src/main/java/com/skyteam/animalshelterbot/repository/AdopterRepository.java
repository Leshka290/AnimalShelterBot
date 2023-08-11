package com.skyteam.animalshelterbot.repository;

import com.skyteam.animalshelterbot.model.Adopter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdopterRepository extends JpaRepository<Adopter, Long> {
    Adopter findByChatId(long chatId);
}
