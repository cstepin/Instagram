package com.example.instaclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class StartActivity extends AppCompatActivity {

    ImageView ivLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        ivLogo = findViewById(R.id.ivLogo);
        Glide.with(this).load(R.drawable.icon).into(ivLogo);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent i = new Intent(StartActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }, 1000);   //5 seconds
    }
}