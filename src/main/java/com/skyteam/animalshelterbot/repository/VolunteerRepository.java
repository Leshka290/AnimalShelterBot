package com.skyteam.animalshelterbot.repository;

import com.skyteam.animalshelterbot.model.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolunteerRepository extends JpaRepository<Volunteer,Long> {
}
