package com.example.loginregister.EntryModule;

import static com.example.loginregister.Utilities.Requests.OnResumeReadPoints;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginregister.Adapters.DropDownAdapter;
import com.example.loginregister.GameQuizzes.GeoGuesser;
import com.example.loginregister.GameQuizzes.QuestionActivity;
import com.example.loginregister.Interfaces.FetchDataCallback;
import com.example.loginregister.R;
import com.example.loginregister.GameQuizzes.TicTacToe;
import com.example.loginregister.Models.UserSingleton;
import com.example.loginregister.Utilities.DesignFunctionalities;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity  {
    UserSingleton sObject = UserSingleton.getInstance();
    TextView usernameTextView;
    ImageButton historyButton, natureButton, cultureButton, artButton, tictactoeButton, geoguesser;
    ImageView facebook, twitter, instagram;
    Spinner hSpinner;

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

        // Initialize variables
        facebook = findViewById(R.id.facebook);
        instagram = findViewById(R.id.instagram);
        twitter = findViewById(R.id.twitter);
        usernameTextView = findViewById(R.id.username_text_view);
        artButton = findViewById(R.id.art_button);
        natureButton = findViewById(R.id.nature_button);
        cultureButton = findViewById(R.id.culture_button);
        historyButton = findViewById(R.id.history_button);
        tictactoeButton = findViewById(R.id.tictactoe_button);
        geoguesser = findViewById(R.id.geoguesser_button);
        hSpinner = findViewById(R.id.hamburger_spinner);



        DropDownAdapter adapter = new DropDownAdapter(this, new String[]{"Презареди", "Профил", "Класация", "Отписване"});
        hSpinner.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        DesignFunctionalities.hideSystemUI(this);

        hSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedItem = (String) parentView.getItemAtPosition(position);
                // Perform logic based on the selected item
                switch (selectedItem) {
                    case "Презареди":
                        break;
                    case "Профил":
                        // Start ProfileActivity
                        Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivity(profileIntent);
                        break;
                    case "Класация":
                        // Start RankingList Activity
                        Intent rankingListIntent = new Intent(MainActivity.this, RankingList.class);
                        startActivity(rankingListIntent);
                        break;
                    case "Отписване":
                        // Handle Logout logic
                        finish();
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing if nothing is selected
            }
        });

        hSpinner.setSelection(0);

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start QuestionActivity and pass the category information
                startQuestionActivity("History", sObject.getPoints(), sObject.getUsername());
            }
        });

        natureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start QuestionActivity and pass the category information
                startQuestionActivity("Nature", sObject.getPoints(), sObject.getUsername());
            }
        });

        artButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start QuestionActivity and pass the category information
                startQuestionActivity("Art", sObject.getPoints(), sObject.getUsername());
            }
        });

        cultureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuestionActivity("Culture", sObject.getPoints(), sObject.getUsername());
            }
        });

        tictactoeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TicTacToe.class);
                intent.putExtra("POINTS", sObject.getPoints());
                startActivity(intent);
            }
        });

        geoguesser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GeoGuesser.class);
                intent.putExtra("POINTS", sObject.getPoints());
                intent.putExtra("CATEGORY", "Geoguesser");
                startActivity(intent);
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://www.facebook.com/");
            }
        });

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://www.instagram.com/");
            }
        });

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://www.twitter.com/");
            }
        });

        OnResumeReadPoints(sObject.getUsername(), new FetchDataCallback() {
            @Override
            public void onDataFetched(String[] result) {
                // Handle the fetched result here
                try {
                    JSONObject json = new JSONObject(result[0]);
                    int points = json.getInt("points");
                    sObject.setPoints(points);
                    usernameTextView.setText("Вписан като: " + sObject.getUsername());
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

    private void gotoUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
}