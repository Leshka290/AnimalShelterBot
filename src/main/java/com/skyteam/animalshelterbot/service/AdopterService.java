package com.skyteam.animalshelterbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.skyteam.animalshelterbot.exception.AdopterNotFoundException;
import com.skyteam.animalshelterbot.listener.constants.AdopterStatus;
import com.skyteam.animalshelterbot.listener.constants.ReportStatus;
import com.skyteam.animalshelterbot.model.Adopter;
import com.skyteam.animalshelterbot.repository.AdopterRepository;
import com.skyteam.animalshelterbot.repository.ClientRepository;
import org.springframework.stereotype.Service;

@Service
public class AdopterService {

    private final AdopterRepository adopterRepository;

    public AdopterService(AdopterRepository adopterRepository, ClientRepository clientRepository) {
        this.adopterRepository = adopterRepository;
    }

    /**
     * Добавление усыновителя
     * @param adopter усыновитель
     * @return усыновитель
     */
    public Adopter createAdopter(Adopter adopter) {
        return adopterRepository.save(adopter);
    }

    /**
     * Изменение усыновителя
     * @param id id усыновителя
     * @return усыновитель
     */
    public Adopter readAdopter(long id) {
        Adopter adopter = adopterRepository.findById(id).orElse(null);
        if (adopter == null) {
            throw new AdopterNotFoundException(id);
        }
        return adopter;
    }

    /**
     * Изменение усыновителя
     * @param adopter усыновитель
     * @return усыновитель
     */
    public Adopter updateAdopter(Adopter adopter) {
        if (adopterRepository.findById(adopter.getId()).orElse(null) == null) {
            return null;
        }
        return adopterRepository.save(adopter);
    }

    /**
     * Получение статуса отправки данных усыновителем
     * @param chatId id усыновитель
     * @return усыновитель
     */
    public ReportStatus getUpdateStatus(long chatId) {
        Adopter adopter = adopterRepository.findByChatId(chatId);
        if (adopter == null) {
            return ReportStatus.DEFAULT;
        }
        return adopter.getReportStatus();
    }

    /**
     * Изменение статуса отправки данных усыновителем
     * @param chatId id усыновитель
     * @param reportStatus статус отправки данных
     */
    public void setUpdateStatus(long chatId, ReportStatus reportStatus) {
        Adopter adopter = adopterRepository.findByChatId(chatId);
        if (adopter == null) {
            throw new AdopterNotFoundException(chatId);
        }
        adopter.setReportStatus(reportStatus);
        adopterRepository.save(adopter);
    }

    /**
     * Сохранение усыновителя в БД и статуса усыновителя
     * @param adopter усыновитель
     * @param status статус усыновителя
     */
    private void setStatus(Adopter adopter, AdopterStatus status) {
        adopter.setStatus(status);
        adopterRepository.save(adopter);
    }
}
