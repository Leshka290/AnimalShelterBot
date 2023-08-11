package com.skyteam.animalshelterbot.service;

import com.skyteam.animalshelterbot.model.Report.CatReport;
import com.skyteam.animalshelterbot.model.images.CatImage;
import com.skyteam.animalshelterbot.repository.CatImageRepository;
import com.skyteam.animalshelterbot.repository.CatReportRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Реализует CRUD операции с отчетами о кошках.
 */
@Service
public class CatReportService {
    private final CatReportRepository catReportsRepository;

    public CatReportService(CatReportRepository catReportsRepository, CatImageRepository catImageRepository) {
        this.catReportsRepository = catReportsRepository;
    }

    /**
     * Добавление отчета о кошке
     * @param catReport отчет о кошке
     * @param multipartFiles фото кошки
     * @return отчет о кошке
     */
    public CatReport postReport(CatReport catReport, MultipartFile... multipartFiles) {
        List<CatImage> images = null;
        if (multipartFiles.length > 0) {

        }
        catReport.setImages(images);
        catReportsRepository.save(catReport);
        return catReportsRepository.save(catReport);
    }


    /**
     * Редактирование отчета о кошке
     * @param catReport отчет о кошке
     * @return отчет о кошке
     */
    public CatReport putReport(CatReport catReport) {
        return catReportsRepository.save(catReport);
    }
    /**
     * Удаление отчета о кошке
     * @param id идентификатор отчета
     * @return статус ок
     */
    public HttpStatus deleteReportsByPet(Long id) {
        catReportsRepository.deleteCatReportsByPetId(id);
        return HttpStatus.OK;
    }

    /**
     * Найти все отчеты о кошках
     * @return коллеция отчетов
     */
    public List<CatReport> getAllReports() {
        return catReportsRepository.findAll();
    }

    /**
     * Найти отчет по животному
     * @param id идентификатор животного
     * @return коллекция отчетов
     */
    public List<CatReport> findReportsFromPet(Long id) {
        return catReportsRepository.findCatReportsByPetId(id);
    }
}
