package com.skyteam.animalshelterbot.model.Report;
import com.skyteam.animalshelterbot.model.Adopter;
import com.skyteam.animalshelterbot.model.DogClient;
import com.skyteam.animalshelterbot.model.images.DogImage;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

    @Entity
    @Table( name = "dog_reports")
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    @ToString
    public class DogReport extends Report {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(referencedColumnName = "id")
        @ToString.Exclude
        private DogClient dogClient;


        @OneToMany(targetEntity = DogImage.class, mappedBy = "dogReport", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        @ToString.Exclude
        private List<DogImage> images;

        public DogReport(Adopter adopterId, LocalDate date, String diet, String commonDescriptionOfStatus, String behavioralChanges) {
            super(adopterId, date, diet, commonDescriptionOfStatus, behavioralChanges);
        }
    }
