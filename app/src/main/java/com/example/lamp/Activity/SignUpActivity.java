package com.example.lamp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.lamp.Api.ApiInterface;
import com.example.lamp.Api.RetrofitClient;
import com.example.lamp.MainActivity;
import com.example.lamp.R;
import com.example.lamp.Registration.UserRegistration;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignUpActivity extends AppCompatActivity {
    private Toolbar dToolbar;
    private Button signUp;
    private EditText nameTV, phoneTV, emailTV, passwordTV, confirmPasswordTV;
    private RadioGroup updateUserType;
    private String userType;
    public static final String TAG = "SignUp";
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initViews();

        dToolbar.setTitle(getString(R.string.signUp));
        dToolbar.setNavigationIcon(R.drawable.ic_arrow);

        dToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        updateUserType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbType1 = (RadioButton) updateUserType.findViewById(updateUserType.getCheckedRadioButtonId());
                userType = (String) rbType1.getText();

                Toast.makeText(SignUpActivity.this, userType+ " is Clicked!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onCreate: " +userType);
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String name = nameTV.getText().toString().trim();
                String phone = phoneTV.getText().toString().trim();
                String email = emailTV.getText().toString().trim();
                String type = userType.trim();
                String password = passwordTV.getText().toString().trim();
                String confirm_pass = confirmPasswordTV.getText().toString().trim();
                if(password.equals(confirm_pass)){


                    Retrofit retrofit = RetrofitClient.getRetrofitClient();

                    ApiInterface api = retrofit.create(ApiInterface.class);

                    Call<UserRegistration>call =api.postByRgQuery(name, phone, email, type, password, confirm_pass);

                    call.enqueue(new Callback<UserRegistration>() {
                        @Override
                        public void onResponse(Call<UserRegistration> call, Response<UserRegistration> response) {
                            Log.d(TAG, "onResponse: " + response.code());
                            if(response.code() == 201){
                                progressBar.setVisibility(View.GONE);
                                UserRegistration userRegistration = response.body();
                                Toast.makeText(SignUpActivity.this, "data inserted , Name is !" + userRegistration.getMessage().getName(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        }

                        @Override
                        public void onFailure(Call<UserRegistration> call, Throwable t) {
                            Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();                        }
                    });

                }
            }
        });
    }
    private void initViews() {
        signUp = findViewById(R.id.signUpBTN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dToolbar = findViewById(R.id.toolbar);
        }

        nameTV = findViewById(R.id.tvName);
        phoneTV = findViewById(R.id.tvPhone);
        emailTV = findViewById(R.id.tvEmail);
        ///////type

        updateUserType = findViewById(R.id.typeOfUser);

        passwordTV = findViewById(R.id.tvPassword);
        confirmPasswordTV = findViewById(R.id.tvRetypePassword);
        progressBar = findViewById(R.id.progressBar);
    }

}