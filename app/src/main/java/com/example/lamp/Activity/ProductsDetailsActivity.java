package com.example.lamp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lamp.Api.ApiInterface;
import com.example.lamp.Api.RetrofitClient;
import com.example.lamp.Fragment.HomeFragment;
import com.example.lamp.Fragment.ProductsDetailsFragment;
import com.example.lamp.FullUserInfo.UpdateUserInfo;
import com.example.lamp.MainActivity;
import com.example.lamp.ProductsInfo.Datum;
import com.example.lamp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProductsDetailsActivity extends AppCompatActivity {

    private ImageView previewImage, thumbnail1, thumbnail2, thumbnail3;
    private TextView delete, title, description, price, stock, startDate, endDate;
    private Toolbar dToolbar;
    private String userType = "";
    private Datum productData;
    private SharedPreferences preferences;
    private String retrievedToken;
    public static final String TAG = "Details";
    private FloatingActionButton update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_details);

        inItView();
        /*dToolbar.setTitle(getString(R.string.details));*/

        Intent intent = getIntent();
        productData = (Datum) intent.getSerializableExtra("productData");
        preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        retrievedToken = preferences.getString("TOKEN", null);
        loadAuthData();
        loadProductData();
        ClickEvents();

    }

    private void loadAuthData() {
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
                            delete.setText("Order now!");
                        }
                    }
                }

                @Override
                public void onFailure(Call<UpdateUserInfo> call, Throwable t) {
                    Toast.makeText(ProductsDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: " + "message: " + t.getMessage());
                }
            });
        }
    }

    private void ClickEvents() {
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (retrievedToken != null) {
                    Retrofit retrofit = RetrofitClient.getRetrofitClient();
                    ApiInterface api = retrofit.create(ApiInterface.class);

                    Call<String> call = api.postByProductDeleteQuery("Bearer "+ retrievedToken, productData.getId());

                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Log.d(TAG, "onResponse: Response Code: "+response.code());
                            if(response.code() == 200){
                                Intent intent1 = new Intent(ProductsDetailsActivity.this, UserInterfaceContainerActivity.class);
                                startActivity(intent1);
                                finish();
                                Toast.makeText(ProductsDetailsActivity.this, "response: "+ response.body(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.d(TAG, "onFailure: "+ t.getMessage());
                            Toast.makeText(ProductsDetailsActivity.this, "Failure: "+ t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductsDetailsActivity.this, ProductUpdateActivity.class);
                intent.putExtra("productData", productData);
                startActivity(intent);

            }
        });

    }

    private void loadProductData() {

        String pTitle = productData.getTitle();
        String pDescription = productData.getDescription();
        String pStock = String.valueOf(productData.getStock());
        String pStartDate = productData.getStartedAt();
        String pEndDate = productData.getExpiredAt();
        String pPrice = String.valueOf(productData.getUnitPrice());
        String unit = productData.getUnit();

        title.setText("Title: "+pTitle);
        description.setText(pDescription);
        stock.setText("Stock Available: "+pStock+" "+unit);
        startDate.setText("Start At: "+pStartDate);
        endDate.setText("Expire At: "+pEndDate);
        price.setText("Price: "+pPrice+"৳/"+unit);

        Picasso.get().load(productData.getPhotos().getOne()).into(previewImage);
        Picasso.get().load(productData.getPhotos().getOne()).into(thumbnail1);
        Picasso.get().load(productData.getPhotos().getTwo()).into(thumbnail2);
        Picasso.get().load(productData.getPhotos().getThree()).into(thumbnail3);

        thumbnail1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.get().load(productData.getPhotos().getOne()).into(previewImage);
            }
        });

        thumbnail2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.get().load(productData.getPhotos().getTwo()).into(previewImage);
            }
        });

        thumbnail3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.get().load(productData.getPhotos().getThree()).into(previewImage);
            }
        });

    }

    private void inItView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dToolbar = findViewById(R.id.toolbar);
        }

        previewImage = findViewById(R.id.previewImage);
        thumbnail1 = findViewById(R.id.thumbnail1);
        thumbnail2 = findViewById(R.id.thumbnail2);
        thumbnail3 = findViewById(R.id.thumbnail3);

        update = findViewById(R.id.updateFAB);
        delete = findViewById(R.id.demoForDetails);
        title = findViewById(R.id.titleTv);
        description = findViewById(R.id.productDescriptionTv);
        stock = findViewById(R.id.stockTv);
        startDate = findViewById(R.id.startDateTv);
        endDate = findViewById(R.id.endDateTv);
        price = findViewById(R.id.priceTv);
    }
}