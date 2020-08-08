package com.example.lamp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.lamp.R;

public class SignUpActivity extends AppCompatActivity {
    private Toolbar dToolbar;
    private Button signUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initViews();
        Intent intent = getIntent();
        final String userType = intent.getStringExtra("userType");

        dToolbar.setTitle(getString(R.string.signUp));
        dToolbar.setNavigationIcon(R.drawable.ic_arrow);

        dToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, BeforeHomeActivity.class);
                intent.putExtra("userType", userType);
                startActivity(intent);
                finish();
                Toast.makeText(SignUpActivity.this, "New " +userType + " is SignedUp!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initViews() {
        signUp = findViewById(R.id.signUpBTN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dToolbar = findViewById(R.id.toolbar);
        }
    }

}