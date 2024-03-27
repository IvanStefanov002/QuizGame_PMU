package com.example.loginregister;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.vishnusivadas.advanced_httpurlconnection.FetchData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QuestionActivity extends AppCompatActivity {
    UserSingleton QASingleton = UserSingleton.getInstance();
    private JSONArray questionsArray;
    private int currentQuestionIndex = 0;
    int total_points, question_points, nTotalPointsGained = 0;
    boolean pointsUpdated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        // Retrieve category information from Intent extras
        String category = getIntent().getStringExtra("CATEGORY");
        total_points = getIntent().getIntExtra("POINTS", 0);

        final RadioButton bAAnswer = findViewById(R.id.answer_a_button); // Initialize answer_a_button
        final RadioButton bBAnswer = findViewById(R.id.answer_b_button); // Initialize answer_b_button
        final RadioButton bCAnswer = findViewById(R.id.answer_c_button); // Initialize answer_c_button
        final Button nextQuestionButton = findViewById(R.id.next_question_button); // Initialize next_question_button
        final Button checkAnswerButton = findViewById(R.id.check_answer_button);// Initialize checkAnswerButton
        TextView categoryTextView = findViewById(R.id.text_header);
        categoryTextView.setText("Category: " + category);

        // Fetch questions related to the selected category
        FetchData fetchData = new FetchData("http://192.168.220.238/LoginRegister/readquestions.php?category=" + category);
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                String result = fetchData.getResult();
                try {
                    questionsArray = new JSONArray(result);

                    // Display the current question
                    displayQuestion(bAAnswer, bBAnswer, bCAnswer);

                    // Set click listener for the "Next Question" button
                    nextQuestionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            currentQuestionIndex++;
                            // Display the next question
                            displayQuestion(bAAnswer, bBAnswer, bCAnswer);
                        }
                    });

                    // Set click listener for the "Check Answer" button
                    checkAnswerButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Get the text of the selected button
                            String selectedAnswer = null;
                            if (bAAnswer.isChecked()) {
                                bAAnswer.setChecked(false);
                                selectedAnswer = "A";
                            } else if (bBAnswer.isChecked()) {
                                bBAnswer.setChecked(false);
                                selectedAnswer = "B";
                            } else if (bCAnswer.isChecked()) {
                                bCAnswer.setChecked(false);
                                selectedAnswer = "C";
                            }

                            // Check if any answer is selected
                            if (selectedAnswer != null) {
                                // Call method to check the user's answer
                                checkAnswer(selectedAnswer);
                            } else {
                                // No answer selected, show a message to the user
                                Toast.makeText(QuestionActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void displayQuestion(RadioButton aAnswer, RadioButton bAnswer, RadioButton cAnswer) {
        aAnswer.setChecked(false);
        bAnswer.setChecked(false);
        cAnswer.setChecked(false);
        pointsUpdated = false;
        Button endQuizButton = findViewById(R.id.end_quiz);
        endQuizButton.setVisibility(View.GONE);

        ImageView qImage = findViewById(R.id.qimage);
        qImage.setVisibility(View.GONE);

        try {
            JSONObject questionObject = questionsArray.getJSONObject(currentQuestionIndex);
            String question = questionObject.getString("question");
            question_points = questionObject.getInt("points");

            // Get the answers array
            JSONArray answersArray = questionObject.getJSONArray("answers");

            // Build the string representation of answers
            for (int i = 0; i < answersArray.length(); i++) {
                JSONObject answerObject = answersArray.getJSONObject(i);
                String answer = answerObject.getString("answer");

                switch (i){
                    case 0:
                        aAnswer.setText(answer);
                        break;
                    case 1:
                        bAnswer.setText(answer);
                        break;
                    case 2:
                        cAnswer.setText(answer);
                        break;
                }
            }

            // Update UI with the question, answers, and points
            TextView tv_points = findViewById(R.id.tv_points);
            tv_points.setText("This question gives: " + question_points + " points");

            TextView questionsTextView = findViewById(R.id.questions_text_view);
            questionsTextView.setText((currentQuestionIndex + 1) + ". Question: " + question + "\n");

            // Enable/disable the "Next Question" button based on the current question index
            Button nextQuestionButton = findViewById(R.id.next_question_button);
            nextQuestionButton.setEnabled(currentQuestionIndex < questionsArray.length() - 1);
            if( !nextQuestionButton.isEnabled() ){
                nextQuestionButton.setVisibility(View.GONE);
                endQuizButton.setVisibility(View.VISIBLE);
                endQuizButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        QASingleton.updateFromPHP(QASingleton.getUsername());
                        finish();
                    }
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void checkAnswer(String userAnswer) {
        char correctAnswer = 0;
        final TextView totalPointsGained = findViewById(R.id.tv2_points);
        totalPointsGained.setText("points gained: " + nTotalPointsGained);

        try {
            JSONObject questionObject = questionsArray.getJSONObject(currentQuestionIndex);
            JSONArray answersArray = questionObject.getJSONArray("answers");

            // Find the correct answer
            for (int i = 0; i < answersArray.length(); i++) {
                JSONObject answerObject = answersArray.getJSONObject(i);
                int isCorrect = answerObject.getInt("correct");
                if (isCorrect == 1) {
                    correctAnswer = (char) ('a' + i); // Converting index to char 'a', 'b', 'c'
                    break;
                }
            }

            // Compare the user's answer with the correct answer
            if (userAnswer.equalsIgnoreCase(String.valueOf(correctAnswer))) {
                // Correct Answer
                String imageName = questionObject.getString("imageName");
                int resourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());
                ImageView qImage = findViewById(R.id.qimage);
                qImage.setImageResource(resourceId);
                qImage.setVisibility(View.VISIBLE); // Make the ImageView visible
                Toast.makeText(QuestionActivity.this, "Correct Answer!", Toast.LENGTH_SHORT).show();

                // Update total points
                total_points += question_points;
                if(!pointsUpdated){
                    nTotalPointsGained += question_points;
                    totalPointsGained.setText("points gained: " + nTotalPointsGained);
                    updatePoints(QASingleton.getUsername(), total_points);
                    pointsUpdated = true;
                }
            } else {
                // Incorrect Answer
                Toast.makeText(QuestionActivity.this, "Incorrect Answer!", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        QASingleton.updateFromPHP(QASingleton.getUsername());
        finish();
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
}

