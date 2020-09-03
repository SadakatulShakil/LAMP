package com.example.lamp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.lamp.Activity.BeforeHomeActivity;
import com.example.lamp.Activity.FarmerActivity;
import com.example.lamp.Activity.SignUpActivity;
import com.example.lamp.Api.ApiInterface;
import com.example.lamp.Api.RetrofitClient;
import com.example.lamp.Login.UserLogin;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private Toolbar dToolbar;
    private Button signUpBt, signinBt;
    private EditText emailET, passwordET;
    private String device_name = "mobile";
    public static final String TAG ="signIn";
    private ProgressBar progressBar;
    private  SharedPreferences preferences;
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

        signinBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email = emailET.getText().toString().trim();
                final String password = passwordET.getText().toString().trim();

                Retrofit retrofit = RetrofitClient.getRetrofitClient();
                ApiInterface api = retrofit.create(ApiInterface.class);

                Call<UserLogin> call = api.postByLoginQuery(email, password, device_name);

                call.enqueue(new Callback<UserLogin>() {
                    @Override
                    public void onResponse(Call<UserLogin> call, Response<UserLogin> response) {
                        Log.d(TAG, "onResponse: " + response.code());
                        if(response.code() == 200){
                            progressBar.setVisibility(View.GONE);
                            UserLogin userLogin = response.body();
                            SharedPreferences preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
                            preferences.edit().putString("TOKEN",userLogin.getToken()).apply();
                            preferences.edit().putString("type", userLogin.getUser().getType()).apply();

                            Log.d(TAG, "onResponse: "+userLogin.getToken());
                            preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
                            boolean isFirstLog = preferences.getBoolean("isFirstLog", false);
                            if(isFirstLog && userLogin.getUser().getType().equals("farmer")){
                                Toast.makeText(MainActivity.this, "Name is !" + userLogin.getUser().getName(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, FarmerActivity.class);
                                //intent.putExtra("loginResponse", userLogin);
                                startActivity(intent);
                                finish();
                            }else{

                                Toast.makeText(MainActivity.this, "Name is !" + userLogin.getUser().getName(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, BeforeHomeActivity.class);
                                //intent.putExtra("loginResponse", userLogin);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserLogin> call, Throwable t) {
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }


    private void initViews() {
        signUpBt = findViewById(R.id.signUpBTN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dToolbar = findViewById(R.id.toolbar);
        }

        emailET = findViewById(R.id.userEmail);
        passwordET = findViewById(R.id.userPassword);
        signinBt = findViewById(R.id.signInBTN);
        progressBar = findViewById(R.id.progressBar);
    }
}