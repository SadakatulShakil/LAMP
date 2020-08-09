package com.example.lamp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.lamp.Activity.SignUpActivity;

public class MainActivity extends AppCompatActivity {
    private Toolbar dToolbar;
    private Button signUpBt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        dToolbar.setTitle(getString(R.string.login));
        dToolbar.setNavigationIcon(R.drawable.ic_arrow);

        signUpBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        dToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    private void initViews() {
        signUpBt = findViewById(R.id.signUpBTN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dToolbar = findViewById(R.id.toolbar);
        }
    }
}