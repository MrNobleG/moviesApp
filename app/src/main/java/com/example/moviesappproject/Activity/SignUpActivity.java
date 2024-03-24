package com.example.moviesappproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviesappproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText usernameEditText, fullNameEditText, emailEditText, passwordEditText;
    private Button signUpButton;
    private TextView signUpText;
    private databaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initializeViews();

        dbHelper = new databaseHelper(this);
        auth = FirebaseAuth.getInstance();

        setSignUpTextView();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    private void initializeViews() {
        usernameEditText = findViewById(R.id.editTextText);
        fullNameEditText = findViewById(R.id.fullNametext);
        emailEditText = findViewById(R.id.emailText);
        passwordEditText = findViewById(R.id.editTextPassword);
        signUpButton = findViewById(R.id.button2);
        signUpText = findViewById(R.id.logintext);
    }

    private void setSignUpTextView() {
        SpannableString spannableString = new SpannableString(signUpText.getText());
        int startIndex = spannableString.toString().indexOf("Login");
        int endIndex = startIndex + "Login".length();

        spannableString.setSpan(new UnderlineSpan(), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.transition, R.anim.transition2);
            }
        };

        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.purple)), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        signUpText.setText(spannableString);
        signUpText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void signUp() {
        String username = usernameEditText.getText().toString().trim();
        String fullName = fullNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (validateInputs(username, fullName, email, password)) {
            createUserWithEmailAndPassword(username, fullName, email, password);
        }
    }

    private boolean validateInputs(String username, String fullName, String email, String password) {
        if (username.isEmpty() || fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "All the fields are required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!fullName.matches("[a-zA-Z]+")) {
            Toast.makeText(SignUpActivity.this, "Full Name should be alphabetic", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isValidEmail(email)) {
            Toast.makeText(SignUpActivity.this, "Email invalid", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.length() < 8) {
            Toast.makeText(SignUpActivity.this, "The password must have 8 or more characters.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean isValidEmail(String email) {
        String[] allowedDomains = {"gmail.com", "yahoo.fr", "hotmail.com"};

        String[] parts = email.split("@");
        if (parts.length != 2) {
            return false;
        }

        String domain = parts[1];
        for (String allowedDomain : allowedDomains) {
            if (domain.equals(allowedDomain)) {
                return true;
            }
        }
        return false;
    }

    private void createUserWithEmailAndPassword(String username, String fullName, String email, String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Boolean check = dbHelper.checkInputs(username, fullName, email, password);
                if (check) {
                    Toast.makeText(SignUpActivity.this, "user already exists , please login", Toast.LENGTH_LONG).show();
                } else {
                    if (task.isSuccessful()) {
                        Boolean insert = dbHelper.insertData(username, fullName, email, password);
                        Toast.makeText(SignUpActivity.this, "SignUp Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                        overridePendingTransition(R.anim.transition, R.anim.transition2);
                    } else {
                        Toast.makeText(SignUpActivity.this, "SignUp Failed Please Check Your Inputs" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
