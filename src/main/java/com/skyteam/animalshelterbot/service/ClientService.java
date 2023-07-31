package com.skyteam.animalshelterbot.service;

import com.skyteam.animalshelterbot.model.CatClient;
import com.skyteam.animalshelterbot.model.DogClient;
import com.skyteam.animalshelterbot.repository.CatClientRepository;
import com.skyteam.animalshelterbot.repository.DogClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {


    @Autowired
    private CatClientRepository catClientRepository;

    @Autowired
    private DogClientRepository dogClientRepository;

    public void saveClientWithoutInfo(Long chatId, String animalType) {
        if (animalType.equals("cat")) {
            CatClient catClient = new CatClient("Cat", "Client", 0L, chatId);
            catClientRepository.save(catClient);
        } else if (animalType.equals("dog")) {
            DogClient dogClient = new DogClient("Dog", "Client", 0L, chatId);
            dogClientRepository.save(dogClient);
        }
    }
    public void saveClientsInfo(String name, String lastName, long phoneNumber, long chatId) {
        if (catClientRepository.existsById(chatId)) {
            CatClient catClient = new CatClient(name, lastName, phoneNumber, chatId);
            catClientRepository.save(catClient);
            //existsByChatId
        } else if (dogClientRepository.existsById(chatId)) {
            DogClient dogClient = new DogClient(name, lastName, phoneNumber, chatId);
            dogClientRepository.save(dogClient);
        } else {
            throw new RuntimeException("Сначала нужно выбрать животное");
        }
    }
}
