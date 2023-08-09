package com.skyteam.animalshelterbot.model.Report;

import javax.persistence.*;
import com.skyteam.animalshelterbot.model.*;
import com.skyteam.animalshelterbot.model.images.CatImage;
import lombok.*;

import java.util.List;

    @Entity
    @Table(name = "cat_reports")
    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    @Builder
    @AllArgsConstructor
    public class CatReport extends Report{

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(referencedColumnName = "id")
        @ToString.Exclude
        private  CatClient catClient;


        @OneToMany(targetEntity = CatImage.class, mappedBy = "catReport", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        private List<CatImage> images;
}
