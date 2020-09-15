package com.example.lamp.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lamp.Api.ApiInterface;
import com.example.lamp.Api.RetrofitClient;
import com.example.lamp.FullUserInfo.UpdateUserInfo;
import com.example.lamp.Orders.Datum;
import com.example.lamp.ProductsInfo.ProductsInfo;
import com.example.lamp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OrderDetailsActivity extends AppCompatActivity {
    private TextView productTitle, productUnitPrice, productUnitCharge, productQuantity, productTotalPrice, deliveryRoot;
    private ImageView productThumbImage;
    private Button paymentConfirmBtn, orderCancelBtn;
    private SharedPreferences preferences;
    private String retrievedToken;
    private Datum orderData;
    public static final String TAG = "orderDetails";
    private ArrayList<com.example.lamp.ProductsInfo.Datum> datumArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        inItView();
        Intent intent = getIntent();
        orderData = (Datum) intent.getSerializableExtra("orderInfo");
        preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        retrievedToken = preferences.getString("TOKEN", null);
        getProductsData();
        String quantity = String.valueOf(orderData.getQuantity());
        String unitPrice = String.valueOf(orderData.getUnitPrice());
        String unit = orderData.getUnit();
        String unitCharge = String.valueOf(orderData.getUnitCharge());
        String totalPrice = String.valueOf(orderData.getTotalPrice());
        String from = orderData.getFrom();
        String to = orderData.getTo();
        productQuantity.setText(quantity+" "+unit);
        productUnitPrice.setText(unitPrice+"৳/"+unit);
        productUnitCharge.setText(unitCharge+"৳/"+unit);
        productTotalPrice.setText(totalPrice+"৳");
        deliveryRoot.setText(from+" to "+to);
    }

    private void getAuthUserData() {

        if (retrievedToken != null) {
            Retrofit retrofit = RetrofitClient.getRetrofitClient();
            ApiInterface api = retrofit.create(ApiInterface.class);

            Call<UpdateUserInfo> call = api.getByAuthQuery("Bearer " + retrievedToken);
            call.enqueue(new Callback<UpdateUserInfo>() {
                @Override
                public void onResponse(Call<UpdateUserInfo> call, Response<UpdateUserInfo> response) {
                    if (response.code() == 200) {
                        UpdateUserInfo updateUserInfo = response.body();

                    }
                }

                @Override
                public void onFailure(Call<UpdateUserInfo> call, Throwable t) {
                    Toast.makeText(OrderDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: " + "message: " + t.getMessage());
                }
            });
        }
    }

    private void getProductsData() {
        Log.d(TAG, "getProductsData: started");
        Retrofit retrofit = RetrofitClient.getRetrofitClient();
        ApiInterface api = retrofit.create(ApiInterface.class);

        Call<ProductsInfo> call = api.getByProductsQuery("Bearer " + retrievedToken);

        call.enqueue(new Callback<ProductsInfo>() {
            @Override
            public void onResponse(Call<ProductsInfo> call, Response<ProductsInfo> response) {
                Log.d(TAG, "onResponse: "+ response.code());
                if (response.code() == 200) {
                    ProductsInfo productsData = response.body();
                    datumArrayList.addAll(productsData.getData());

                    for(com.example.lamp.ProductsInfo.Datum datumProduct : datumArrayList){
                        if(datumProduct.getId().equals(orderData.getProductId())){
                            productTitle.setText(datumProduct.getTitle());
                            Picasso.get().load(datumProduct.getPhotos().getOne()).into(productThumbImage);
                        }
                    }

                }

            }

            @Override
            public void onFailure(Call<ProductsInfo> call, Throwable t) {
                Toast.makeText(OrderDetailsActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: "+t.getLocalizedMessage());
            }
        });

    }
    private void inItView() {
        productTitle = findViewById(R.id.productTitle);
        productUnitPrice = findViewById(R.id.unitPriceTv);
        productUnitCharge = findViewById(R.id.unitChargeTv);
        productQuantity = findViewById(R.id.quantityTv);
        productTotalPrice = findViewById(R.id.totalPriceTv);
        deliveryRoot = findViewById(R.id.deliveryRootTv);
        productThumbImage = findViewById(R.id.image1);
        paymentConfirmBtn = findViewById(R.id.paymentCallBTN);
        orderCancelBtn = findViewById(R.id.cancelCallBTN);
    }
}