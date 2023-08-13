package com.skyteam.animalshelterbot.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ResourceBundle;

import com.pengrad.telegrambot.response.SendResponse;
import com.skyteam.animalshelterbot.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;import com.pengrad.telegrambot.response.GetFileResponse;
import com.skyteam.animalshelterbot.exception.ClientNotFoundException;
import com.skyteam.animalshelterbot.listener.constants.PetType;
import com.skyteam.animalshelterbot.model.Adopter;
import com.skyteam.animalshelterbot.model.Client;
import com.skyteam.animalshelterbot.model.Report.CatReport;
import com.skyteam.animalshelterbot.model.Report.DogReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = SaveEntityService.class)
public class SaveEntityServiceTests {
    @MockBean
    private TelegramBot telegramBot;

    @MockBean
    private VolunteerService volunteerService;

    @MockBean
    private ClientRepository clientRepository;

    @MockBean
    private AdopterRepository adopterRepository;

    @MockBean
    private CatReportRepository catReportRepository;

    @MockBean
    private DogReportRepository dogReportRepository;

    @MockBean
    private MenuBuilderService menuBuilderService;
@MockBean
private CatImageRepository catImageRepository;
    @MockBean
    private DogImageRepository dogImageRepository;
    @MockBean
    private ResourceBundle messagesBundle;

    @Autowired
    private SaveEntityService saveEntityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveClient() {
        long chatId = 123456789;
        PetType lastPetType = PetType.CAT;
        Client client = new Client(chatId, lastPetType);

        when(clientRepository.findByChatId(chatId)).thenReturn(null);

        saveEntityService.saveClient(chatId, lastPetType);

        verify(clientRepository).save(client);
    }

    @Test
    void testSaveAdopter_Success() {
        Update update = mock(Update.class);
        Adopter adopter = new Adopter("Danil", "Smirnov", "danil_smirnov", "1234567890", 123456789, PetType.CAT);

        when(update.message().contact()).thenReturn(update.message().contact());
        when(update.message().contact().firstName()).thenReturn("Danil");
        when(update.message().contact().lastName()).thenReturn("Smirnov");
        when(update.message().chat().username()).thenReturn("danil_smirnov");
        when(update.message().contact().phoneNumber()).thenReturn("1234567890");
        when(update.message().chat().id()).thenReturn(123456789L);
        when(clientRepository.findByChatId(anyLong())).thenReturn(mock(Client.class));
        when(adopterRepository.findByChatId(anyLong())).thenReturn(null);
        when(adopterRepository.save(any(Adopter.class))).thenReturn(adopter);
        when(messagesBundle.getString(anyString())).thenReturn("Message");

        saveEntityService.saveAdopter(update);

        verify(adopterRepository).save(adopter);
        when(telegramBot.execute(any(SendMessage.class))).thenReturn(mock(SendResponse.class));
    }

    @Test
    void testSaveAdopter_AlreadyExists() {
        Update update = mock(Update.class);

        when(update.message().contact()).thenReturn(update.message().contact());
        when(update.message().chat().id()).thenReturn(123456789L);
        when(adopterRepository.findByChatId(anyLong())).thenReturn(mock(Adopter.class));
        when(messagesBundle.getString(anyString())).thenReturn("Message");

        saveEntityService.saveAdopter(update);

        when(telegramBot.execute(any(SendMessage.class))).thenReturn(mock(SendResponse.class));
    }

    @Test
    void testSaveAdopter_ClientNotFound() {
        Update update = mock(Update.class);

        when(update.message().contact()).thenReturn(update.message().contact());
        when(update.message().chat().id()).thenReturn(123456789L);
        when(clientRepository.findByChatId(anyLong())).thenReturn(null);
        when(adopterRepository.findByChatId(anyLong())).thenReturn(null);
        when(messagesBundle.getString(anyString())).thenReturn("Message");

        assertThrows(ClientNotFoundException.class, () -> {
            saveEntityService.saveAdopter(update);
        });

        when(telegramBot.execute(any(SendMessage.class))).thenReturn(mock(SendResponse.class));
    }

    @Test
    void testSaveAdoptionReport_Cat_Success() {
        long chatId = 123456789L;
        Adopter adopter = new Adopter("Danil", "Smirnov", "danil_smirnov", "1234567890", chatId, PetType.CAT);
        CatReport catReport = new CatReport(LocalDate.now(), null, null, null);

        when(adopterRepository.findByChatId(chatId)).thenReturn(adopter);
        when(catReportRepository.findCatReportByAdopterId(adopter)).thenReturn(null);
        when(catReportRepository.save(any(CatReport.class))).thenReturn(catReport);
        when(messagesBundle.getString(anyString())).thenReturn("Message");

        saveEntityService.saveAdoptionReport(chatId);

        verify(catReportRepository).save(catReport);
        when(telegramBot.execute(any(SendMessage.class))).thenReturn(mock(SendResponse.class));
    }

    @Test
    void testSaveAdoptionReport_Dog_Success() {
        long chatId = 123456789L;
        Adopter adopter = new Adopter("Danil", "Smirnov", "danil_smirnov", "1234567890", chatId, PetType.DOG);
        DogReport dogReport = new DogReport(LocalDate.now(), null, null, null);

        when(adopterRepository.findByChatId(chatId)).thenReturn(adopter);
        when(dogReportRepository.findDogReportByAdopterId(adopter)).thenReturn(null);
        when(dogReportRepository.save(any(DogReport.class))).thenReturn(dogReport);
        when(messagesBundle.getString(anyString())).thenReturn("Message");

        saveEntityService.saveAdoptionReport(chatId);

        verify(dogReportRepository).save(dogReport);
        when(telegramBot.execute(any(SendMessage.class))).thenReturn(mock(SendResponse.class));
    }

}
