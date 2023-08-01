package com.skyteam.animalshelterbot.model;

import java.util.Objects;

/**
 * Абстрактный класс клиент приюта для кошек(или собак) со свойствами:
 * <p>
 * <b>firstName</b>,<b>lastName</b>,<b>phoneNumber</b>,<b>chatId</b>
 * @author youcanwakemeup
 */
public abstract class Client {
    /**
     * Имя клиента
     */
    private String firstName;
    /**
     * Фамилия клиента
     */
    private String lastName;
    /**
     * Телефонный номер клиента
     */
    private Long phoneNumber;
    /**
     * Идентификатор чата
     */
    private Long chatId;


    public Client() {}

    public Client(String firstName, String lastName, Long phoneNumber, Long chatId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.chatId = chatId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(firstName, client.firstName) && Objects.equals(lastName, client.lastName) && Objects.equals(phoneNumber, client.phoneNumber) && Objects.equals(chatId, client.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, phoneNumber, chatId);
    }
}
