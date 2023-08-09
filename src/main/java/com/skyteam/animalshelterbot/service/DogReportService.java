package com.skyteam.animalshelterbot.service;

import com.skyteam.animalshelterbot.model.Report.DogReport;
import com.skyteam.animalshelterbot.model.images.DogImage;
import com.skyteam.animalshelterbot.repository.DogImageRepository;
import com.skyteam.animalshelterbot.repository.DogReportsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Service
public class DogReportService {
  private final   DogReportsRepository dogReportsRepository;
  private final DogImageRepository dogImageRepository;

    public DogReportService(DogReportsRepository dogReportsRepository, DogImageRepository dogImageRepository) {
        this.dogReportsRepository = dogReportsRepository;
        this.dogImageRepository = dogImageRepository;
    }

    public DogReport postReport(DogReport dogReport, MultipartFile... multipartFiles) {
        List<DogImage> images = null;
        if (multipartFiles.length > 0) {

        }
        dogReport.setImages(images);
        dogReportsRepository.save(dogReport);
        return dogReportsRepository.save(dogReport);
    }



    public DogReport putReport(DogReport dogReport) {
        return dogReportsRepository.save(dogReport);
    }


    public HttpStatus deleteReportsByPet(Long id) {
        dogReportsRepository.deleteDogReportsByPetId(id);
        return HttpStatus.OK;
    }


    public List<DogReport> getAllReports() {
        return dogReportsRepository.findAll();
    }


    public List<DogReport> findReportsFromPet(Long id) {
        return dogReportsRepository.findDogReportsByPetId(id);
    }
}
