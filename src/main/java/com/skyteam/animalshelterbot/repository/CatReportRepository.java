package com.skyteam.animalshelterbot.repository;


import com.skyteam.animalshelterbot.model.Adopter;
import com.skyteam.animalshelterbot.model.Report.CatReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatReportRepository  extends JpaRepository<CatReport, Long> {
    List<CatReport> findCatReportsByCatClientId(Long id);

    CatReport findAdoptionReportByAdopterId(Adopter adopter);
    List<CatReport> findCatReportsByPetId(Long id);

    void deleteCatReportsByPetId(Long id);
}
