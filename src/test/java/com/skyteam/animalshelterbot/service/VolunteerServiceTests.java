package com.skyteam.animalshelterbot.service;

import com.skyteam.animalshelterbot.exception.VolunteerNotFoundException;
import com.skyteam.animalshelterbot.model.Volunteer;
import com.skyteam.animalshelterbot.repository.VolunteerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
@ContextConfiguration(classes = VolunteerService.class)
class VolunteerServiceTest {

    @MockBean
    private VolunteerRepository volunteerRepository;

    @Autowired
    private VolunteerService volunteerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindVolunteer() {
        Long id = 1L;
        Volunteer volunteer = new Volunteer();
        when(volunteerRepository.findById(id)).thenReturn(Optional.of(volunteer));

        Volunteer foundVolunteer = volunteerService.findVolunteer(id);

        assertEquals(volunteer, foundVolunteer);
    }

    @Test
    void testFindVolunteerNotFound() {
        Long id = 1L;
        when(volunteerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(VolunteerNotFoundException.class, () -> volunteerService.findVolunteer(id));
    }

    @Test
    void testCheckVolunteerExists() {
        Long id = 1L;
        Volunteer volunteer = new Volunteer();
        when(volunteerRepository.findById(id)).thenReturn(Optional.of(volunteer));

        boolean exists = volunteerService.checkVolunteer(id);

        assertTrue(exists);
    }

    @Test
    void testCheckVolunteerNotExists() {
        Long id = 1L;
        when(volunteerRepository.findById(id)).thenReturn(Optional.empty());

        boolean exists = volunteerService.checkVolunteer(id);

        assertFalse(exists);
    }

    @Test
    void testSaveVolunteer() {
        Volunteer volunteer = new Volunteer();
        when(volunteerRepository.save(any(Volunteer.class))).thenReturn(volunteer);

        Volunteer savedVolunteer = volunteerService.saveVolunteer(volunteer);

        assertEquals(volunteer, savedVolunteer);
    }

    @Test
    void testDeleteVolunteer() {
        Volunteer volunteer = new Volunteer();
        HttpStatus result = volunteerService.deleteVolunteer(volunteer);

        verify(volunteerRepository, times(1)).delete(volunteer);
        assertEquals(HttpStatus.OK, result);
    }

    @Test
    void testPutVolunteer() {
        Volunteer volunteer = new Volunteer();
        when(volunteerRepository.save(any(Volunteer.class))).thenReturn(volunteer);

        Volunteer updatedVolunteer = volunteerService.putVolunteer(volunteer);

        assertEquals(volunteer, updatedVolunteer);
    }

    @Test
    void testFindFreeVolunteer() {
        Volunteer volunteer = new Volunteer();
        when(volunteerRepository.findVolunteersByIsFreeTrue()).thenReturn(Collections.singletonList(volunteer));

        Volunteer freeVolunteer = volunteerService.findFreeVolunteer();

        assertEquals(volunteer, freeVolunteer);
    }

    @Test
    void testFindFreeVolunteerNotFound() {
        when(volunteerRepository.findVolunteersByIsFreeTrue()).thenReturn(Collections.emptyList());

        assertThrows(VolunteerNotFoundException.class, () -> volunteerService.findFreeVolunteer());
    }

    @Test
    void testGetAllVolunteers() {
        List<Volunteer> volunteers = Collections.singletonList(new Volunteer());
        when(volunteerRepository.findAll()).thenReturn(volunteers);

        List<Volunteer> allVolunteers = volunteerService.getAllVolunteers();

        assertEquals(volunteers, allVolunteers);
    }
}
