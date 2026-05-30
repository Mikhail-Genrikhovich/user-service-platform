package com.homework.notificationservice.dto;

public class UserEvent {

    private String eventType;
    private String email;

    public UserEvent() {
    }

    public UserEvent(String eventType, String email) {
        this.eventType = eventType;
        this.email = email;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
