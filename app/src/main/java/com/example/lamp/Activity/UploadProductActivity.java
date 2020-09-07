package com.example.lamp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.lamp.Fragment.LiveAuctionFragment;
import com.example.lamp.Fragment.LiveFixedFragment;
import com.example.lamp.Fragment.PrebookFutureFragment;
import com.example.lamp.R;

public class UploadProductActivity extends AppCompatActivity {

    private FrameLayout frameForUpload;
    private static final int PERMISSION_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_product);
        initView();
        requestPermission();
        Intent intent = getIntent();
        final String productType = intent.getStringExtra("productType");

        if(productType.equals("live_fixed")){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, new LiveFixedFragment())
                    .commit();
        }
        else if(productType.equals("live_auction")){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, new LiveAuctionFragment())
                    .commit();
        }
        else if(productType.equals("prebook_fixed")){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, new PrebookFutureFragment())
                    .commit();
        }
    }

    /////Start Storage Request Permission///////
    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(UploadProductActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(UploadProductActivity.this, "Write External Storage permission allows us to save files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(UploadProductActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(UploadProductActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

    /////End Storage Request Permission///////

    private void initView() {

        frameForUpload = findViewById(R.id.fragmentContainer);
    }
}