package com.skyteam.animalshelterbot.service;

import com.skyteam.animalshelterbot.model.Adopter;
import com.skyteam.animalshelterbot.model.Report.DogReport;
import com.skyteam.animalshelterbot.repository.DogImageRepository;
import com.skyteam.animalshelterbot.repository.DogReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = DogReportService.class)
public class DogRepostServiceTests {
    @MockBean
    private DogReportRepository dogReportRepository;

    @MockBean
    private DogImageRepository dogImageRepository;
    @Autowired
    private DogReportService dogReportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testPutReport() {
        DogReport dogReport = new DogReport(LocalDate.now(), "testDiet", "testDescription", "testChanges");

        when(dogReportRepository.save(dogReport)).thenReturn(dogReport);

        DogReport savedReport = dogReportService.putReport(dogReport);

        assertEquals(dogReport, savedReport);
    }

    @Test
    void testDeleteReportsByPet() {
        Long petId = 123L;

        HttpStatus status = dogReportService.deleteReportsByPet(petId);

        assertEquals(HttpStatus.OK, status);
        verify(dogReportRepository).deleteDogReportsByPetId(petId);
    }

    @Test
    void testGetAllReports() {
        List<DogReport> expectedReports = new ArrayList<>();
        expectedReports.add(new DogReport(LocalDate.now(), "testDiet", "testDescription", "testChanges"));
        expectedReports.add(new DogReport(LocalDate.of(2023, 12, 12), "testDietTest", "testDescriptionTest", "testChangesTest"));
        expectedReports.add(new DogReport(LocalDate.of(2015, 5, 15), "testDietTestTest", "testDescriptionTestTest", "testChangesTestTest"));


        when(dogReportRepository.findAll()).thenReturn(expectedReports);

        List<DogReport> actualReports = dogReportService.getAllReports();

        assertEquals(expectedReports, actualReports);
    }

    @Test
    void testFindReportsFromPet() {
        Long petId = 456L;
        List<DogReport> expectedReports = new ArrayList<>();
        expectedReports.add(new DogReport(LocalDate.now(), "testDiet", "testDescription", "testChanges"));
        expectedReports.add(new DogReport(LocalDate.of(2023, 12, 12), "testDietTest", "testDescriptionTest", "testChangesTest"));
        expectedReports.add(new DogReport(LocalDate.of(2015, 5, 15), "testDietTestTest", "testDescriptionTestTest", "testChangesTestTest"));
        when(dogReportRepository.findDogReportsByPetId(petId)).thenReturn(expectedReports);

        List<DogReport> actualReports = dogReportService.findReportsFromPet(petId);

        assertEquals(expectedReports, actualReports);
    }
}
