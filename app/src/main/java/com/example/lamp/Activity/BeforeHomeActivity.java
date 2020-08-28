package com.example.lamp.Activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lamp.Api.ApiInterface;
import com.example.lamp.Api.RetrofitClient;
import com.example.lamp.FullUserInfo.Address;
import com.example.lamp.FullUserInfo.UpdateUserInfo;
import com.example.lamp.Login.UserLogin;
import com.example.lamp.MainActivity;
import com.example.lamp.R;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BeforeHomeActivity extends AppCompatActivity {

    private Button btVerifyNext, btFillProfileNext;
    private ConstraintLayout profileLayout,verifyLayout;
    private String userType;
    private RadioGroup updateUserType;
    private static final String TAG = "BeforeHomeActivity";
    private EditText nameET, emailET, phoneET, cityET, zipET, locationET, countryET;
    private ImageView proImageView, nidImageView;
    private Uri profileImageUri, nidImageUri;
    Address address;
    UpdateUserInfo updateUserInfo;
    UserLogin userLoginResponse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_home);

        init();

        Intent intent = getIntent();
        userLoginResponse = (UserLogin) intent.getSerializableExtra("loginResponse");
        Log.d(TAG, "onCreate: " + userLoginResponse.getToken());
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

                String name = nameET.getText().toString().trim();
                String email = emailET.getText().toString().trim();
                String phone = phoneET.getText().toString().trim();
                String type = userType;
                String location = locationET.getText().toString().trim();
                String city = cityET.getText().toString().trim();
                String zip = zipET.getText().toString().trim();
                String country = countryET.getText().toString().trim();

                address = new Address(location, city, zip, country);

                String photo = profileImageUri + "." + "jpg";
                String nid = nidImageUri + "." + "jpg";
                String token = userLoginResponse.getToken();

                Retrofit retrofit = RetrofitClient.getRetrofitClient();

                ApiInterface api = retrofit.create(ApiInterface.class);

                Call<UpdateUserInfo> call = api.postByUpdateInfo(name,email,phone,type,photo,nid,token,address);

                call.enqueue(new Callback<UpdateUserInfo>() {
                    @Override
                    public void onResponse(Call<UpdateUserInfo> call, Response<UpdateUserInfo> response) {
                        Log.d(TAG, "onResponse: "+response.code());

                    if(response.code() == 200){

                        UpdateUserInfo updateUserInfo = response.body();
                        Toast.makeText(BeforeHomeActivity.this, "User Name: "+updateUserInfo.getName(), Toast.LENGTH_SHORT).show();
                        if(updateUserInfo.getType().equals("Farmer")){
                            Intent intent = new Intent(BeforeHomeActivity.this, FarmerActivity.class);
                            startActivity(intent);
                            finish();
                        }else if(updateUserInfo.getType().equals("WholeSeller")){
                            Intent intent = new Intent(BeforeHomeActivity.this, WholeSellerActivity.class);
                            startActivity(intent);
                            finish();
                        }else if(updateUserInfo.getType().equals("WholeSeller")){
                            Intent intent = new Intent(BeforeHomeActivity.this, AgentActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    }

                    @Override
                    public void onFailure(Call<UpdateUserInfo> call, Throwable t) {
                        Toast.makeText(BeforeHomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });
        proImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser1();
            }
        });
        nidImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser2();
            }
        });

    }

    private void openFileChooser1() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }
    private void openFileChooser2() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            profileImageUri = data.getData();
            Picasso.get().load(profileImageUri).into(proImageView);

        }else if(requestCode == 2 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null){
            nidImageUri = data.getData();
            Picasso.get().load(nidImageUri).into(nidImageView);
        }
    }

    public String getFileExtension(Uri ImageUri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(ImageUri));
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