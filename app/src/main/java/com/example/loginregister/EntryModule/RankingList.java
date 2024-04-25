package com.example.loginregister.EntryModule;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loginregister.Models.UserSingleton;
import com.example.loginregister.R;
import com.example.loginregister.Utilities.DesignFunctionalities;
import com.example.loginregister.Utilities.Requests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RankingList extends AppCompatActivity {

    private static final int MAX_DISPLAYED_USERS = 5;
    TextView tvUsername1, tvUsername2, tvUsername3, tvUsername4, tvUsername5;
    TextView tvWinner1, tvWinner2, tvWinner3, tvFooter;
    ImageButton ibBackbutton;
    UserSingleton usInstance = UserSingleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rakinglist);
        DesignFunctionalities.hideSystemUI(this);

        // Initialize views
        tvUsername1 = findViewById(R.id.username1);
        tvUsername2 = findViewById(R.id.username2);
        tvUsername3 = findViewById(R.id.username3);
        tvUsername4 = findViewById(R.id.username4);
        tvUsername5 = findViewById(R.id.username5);
        tvWinner1 = findViewById(R.id.winner1);
        tvWinner2 = findViewById(R.id.winner2);
        tvWinner3 = findViewById(R.id.winner3);
        tvFooter = findViewById(R.id.footerTv);
        ibBackbutton = findViewById(R.id.bb);

        // Back button click listener
        ibBackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        DesignFunctionalities.hideSystemUI(this);
        updateRankingList();
    }

    private void updateRankingList() {
        List<Map<String, Object>> userInfoList = Requests.getAllUsersInfo();
        Map<String, Integer> userPointsMap = new HashMap<>();

        for (Map<String, Object> userInfo : userInfoList) {
            String username = (String) userInfo.get("username");
            int points = (int) userInfo.get("points");
            userPointsMap.put(username, points);
        }

        List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(userPointsMap.entrySet());
        Collections.sort(sortedList, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> entry1, Map.Entry<String, Integer> entry2) {
                return entry2.getValue().compareTo(entry1.getValue());
            }
        });

        String givenUsername = usInstance.getUsername();
        int index = -1;

        for (int i = 0; i < sortedList.size(); i++) {
            Map.Entry<String, Integer> entry = sortedList.get(i);
            if (entry.getKey().equals(givenUsername)) {
                index = i + 1;
                break;
            }
        }

        if (index > MAX_DISPLAYED_USERS) {
            tvFooter.setText("Съжелявам! Не си в Top 5! Ти си #" + index);
        }

        for (int i = 0; i < Math.min(sortedList.size(), MAX_DISPLAYED_USERS); i++) {
            Map.Entry<String, Integer> entry = sortedList.get(i);
            switch (i) {
                case 0:
                    setUserInfo(tvUsername1, tvWinner1, entry.getKey(), entry.getValue());
                    break;
                case 1:
                    setUserInfo(tvUsername2, tvWinner2, entry.getKey(), entry.getValue());
                    break;
                case 2:
                    setUserInfo(tvUsername3, tvWinner3, entry.getKey(), entry.getValue());
                    break;
                case 3:
                    setUserInfo(tvUsername4, null, entry.getKey(), entry.getValue());
                    break;
                case 4:
                    setUserInfo(tvUsername5, null, entry.getKey(), entry.getValue());
                    break;
            }
        }
    }

    private void setUserInfo(TextView usernameTextView, TextView winnerTextView, String username, int points) {
        usernameTextView.setText("Username: " + username + ", Points: " + points);
        if (username.equals(usInstance.getUsername())) {
            usernameTextView.setTextColor(Color.RED);
        }
        if (winnerTextView != null) {
            winnerTextView.setText(username);
        }
    }
}
