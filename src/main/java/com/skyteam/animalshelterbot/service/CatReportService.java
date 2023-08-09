package com.skyteam.animalshelterbot.service;

import com.skyteam.animalshelterbot.model.Report.CatReport;
import com.skyteam.animalshelterbot.model.images.CatImage;
import com.skyteam.animalshelterbot.repository.CatImageRepository;
import com.skyteam.animalshelterbot.repository.CatReportRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Service
public class CatReportService {
    private final CatReportRepository catReportsRepository;
    private final CatImageRepository catImageRepository;

    public CatReportService(CatReportRepository catReportsRepository, CatImageRepository catImageRepository) {
        this.catReportsRepository = catReportsRepository;
        this.catImageRepository = catImageRepository;
    }

    public CatReport postReport(CatReport catReport, MultipartFile... multipartFiles) {
        List<CatImage> images = null;
        if (multipartFiles.length > 0) {

        }
        catReport.setImages(images);
        catReportsRepository.save(catReport);
        return catReportsRepository.save(catReport);
    }



    public CatReport putReport(CatReport catReport) {
        return catReportsRepository.save(catReport);
    }


    public HttpStatus deleteReportsByPet(Long id) {
        catReportsRepository.deleteCatReportsByCatId(id);
        return HttpStatus.OK;
    }


    public List<CatReport> getAllReports() {
        return catReportsRepository.findAll();
    }


    public List<CatReport> findReportsFromPet(Long id) {
        return catReportsRepository.findCatReportsByPetId(id);
    }
}
