package com.skyteam.animalshelterbot.service;

import com.skyteam.animalshelterbot.model.Report.DogReport;
import com.skyteam.animalshelterbot.model.images.DogImage;
import com.skyteam.animalshelterbot.repository.DogImageRepository;
import com.skyteam.animalshelterbot.repository.DogReportsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.skyteam.animalshelterbot.model.*;

import java.util.List;
@Service
public class DogReportService {
  private final   DogReportsRepository dogReportsRepository;

    public DogReportService(DogReportsRepository dogReportsRepository, DogImageRepository dogImageRepository) {
        this.dogReportsRepository = dogReportsRepository;
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


    public HttpStatus deleteReportsByPet(Pet pet) {
        dogReportsRepository.deleteDogReportsByPet(pet);
        return HttpStatus.OK;
    }


    public List<DogReport> getAllReports() {
        return dogReportsRepository.findAll();
    }


    public List<DogReport> findReportsFromPet(Pet pet) {
        return dogReportsRepository.findDogReportsByPet(pet);
    }
}
