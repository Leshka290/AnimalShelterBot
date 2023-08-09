package com.skyteam.animalshelterbot.service;

import com.skyteam.animalshelterbot.model.Report.DogReport;
import com.skyteam.animalshelterbot.model.images.DogImage;
import com.skyteam.animalshelterbot.repository.DogImageRepository;
import com.skyteam.animalshelterbot.repository.DogReportRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
/**
 * Реализует CRUD операции с отчетами о собаках.
 */
@Service
public class DogReportService {
  private final DogReportRepository dogReportsRepository;
  private final DogImageRepository dogImageRepository;

    public DogReportService(DogReportRepository dogReportsRepository, DogImageRepository dogImageRepository) {
        this.dogReportsRepository = dogReportsRepository;
        this.dogImageRepository = dogImageRepository;
    }

    /**
     * Добавление отчета о собаке
     * @param dogReport отчет о собаке
     * @param multipartFiles фото собаки
     * @return отчет о собаке
     */
    public DogReport postReport(DogReport dogReport, MultipartFile... multipartFiles) {
        List<DogImage> images = null;
        if (multipartFiles.length > 0) {

        }
        dogReport.setImages(images);
        dogReportsRepository.save(dogReport);
        return dogReportsRepository.save(dogReport);
    }


    /**
     * Редактировать отчет о собаке
     * @param dogReport отчет о собаке
     * @return отчет о собаке
     */
    public DogReport putReport(DogReport dogReport) {
        return dogReportsRepository.save(dogReport);
    }

    /**
     * Удалить отчет о собаке по Id
     * @param id идентификатор отчета о собаке
     * @return статус ок
     */
    public HttpStatus deleteReportsByPet(Long id) {
        dogReportsRepository.deleteDogReportsByPetId(id);
        return HttpStatus.OK;
    }

    /**
     * Получить все отчеты о собаках
     * @return коллекция отчетов
     */
    public List<DogReport> getAllReports() {
        return dogReportsRepository.findAll();
    }

    /**
     * Получить отчет по собаке
     * @param id идентификатор отчета
     * @return коллекция отчетов
     */
    public List<DogReport> findReportsFromPet(Long id) {
        return dogReportsRepository.findDogReportsByPetId(id);
    }
}