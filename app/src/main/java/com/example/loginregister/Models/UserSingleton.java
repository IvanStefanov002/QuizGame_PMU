package com.example.loginregister.Models;

public class UserSingleton {
    private static UserSingleton instance;
    private String username;
    private int points;
    private UserSingleton() {
        username = "";
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
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public int getPoints() {
        return points;
    }
    public void setPoints(int points) {
        this.points = points;
    }
}

