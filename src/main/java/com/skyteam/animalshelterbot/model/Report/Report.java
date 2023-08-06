package com.skyteam.animalshelterbot.model.Report;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
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

    @Column(name = "common_status")
    private String commonDescriptionOfStatus;

    @Column(name = "behavior")
    private String behavioralChanges;

    @Column(name="pet_id")
    private Long petId;

    @Column(name = "checked_by_volunteer")
    private boolean checkedByVolunteer;

    @PrePersist
    private void init() {
        date = LocalDateTime.now().toLocalDate();
    }
}

