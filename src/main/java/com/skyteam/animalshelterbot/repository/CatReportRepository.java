package com.skyteam.animalshelterbot.repository;


import com.skyteam.animalshelterbot.model.Report.CatReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatReportRepository  extends JpaRepository<CatReport, Long> {
    List<CatReport> findCatReportsByClient(Long id);

    List<CatReport> findCatReportsByPetId(Long id);

    void deleteCatReportsByCatId(Long id);
}
