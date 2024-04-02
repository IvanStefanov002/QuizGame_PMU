package com.example.loginregister.GameQuizzes;

import static com.example.loginregister.Utilities.Requests.updateFromPHP;
import static com.example.loginregister.Utilities.Requests.updatePoints;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
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

        rbPin1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        rbPin1.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.pinChecked));
                    }
                } else {
                    // Reset background tint if needed
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        rbPin1.setBackgroundTintList(null);
                    }
                }
            }
        });


        rbPin2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        rbPin2.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.pinChecked));
                    }
                } else {
                    // Reset background tint if needed
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        rbPin2.setBackgroundTintList(null);
                    }
                }
            }
        });

        rbPin3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        rbPin3.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.pinChecked));
                    }
                } else {
                    // Reset background tint if needed
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        rbPin3.setBackgroundTintList(null);
                    }
                }
            }
        });

        rbPin4.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    rbPin4.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.pinChecked));
                }
            } else {
                // Reset background tint if needed
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    rbPin4.setBackgroundTintList(null);
                }
            }
        });

        rbPin5.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    rbPin4.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.pinChecked));
                }
            } else {
                // Reset background tint if needed
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    rbPin4.setBackgroundTintList(null);
                }
            }
        });

        rbPin6.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    rbPin4.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.pinChecked));
                }
            } else {
                // Reset background tint if needed
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    rbPin4.setBackgroundTintList(null);
                }
            }
        });

        rbPin7.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    rbPin4.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.pinChecked));
                }
            } else {
                // Reset background tint if needed
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    rbPin4.setBackgroundTintList(null);
                }
            }
        });

        rbPin8.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    rbPin4.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.pinChecked));
                }
            } else {
                // Reset background tint if needed
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    rbPin4.setBackgroundTintList(null);
                }
            }
        });

        rbPin9.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    rbPin4.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.pinChecked));
                }
            } else {
                // Reset background tint if needed
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    rbPin4.setBackgroundTintList(null);
                }
            }
        });

        FetchData fetchData = new FetchData("http://192.168.69.107/LoginRegister/readquestions.php?category=" + category);
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                String result = fetchData.getResult();
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
                            displayQuestion(rbPin1, rbPin2, rbPin3, rbPin4, rbPin5, rbPin6, rbPin7, rbPin8, rbPin9);
                        }
                    });
                    bCheckAnswer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Get the text of the selected button
                            String selectedAnswer = null;
                            if (rbPin1.isChecked()) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    rbPin1.setBackgroundTintList(null);
                                }
                                rbPin1.setChecked(false);
                                selectedAnswer = "p1";
                            } else if (rbPin2.isChecked()) {
                                rbPin2.setChecked(false);
                                selectedAnswer = "p2";
                            } else if (rbPin3.isChecked()) {
                                rbPin3.setChecked(false);
                                selectedAnswer = "p3";
                            } else if (rbPin4.isChecked()) {
                                rbPin4.setChecked(false);
                                selectedAnswer = "p4";
                            } else if (rbPin5.isChecked()) {
                                rbPin5.setChecked(false);
                                selectedAnswer = "p5";
                            } else if (rbPin6.isChecked()) {
                                rbPin6.setChecked(false);
                                selectedAnswer = "p6";
                            } else if (rbPin7.isChecked()) {
                                rbPin7.setChecked(false);
                                selectedAnswer = "p7";
                            } else if (rbPin8.isChecked()) {
                                rbPin8.setChecked(false);
                                selectedAnswer = "p8";
                            } else if (rbPin9.isChecked()) {
                                rbPin9.setChecked(false);
                                selectedAnswer = "p9";
                            }

                            // Check if any answer is selected
                            if (selectedAnswer != null) {
                                // Call method to check the user's answer
                                checkAnswer(selectedAnswer);
                            } else {
                                // No answer selected, show a message to the user
                                Toast.makeText(GeoGuesser.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void displayQuestion(RadioButton mPin1, RadioButton mPin2, RadioButton mPin3, RadioButton mPin4, RadioButton mPin5, RadioButton mPin6, RadioButton mPin7, RadioButton mPin8, RadioButton mPin9) {
        mPin1.setChecked(false);
        mPin2.setChecked(false);
        mPin3.setChecked(false);
        mPin4.setChecked(false);
        mPin5.setChecked(false);
        mPin6.setChecked(false);
        mPin7.setChecked(false);
        mPin8.setChecked(false);
        mPin9.setChecked(false);

        pointsUpdated = false;
        bEndQuiz.setVisibility(View.GONE);

        try {
            //[{"question":"Question1","answers":[{"answer":"p1","correct":1}],"points":"10","imageName":""}
            JSONObject questionObject = questionsArray.getJSONObject(currentQuestionIndex);
            String question = questionObject.getString("question");
            question_points = questionObject.getInt("points");

            // Build the string representation of answers
//            for (int i = 0; i < answersArray.length(); i++) {
//                JSONObject answerObject = answersArray.getJSONObject(i);
//                String answer = answerObject.getString("answer");
//
////                switch (i){
////                    case 1:
////                        mPin1.setText(answer);
////                        break;
////                    case 2:
////                        mPin2.setText(answer);
////                        break;
////                    case 3:
////                        mPin3.setText(answer);
////                        break;
////                    case 4:
////                        mPin4.setText(answer);
////                        break;
////                    case 5:
////                        mPin5.setText(answer);
////                        break;
////                    case 6:
////                        mPin6.setText(answer);
////                        break;
////                    case 7:
////                        mPin7.setText(answer);
////                        break;
////                    case 8:
////                        mPin8.setText(answer);
////                        break;
////                    case 9:
////                        mPin9.setText(answer);
////                        break;
////                }
//            }

            // Update UI with the question, answers, and points
            TextView questionsTextView = findViewById(R.id.question_text);
            questionsTextView.setText((currentQuestionIndex + 1) + ". Question: " + question + "\n");

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
            handleCorrectAnswer();
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


    private void handleCorrectAnswer() {
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