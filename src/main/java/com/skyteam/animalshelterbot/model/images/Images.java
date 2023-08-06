package com.skyteam.animalshelterbot.model.images;
import com.skyteam.animalshelterbot.model.*;
import javax.persistence.*;
import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class Images {

    private String telegramFileId;

    private Long fileSize;

    @Column(name = "file_as_array_of_bytes")
    private byte[] fileAsArrayOfBytes;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    private boolean isPreview;

}
