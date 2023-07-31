package com.skyteam.animalshelterbot.model;

import java.util.Objects;

public abstract class Client {

    private String firstName;
    private String lastName;
    private Long phoneNumber;
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
