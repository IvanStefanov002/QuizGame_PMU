package com.example.loginregister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.vishnusivadas.advanced_httpurlconnection.FetchData;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity  {
    UserSingleton sObject = UserSingleton.getInstance();
    private TextView usernameTextView, pointsTextView;
    int total_points = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize TextViews
        usernameTextView = findViewById(R.id.username_text_view);
        pointsTextView = findViewById(R.id.points_text_view);


        // Find ImageButton references for each category
        ImageButton historyButton = findViewById(R.id.history_button);
        ImageButton geographyButton = findViewById(R.id.geography_button);
        ImageButton programmingButton = findViewById(R.id.programming_button);
        ImageButton sportsButton = findViewById(R.id.sport_button);
        ImageButton tictactoeButton = findViewById(R.id.tictactoe_button);

        // Set OnClickListener for each category ImageButton
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start QuestionActivity and pass the category information
                startQuestionActivity("History", total_points, String.valueOf(usernameTextView));
            }
        });

        geographyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start QuestionActivity and pass the category information
                startQuestionActivity("Geography", total_points, String.valueOf(usernameTextView));
            }
        });

        programmingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start QuestionActivity and pass the category information
                startQuestionActivity("Programming", total_points, String.valueOf(usernameTextView));
            }
        });

        sportsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuestionActivity("Sports", total_points, String.valueOf(usernameTextView));
            }
        });

        tictactoeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TicTacToe.class);
                intent.putExtra("POINTS", total_points);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                FetchData fetchData = new FetchData("http://192.168.220.238/LoginRegister/readpoints.php?username=" + sObject.getUsername());
                if (fetchData.startFetch()) {
                    if (fetchData.onComplete()) {
                        String result = fetchData.getResult();

                        try {
                            JSONObject json = new JSONObject(result);
                            int points = json.getInt("points");
                            total_points = points;
                            usernameTextView.setText("Logged as: " + sObject.getUsername());
                            pointsTextView.setText("Points: " + sObject.getPoints());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private void startQuestionActivity(String category, int points, String username) {
        Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
        intent.putExtra("CATEGORY", category);
        intent.putExtra("POINTS", points);
        intent.putExtra("USERNAME", username);
        startActivity(intent);
    }
}