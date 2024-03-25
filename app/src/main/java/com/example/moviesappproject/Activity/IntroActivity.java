package com.example.moviesappproject.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.moviesappproject.R;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.transition3, R.anim.transition4);
    }
}