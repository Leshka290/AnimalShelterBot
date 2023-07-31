package com.skyteam.animalshelterbot.repository;

import com.skyteam.animalshelterbot.model.CatClient;
import com.skyteam.animalshelterbot.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatClientRepository extends JpaRepository<CatClient, Long> {
    //boolean existsByChatId(long chatId);
}
