package com.skyteam.animalshelterbot.repository;

import com.skyteam.animalshelterbot.model.Adopter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdopterRepository extends JpaRepository<Adopter, Long> {
    Adopter findByChatId(long chatId);
}
