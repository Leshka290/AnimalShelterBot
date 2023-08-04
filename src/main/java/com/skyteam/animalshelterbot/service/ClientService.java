package com.skyteam.animalshelterbot.service;

import com.skyteam.animalshelterbot.listener.constants.PetType;
import com.skyteam.animalshelterbot.model.CatClient;
import com.skyteam.animalshelterbot.model.DogClient;
import com.skyteam.animalshelterbot.repository.CatClientRepository;
import com.skyteam.animalshelterbot.repository.DogClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import static com.skyteam.animalshelterbot.listener.constants.PetType.CAT;
import static com.skyteam.animalshelterbot.listener.constants.PetType.DOG;

/**
 *Реализует запись в БД данных клиентов приюта.
 * @author youcanwakemeup
 */
@Service
public class ClientService {


    @Autowired
    private CatClientRepository catClientRepository;

    @Autowired
    private DogClientRepository dogClientRepository;

    /**
     * Записывает дынные клиента в БД после выбора им приюта, из которого он хочет взять животное.
     * Используется метод репозитория {@link JpaRepository#save(Object)}
     * @param chatId идентификатор чата
     * @param animalType тип животного (кошка или собака)
     */
    public void saveClientWithoutInfo(Long chatId, PetType animalType) {
        if (animalType.equals(CAT)) {
            CatClient catClient = new CatClient("Cat", "Client", 0L, chatId);
            catClientRepository.save(catClient);
        } else if (animalType.equals(DOG)) {
            DogClient dogClient = new DogClient("Dog", "Client", 0L, chatId);
            dogClientRepository.save(dogClient);
        }
    }

    /**
     * Записывает дынные клиента для связи в БД.
     * Используется метод репозитория {@link JpaRepository#save(Object)}
     * @param name имя клиента
     * @param lastName фамилия клиента
     * @param phoneNumber телефонный номер клиента
     * @param chatId идентификатор чата
     * @throws RuntimeException если животное не выбрано
     */
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
