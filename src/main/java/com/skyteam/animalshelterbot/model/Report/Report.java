package com.skyteam.animalshelterbot.model.Report;

import com.skyteam.animalshelterbot.model.Adopter;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Getter
@Setter
@ToString
@MappedSuperclass
public abstract class Report {

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "diet")
    private String diet;

    @ManyToOne
    @JoinColumn(name = "adopter_id")
    private Adopter adopterId;

    @Column(name = "common_status")
    private String commonDescriptionOfStatus;

    @Column(name = "behavior")
    private String behavioralChanges;

    @Column(name = "pet_id")
    private Long petId;

    @Column(name = "checked_by_volunteer")
    private boolean checkedByVolunteer;

    public Report(Adopter adopterId, LocalDate date, String diet, String commonDescriptionOfStatus, String behavioralChanges) {
        this.adopterId = adopterId;
        this.date = date;
        this.diet = diet;
        this.commonDescriptionOfStatus = commonDescriptionOfStatus;
        this.behavioralChanges = behavioralChanges;
    }
    @PrePersist
    private void init() {
        date = LocalDateTime.now().toLocalDate();
    }
}

