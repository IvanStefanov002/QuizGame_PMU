package com.example.loginregister.Utilities;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.loginregister.Interfaces.FetchDataCallback;
import com.example.loginregister.Interfaces.RequestsHandler;
import com.example.loginregister.Models.UserSingleton;
import com.vishnusivadas.advanced_httpurlconnection.FetchData;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Requests {
    public static void Register(String fullname, String username, String password, String email, Context context, RequestsHandler listener) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                String[] field = new String[4];
                field[0] = "fullname";
                field[1] = "username";
                field[2] = "password";
                field[3] = "email";

                //Creating array for data
                String[] data = new String[4];
                data[0] = fullname;
                data[1] = username;
                data[2] = password;
                data[3] = email;
                PutData putData = new PutData("http://192.168.125.238/LoginRegister/signup.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        listener.onSuccess(putData.getResult());
                    } else {
                        listener.onFailure(putData.getResult());
                    }
                }
            }
        });
    }

    public static void Login(String username, String password, Context context, RequestsHandler listener) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                String[] field = new String[2];
                field[0] = "username";
                field[1] = "password";

                //Creating array for data
                String[] data = new String[2];
                data[0] = username;
                data[1] = password;
                PutData putData = new PutData("http://192.168.125.238/LoginRegister/login.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        listener.onSuccess(putData.getResult());
                    } else {
                        listener.onFailure(putData.getResult());
                    }
                }
            }
        });
    }

    public static void OnResumeReadPoints(String username, FetchDataCallback callback) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                FetchData fetchData = new FetchData("http://192.168.125.238/LoginRegister/readpoints.php?username=" + username);
                if (fetchData.startFetch()) {
                    if (fetchData.onComplete()) {
                        String[] result = {fetchData.getResult()};
                        callback.onDataFetched(result);
                    }
                }
            }
        });
    }

    public static String readQuestions(String category) {
        FetchData fetchData = new FetchData("http://192.168.125.238/LoginRegister/readquestions.php?category=" + category);
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                return fetchData.getResult();
            }
        }
        return null;
    }

    public static void updatePoints(String username, int urlPoints) {
        // Save result to DB
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                FetchData fetchData = new FetchData("http://192.168.125.238/LoginRegister/updatepoints.php?username=" + username  + "&points=" + urlPoints);
                fetchData.startFetch();
            }
        });
    }

    public static void updateFromPHP(String s_username) {
        UserSingleton instance = UserSingleton.getInstance(); // Get the singleton instance
        FetchData fetchData = new FetchData("http://192.168.125.238/LoginRegister/readpoints.php?username=" + s_username);
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                String result = fetchData.getResult();
                try {
                    JSONObject json = new JSONObject(result);
                    String username = json.getString("username");
                    String email = json.getString("email");
                    int points = json.getInt("points");
                    instance.setUsername(username); // Update username in singleton
                    instance.setEmail(email);
                    instance.setPoints(points); // Update points in singleton
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static List<Map<String, Object>> getAllUsersInfo() {
        FetchData fetchData = new FetchData("http://192.168.125.238/LoginRegister/getusersinfo.php");
        List<Map<String, Object>> usersList = new ArrayList<>(); // Initialize list to store user info

        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                String result = fetchData.getResult();
                try {
                    JSONArray jsonArray = new JSONArray(result); // Parse the JSON array

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = jsonArray.getJSONObject(i);
                        String username = json.getString("username");
                        int points = json.getInt("points");
                        Map<String, Object> userInfo = new HashMap<>();
                        userInfo.put("username", username);
                        userInfo.put("points", points);
                        usersList.add(userInfo);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return usersList;
    }

    public static String getUserImageNameFromDB(String username){
        FetchData fetchData = new FetchData("http://192.168.125.238/LoginRegister/getuserimage.php?username=" + username);
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                return fetchData.getResult();
            }
        }
        return null;
    }

    public static boolean updateUserImageInDB(String username, String imageName){
        FetchData fetchData = new FetchData("http://192.168.125.238/LoginRegister/updateimage.php?username=" + username + "&image='" + imageName + "'");
        if (fetchData.startFetch()) {
            return fetchData.onComplete();
        }
        return false;
    }



}

