package com.skyteam.animalshelterbot.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Класс клиент приюта для кошек со свойствами:
 * <p>
 * <b>id</b>,<b>firstName</b>,<b>lastName</b>,<b>phoneNumber</b>,<b>chatId</b>
 * @author youcanwakemeup
 */
@Entity
public class CatClient extends Client {
    /**
     * Идентификатор по порядку генерируемый в БД.
     */
    @Id
    @GeneratedValue
    private Long id;

    public CatClient() {}

    public CatClient(String firstName, String lastName, Long phoneNumber, Long chatId) {
        super(firstName, lastName, phoneNumber, chatId);
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
