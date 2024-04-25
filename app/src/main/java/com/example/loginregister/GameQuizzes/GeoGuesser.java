package com.example.loginregister.GameQuizzes;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.loginregister.Models.UserSingleton;
import com.example.loginregister.R;
import com.example.loginregister.Utilities.DesignFunctionalities;
import com.example.loginregister.Utilities.Requests;
import com.vishnusivadas.advanced_httpurlconnection.FetchData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GeoGuesser extends AppCompatActivity {
    UserSingleton GGSingleton = UserSingleton.getInstance();
    private JSONArray questionsArray;
    private int currentQuestionIndex = 0;
    boolean pointsUpdated = false;
    int total_points, question_points = 0;
    RadioButton rbPin1, rbPin2, rbPin3, rbPin4, rbPin5, rbPin6, rbPin7, rbPin8, rbPin9;
    Button bNextQuestion, bCheckAnswer, bEndQuiz;

    @Override
    protected void onResume() {
        super.onResume();
        DesignFunctionalities.hideSystemUI(this); // Call hideSystemUI method from DesignFunctionalities
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            DesignFunctionalities.hideSystemUI(this); // Call hideSystemUI method from DesignFunctionalities
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geoguessr);

        bNextQuestion = findViewById(R.id.next_question_button);
        bCheckAnswer = findViewById(R.id.check_answer_button);
        bEndQuiz = findViewById(R.id.end_quiz_button);

        String category = getIntent().getStringExtra("CATEGORY");
        total_points = getIntent().getIntExtra("POINTS", 0);

        //hide EndQuiz button
        bEndQuiz.setVisibility(View.GONE);
        bCheckAnswer.setVisibility(View.VISIBLE);

        // Initialize RadioButton views after setContentView
        rbPin1 = findViewById(R.id.pin1);
        rbPin2 = findViewById(R.id.pin2);
        rbPin3 = findViewById(R.id.pin3);
        rbPin4 = findViewById(R.id.pin4);
        rbPin5 = findViewById(R.id.pin5);
        rbPin6 = findViewById(R.id.pin6);
        rbPin7 = findViewById(R.id.pin7);
        rbPin8 = findViewById(R.id.pin8);
        rbPin9 = findViewById(R.id.pin9);

        CompoundButton.OnCheckedChangeListener pinCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        buttonView.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.pinChecked));
                    }
                } else {
                    // Reset background tint if needed
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        buttonView.setBackgroundTintList(null);
                    }
                }
            }
        };

        rbPin1.setOnCheckedChangeListener(pinCheckedChangeListener);
        rbPin2.setOnCheckedChangeListener(pinCheckedChangeListener);
        rbPin3.setOnCheckedChangeListener(pinCheckedChangeListener);
        rbPin4.setOnCheckedChangeListener(pinCheckedChangeListener);
        rbPin5.setOnCheckedChangeListener(pinCheckedChangeListener);
        rbPin6.setOnCheckedChangeListener(pinCheckedChangeListener);
        rbPin7.setOnCheckedChangeListener(pinCheckedChangeListener);
        rbPin8.setOnCheckedChangeListener(pinCheckedChangeListener);
        rbPin9.setOnCheckedChangeListener(pinCheckedChangeListener);


        String result = Requests.readQuestions(category);
        try {
            questionsArray = new JSONArray(result);

            // Display the current question
            displayQuestion(rbPin1, rbPin2, rbPin3, rbPin4, rbPin5, rbPin6, rbPin7, rbPin8, rbPin9);

            // Set click listener for the "Next Question" button
            bNextQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentQuestionIndex++;
                    // Display the next question
                    bCheckAnswer.setVisibility(View.VISIBLE);
                    displayQuestion(rbPin1, rbPin2, rbPin3, rbPin4, rbPin5, rbPin6, rbPin7, rbPin8, rbPin9);
                }
            });
            bCheckAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the selected answer
                    String selectedAnswer = getSelectedAnswer();

                    // Check if any answer is selected
                    if (selectedAnswer != null) {
                        // Call method to check the user's answer
                        bCheckAnswer.setVisibility(View.GONE);
                        checkAnswer(selectedAnswer);
                    } else {
                        // No answer selected, show a message to the user
                        Toast.makeText(GeoGuesser.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                    }
                }

                // Method to retrieve the selected answer
                private String getSelectedAnswer() {
                    RadioButton[] radioButtons = {rbPin1, rbPin2, rbPin3, rbPin4, rbPin5, rbPin6, rbPin7, rbPin8, rbPin9};
                    String selectedAnswer = null;
                    for (RadioButton radioButton : radioButtons) {
                        if (radioButton.isChecked()) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                radioButton.setBackgroundTintList(null);
                            }
                            radioButton.setChecked(false);
                            selectedAnswer = radioButton.getText().toString();
                            break;
                        }
                    }
                    return selectedAnswer;
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void displayQuestion(RadioButton mPin1, RadioButton mPin2, RadioButton mPin3, RadioButton mPin4, RadioButton mPin5, RadioButton mPin6, RadioButton mPin7, RadioButton mPin8, RadioButton mPin9) {
        RadioButton[] radioButtons = {mPin1, mPin2, mPin3, mPin4, mPin5, mPin6, mPin7, mPin8, mPin9};
        for (RadioButton radioButton : radioButtons) {
            radioButton.setChecked(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                radioButton.setBackgroundTintList(null);
            }
        }

        pointsUpdated = false;
        bEndQuiz.setVisibility(View.GONE);

        try {
            JSONObject questionObject = questionsArray.getJSONObject(currentQuestionIndex);
            String question = questionObject.getString("question");
            question_points = questionObject.getInt("points");

            // Update UI with the question, answers, and points
            TextView questionsTextView = findViewById(R.id.question_text);
            questionsTextView.setText("Познай: " + question + "\n");

            // Enable/disable the "Next Question" button based on the current question index
            bNextQuestion.setEnabled(currentQuestionIndex < questionsArray.length() - 1);
            if( !bNextQuestion.isEnabled() ){
                bNextQuestion.setVisibility(View.GONE);
                bEndQuiz.setVisibility(View.VISIBLE);
                bEndQuiz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Requests.updateFromPHP(GGSingleton.getUsername());
                        finish();
                    }
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void checkAnswer(String userAnswer) {
        String correctAnswerChar = findCorrectAnswer();
        if (userAnswer.equalsIgnoreCase(correctAnswerChar)) {
            handleCorrectAnswer(userAnswer);
        } else {
            handleIncorrectAnswer();
        }
    }

    private String findCorrectAnswer() {
        String correctAnswer = "";
        try {
            JSONObject questionObject = questionsArray.getJSONObject(currentQuestionIndex);
            JSONArray answersArray = questionObject.getJSONArray("answers");
            JSONObject answerObject = answersArray.getJSONObject(0);
            correctAnswer = answerObject.getString("answer");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return correctAnswer;
    }


    private void handleCorrectAnswer(String userAnswer) {
        Toast.makeText(GeoGuesser.this, "Correct Answer!", Toast.LENGTH_SHORT).show();
        // Update total points
        total_points += question_points;
        if (!pointsUpdated) {
            Requests.updatePoints(GGSingleton.getUsername(), total_points);
            pointsUpdated = true;
        }
    }

    private void handleIncorrectAnswer() {
        Toast.makeText(GeoGuesser.this, "Incorrect Answer!", Toast.LENGTH_SHORT).show();
    }

    //TODO fix multiple requests on back/resume
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Requests.updateFromPHP(GGSingleton.getUsername());
        finish();
    }
}