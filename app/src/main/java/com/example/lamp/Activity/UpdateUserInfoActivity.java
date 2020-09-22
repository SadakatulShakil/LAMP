package com.example.lamp.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.lamp.Api.ApiInterface;
import com.example.lamp.Api.RetrofitClient;
import com.example.lamp.FullUserInfo.Address;
import com.example.lamp.FullUserInfo.UpdateUserInfo;
import com.example.lamp.R;
import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UpdateUserInfoActivity extends AppCompatActivity {
    private Button btVerifyNext, btFillProfileNext;
    private ConstraintLayout profileLayout,verifyLayout;
    private String userType,nickProPath, nickNidPath,result="",retrievedToken,firstTime;
    private RadioGroup updateUserType;
    private RadioButton rb1, rb2, rb3;
    private static final String TAG = "UpdateActivity";
    private EditText nameET, emailET, phoneET, cityET, zipET, locationET, countryET;
    private ImageView proImageView, nidImageView;
    private Uri profileImageUri, nidImageUri;
    Address address;
    UpdateUserInfo updateUserInfo;
    Intent nidIntent, proIntent;
    private File profileFile, nidFile;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private SharedPreferences preferences;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);

        init();

        requestPermission();

        updateUserType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbType1 = (RadioButton) updateUserType.findViewById(updateUserType.getCheckedRadioButtonId());
                userType = (String) rbType1.getText();

                Toast.makeText(UpdateUserInfoActivity.this, userType+ " is Clicked!", Toast.LENGTH_SHORT).show();
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
                        String name  = updateUserInfo.getName();
                        nameET.setText(name);
                        String email = updateUserInfo.getEmail();
                        emailET.setText(email);
                        String phone = updateUserInfo.getPhone();
                        phoneET.setText(phone);
                        userType = updateUserInfo.getType();

                        if(userType.equals("farmer")){
                            rb1.setChecked(true);
                        }else if(userType.equals("whole seller")){
                            rb2.setChecked(true);
                        }else if(userType.equals("agent")){
                            rb3.setChecked(true);
                        }
                        Toast.makeText(UpdateUserInfoActivity.this, "user type: "+updateUserInfo.getType(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onResponse: " + updateUserInfo.getType());
                        String city = updateUserInfo.getAddress().getCity();
                        cityET.setText(city);
                        String zip = updateUserInfo.getAddress().getZip();
                        zipET.setText(zip);
                        String location = updateUserInfo.getAddress().getLocation();
                        locationET.setText(location);
                        String country = updateUserInfo.getAddress().getCountry();
                        countryET.setText(country);

                        /*Picasso.get().load(updateUserInfo.getPhoto()).into(proImageView);
                        Picasso.get().load(updateUserInfo.getNid()).into(nidImageView);*/

                    }

                }

                @Override
                public void onFailure(Call<UpdateUserInfo> call, Throwable t) {
                    Toast.makeText(UpdateUserInfoActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
                progressBar.setVisibility(View.VISIBLE);
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

                Call<UpdateUserInfo> call = api.postByUpdateInfo("Bearer "+retrievedToken, type, location,
                        city, zip, country, phone, email, requestBody, requestBody1, name);

                call.enqueue(new Callback<UpdateUserInfo>() {
                    @Override
                    public void onResponse(Call<UpdateUserInfo> call, Response<UpdateUserInfo> response) {
                        Log.d(TAG, "onResponse: "+response.code());

                        if(response.code() == 200){
                            progressBar.setVisibility(View.GONE);
                            UpdateUserInfo updateUserInfo = response.body();
                            Toast.makeText(UpdateUserInfoActivity.this, "User Information Updated successfully!", Toast.LENGTH_SHORT).show();

                            preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
                            preferences.edit().putBoolean("isFirstLog", true).apply();
                        }
                        else if(response.code() == 413){
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(UpdateUserInfoActivity.this, "Your Image Size too High!", Toast.LENGTH_LONG).show();
                        }
                        else{
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(UpdateUserInfoActivity.this, "Please choose profile and Nid image!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateUserInfo> call, Throwable t) {
                        Toast.makeText(UpdateUserInfoActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: "+"message: "+ t.getMessage());

                    }
                });

            }
        });
    }

    ///////////Functionally////////////

    /////Start Storage Request Permission///////
    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(UpdateUserInfoActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(UpdateUserInfoActivity.this, "Write External Storage permission allows us to save files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(UpdateUserInfoActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(UpdateUserInfoActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
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
        progressBar = findViewById(R.id.progressBar);
    }
}