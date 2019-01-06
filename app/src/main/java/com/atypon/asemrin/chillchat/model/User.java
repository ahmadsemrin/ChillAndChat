package com.atypon.asemrin.chillchat.model;

public class User {
    private String id;
    private String fullName;
    private String username;
    private String email;
    private String imageURL;
    private String status;
    private String search;

    public User() {
    }

    public User(String fullName, String username, String email, String imageURL, String status) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.imageURL = imageURL;
        this.status = status;
        this.search = fullName.toLowerCase();
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

    public String getStatus() {
        return status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSearch(String search) {
        this.search = search;
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

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }
}
