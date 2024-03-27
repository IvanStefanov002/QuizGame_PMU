package com.example.loginregister;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.vishnusivadas.advanced_httpurlconnection.FetchData;

import pl.droidsonroids.gif.GifImageView;

public class TicTacToe extends AppCompatActivity {
    private Button[][] buttons = new Button[3][3];
    private boolean player1Turn = true;
    private int checkCount = 0;
    int total_points = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tictactoe);

        UserSingleton GameSingleton = UserSingleton.getInstance();
        Button resetButton = findViewById(R.id.reset_button);
        GifImageView fireworks = findViewById(R.id.fw);
        ConstraintLayout mPage = findViewById(R.id.tictactoe);
        TextView theader = findViewById(R.id.text_header);

        int points = getIntent().getIntExtra("POINTS", 0);
        total_points = points;

        fireworks.setVisibility(View.GONE);

        // Initialize buttons
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String button = "button_"+i+j;
                int buttonId = getResources().getIdentifier(button, "id", getPackageName());
                if(buttonId!=0) {
                    buttons[i][j] = findViewById(buttonId); // This line could cause NullPointerException if buttons[i][j] is not initialized properly
                    theader.setText("Tic-Tac-Toe game! On turn is X");
                    buttons[i][j].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!((Button) v).getText().toString().isEmpty()) {
                                return; // Ignore if the button is already filled
                            }
                            if (player1Turn) {
                                ((Button) v).setText("X");
                                theader.setText("Tic-Tac-Toe game! On turn is 0");
                            } else {
                                ((Button) v).setText("O");
                                theader.setText("Tic-Tac-Toe game! On turn is X");
                            }

                            String winner = checkForWinner();
                            if (winner != null) {
                                disableButtons();
                                Toast.makeText(getApplicationContext(), "Player " + winner + " wins!", Toast.LENGTH_SHORT).show();
                                updatePoints(GameSingleton.getUsername(), total_points);
                                hideButtons();
                                mPage.setBackgroundColor(Color.BLACK);
                                fireworks.setVisibility(View.VISIBLE);
                                resetButton.setVisibility(View.VISIBLE);

                                resetButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finish();
                                    }
                                });

                            }else {
                                if(checkCount==9){
                                    Toast.makeText(getApplicationContext(), "Draw! Nobody wins!", Toast.LENGTH_SHORT).show();
                                    disableButtons();
                                    hideButtons();
                                    mPage.setBackgroundColor(Color.BLACK);
                                    fireworks.setVisibility(View.VISIBLE);
                                    String imageName = "sadface";
                                    int resourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());
                                    fireworks.setImageResource(resourceId);
                                    resetButton.setVisibility(View.VISIBLE);

                                    resetButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            finish();
                                        }
                                    });
                                }
                                player1Turn = !player1Turn;
                            }
                        }
                    });
                }
            }

        }
    }

    private void updatePoints(String username, int urlPoints) {
        // Save result to DB
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                FetchData fetchData = new FetchData("http://192.168.220.238/LoginRegister/updatepoints.php?username=" + username  + "&points=" + urlPoints);
                fetchData.startFetch();
            }
        });
    }
    private String checkForWinner() {
        checkCount+=1;
        String[][] field = new String[3][3];
        // Initialize the field array with the text of each button
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        // Check rows, columns, and diagonals for a winning condition
        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2]) && !field[i][0].isEmpty()) {
                return field[i][0]; // Winner in a row
            }
            if (field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i]) && !field[0][i].isEmpty()) {
                return field[0][i]; // Winner in a column
            }
        }
        if (field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) && !field[0][0].isEmpty()) {
            return field[0][0]; // Winner in the main diagonal
        }
        if (field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0]) && !field[0][2].isEmpty()) {
            return field[0][2]; // Winner in the other diagonal
        }

        // No winner found
        return null;
    }

    private void disableButtons() {
        // Iterate through all buttons and remove the OnClickListener
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setOnClickListener(null);
            }
        }
    }

    private void hideButtons() {
        // Iterate through all buttons and remove the OnClickListener
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setVisibility(View.GONE);
            }
        }
    }
}

