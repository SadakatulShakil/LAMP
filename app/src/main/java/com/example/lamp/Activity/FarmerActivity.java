package com.example.lamp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lamp.Adapter.productTypeAdapter;
import com.example.lamp.Api.ApiInterface;
import com.example.lamp.Api.RetrofitClient;
import com.example.lamp.FullUserInfo.UpdateUserInfo;
import com.example.lamp.MainActivity;
import com.example.lamp.Model.productType;
import com.example.lamp.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FarmerActivity extends AppCompatActivity {
    private Toolbar dToolbar;
    private ArrayList<productType> mProductTypeList;
    private productTypeAdapter  mAdapter;
    private Spinner mSpinner;
    private Button btnUpload;
    private TextView userName, logOut;
    private String clickedProductTypeName, retrievedToken;
    private UpdateUserInfo updateUserInfo;
    public static final String TAG = "Farmer";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer);


        initList();
        initView();
        Intent intent = getIntent();
        updateUserInfo = (UpdateUserInfo) intent.getSerializableExtra("userData");
        SharedPreferences preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        retrievedToken  = preferences.getString("TOKEN",null);
        String name = updateUserInfo.getName();
        userName.setText("Hello ! "+name);

        String token = updateUserInfo.getToken();
        dToolbar.setTitle(getString(R.string.farmer));

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickedProductTypeName.equals("Select Product Type...")){
                    Toast.makeText(FarmerActivity.this, "please select your right product type", Toast.LENGTH_SHORT).show();
                }
                else{

                    Intent intent = new Intent(FarmerActivity.this, UploadProductActivity.class);
                    intent.putExtra("productType", clickedProductTypeName);
                    startActivity(intent);
                }
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: "+ retrievedToken);
                Retrofit retrofit = RetrofitClient.getRetrofitClient();

                ApiInterface api = retrofit.create(ApiInterface.class);

                Call<UpdateUserInfo> call = api.postByLogOutQuery("Bearer "+ retrievedToken);

                call.enqueue(new Callback<UpdateUserInfo>() {
                    @Override
                    public void onResponse(Call<UpdateUserInfo> call, Response<UpdateUserInfo> response) {
                        Log.d(TAG, "onResponse: " +"responseCode: "+ response.code());
                        if(response.code() == 200){
                            UpdateUserInfo updateUserInfo = response.body();

                            Log.d(TAG, "onResponse: "+ response.body());
                            Toast.makeText(FarmerActivity.this, "Response" +updateUserInfo.getToken(), Toast.LENGTH_SHORT).show();
                            Intent intent =  new Intent(FarmerActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateUserInfo> call, Throwable t) {
                        Toast.makeText(FarmerActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: "+"message: "+ t.getMessage());

                    }
                });

            }
        });
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dToolbar = findViewById(R.id.toolbar);
        }
        btnUpload = findViewById(R.id.uploadBt);
        mSpinner = findViewById(R.id.spinner);
        userName = findViewById(R.id.welcomeName);
        logOut = findViewById(R.id.logOutBt);
        mAdapter = new productTypeAdapter(this, mProductTypeList);

        mSpinner.setAdapter(mAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                productType clickedType = (productType) parent.getItemAtPosition(position);

                clickedProductTypeName = clickedType.getProductType();

                Toast.makeText(FarmerActivity.this, clickedProductTypeName +" is selected !", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initList() {
        mProductTypeList = new ArrayList<>();
        mProductTypeList.add(new productType("Select Product Type..."));
        mProductTypeList.add(new productType("Live Product Fixed"));
        mProductTypeList.add(new productType("Live Product Auction"));
        mProductTypeList.add(new productType("Prebook Future Product"));

    }
}