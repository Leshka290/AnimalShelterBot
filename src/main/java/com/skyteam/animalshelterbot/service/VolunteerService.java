package com.skyteam.animalshelterbot.service;


import org.springframework.http.HttpStatus;
import com.skyteam.animalshelterbot.exception.VolunteerNotFoundException;
import com.skyteam.animalshelterbot.model.Volunteer;
import com.skyteam.animalshelterbot.repository.VolunteerRepository;

import java.util.List;

public class VolunteerService {
    private final VolunteerRepository volunteerRepository;


    public VolunteerService(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }


    public Volunteer findVolunteer(Long id) {
        return volunteerRepository.findById(id).orElseThrow(() -> new VolunteerNotFoundException("Волонтер не был найден"));
    }


    public boolean checkVolunteer(Long id) {
        try {
            return findVolunteer(id) != null;
        } catch (VolunteerNotFoundException e) {
            return false;
        }
    }


    public Volunteer saveVolunteer(Volunteer volunteer) {
        return volunteerRepository.save(volunteer);
    }


    public HttpStatus deleteVolunteer(Volunteer volunteer) {
        volunteerRepository.delete(volunteer);
        return HttpStatus.OK;
    }


    public Volunteer putVolunteer(Volunteer volunteer) {
        return volunteerRepository.save(volunteer);
    }


    public Volunteer findFreeVolunteer() {
        return volunteerRepository.findVolunteersByIsFreeTrue().stream().findAny()
                .orElseThrow(() -> new VolunteerNotFoundException("Все волонтеры на данный момент заняты. Просим вас подождать"));
    }


    public List<Volunteer> gatAllVolunteers() {
        return volunteerRepository.findAll();
    }
}
