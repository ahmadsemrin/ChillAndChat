package com.atypon.asemrin.chillchat.model;

public class Chat {
    private String sender;
    private String receiver;
    private String message;
    private boolean seen;

    public Chat() {

    }

    public Chat(String sender, String receiver, String message, boolean seen) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.seen = seen;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSeen() {
        return seen;
    }
}
