package com.example.lamp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lamp.Adapter.AuctionListAdapter;
import com.example.lamp.Api.ApiInterface;
import com.example.lamp.Api.RetrofitClient;
import com.example.lamp.AuctionList.AuctionShowResponse;
import com.example.lamp.AuctionList.Datum;
import com.example.lamp.FullUserInfo.UpdateUserInfo;
import com.example.lamp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AuctionActionActivity extends AppCompatActivity {
    private com.example.lamp.ProductsInfo.Datum productData;
    private ImageView previewImage;
    private TextView unitPrice, stock,minimumBidPrice;
    private RecyclerView auctionRecyclerView;
    private AuctionListAdapter auctionListAdapter;
    private ArrayList<Datum> mAuctionArrayList = new ArrayList<>();
    private int actualStock;
    private double actualMinPrice, actualUnitPrice;
    private String retrievedToken, pStock, mBidPrice, pUnitPrice;
    private String userId = "", userType = "", auctionId, totalAmount;
    private SharedPreferences preferences;
    public static final String TAG = "AuctionAction";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction_action);

        inItView();
        Intent intent = getIntent();
        productData = (com.example.lamp.ProductsInfo.Datum) intent.getSerializableExtra("productData");
        preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        retrievedToken = preferences.getString("TOKEN", null);
        loadAuthData();
        loadProductData();
        loadAuctionList(productData.getId());
    }

    private void loadProductData() {
        Picasso.get().load(productData.getPhotos().getOne()).into(previewImage);
        actualStock = productData.getStock();
        actualUnitPrice = productData.getUnitPrice();
        actualMinPrice = productData.getMinBidPrice();

        pStock = String.valueOf(actualStock);
        pUnitPrice = String.valueOf(actualUnitPrice);
        mBidPrice = String.valueOf(actualMinPrice);
        String unit = productData.getUnit();

        stock.setText("Product Quantity: "+pStock + unit);
        unitPrice.setText("Unit Price: "+pUnitPrice+"৳/"+unit);
        minimumBidPrice.setText("Minimum Bid Price: "+mBidPrice+"৳");
    }

    private void loadAuctionList(String productId) {
        if (retrievedToken != null) {
            Retrofit retrofit = RetrofitClient.getRetrofitClient();
            ApiInterface api = retrofit.create(ApiInterface.class);
            Call<AuctionShowResponse> call = api.getByAuctionQuery("Bearer " + retrievedToken, productId);

            call.enqueue(new Callback<AuctionShowResponse>() {
                @Override
                public void onResponse(Call<AuctionShowResponse> call, Response<AuctionShowResponse> response) {
                    Log.d(TAG, "onResponse: " + response.code());
                    if (response.code() == 200) {
                        AuctionShowResponse auctionShowResponse = response.body();

                        mAuctionArrayList.clear();
                        mAuctionArrayList.addAll(auctionShowResponse.getData());
                        auctionListAdapter = new AuctionListAdapter(AuctionActionActivity.this, mAuctionArrayList,userType);
                        auctionRecyclerView.setLayoutManager(new LinearLayoutManager(AuctionActionActivity.this));
                        auctionRecyclerView.setAdapter(auctionListAdapter);
                        auctionListAdapter.notifyDataSetChanged();

                    }
                }

                @Override
                public void onFailure(Call<AuctionShowResponse> call, Throwable t) {
                    Toast.makeText(AuctionActionActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: " + "message: " + t.getMessage());
                }
            });

        }
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
                        userId = updateUserInfo.getId();
                        userType = updateUserInfo.getType();


                    }
                }

                @Override
                public void onFailure(Call<UpdateUserInfo> call, Throwable t) {
                    Toast.makeText(AuctionActionActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: " + "message: " + t.getMessage());
                }
            });
        }
    }

    private void inItView() {
        previewImage = findViewById(R.id.previewImage);
        minimumBidPrice = findViewById(R.id.bidPriceDemo);
        unitPrice = findViewById(R.id.unitPriceTv);
        stock = findViewById(R.id.stockTv);
        auctionRecyclerView = findViewById(R.id.recyclerViewForBidList);
    }
}