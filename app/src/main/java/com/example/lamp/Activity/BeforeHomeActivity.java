package com.example.lamp.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import com.example.lamp.Api.ApiInterface;
import com.example.lamp.Api.RetrofitClient;
import com.example.lamp.FullUserInfo.Address;
import com.example.lamp.FullUserInfo.UpdateUserInfo;
import com.example.lamp.Login.UserLogin;
import com.example.lamp.MainActivity;
import com.example.lamp.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BeforeHomeActivity extends AppCompatActivity {

    private Button btVerifyNext, btFillProfileNext;
    private ConstraintLayout profileLayout,verifyLayout;
    private String userType,nickProPath, nickNidPath,result="",retrievedToken,firstTime;
    private RadioGroup updateUserType;
    private RadioButton rb1, rb2, rb3;
    private static final String TAG = "BeforeHomeActivity";
    private EditText nameET, emailET, phoneET, cityET, zipET, locationET, countryET;
    private ImageView proImageView, nidImageView;
    private Uri profileImageUri, nidImageUri;
    Address address;
    UpdateUserInfo updateUserInfo;
    Intent nidIntent, proIntent;
    private File profileFile, nidFile;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_home);

        init();

        requestPermission();

        updateUserType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbType1 = (RadioButton) updateUserType.findViewById(updateUserType.getCheckedRadioButtonId());
                userType = (String) rbType1.getText();

                Toast.makeText(BeforeHomeActivity.this, userType+ " is Clicked!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onCreate: " +userType);

            }
        });

        //get Some info of User//

        preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        retrievedToken  = preferences.getString("TOKEN",null);


        if(retrievedToken != null){
            Retrofit retrofit = RetrofitClient.getRetrofitClient();
            ApiInterface api = retrofit.create(ApiInterface.class);

            Call<UpdateUserInfo> call = api.getByAuthQuery("Bearer "+retrievedToken);

            call.enqueue(new Callback<UpdateUserInfo>() {
                @Override
                public void onResponse(Call<UpdateUserInfo> call, Response<UpdateUserInfo> response) {
                    if(response.code() == 200){
                        updateUserInfo = response.body();
                        nameET.setText(updateUserInfo.getName());
                        emailET.setText(updateUserInfo.getEmail());
                        phoneET.setText(updateUserInfo.getPhone());

                        if(updateUserInfo.getType().equals("farmer")){
                            rb1.setChecked(true);
                        }else if(updateUserInfo.getType().equals("wholeseller")){
                            rb2.setChecked(true);
                        }else if(updateUserInfo.getType().equals("agent")){
                            rb3.setChecked(true);
                        }

                    }

                }

                @Override
                public void onFailure(Call<UpdateUserInfo> call, Throwable t) {
                    Toast.makeText(BeforeHomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: "+"message: "+ t.getMessage());
                }
            });
        }


        proImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseProfileImage();
            }
        });

        nidImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseNidImage();
            }
        });

                btFillProfileNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String name = nameET.getText().toString().trim();
                        String email = emailET.getText().toString().trim();
                        String phone = phoneET.getText().toString().trim();
                        String type = userType;
                        String location = locationET.getText().toString().trim();
                        String city = cityET.getText().toString().trim();
                        String zip = zipET.getText().toString().trim();
                        String country = countryET.getText().toString().trim();

                        address = new Address(location, city, zip, country);

                        profileFile = new File(getRealPathFromURI(profileImageUri));
                        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), profileFile);
                        Log.d(TAG, "onClick: "+requestBody.toString());

                        nidFile = new File(getRealPathFromURI(nidImageUri));
                        RequestBody requestBody1 = RequestBody.create(MediaType.parse("application/octet-stream"), nidFile);

                        Retrofit retrofit = RetrofitClient.getRetrofitClient();

                        ApiInterface api = retrofit.create(ApiInterface.class);

                        Call<UpdateUserInfo> call = api.postByUpdateInfo("Bearer "+retrievedToken, type, location, city, zip, country, phone, email, requestBody, requestBody1, name);

                        call.enqueue(new Callback<UpdateUserInfo>() {
                            @Override
                            public void onResponse(Call<UpdateUserInfo> call, Response<UpdateUserInfo> response) {
                                Log.d(TAG, "onResponse: "+response.code());

                                if(response.code() == 200){

                                    UpdateUserInfo updateUserInfo = response.body();
                                    Toast.makeText(BeforeHomeActivity.this, "User Name: "+updateUserInfo.getType(), Toast.LENGTH_SHORT).show();
                                    if(updateUserInfo.getType().equals("farmer")){
                                        Intent intent = new Intent(BeforeHomeActivity.this, FarmerActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else if(updateUserInfo.getType().equals("wholeseller")){
                                        Intent intent = new Intent(BeforeHomeActivity.this, WholeSellerActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else if(updateUserInfo.getType().equals("agent")){
                                        Intent intent = new Intent(BeforeHomeActivity.this, AgentActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
                                    preferences.edit().putBoolean("isFirstLog", true).apply();
                                }
                            }

                            @Override
                            public void onFailure(Call<UpdateUserInfo> call, Throwable t) {
                                Toast.makeText(BeforeHomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onFailure: "+"message: "+ t.getMessage());

                            }
                        });

                    }
                });
            }

   /* private void firstLoginCheck(UpdateUserInfo updateUserInfo) {
            SharedPreferences preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
            firstTime = preferences.getString("firstTime", "");

            if(firstTime.equals("Yes")){
                if(updateUserInfo.getType().equals("farmer")){

                    Intent intent1 = new Intent(BeforeHomeActivity.this, FarmerActivity.class);
                    startActivity(intent1);
                    finish();
                }else if(updateUserInfo.getType().equals("wholeseller")){

                    Intent intent1 = new Intent(BeforeHomeActivity.this, WholeSellerActivity.class);
                    startActivity(intent1);
                    finish();
                }else if(updateUserInfo.getType().equals("Agent")){

                    Intent intent1 = new Intent(BeforeHomeActivity.this, AgentActivity.class);
                    startActivity(intent1);
                    finish();
                }
            }else{
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("firstTime","Yes");
                editor.apply();
            }

    }*/


    ///////////Functionally////////////

    /////Start Storage Request Permission///////
    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(BeforeHomeActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(BeforeHomeActivity.this, "Write External Storage permission allows us to save files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(BeforeHomeActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(BeforeHomeActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
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

    private void ChooseProfileImage() {
        proIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(proIntent,1);
    }

    private void ChooseNidImage() {
        nidIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(nidIntent,2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, "onActivityResult: "+"Activity result success");
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){

            case 1:
                if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                    profileImageUri = data.getData();
                    //getRealPathFromURI(profileImageUri);
                    Log.d(TAG, "onActivityResult: "+profileImageUri);
                    Picasso.get().load(profileImageUri).into(proImageView);

                }
                break;

            case 2:
                if(resultCode == Activity.RESULT_OK && data != null && data.getData() != null){
                    nidImageUri = data.getData();
                    //getRealPathFromURI(nidImageUri);
                    Picasso.get().load(nidImageUri).into(nidImageView);
                }
                break;
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private void init() {

        btVerifyNext = findViewById(R.id.verifyNext);
        btFillProfileNext = findViewById(R.id.nextBtn);
        profileLayout = findViewById(R.id.fillProfileLayout);
        verifyLayout = findViewById(R.id.verifypLayout);
        rb1 = findViewById(R.id.radioButton1);
        rb2 = findViewById(R.id.radioButton2);
        rb3 = findViewById(R.id.radioButton3);

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