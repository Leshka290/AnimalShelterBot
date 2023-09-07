package com.skyteam.animalshelterbot.repository;

import com.skyteam.animalshelterbot.model.QuestionsForVolunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionsForVolunteerRepository extends JpaRepository<QuestionsForVolunteer,Long> {
}
