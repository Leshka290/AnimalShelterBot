package com.skyteam.animalshelterbot.controller;


import com.skyteam.animalshelterbot.model.Pet;
import com.skyteam.animalshelterbot.model.Report;
import com.skyteam.animalshelterbot.service.ReportService;
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

    private final ReportService reportService;



    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/pets")
    @Operation(
            summary = "Добавить отчёт о животном",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "отчет о животном добавлен",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = Report.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Если отчет уже добавлен"
                    )
            }
    )

    public ResponseEntity<Report> postReport(@RequestBody Report report,
                                                   @RequestParam("images") MultipartFile... multipartFiles) {
        return ResponseEntity.ok(reportService.postReport(report, multipartFiles));
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
                                            schema = @Schema(implementation = Report.class)
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
            return ResponseEntity.ok(reportService.findReportsFromPet(pet.getId()));
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteReportsByPet(T pet) {
            return ResponseEntity.ok(reportService.deleteReportByPet(pet.getId()));
    }
}
