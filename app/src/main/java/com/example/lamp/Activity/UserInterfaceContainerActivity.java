package com.example.lamp.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.lamp.Api.ApiInterface;
import com.example.lamp.Api.RetrofitClient;
import com.example.lamp.Fragment.HomeFragment;
import com.example.lamp.Fragment.OrdersFragment;
import com.example.lamp.Fragment.ProfileFragment;
import com.example.lamp.Fragment.UploadProductsFragment;
import com.example.lamp.FullUserInfo.UpdateUserInfo;
import com.example.lamp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserInterfaceContainerActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavView;
    private FrameLayout frameLayout;
    private String retrievedToken;
    private SharedPreferences preferences;
    public static final String TAG = "UInterface";
    private String userType="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_interface_container);

        initView();
        preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        retrievedToken = preferences.getString("TOKEN", null);


        if (retrievedToken != null) {
                Retrofit retrofit = RetrofitClient.getRetrofitClient();
                ApiInterface api = retrofit.create(ApiInterface.class);

                Call<UpdateUserInfo> call = api.getByAuthQuery("Bearer " + retrievedToken);
                call.enqueue(new Callback<UpdateUserInfo>() {
                    @Override
                    public void onResponse(Call<UpdateUserInfo> call, Response<UpdateUserInfo> response) {
                        if (response.code() == 200) {
                            UpdateUserInfo updateUserInfo = response.body();
                            userType = updateUserInfo.getType();
                            if(userType.equals("whole seller")){
                                bottomNavView.getMenu().removeItem(R.id.uploadNv);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateUserInfo> call, Throwable t) {
                        Toast.makeText(UserInterfaceContainerActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: " + "message: " + t.getMessage());
                    }
                });
            }


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, new HomeFragment())
                .commit();

        initBottomNavigation();
    }

    private void initBottomNavigation() {
        bottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.homeNv:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.uploadNv:
                        selectedFragment = new UploadProductsFragment();
                        break;
                    case R.id.ordersNv:
                        selectedFragment = new OrdersFragment();
                        break;
                    case R.id.profileNv:
                        selectedFragment = new ProfileFragment();
                        break;

                    default:
                        break;
                }
                if (selectedFragment != null) {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.fragmentContainer, selectedFragment)
                            .commit();
                }
                return true;
            }
        });
    }

    private void initView() {

        bottomNavView = findViewById(R.id.bottomNavigationView);

        frameLayout = findViewById(R.id.fragmentContainer);
    }
}