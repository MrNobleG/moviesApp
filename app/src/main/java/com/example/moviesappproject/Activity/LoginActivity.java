package com.example.moviesappproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText userEmailEditText, passwordEditText;
    private TextView signUpTextView;
    private databaseHelper dbHelper;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        initializeViews();

        dbHelper = new databaseHelper(this);
        auth = FirebaseAuth.getInstance();

        setSignUpTextView();

        Button loginButton = findViewById(R.id.button2);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void initializeViews() {
        userEmailEditText = findViewById(R.id.editTextText);
        passwordEditText = findViewById(R.id.editTextPassword);
        signUpTextView = findViewById(R.id.signuptext);
    }

    private void setSignUpTextView() {
        SpannableString spannableString = new SpannableString(signUpTextView.getText());
        int startIndex = spannableString.toString().indexOf("SignUp");
        int endIndex = startIndex + "SignUp".length();

        spannableString.setSpan(new UnderlineSpan(), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                overridePendingTransition(R.anim.transition, R.anim.transition2);
            }
        };

        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.purple)), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        signUpTextView.setText(spannableString);
        signUpTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void loginUser() {
        String useremail = userEmailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (useremail.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "All the fields are required", Toast.LENGTH_SHORT).show();
        } else {
            auth.signInWithEmailAndPassword(useremail,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("useremail", useremail);
                    editor.apply();
                    Toast.makeText(LoginActivity.this,"Login Successfully",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("useremail",useremail);
                    startActivity(intent);
                    overridePendingTransition(R.anim.transition, R.anim.transition2);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginActivity.this,"Login Failed",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
