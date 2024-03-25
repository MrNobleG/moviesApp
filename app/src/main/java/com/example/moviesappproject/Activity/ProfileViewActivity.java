package com.example.moviesappproject.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.moviesappproject.R;

import org.junit.Test;
import static org.junit.Assert.*;
public class ProfileViewActivity extends AppCompatActivity {
    private TextView userNameTextView, fullNameTextView, emailTextView;
    private ImageView backButton;
    private databaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        userNameTextView = findViewById(R.id.usern);
        fullNameTextView = findViewById(R.id.fn);
        emailTextView = findViewById(R.id.email);
        backButton = findViewById(R.id.backImg);

        dbHelper = new databaseHelper(this);

        String userEmail = getIntent().getStringExtra("email");
        emailTextView.setText(userEmail);
        User user = dbHelper.getUserByEmail(userEmail);
        if (user != null) {
            userNameTextView.setText(user.username);
            fullNameTextView.setText(user.fullname);

        }else{
            Toast.makeText(ProfileViewActivity.this,"User not found ",Toast.LENGTH_LONG).show();
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMainActivity();
            }
        });


        TextView logoutTextView = findViewById(R.id.logoutTextView);
        logoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutConfirmationDialog();
            }
        });
    }


    private void navigateToMainActivity() {
        Intent intent = new Intent(ProfileViewActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.transition, R.anim.transition2);
    }

    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Log Out")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void logout() {
        Toast.makeText(ProfileViewActivity.this, "You Logged out", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(ProfileViewActivity.this, LoginActivity.class));
        overridePendingTransition(R.anim.transition, R.anim.transition2);
    }

    }


