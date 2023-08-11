package com.skyteam.animalshelterbot.service;

import com.skyteam.animalshelterbot.model.Image;
import com.skyteam.animalshelterbot.model.Report;
import com.skyteam.animalshelterbot.repository.ImageRepository;
import com.skyteam.animalshelterbot.repository.ReportRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Реализует CRUD операции с отчетами о кошках.
 */
@Service
public class ReportService {
    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository, ImageRepository imageRepository) {
        this.reportRepository = reportRepository;
    }



    /**
     * Добавление отчета о животном
     * @param report отчет о животном
     * @param multipartFiles фото животного
     * @return отчет о животном
     */
    public Report postReport(Report report, MultipartFile... multipartFiles) {
       List <Image> images = null;
        if (multipartFiles.length > 0) {

        }
        report.setImage(images);
        reportRepository.save(report);
        return reportRepository.save(report);
    }


    /**
     * Редактирование отчета о животном
     * @param report отчет о животном
     * @return отчет о животном
     */
    public Report putReport(Report report) {
        return reportRepository.save(report);
    }
    /**
     * Удаление отчета о животном
     * @param id идентификатор отчета
     * @return статус ок
     */
    public HttpStatus deleteReportByPet(Long id) {
        reportRepository.deleteReportsByPetId(id);
        return HttpStatus.OK;
    }

    /**
     * Найти все отчеты о кошках
     * @return коллеция отчетов
     */
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    /**
     * Найти отчет по животному
     * @param id идентификатор животного
     * @return коллекция отчетов
     */
    public List<Report> findReportsFromPet(Long id) {
        return reportRepository.findReportsByPetId(id);
    }
}
