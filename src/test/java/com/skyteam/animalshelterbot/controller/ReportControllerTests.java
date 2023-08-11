package com.skyteam.animalshelterbot.controller;

import com.skyteam.animalshelterbot.listener.constants.PetType;
import com.skyteam.animalshelterbot.model.Pet;
import com.skyteam.animalshelterbot.model.Report.CatReport;
import com.skyteam.animalshelterbot.model.Report.DogReport;
import com.skyteam.animalshelterbot.model.Report.Report;
import com.skyteam.animalshelterbot.service.AdopterService;
import com.skyteam.animalshelterbot.service.CatReportService;
import com.skyteam.animalshelterbot.service.DogReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = ReportController.class)
public class ReportControllerTests {
    @MockBean
    private DogReportService dogReportService;

    @MockBean
    private CatReportService catReportService;

    @Autowired
    private ReportController<DogReport, Pet> dogReportController;

    @Autowired
    private ReportController<CatReport, Pet> catReportController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFindReportsByPet_Dog() {
        Long dogId = 1L;
        DogReport dogReport = new DogReport();
        List<DogReport> dogReports = new ArrayList<>();
        dogReports.add(dogReport);

        when(dogReportService.findReportsFromPet(dogId)).thenReturn(dogReports);

        Pet dog = new Pet();
        dog.setId(dogId);
        dog.setPetType(PetType.DOG);

        ResponseEntity<List<? extends Report>> response = dogReportController.findReportsByPet(dog);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dogReports, response.getBody());
        verify(dogReportService).findReportsFromPet(dogId);
    }

    @Test
    void testFindReportsByPet_Cat() {
        Long catId = 2L;
        CatReport catReport = new CatReport();
        List<CatReport> catReports = new ArrayList<>();
        catReports.add(catReport);

        when(catReportService.findReportsFromPet(catId)).thenReturn(catReports);

        Pet cat = new Pet();
        cat.setId(catId);
        cat.setPetType(PetType.CAT);

        ResponseEntity<List<? extends Report>> response = catReportController.findReportsByPet(cat);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(catReports, response.getBody());
        verify(catReportService).findReportsFromPet(catId);
    }

    @Test
    void testDeleteReportsByPet_Dog() {
        Long dogId = 1L;
        Pet dog = new Pet();
        dog.setId(dogId);
        dog.setPetType(PetType.DOG);

        when(dogReportService.deleteReportsByPet(dogId)).thenReturn(HttpStatus.OK);

        ResponseEntity<HttpStatus> response = dogReportController.deleteReportsByPet(dog);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(dogReportService).deleteReportsByPet(dogId);
    }

    @Test
    void testDeleteReportsByPet_Cat() {
        Long catId = 2L;
        Pet cat = new Pet();
        cat.setId(catId);
        cat.setPetType(PetType.CAT);

        when(catReportService.deleteReportsByPet(catId)).thenReturn(HttpStatus.OK);

        ResponseEntity<HttpStatus> response = catReportController.deleteReportsByPet(cat);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(catReportService).deleteReportsByPet(catId);
    }
}
