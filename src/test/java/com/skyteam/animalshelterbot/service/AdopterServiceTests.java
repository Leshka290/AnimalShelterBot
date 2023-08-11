package com.skyteam.animalshelterbot.service;

import com.skyteam.animalshelterbot.exception.AdopterNotFoundException;
import com.skyteam.animalshelterbot.listener.constants.ReportStatus;
import com.skyteam.animalshelterbot.model.Adopter;
import com.skyteam.animalshelterbot.repository.AdopterRepository;
import com.skyteam.animalshelterbot.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = AdopterService.class)
public class AdopterServiceTests {
    @MockBean
    private AdopterRepository adopterRepository;

    @MockBean
    private ClientRepository clientRepository;
    @Autowired
    private AdopterService adopterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAdopter() {
        Adopter adopter = new Adopter();
        when(adopterRepository.save(any(Adopter.class))).thenReturn(adopter);

        Adopter createdAdopter = adopterService.createAdopter(adopter);

        assertEquals(adopter, createdAdopter);
        verify(adopterRepository, times(1)).save(adopter);
    }

    @Test
    void testReadAdopter() {
        long adopterId = 1L;
        Adopter adopter = new Adopter();
        when(adopterRepository.findById(adopterId)).thenReturn(java.util.Optional.of(adopter));

        Adopter readAdopter = adopterService.readAdopter(adopterId);

        assertEquals(adopter, readAdopter);
    }

    @Test
    void testReadAdopterNotFound() {
        long adopterId = 1L;
        when(adopterRepository.findById(adopterId)).thenReturn(java.util.Optional.empty());

        assertThrows(AdopterNotFoundException.class, () -> adopterService.readAdopter(adopterId));
    }

    @Test
    void testUpdateAdopter() {
        Adopter adopter = new Adopter();
        when(adopterRepository.findById(adopter.getId())).thenReturn(java.util.Optional.of(adopter));
        when(adopterRepository.save(any(Adopter.class))).thenReturn(adopter);

        Adopter updatedAdopter = adopterService.updateAdopter(adopter);

        assertEquals(adopter, updatedAdopter);
        verify(adopterRepository, times(1)).save(adopter);
    }

    @Test
    void testUpdateAdopterNotFound() {
        Adopter adopter = new Adopter();
        when(adopterRepository.findById(adopter.getId())).thenReturn(java.util.Optional.empty());

        Adopter updatedAdopter = adopterService.updateAdopter(adopter);

        assertNull(updatedAdopter);
        verify(adopterRepository, never()).save(any(Adopter.class));
    }

    @Test
    void testGetUpdateStatus() {
        long chatId = 12345L;
        Adopter adopter = new Adopter();
        adopter.setReportStatus(ReportStatus.UPDATED);
        when(adopterRepository.findByChatId(chatId)).thenReturn(adopter);

        ReportStatus status = adopterService.getUpdateStatus(chatId);

        assertEquals(ReportStatus.UPDATED, status);
    }

    @Test
    void testGetUpdateStatusDefault() {
        long chatId = 12345L;
        when(adopterRepository.findByChatId(chatId)).thenReturn(null);

        ReportStatus status = adopterService.getUpdateStatus(chatId);

        assertEquals(ReportStatus.DEFAULT, status);
    }

    @Test
    void testSetUpdateStatus() {
        long chatId = 12345L;
        Adopter adopter = new Adopter();
        adopter.setReportStatus(ReportStatus.DEFAULT);
        when(adopterRepository.findByChatId(chatId)).thenReturn(adopter);
        when(adopterRepository.save(any(Adopter.class))).thenReturn(adopter);

        adopterService.setUpdateStatus(chatId, ReportStatus.UPDATED);

        assertEquals(ReportStatus.UPDATED, adopter.getReportStatus());
        verify(adopterRepository, times(1)).save(adopter);
    }

    @Test
    void testSetUpdateStatusNotFound() {
        long chatId = 12345L;
        when(adopterRepository.findByChatId(chatId)).thenReturn(null);

        assertThrows(AdopterNotFoundException.class, () -> adopterService.setUpdateStatus(chatId, ReportStatus.UPDATED));
    }
}
