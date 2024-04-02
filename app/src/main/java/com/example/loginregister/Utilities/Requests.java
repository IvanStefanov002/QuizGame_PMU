package com.example.loginregister.Utilities;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.loginregister.Interfaces.FetchDataCallback;
import com.example.loginregister.Interfaces.RequestsHandler;
import com.example.loginregister.Models.UserSingleton;
import com.vishnusivadas.advanced_httpurlconnection.FetchData;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONException;
import org.json.JSONObject;

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
                PutData putData = new PutData("http://192.168.69.107/LoginRegister/signup.php", "POST", field, data);
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
                PutData putData = new PutData("http://192.168.69.107/LoginRegister/login.php", "POST", field, data);
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
                FetchData fetchData = new FetchData("http://192.168.69.107/LoginRegister/readpoints.php?username=" + username);
                if (fetchData.startFetch()) {
                    if (fetchData.onComplete()) {
                        String[] result = {fetchData.getResult()};
                        callback.onDataFetched(result);
                    }
                }
            }
        });
    }

    public static void updatePoints(String username, int urlPoints) {
        // Save result to DB
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                FetchData fetchData = new FetchData("http://192.168.69.107/LoginRegister/updatepoints.php?username=" + username  + "&points=" + urlPoints);
                fetchData.startFetch();
            }
        });
    }

    public static void updateFromPHP(String s_username) {
        UserSingleton instance = UserSingleton.getInstance(); // Get the singleton instance
        FetchData fetchData = new FetchData("http://192.168.69.107/LoginRegister/readpoints.php?username=" + s_username);
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                String result = fetchData.getResult();
                try {
                    JSONObject json = new JSONObject(result);
                    String username = json.getString("username");
                    int points = json.getInt("points");
                    instance.setUsername(username); // Update username in singleton
                    instance.setPoints(points); // Update points in singleton
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

