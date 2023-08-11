package com.skyteam.animalshelterbot.repository;


import com.skyteam.animalshelterbot.model.Adopter;
import com.skyteam.animalshelterbot.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository  extends JpaRepository<Report, Long> {
    List<Report> findReportsByAdopterId_Id(Long id);

    Report findReportsByAdopterId(Adopter adopter);
    List<Report> findReportsByPetId(Long id);

    void deleteReportsByPetId(Long id);
}
