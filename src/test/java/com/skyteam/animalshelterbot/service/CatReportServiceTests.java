package com.skyteam.animalshelterbot.service;

import com.skyteam.animalshelterbot.model.Report.CatReport;
import com.skyteam.animalshelterbot.model.Report.DogReport;
import com.skyteam.animalshelterbot.repository.CatImageRepository;
import com.skyteam.animalshelterbot.repository.CatReportRepository;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = CatReportService.class)
public class CatReportServiceTests {
    @MockBean
    private CatReportRepository catReportRepository;

    @MockBean
    private CatImageRepository catImageRepository;
    @Autowired
    private CatReportService catReportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testPutReport() {
        CatReport catReport = new CatReport(LocalDate.now(), "testDiet", "testDescription", "testChanges");

        when(catReportRepository.save(catReport)).thenReturn(catReport);

        CatReport savedReport = catReportService.putReport(catReport);

        assertEquals(catReport, savedReport);
    }

    @Test
    void testDeleteReportsByPet() {
        Long petId = 123L;

        HttpStatus status = catReportService.deleteReportsByPet(petId);

        assertEquals(HttpStatus.OK, status);
        verify(catReportRepository).deleteCatReportsByPetId(petId);
    }

    @Test
    void testGetAllReports() {
        List<CatReport> expectedReports = new ArrayList<>();
        expectedReports.add(new CatReport(LocalDate.now(), "testDiet", "testDescription", "testChanges"));
        expectedReports.add(new CatReport(LocalDate.of(2023, 12, 12), "testDietTest", "testDescriptionTest", "testChangesTest"));
        expectedReports.add(new CatReport(LocalDate.of(2015, 5, 15), "testDietTestTest", "testDescriptionTestTest", "testChangesTestTest"));


        when(catReportRepository.findAll()).thenReturn(expectedReports);

        List<CatReport> actualReports = catReportService.getAllReports();

        assertEquals(expectedReports, actualReports);
    }

    @Test
    void testFindReportsFromPet() {
        Long petId = 456L;
        List<CatReport> expectedReports = new ArrayList<>();
        expectedReports.add(new CatReport(LocalDate.now(), "testDiet", "testDescription", "testChanges"));
        expectedReports.add(new CatReport(LocalDate.of(2023, 12, 12), "testDietTest", "testDescriptionTest", "testChangesTest"));
        expectedReports.add(new CatReport(LocalDate.of(2015, 5, 15), "testDietTestTest", "testDescriptionTestTest", "testChangesTestTest"));
        when(catReportRepository.findCatReportsByPetId(petId)).thenReturn(expectedReports);

        List<CatReport> actualReports = catReportService.findReportsFromPet(petId);

        assertEquals(expectedReports, actualReports);
    }
}
