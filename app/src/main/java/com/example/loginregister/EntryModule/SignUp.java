package com.example.loginregister.EntryModule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginregister.Interfaces.RequestsHandler;
import com.example.loginregister.R;
import com.example.loginregister.Utilities.DesignFunctionalities;
import com.example.loginregister.Utilities.Requests;
import com.google.android.material.textfield.TextInputEditText;

public class SignUp extends AppCompatActivity {

    TextInputEditText textInputEditTextFullname, textInputEditTextUsername, textInputEditTextPassword, textInputEditTextEmail;
    Button buttonSignUp;
    TextView textViewLogin;
    ProgressBar progressBar;

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
        setContentView(R.layout.activity_signup);
        DesignFunctionalities.hideSystemUI(this);

        textInputEditTextFullname = findViewById(R.id.fullname);
        textInputEditTextUsername = findViewById(R.id.username);
        textInputEditTextPassword = findViewById(R.id.password);
        textInputEditTextEmail = findViewById(R.id.email);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        textViewLogin = findViewById(R.id.loginText);
        progressBar = findViewById(R.id.progress);

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String fullname, username, password, email;
                fullname = String.valueOf(textInputEditTextFullname.getText());
                username = String.valueOf(textInputEditTextUsername.getText());
                password = String.valueOf(textInputEditTextPassword.getText());
                email = String.valueOf(textInputEditTextEmail.getText());

                if(!fullname.equals("") && !username.equals("") && !password.equals("") && !email.equals("")) {
                    progressBar.setVisibility(View.VISIBLE);
                    Requests.Register(fullname, username, password, email, getApplicationContext(), new RequestsHandler() {
                        @Override
                        public void onSuccess(String data) {
                            progressBar.setVisibility(View.GONE);
                            if(data.equals("Sign Up Success")) {
                                Toast.makeText(getApplicationContext(), "Успешна регистрация!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplication(), Login.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(getApplicationContext(), "Неуспешна регистрация!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(String data) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(getApplicationContext(), "Всички полета са задължителни!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}