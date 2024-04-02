package com.example.loginregister.EntryModule;

import static com.example.loginregister.Utilities.Requests.updateFromPHP;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginregister.Interfaces.RequestsHandler;
import com.example.loginregister.R;
import com.example.loginregister.Utilities.DesignFunctionalities;
import com.example.loginregister.Utilities.Requests;
import com.google.android.material.textfield.TextInputEditText;

public class Login extends AppCompatActivity {
    TextInputEditText textInputEditTextUsername, textInputEditTextPassword;
    ProgressBar progressBar;
    TextView textViewSignUp;
    ImageView tweet, insta, twit;
    Button buttonLogin;

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
        setContentView(R.layout.activity_login);

        tweet = findViewById(R.id.facebook);
        insta = findViewById(R.id.instagram);
        twit = findViewById(R.id.twitter);

        tweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://www.facebook.com/");
            }
        });

        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://www.instagram.com/");
            }
        });

        twit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://www.twitter.com/");
            }
        });

        textInputEditTextUsername = findViewById(R.id.username);
        textInputEditTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewSignUp = findViewById(R.id.signUpText);
        progressBar = findViewById(R.id.progress);

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), SignUp.class);
                startActivity(intent);
                finish();
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String username, password;
                username = String.valueOf(textInputEditTextUsername.getText());
                password = String.valueOf(textInputEditTextPassword.getText());

                if(!username.equals("") && !password.equals("")) {
                    progressBar.setVisibility(View.VISIBLE);
                    Requests.Login(username, password, getApplicationContext(), new RequestsHandler() {
                        @Override
                        public void onSuccess(String data) {
                            progressBar.setVisibility(View.GONE);
                            if(data.equals("Login Success")) {
                                updateFromPHP(username);
                                Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplication(), MainActivity.class);
                                startActivity(intent);
                            }
                        }
                        @Override
                        public void onFailure(String data) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(getApplicationContext(), "All fields are required!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void gotoUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
}