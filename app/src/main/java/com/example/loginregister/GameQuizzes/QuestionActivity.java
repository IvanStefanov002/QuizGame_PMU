package com.example.loginregister.GameQuizzes;

import static com.example.loginregister.Utilities.Requests.updateFromPHP;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loginregister.R;
import com.example.loginregister.Models.UserSingleton;
import com.example.loginregister.Utilities.DesignFunctionalities;
import com.example.loginregister.Utilities.Requests;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QuestionActivity extends AppCompatActivity {
    UserSingleton QASingleton = UserSingleton.getInstance();
    private JSONArray questionsArray;
    private int currentQuestionIndex = 0;
    int total_points, question_points, nTotalPointsGained = 0;
    boolean pointsUpdated = false;
    RadioButton bAAnswer, bBAnswer, bCAnswer;
    Button checkAnswerButton, nextQuestionButton;

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
        setContentView(R.layout.activity_question);

        // Retrieve category information from Intent extras
        String category = getIntent().getStringExtra("CATEGORY");
        total_points = getIntent().getIntExtra("POINTS", 0);

        bAAnswer = findViewById(R.id.answer_a_button); // Initialize answer_a_button
        bBAnswer = findViewById(R.id.answer_b_button); // Initialize answer_b_button
        bCAnswer = findViewById(R.id.answer_c_button); // Initialize answer_c_button
        nextQuestionButton = findViewById(R.id.next_question_button); // Initialize next_question_button
        checkAnswerButton = findViewById(R.id.check_answer_button);// Initialize checkAnswerButton
        TextView categoryTextView = findViewById(R.id.text_header);
        categoryTextView.setText("Категория: " + category);

        String result = Requests.readQuestions(category);
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
                        checkAnswerButton.setVisibility(View.GONE);
                    } else {
                        // No answer selected, show a message to the user
                        Toast.makeText(QuestionActivity.this, "Моля изберете отговор", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void displayQuestion(RadioButton aAnswer, RadioButton bAnswer, RadioButton cAnswer) {
        aAnswer.setChecked(false);
        bAnswer.setChecked(false);
        cAnswer.setChecked(false);
        pointsUpdated = false;
        Button endQuizButton = findViewById(R.id.end_quiz);
        endQuizButton.setVisibility(View.GONE);
        checkAnswerButton.setVisibility(View.VISIBLE);

        try {
            JSONObject questionObject = questionsArray.getJSONObject(currentQuestionIndex);
            String imageName = questionObject.getString("imageName");
            int resourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());
            ImageView qImage = findViewById(R.id.qimage);
            qImage.setImageResource(resourceId);
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
            tv_points.setText("Въпросът носи: " + question_points + " точки");

            TextView questionsTextView = findViewById(R.id.questions_text_view);
            questionsTextView.setText((currentQuestionIndex + 1) + ". Въпрос: " + question + "\n");

            // Enable/disable the "Next Question" button based on the current question index
            Button nextQuestionButton = findViewById(R.id.next_question_button);
            nextQuestionButton.setEnabled(currentQuestionIndex < questionsArray.length() - 1);
            if( !nextQuestionButton.isEnabled() ){
                nextQuestionButton.setVisibility(View.GONE);
                endQuizButton.setVisibility(View.VISIBLE);
                endQuizButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateFromPHP(QASingleton.getUsername());
                        finish();
                    }
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void checkAnswer(String userAnswer) {
        char correctAnswerChar = findCorrectAnswer();
        final TextView totalPointsGained = findViewById(R.id.tv2_points);
        totalPointsGained.setText("получени точки: " + nTotalPointsGained);

        if (userAnswer.equalsIgnoreCase(String.valueOf(correctAnswerChar))) {
            handleCorrectAnswer();
        } else {
            handleIncorrectAnswer();
        }
    }

    private char findCorrectAnswer() {
        char correctAnswer = 0;
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return correctAnswer;
    }

    private void handleCorrectAnswer() {
        Toast.makeText(QuestionActivity.this, "Верен отговор!", Toast.LENGTH_SHORT).show();
        // Update total points
        total_points += question_points;
        if (!pointsUpdated) {
            nTotalPointsGained += question_points;
            final TextView totalPointsGained = findViewById(R.id.tv2_points);
            totalPointsGained.setText("получени точки: " + nTotalPointsGained);
            Requests.updatePoints(QASingleton.getUsername(), total_points);
            pointsUpdated = true;
        }
    }

    private void handleIncorrectAnswer() {
        Toast.makeText(QuestionActivity.this, "Грешен отговор!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateFromPHP(QASingleton.getUsername());
        finish();
    }
}

