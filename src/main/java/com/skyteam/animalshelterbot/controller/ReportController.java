package com.skyteam.animalshelterbot.controller;


import com.skyteam.animalshelterbot.listener.constants.PetType;
import com.skyteam.animalshelterbot.model.Pet;
import com.skyteam.animalshelterbot.model.Report.CatReport;
import com.skyteam.animalshelterbot.model.Report.DogReport;
import com.skyteam.animalshelterbot.model.Report.Report;
import com.skyteam.animalshelterbot.service.CatReportService;
import com.skyteam.animalshelterbot.service.DogReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController<N extends Report, T extends Pet> {

    private final DogReportService dogReportService;

    private final CatReportService catReportService;

    public ReportController(DogReportService dogReportService,
                            CatReportService catReportService) {
        this.dogReportService = dogReportService;
        this.catReportService = catReportService;
    }

    @PostMapping("/dogs")
    public ResponseEntity<DogReport> postDogReport(@RequestBody DogReport dogReport,
                                                   @RequestParam("images") MultipartFile... multipartFiles) {


        return ResponseEntity.ok(dogReportService.postReport(dogReport, multipartFiles));
    }

    @PostMapping("/cats")
    public ResponseEntity<CatReport> postCatReport(@RequestBody CatReport catReport) {
        return ResponseEntity.ok(catReportService.postReport(catReport));
    }

    @GetMapping
    public ResponseEntity<List<? extends Report>> findReportsByPet(@RequestBody T pet) {
        if (pet.getPetType().equals(PetType.CAT)) {
            return ResponseEntity.ok(catReportService.findReportsFromPet(pet));
        } else if (pet.getPetType().equals(PetType.DOG)) {
            return ResponseEntity.ok(dogReportService.findReportsFromPet(pet));
        } else throw new RuntimeException("Не валидно");
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteReportsByPet(T pet) {
        if (pet.getPetType().equals(PetType.CAT)) {
            return ResponseEntity.ok(catReportService.deleteReportsByPet(pet));
        } else if (pet.getPetType().equals(PetType.DOG)) {
            return ResponseEntity.ok(dogReportService.deleteReportsByPet(pet));
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
