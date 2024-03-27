package com.example.loginregister;

import com.vishnusivadas.advanced_httpurlconnection.FetchData;
import org.json.JSONException;
import org.json.JSONObject;

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
    public void updateFromPHP(String s_username) {
        FetchData fetchData = new FetchData("http://192.168.220.238/LoginRegister/readpoints.php?username=" + s_username);
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                String result = fetchData.getResult();
                try {
                    JSONObject json = new JSONObject(result);
                    String username = json.getString("username");
                    int points = json.getInt("points");
                    instance.setUsername(username);
                    instance.setPoints(points);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

