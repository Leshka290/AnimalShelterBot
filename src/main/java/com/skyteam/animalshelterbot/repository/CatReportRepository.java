package com.skyteam.animalshelterbot.repository;


import com.skyteam.animalshelterbot.model.Report.CatReport;
import com.skyteam.animalshelterbot.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatReportRepository  extends JpaRepository<CatReport, Long> {
    List<CatReport> findCatReportsByClient(Client client);

    List<CatReport> findCatReportsByCat(Pet pet);

    void deleteCatReportsByCat(Pet pet);
}
