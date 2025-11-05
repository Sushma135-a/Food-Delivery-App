package com.foodapp;

public class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }

    public String toStorageString() {
        return username + ":" + password;
    }

    public static User fromStorageString(String s) {
        String[] parts = s.split(":");
        if (parts.length != 2) return null;
        return new User(parts[0], parts[1]);
    }
}
