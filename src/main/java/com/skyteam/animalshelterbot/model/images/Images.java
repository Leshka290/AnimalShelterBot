package com.skyteam.animalshelterbot.model.images;
import com.skyteam.animalshelterbot.model.*;
import javax.persistence.*;
import lombok.*;

/**
 * Родительский класс для описания фото животного для отчета со свойствами:
 * <p>
 * <b>telegramFileId</b>,<b>fileSize</b>,<b>fileAsArrayOfBytes</b>,<b>pet</b>,<b>isPreview</b>,
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class Images {
    /**
     * Id файла
     */
    private String telegramFileId;
    /**
     * Размер файла
     */
    private Long fileSize;

    @Column(name = "file_as_array_of_bytes")
    private byte[] fileAsArrayOfBytes;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;


    private boolean isPreview;

}
