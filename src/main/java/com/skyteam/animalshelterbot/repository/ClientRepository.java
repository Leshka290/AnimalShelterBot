package com.skyteam.animalshelterbot.repository;

import com.skyteam.animalshelterbot.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByChatId(long chatId);
}
