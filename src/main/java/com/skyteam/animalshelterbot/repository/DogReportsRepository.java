package com.skyteam.animalshelterbot.repository;
import com.skyteam.animalshelterbot.model.DogClient;
import com.skyteam.animalshelterbot.model.Report.DogReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.skyteam.animalshelterbot.model.*;
import java.util.List;

@Repository
public interface DogReportsRepository extends JpaRepository<DogReport, Long> {
    List<DogReport> findDogReportsByDogClient(DogClient dogClient);

    List<DogReport> findDogReportsByPet(Pet pet);

    void deleteDogReportsByPet(Pet pet);
}
