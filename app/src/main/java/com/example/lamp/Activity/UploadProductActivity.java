package com.example.lamp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.lamp.Fragment.LiveAuctionFragment;
import com.example.lamp.Fragment.LiveFixedFragment;
import com.example.lamp.Fragment.PrebookFutureFragment;
import com.example.lamp.R;

public class UploadProductActivity extends AppCompatActivity {

    private FrameLayout frameForUpload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_product);
        initView();

        Intent intent = getIntent();
        final String productType = intent.getStringExtra("productType");

        if(productType.equals("Live Product Fixed")){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, new LiveFixedFragment())
                    .commit();
        }
        else if(productType.equals("Live Product Auction")){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, new LiveAuctionFragment())
                    .commit();
        }
        else if(productType.equals("Prebook Future Product")){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, new PrebookFutureFragment())
                    .commit();
        }
    }

    private void initView() {

        frameForUpload = findViewById(R.id.fragmentContainer);
    }
}