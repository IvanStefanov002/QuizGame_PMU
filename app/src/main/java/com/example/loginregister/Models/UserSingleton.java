package com.example.loginregister.Models;

public class UserSingleton {
    private static UserSingleton instance;
    private String username;
    private String email;
    private int points;
    private UserSingleton() {
        username = "";
        email = "";
        points = 0;
    }
    // Method to get the singleton instance
    public static synchronized UserSingleton getInstance() {
        if (instance == null) {
            instance = new UserSingleton();
        }
        return instance;
    }
    // Getter and setter methods for user parameters
    public String getUsername() { return username; }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public int getPoints() {
        return points;
    }
    public void setPoints(int points) {
        this.points = points;
    }
}

