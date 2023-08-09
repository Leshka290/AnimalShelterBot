package com.skyteam.animalshelterbot.repository;

import com.skyteam.animalshelterbot.model.Report.DogReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DogReportsRepository extends JpaRepository<DogReport, Long> {
    List<DogReport> findDogReportsByDogClientId(Long id);

    List<DogReport> findDogReportsByPetId(Long id);

    void deleteDogReportsByPetId(Long id);
}
