package com.example.notetakingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {
    EditText emailEditText, passwordEditText;
    Button loginBtn;
    ProgressBar progressBar;
    TextView signupBtnTextView;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginBtn = findViewById(R.id.log_in_btn);
        progressBar = findViewById(R.id.progress_bar);
        signupBtnTextView = findViewById(R.id.signup_text_view_btn);

        loginBtn.setOnClickListener(view -> loginUser());
        signupBtnTextView.setOnClickListener(view -> startActivity(new Intent(LogInActivity.this,Registration.class)));
    }

    void loginUser (){

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        boolean isValidated = validateData(email,password);
        if(!isValidated){
            return;
        }

        loginAccountInFirebase(email,password);

    }

    void loginAccountInFirebase(String email,String password){
        auth = FirebaseAuth.getInstance();
        changeInProgress(true);
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeInProgress(false);
                        if (task.isSuccessful()){
                            //login is success
                            if (auth.getCurrentUser().isEmailVerified()){
                                //go to Main Activity//
                                startActivity(new Intent(LogInActivity.this,MainActivity.class));
                                finish();
                            }else {
                                Utility.showToast(LogInActivity.this, "Email not verified, Please verify your email.");
                            }

                        } else {
                            //failure
                            Utility.showToast(LogInActivity.this,task.getException().getLocalizedMessage());
                        }
                    }
                });

    }

    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email, String password) {
        // validate the data that are input by user.

        //if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
          //  Toast.makeText(this, "Email is invalid!", Toast.LENGTH_SHORT).show();
            //return false;
        //}

        if (password.length() < 6) {
            Toast.makeText(this, "Password length is too short!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}

