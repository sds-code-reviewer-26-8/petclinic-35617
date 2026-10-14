package com.example.petclinic.controller.response;

public class ErrorDto {

    private int status;
    private String message;

    public ErrorDto() {
    }

    public ErrorDto(int status, String message) {
        this.status = status;
        this.message = message;
    }

    /* ----- Getter / Setter ----- */
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
