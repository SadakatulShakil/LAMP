package com.example.lamp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lamp.R;

public class BeforeHomeActivity extends AppCompatActivity {

    private Button btVerifyNext, btFillProfileNext;
    private ConstraintLayout profileLayout,verifyLayout;
    private String userType;
    private static final String TAG = "BeforeHomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_home);

        init();

        Intent intent = getIntent();

         userType = (String) intent.getSerializableExtra("userType");

        /*final Intent intent = getIntent();
        final String userType = intent.getStringExtra("userType");*/

   /*     updateUserType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbType1 = (RadioButton) updateUserType.findViewById(updateUserType.getCheckedRadioButtonId());
                userType = (String) rbType1.getText();

                Toast.makeText(BeforeHomeActivity.this, userType+ " is Clicked!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onCreate: " +userType);

                btFillProfileNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(userType.equals("Farmer")){
                            Intent intent = new Intent(BeforeHomeActivity.this, FarmerActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else if(userType.equals("WholeSeller")){
                            Intent intent = new Intent(BeforeHomeActivity.this, WholeSellerActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else if(userType.equals("Agent")){
                            Intent intent = new Intent(BeforeHomeActivity.this, AgentActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });*/

        btVerifyNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BeforeHomeActivity.this, "Number Verified", Toast.LENGTH_SHORT).show();
                profileLayout.setVisibility(View.VISIBLE);
                verifyLayout.setVisibility(View.GONE);

            }
        });

        btFillProfileNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userType.equals("Farmer")) {
                    Intent intent = new Intent(BeforeHomeActivity.this, FarmerActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(userType.equals("WholeSeller")){
                    Intent intent = new Intent(BeforeHomeActivity.this, WholeSellerActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(userType.equals("Agent")){
                    Intent intent = new Intent(BeforeHomeActivity.this, AgentActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }

    private void init() {

        btVerifyNext = findViewById(R.id.verifyNext);
        btFillProfileNext = findViewById(R.id.nextBtn);
        profileLayout = findViewById(R.id.fillProfileLayout);
        verifyLayout = findViewById(R.id.verifypLayout);
    }
}