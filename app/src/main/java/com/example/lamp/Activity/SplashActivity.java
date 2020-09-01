package com.example.lamp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lamp.Api.ApiInterface;
import com.example.lamp.Api.RetrofitClient;
import com.example.lamp.FullUserInfo.UpdateUserInfo;
import com.example.lamp.Login.UserLogin;
import com.example.lamp.MainActivity;
import com.example.lamp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 4000;

    private ImageView logoImg, textImag;
    private TextView demo;
    private Animation topAnim, bottomAnim;
    private UpdateUserInfo updateUserInfo;

    public static final String TAG ="splash";

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

                SharedPreferences preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
                String retrievedToken  = preferences.getString("TOKEN",null);
                String retrieveType = preferences.getString("type", null);

                Log.d(TAG, "onCreate: "+"token: "+retrievedToken + "\ntype: "+retrieveType);

                if(retrievedToken != null
                        && retrieveType.equals("farmer")){
                    Retrofit retrofit = RetrofitClient.getRetrofitClient();
                    ApiInterface api = retrofit.create(ApiInterface.class);

                    Call<UpdateUserInfo> call = api.getByAuthQuery("Bearer "+retrievedToken);
                    call.enqueue(new Callback<UpdateUserInfo>() {
                        @Override
                        public void onResponse(Call<UpdateUserInfo> call, Response<UpdateUserInfo> response) {
                            Log.d(TAG, "onResponse: "+"response code: " +response.code());
                            if(response.code() == 200){
                                updateUserInfo = response.body();
                                Intent intent = new Intent(SplashActivity.this, FarmerActivity.class);
                                intent.putExtra("userData", updateUserInfo);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<UpdateUserInfo> call, Throwable t) {
                            Toast.makeText(SplashActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

                }else if(retrievedToken != null
                        && retrieveType.equals("WholeSeller")){
                    Intent intent = new Intent(SplashActivity.this, WholeSellerActivity.class);
                    intent.putExtra("userData", updateUserInfo);
                    startActivity(intent);
                    finish();
                }else if(retrievedToken != null
                        && retrieveType.equals("Agent")){
                    Intent intent = new Intent(SplashActivity.this, AgentActivity.class);
                    intent.putExtra("userData", updateUserInfo);
                    startActivity(intent);
                    finish();
                }else if(retrievedToken == null){
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
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