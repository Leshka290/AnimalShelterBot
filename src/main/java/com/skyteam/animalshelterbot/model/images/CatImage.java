package com.skyteam.animalshelterbot.model.images;
import javax.persistence.*;


import com.skyteam.animalshelterbot.model.Report.DogReport;
import lombok.*;
@Entity
@Table(schema = "images", name = "dog_images")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatImage extends Images{
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "report_id")
    private DogReport catReport;
}
