package com.example.lamp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lamp.FullUserInfo.Address;
import com.example.lamp.FullUserInfo.UpdateUserInfo;
import com.example.lamp.R;

public class BeforeHomeActivity extends AppCompatActivity {

    private Button btVerifyNext, btFillProfileNext;
    private ConstraintLayout profileLayout,verifyLayout;
    private String userType;
    private RadioGroup updateUserType;
    private static final String TAG = "BeforeHomeActivity";
    private EditText nameET, emailET, phoneET, cityET, zipET, locationET, countryET;
    private ImageView proImageView, nidImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_home);

        init();

        Intent intent = getIntent();

        // userType = (String) intent.getSerializableExtra("userType");

        /*final Intent intent = getIntent();
        final String userType = intent.getStringExtra("userType");*/

        updateUserType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbType1 = (RadioButton) updateUserType.findViewById(updateUserType.getCheckedRadioButtonId());
                userType = (String) rbType1.getText();

                Toast.makeText(BeforeHomeActivity.this, userType+ " is Clicked!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onCreate: " +userType);

               /* btFillProfileNext.setOnClickListener(new View.OnClickListener() {
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
                });*/
            }
        });

      /*  btVerifyNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BeforeHomeActivity.this, "Number Verified", Toast.LENGTH_SHORT).show();
                profileLayout.setVisibility(View.VISIBLE);
                verifyLayout.setVisibility(View.GONE);

            }
        });*/

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

        Address address = new Address(locationET.getText().toString().trim(), cityET.getText().toString().trim(),
                zipET.getText().toString().trim(), countryET.getText().toString().trim());

        UpdateUserInfo updateUserInfo = new UpdateUserInfo(nameET.getText().toString(), emailET.getText().toString().trim(),
                phoneET.getText().toString().trim(), userType, address, "http://159.89.203.166/storage/users/xVnpaTZ23cxzSakYjxznhHKkaWIttPRwDY4tlSoh.png",
                "http://159.89.203.166/storage/users/nid/xqqE8QfOYhwUqnfenNnuSX9s8tx36hWbYiLiM7Ie.png");
    }

    private void init() {

        btVerifyNext = findViewById(R.id.verifyNext);
        btFillProfileNext = findViewById(R.id.nextBtn);
        profileLayout = findViewById(R.id.fillProfileLayout);
        verifyLayout = findViewById(R.id.verifypLayout);

        updateUserType = findViewById(R.id.typeOfUser);

        nameET = findViewById(R.id.tvName);
        emailET = findViewById(R.id.tvEmail);
        phoneET = findViewById(R.id.tvPhone);
        cityET = findViewById(R.id.tvCity);
        zipET = findViewById(R.id.tvZip);
        locationET = findViewById(R.id.tvLocation);
        countryET = findViewById(R.id.tvCountry);

        proImageView = findViewById(R.id.profileImage);
        nidImageView = findViewById(R.id.nidImage);
    }
}