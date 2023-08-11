package com.skyteam.animalshelterbot.model.images;
import javax.persistence.*;


import com.skyteam.animalshelterbot.model.Report.DogReport;
import lombok.*;

/**
 * Класс описывающий фото кота для отчета
 */
@Entity
@Table(name = "cat_images")
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

    public CatImage(byte[] image) {
        super(image);
    }
}
