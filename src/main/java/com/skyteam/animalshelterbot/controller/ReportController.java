package com.skyteam.animalshelterbot.controller;


import com.skyteam.animalshelterbot.listener.constants.PetType;
import com.skyteam.animalshelterbot.model.Pet;
import com.skyteam.animalshelterbot.model.Report.CatReport;
import com.skyteam.animalshelterbot.model.Report.DogReport;
import com.skyteam.animalshelterbot.model.Report.Report;
import com.skyteam.animalshelterbot.service.CatReportService;
import com.skyteam.animalshelterbot.service.DogReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "API для работы с отчетами усыновителей", description = "Содержит методы для создания, удалеия и нахождения отчетов")
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
    @Operation(
            summary = "Добавить отчёт о собаке",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "отчет о собаке добавлен",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = DogReport.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Если отчет уже добавлен"
                    )
            }
    )

    public ResponseEntity<DogReport> postDogReport(@RequestBody DogReport dogReport,
                                                   @RequestParam("images") MultipartFile... multipartFiles) {
        return ResponseEntity.ok(dogReportService.postReport(dogReport, multipartFiles));
    }

    @PostMapping("/cats")
    @Operation(
            summary = "Добавить отчёт о кошке",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "отчет о кошке добавлен",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = CatReport.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Если отчет уже добавлен"
                    )
            }
    )
    public ResponseEntity<CatReport> postCatReport(@RequestBody CatReport catReport) {
        return ResponseEntity.ok(catReportService.postReport(catReport));
    }

    @GetMapping
    @Operation(
            summary = "Найти отчёт по Id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "отчет найден",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = DogReport.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Нет отчета"
                    )
            }
    )
    public ResponseEntity<List<? extends Report>> findReportsByPet(@RequestBody T pet) {
        if (pet.getPetType().equals(PetType.CAT)) {
            return ResponseEntity.ok(catReportService.findReportsFromPet(pet.getId()));
        } else if (pet.getPetType().equals(PetType.DOG)) {
            return ResponseEntity.ok(dogReportService.findReportsFromPet(pet.getId()));
        } else throw new RuntimeException("Не валидно");
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteReportsByPet(T pet) {
        if (pet.getPetType().equals(PetType.CAT)) {
            return ResponseEntity.ok(catReportService.deleteReportsByPet(pet.getId()));
        } else if (pet.getPetType().equals(PetType.DOG)) {
            return ResponseEntity.ok(dogReportService.deleteReportsByPet(pet.getId()));
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
