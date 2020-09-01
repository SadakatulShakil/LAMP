package com.example.lamp.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BeforeHomeActivity extends AppCompatActivity {

    private Button btVerifyNext, btFillProfileNext;
    private ConstraintLayout profileLayout,verifyLayout;
    private String userType,nickProPath, nickNidPath,result="";
    private RadioGroup updateUserType;
    private static final String TAG = "BeforeHomeActivity";
    private EditText nameET, emailET, phoneET, cityET, zipET, locationET, countryET;
    private ImageView proImageView, nidImageView;
    private Uri profileImageUri, nidImageUri;
    Address address;
    UpdateUserInfo updateUserInfo;
    UserLogin userLoginResponse;
    Intent nidIntent, proIntent;
    private File profileFile, nidFile;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_home);

        init();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            finish();
            startActivity(intent);
            return;
        }

        Intent intent = getIntent();
        userLoginResponse = (UserLogin) intent.getSerializableExtra("loginResponse");
        Log.d(TAG, "onCreate: " + userLoginResponse.getToken());

        updateUserType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbType1 = (RadioButton) updateUserType.findViewById(updateUserType.getCheckedRadioButtonId());
                userType = (String) rbType1.getText();

                Toast.makeText(BeforeHomeActivity.this, userType+ " is Clicked!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onCreate: " +userType);

            }
        });

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

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
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

                        profileFile = new File(result);
                        RequestBody requestBody = RequestBody.create(MediaType.parse(getContentResolver().getType(profileImageUri)), profileFile);
                        Log.d(TAG, "onClick: "+profileFile);

                        nidFile = new File(result);
                        RequestBody requestBody1 = RequestBody.create(MediaType.parse(getContentResolver().getType(nidImageUri)), nidFile);


                        String token = userLoginResponse.getToken();

                        Retrofit retrofit = RetrofitClient.getRetrofitClient();

                        ApiInterface api = retrofit.create(ApiInterface.class);

                        Call<UpdateUserInfo> call = api.postByUpdateInfo("Bearer "+token, type,location,city,zip,country, phone, email, requestBody, requestBody1, name);

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
                                    }else if(updateUserInfo.getType().equals("Agent")){
                                        Intent intent = new Intent(BeforeHomeActivity.this, AgentActivity.class);
                                        startActivity(intent);
                                        finish();
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
                });
                }
            } else {
                requestPermission(); // Code for permission
            }

    }

    ///////////Functionally////////////


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(BeforeHomeActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(BeforeHomeActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(BeforeHomeActivity.this, "Write External Storage permission allows us to save files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(BeforeHomeActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
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


    private void ChooseProfileImage() {
        proIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(proIntent,1);
    }

    private void ChooseNidImage() {
        nidIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(nidIntent,2);
    }

    public String getMimeType(Uri uri) {
        String extension;

        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

        }

        return extension;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, "onActivityResult: "+"Activity result success");
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){

            case 1:
                if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                    profileImageUri = data.getData();
                    getRealPathFromURI(profileImageUri);
                   /* String path1 = profileImageUri.getPath()+"."+getMimeType(profileImageUri);
                    String profilePath = Environment.getExternalStorageDirectory().toString() + path1;
                    Log.d(TAG, "onActivityResult: " + "profile: "+ profilePath);
                    profileFile = new File(profilePath);
                    nickProPath = profileFile.getName();
                    Log.d(TAG, "onActivityResult: "+nickProPath);*/
                    Picasso.get().load(profileImageUri).into(proImageView);

                }
                break;

            case 2:
                if(resultCode == Activity.RESULT_OK && data != null && data.getData() != null){
                    nidImageUri = data.getData();
                    /*String path2 = nidImageUri.getPath()+"."+getMimeType(nidImageUri);
                    String nidPath = Environment.getExternalStorageDirectory().toString() + path2;
                    Log.d(TAG, "onActivityResult: " + "profile: "+ nidPath);
                    nidFile = new File(nidPath);
                    nickNidPath = nidFile.getName();
                    Log.d(TAG, "onActivityResult: "+nickNidPath);*/
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