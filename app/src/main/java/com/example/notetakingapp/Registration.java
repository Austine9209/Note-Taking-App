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

public class Registration extends AppCompatActivity {

    EditText emailEditText, passwordEditText, confirmPasswordEditText;
    Button createAccountBtn;
    ProgressBar progressBar;
    TextView loginBtnTextView;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        confirmPasswordEditText = findViewById(R.id.confirm_password_edit_text);
        createAccountBtn = findViewById(R.id.create_account_btn);
        progressBar = findViewById(R.id.progress_bar);
        loginBtnTextView = findViewById(R.id.login_text_view_btn);

        createAccountBtn.setOnClickListener(view -> createAccount());
        loginBtnTextView.setOnClickListener(view -> startActivity(new Intent(Registration.this,LogInActivity.class)));
    }

    void createAccount() {

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        boolean isValidated = validateData(email,password,confirmPassword);
        if(!isValidated){
            return;
        }

        createAccountInFirebase(email,password);

    }

    void createAccountInFirebase(String email,String password){
        changeInProgress(true);

        auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeInProgress(false);
                        if (task.isSuccessful()){
                            //creating acc is done
                            Utility.showToast(Registration.this, "Registration is successful!");
                            auth.getCurrentUser().sendEmailVerification();
                            auth.signOut();
                            finish();
                        } else
                            //failure
                            Toast.makeText(Registration.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            createAccountBtn.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            createAccountBtn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email, String password, String confirmPassword) {
        // validate the data that are input by user.

      //  if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
      //      Toast.makeText(this, "Email is invalid!", Toast.LENGTH_SHORT).show();
      //      return false;
        // }

        if(password.length()<6){
            Toast.makeText(this, "Password length is too short!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!password.equals(confirmPassword)){
            Toast.makeText(this, "Password not matched!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

}