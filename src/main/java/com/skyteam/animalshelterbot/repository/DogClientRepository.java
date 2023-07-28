package com.skyteam.animalshelterbot.repository;

import com.skyteam.animalshelterbot.model.DogClient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DogClientRepository extends JpaRepository<DogClient, Long> {
    boolean existsByChatId(long chatId);

}
