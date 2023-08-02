package com.skyteam.animalshelterbot.repository;

import com.skyteam.animalshelterbot.model.DogClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DogClientRepository extends JpaRepository<DogClient, Long> {

}
