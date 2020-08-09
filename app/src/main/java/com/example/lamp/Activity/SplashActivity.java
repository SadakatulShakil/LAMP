package com.example.lamp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lamp.MainActivity;
import com.example.lamp.R;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 4000;

    private ImageView logoImg, textImag;
    private TextView demo;
    private Animation topAnim, bottomAnim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        initView();
        //Assign animation
        AssignAnim();
        //splash coding
        RunSplash();
    }

    private void RunSplash() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN);
    }

    private void AssignAnim() {
        logoImg.setAnimation(topAnim);
        textImag.setAnimation(bottomAnim);
        demo.setAnimation(bottomAnim);
    }

    private void initView() {
        logoImg = findViewById(R.id.logo);
        textImag = findViewById(R.id.textSplash);
        demo = findViewById(R.id.demoText);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
    }
}