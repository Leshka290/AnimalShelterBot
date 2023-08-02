package com.skyteam.animalshelterbot.repository;

import com.skyteam.animalshelterbot.model.CatClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatClientRepository extends JpaRepository<CatClient, Long> {
}
