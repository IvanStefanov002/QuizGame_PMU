package com.example.loginregister.EntryModule;

import static com.example.loginregister.Utilities.Requests.OnResumeReadPoints;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.loginregister.GameQuizzes.GeoGuesser;
import com.example.loginregister.GameQuizzes.QuestionActivity;
import com.example.loginregister.Interfaces.FetchDataCallback;
import com.example.loginregister.R;
import com.example.loginregister.GameQuizzes.TicTacToe;
import com.example.loginregister.Models.UserSingleton;
import com.example.loginregister.Utilities.DesignFunctionalities;
import com.example.loginregister.Utilities.Requests;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity  {
    UserSingleton sObject = UserSingleton.getInstance();
    private TextView usernameTextView, pointsTextView;
    int total_points = 0;
    ImageButton historyButton, geographyButton, programmingButton, sportsButton, tictactoeButton, geoguesser;

    //to hide android home buttons
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            DesignFunctionalities.hideSystemUI(this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize TextViews
        usernameTextView = findViewById(R.id.username_text_view);
        pointsTextView = findViewById(R.id.points_text_view);
    }

    @Override
    public void onResume() {
        super.onResume();
        DesignFunctionalities.hideSystemUI(this);

        historyButton = findViewById(R.id.history_button);
        geographyButton = findViewById(R.id.geography_button);
        programmingButton = findViewById(R.id.programming_button);
        sportsButton = findViewById(R.id.sport_button);
        tictactoeButton = findViewById(R.id.tictactoe_button);
        geoguesser = findViewById(R.id.geoguesser_button);

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

        geoguesser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GeoGuesser.class);
                intent.putExtra("POINTS", total_points);
                intent.putExtra("CATEGORY", "Geoguesser");
                startActivity(intent);
            }
        });
        OnResumeReadPoints(sObject.getUsername(), new FetchDataCallback() {
            @Override
            public void onDataFetched(String[] result) {
                // Handle the fetched result here
                try {
                    JSONObject json = new JSONObject(result[0]);
                    int points = json.getInt("points");
                    total_points = points;
                    usernameTextView.setText("Logged as: " + sObject.getUsername());
                    pointsTextView.setText("Points: " + sObject.getPoints());
                } catch (JSONException e) {
                    e.printStackTrace();
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