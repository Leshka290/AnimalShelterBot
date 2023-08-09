package com.skyteam.animalshelterbot.service;


import org.springframework.http.HttpStatus;
import com.skyteam.animalshelterbot.exception.VolunteerNotFoundException;
import com.skyteam.animalshelterbot.model.Volunteer;
import com.skyteam.animalshelterbot.repository.VolunteerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Реализует CRUD операции с волонтерами.
 */
@Service
public class VolunteerService {
    private final VolunteerRepository volunteerRepository;


    public VolunteerService(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }


    public Volunteer findVolunteer(Long id) {
        return volunteerRepository.findById(id).orElseThrow(() -> new VolunteerNotFoundException("Волонтер не был найден"));
    }

    /**
     * Проверить волонтера
     * @param id идентификатор волонтера
     */
    public boolean checkVolunteer(Long id) {
        try {
            return findVolunteer(id) != null;
        } catch (VolunteerNotFoundException e) {
            return false;
        }
    }

    /**
     * Добавить волонтера
     * @param volunteer волонтер
     * @return волонтер
     */
    public Volunteer saveVolunteer(Volunteer volunteer) {
        return volunteerRepository.save(volunteer);
    }

    /**
     * Удалить волонтера
     * @param volunteer волонтер
     * @return статус ок
     */
    public HttpStatus deleteVolunteer(Volunteer volunteer) {
        volunteerRepository.delete(volunteer);
        return HttpStatus.OK;
    }

    /**
     * Редактировать волонтера
     * @param volunteer волонтер
     * @return волонтер
     */
    public Volunteer putVolunteer(Volunteer volunteer) {
        return volunteerRepository.save(volunteer);
    }

    /**
     * Найти свободного волонтера
     * @return волонтер
     */
    public Volunteer findFreeVolunteer() {
        return volunteerRepository.findVolunteersByIsFreeTrue().stream().findAny()
                .orElseThrow(() -> new VolunteerNotFoundException("Все волонтеры на данный момент заняты. Просим вас подождать"));
    }

    /**
     * Найти всех волонтеров
     * @return коллекция волонтеров
     */
    public List<Volunteer> gatAllVolunteers() {
        return volunteerRepository.findAll();
    }
}
