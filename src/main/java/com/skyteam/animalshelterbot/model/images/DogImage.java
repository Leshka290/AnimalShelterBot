package com.skyteam.animalshelterbot.model.images;
import javax.persistence.*;

import com.skyteam.animalshelterbot.model.Report.DogReport;
import lombok.*;
/**
 * Класс описывающий фото собаки для отчета
 */
@Entity
@Table(name = "dog_images")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DogImage extends Images{
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "report_id")
    private DogReport dogReport;

    public DogImage(byte[] image) {
        super(image);
    }
}
