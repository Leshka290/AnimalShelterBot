package com.skyteam.animalshelterbot.service;

import com.skyteam.animalshelterbot.model.CatClient;
import com.skyteam.animalshelterbot.model.DogClient;
import com.skyteam.animalshelterbot.repository.CatClientRepository;
import com.skyteam.animalshelterbot.repository.DogClientRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = ClientService.class)
public class ClientServiceTest {

    @MockBean
    private CatClientRepository catClientRepository;

    @MockBean
    private DogClientRepository dogClientRepository;

    @Autowired
    private ClientService clientService;

    @Test
    public void testSaveClientWithoutInfoForCat() {
        Long chatId = 123L;
        String animalType = "cat";
        CatClient catClient = new CatClient("Cat", "Client", 0L, chatId);
        when(catClientRepository.save(Mockito.any(CatClient.class))).thenReturn(catClient);
        clientService.saveClientWithoutInfo(chatId, animalType);
        verify(catClientRepository, Mockito.times(1)).save(Mockito.any(CatClient.class));
        verify(dogClientRepository, Mockito.times(0)).save(Mockito.any(DogClient.class));
    }

    @Test
    public void testSaveClientWithoutInfoForDog() {
        Long chatId = 456L;
        String animalType = "dog";
        DogClient dogClient = new DogClient("Dog", "Client", 0L, chatId);
        when(dogClientRepository.save(Mockito.any(DogClient.class))).thenReturn(dogClient);
        clientService.saveClientWithoutInfo(chatId, animalType);
        verify(catClientRepository, Mockito.times(0)).save(Mockito.any(CatClient.class));
        verify(dogClientRepository, Mockito.times(1)).save(Mockito.any(DogClient.class));
    }

    @Test
    public void testSaveClientsInfoForCat() {
        long chatId = 123L;
        String name = "Kostya";
        String lastName = "Tests";
        long phoneNumber = 111222333444L;
        when(catClientRepository.existsById(chatId)).thenReturn(true);
        clientService.saveClientsInfo(name, lastName, phoneNumber, chatId);
        verify(catClientRepository, times(1)).save(any(CatClient.class));
        verify(dogClientRepository, never()).save(any(DogClient.class));
    }

    @Test
    public void testSaveClientsInfoForDog() {
        long chatId = 456L;
        String name = "Dima";
        String lastName = "Testing";
        long phoneNumber = 123456789L;
        when(dogClientRepository.existsById(chatId)).thenReturn(true);
        clientService.saveClientsInfo(name, lastName, phoneNumber, chatId);
        verify(dogClientRepository, times(1)).save(any(DogClient.class));
        verify(catClientRepository, never()).save(any(CatClient.class));
    }

    @Test
    public void testSaveClientsInfoWithNoAnimalSelected() {
        long chatId = 123L;
        String name = "Danil";
        String lastName = "Test";
        long phoneNumber = 987654321L;
        when(catClientRepository.existsById(chatId)).thenReturn(false);
        when(dogClientRepository.existsById(chatId)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> clientService.saveClientsInfo(name, lastName, phoneNumber, chatId));
        verify(catClientRepository, never()).save(any(CatClient.class));
        verify(dogClientRepository, never()).save(any(DogClient.class));
    }
}







