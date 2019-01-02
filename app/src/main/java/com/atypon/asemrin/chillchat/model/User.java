package com.atypon.asemrin.chillchat.model;

public class User {
    private String id;
    private String fullName;
    private String username;
    private String email;
    private String imageURL;

    public User() {
    }

    public User(String fullName, String username, String email, String imageURL) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.imageURL = imageURL;
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
