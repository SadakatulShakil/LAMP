package com.example.lamp.Activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.lamp.Fragment.HomeFragment;
import com.example.lamp.Fragment.OrdersFragment;
import com.example.lamp.Fragment.ProfileFragment;
import com.example.lamp.Fragment.UploadProductsFragment;
import com.example.lamp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserInterfaceContainerActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavView;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_interface_container);

        initView();

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