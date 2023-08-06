package com.skyteam.animalshelterbot.model.Report;
import com.skyteam.animalshelterbot.model.DogClient;
import com.skyteam.animalshelterbot.model.images.DogImage;
import lombok.*;
import javax.persistence.*;
import java.util.List;

    @Entity
    @Table(schema = "reports", name = "dog_reports")
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
}
