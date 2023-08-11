package com.skyteam.animalshelterbot.service;

import com.skyteam.animalshelterbot.exception.ClientNotFoundException;
import com.skyteam.animalshelterbot.listener.constants.PetType;
import com.skyteam.animalshelterbot.model.Client;
import com.skyteam.animalshelterbot.repository.ClientRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 *Реализует запись в БД данных клиентов приюта.
 * @author youcanwakemeup
 */
@Service
public class ClientService {


    private final ClientRepository clientRepository;


    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * Записывает дынные клиента в БД после выбора им приюта, из которого он хочет взять животное.
     * Используется метод репозитория {@link JpaRepository#save(Object)}
     * @param chatId идентификатор чата
     * @param animalType тип животного (кошка или собака)
     */
    public void saveClientWithoutInfo(Long chatId, PetType animalType) {
            Client client = new Client(chatId, animalType);
            clientRepository.save(client);
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
        if (clientRepository.existsById(chatId)) {
            Client client = new Client(name, lastName, phoneNumber, chatId);
            clientRepository.save(client);
        } else {
            throw new ClientNotFoundException("Сначала нужно выбрать животное");
        }
    }
}
