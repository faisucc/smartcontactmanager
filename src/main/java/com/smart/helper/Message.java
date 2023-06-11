package com.smart.helper;

public class Message {

    private String message;
    private String contentType;

    public Message(String message, String contentType) {
        this.message = message;
        this.contentType = contentType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
