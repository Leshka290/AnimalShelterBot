package com.skyteam.animalshelterbot.model;

import lombok.*;

import javax.persistence.*;

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
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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

    @ManyToOne
    @JoinColumn(name = "report_id")
    private Report report;

    public Image(byte[] fileAsArrayOfBytes) {
        this.fileAsArrayOfBytes = fileAsArrayOfBytes;
    }
}
