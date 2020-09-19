package com.example.lamp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lamp.Adapter.AuctionListAdapter;
import com.example.lamp.Adapter.CityAdapter;
import com.example.lamp.Adapter.CommentsAdapter;
import com.example.lamp.Api.ApiInterface;
import com.example.lamp.Api.RetrofitClient;
import com.example.lamp.AuctionList.AuctionShowResponse;
import com.example.lamp.BidAuction.BidAuctionResponse;
import com.example.lamp.FullUserInfo.UpdateUserInfo;
import com.example.lamp.Model.City;
import com.example.lamp.OrderStore.OrderStore;
import com.example.lamp.ProductsInfo.Datum;
import com.example.lamp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BidProductActivity extends AppCompatActivity {
    private Datum productData;
    public static final String TAG = "Bid";
    private String retrievedToken, pStock, mBidPrice, pUnitPrice, unit, productId;
    private int actualStock, unitCharge, totalCharge;
    private double actualUnitPrice, minBidPrice,pTotalAmount;
    private String userId = "", userType = "", auctionId, totalAmount;
    private ImageView previewImage;
    private TextView minimumBidPrice, stock, unitPrice;
    private EditText message, yourBidPrice;
    private Spinner deliverySpinner1, deliverySpinner2;
    private Button submitbt;
    private String deliverCity1, deliveryCity2;
    private ArrayList<City> mOrderCityList = new ArrayList<>();
    private CityAdapter mCityAdapter;
    private SharedPreferences preferences;
    private RecyclerView auctionRecyclerView;
    private AuctionListAdapter auctionListAdapter;
    private ArrayList<com.example.lamp.AuctionList.Datum> mAuctionArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_product);
        inItCityList();
        initView();
        Intent intent = getIntent();
        productData = (Datum) intent.getSerializableExtra("productData");
        preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        retrievedToken = preferences.getString("TOKEN", null);

        loadAuthData();
        loadProductData();
        submitbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadBidAuction();
            }
        });
        loadAuctionList(productId);
    }

    private void loadBidAuction() {
        String bMessage = message.getText().toString().trim();
        final String bAmount = yourBidPrice.getText().toString().trim();
        double actualAmount = Integer.parseInt(bAmount);
        String fromCity = deliverCity1;
        String toCity = deliveryCity2;
        String status = "pending";
        if (retrievedToken != null) {
            Retrofit retrofit = RetrofitClient.getRetrofitClient();
            ApiInterface api = retrofit.create(ApiInterface.class);
            Call<BidAuctionResponse> call = api.postByBidAuctionStoreQuery("Bearer " + retrievedToken, productId, userId, productId, bMessage,
                    fromCity, toCity, actualStock, minBidPrice, unit, totalCharge, unitCharge, actualAmount, status);

            call.enqueue(new Callback<BidAuctionResponse>() {
                @Override
                public void onResponse(Call<BidAuctionResponse> call, Response<BidAuctionResponse> response) {
                    Log.d(TAG, "onResponse: " + response.code());
                    if (response.code() == 200) {
                        BidAuctionResponse bidAuctionResponse = response.body();
                        message.setText("");
                        yourBidPrice.setText("");
                        Toast.makeText(BidProductActivity.this, "Your Bid is successfully done", Toast.LENGTH_SHORT).show();
                        loadAuctionList(productId);
                    }

                }

                @Override
                public void onFailure(Call<BidAuctionResponse> call, Throwable t) {
                    Toast.makeText(BidProductActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: " + "message: " + t.getMessage());
                }
            });

        }
    }

    private void loadAuctionList(String id) {
        if (retrievedToken != null) {
            Retrofit retrofit = RetrofitClient.getRetrofitClient();
            ApiInterface api = retrofit.create(ApiInterface.class);
            Call<AuctionShowResponse> call = api.getByAuctionQuery("Bearer " + retrievedToken, id);

            call.enqueue(new Callback<AuctionShowResponse>() {
                @Override
                public void onResponse(Call<AuctionShowResponse> call, Response<AuctionShowResponse> response) {
                    Log.d(TAG, "onResponse: " + response.code());
                    if (response.code() == 200) {
                        AuctionShowResponse auctionShowResponse = response.body();

                        mAuctionArrayList.clear();
                        mAuctionArrayList.addAll(auctionShowResponse.getData());
                        auctionListAdapter = new AuctionListAdapter(BidProductActivity.this, mAuctionArrayList,userType);
                        auctionRecyclerView.setLayoutManager(new LinearLayoutManager(BidProductActivity.this));
                        auctionRecyclerView.setAdapter(auctionListAdapter);
                        auctionListAdapter.notifyDataSetChanged();

                    }
                }

                @Override
                public void onFailure(Call<AuctionShowResponse> call, Throwable t) {
                    Toast.makeText(BidProductActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: " + "message: " + t.getMessage());
                }
            });

        }
    }

    private void loadProductData() {

        Picasso.get().load(productData.getPhotos().getOne()).into(previewImage);

        minBidPrice = productData.getMinBidPrice();
        mBidPrice = String.valueOf(minBidPrice);
        actualStock = productData.getStock();
        actualUnitPrice = productData.getUnitPrice();
        pStock = String.valueOf(actualStock);
        pUnitPrice = String.valueOf(actualUnitPrice);
        unit = productData.getUnit();
        stock.setText("Stock: " + pStock + " " + unit);
        unitPrice.setText("Unit Price" + pUnitPrice + "৳/" + unit);
        productId = productData.getId();
        unitCharge = 10;
        totalCharge = (productData.getStock() * unitCharge);
        pTotalAmount = minBidPrice+totalCharge;
        minimumBidPrice.setText("Minimum Bid Price: " + pTotalAmount + "৳");
        totalAmount = String.valueOf(pTotalAmount);
        yourBidPrice.setText(totalAmount);


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
                    Toast.makeText(BidProductActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: " + "message: " + t.getMessage());
                }
            });
        }
    }
    private void inItCityList() {
        mOrderCityList = new ArrayList<>();
        mOrderCityList.add(new City("Select City."));
        mOrderCityList.add(new City("Barguna"));
        mOrderCityList.add(new City("Barisal"));
        mOrderCityList.add(new City("Dhaka"));
        mOrderCityList.add(new City("Gazipur"));
        mOrderCityList.add(new City("Jhalokati"));
        mOrderCityList.add(new City("Bhola"));
        mOrderCityList.add(new City("Patuakhali"));
        mOrderCityList.add(new City("Pirojpur"));
        mOrderCityList.add(new City("Bandarban"));
        mOrderCityList.add(new City("Brahmanbaria"));
        mOrderCityList.add(new City("Bogra"));
        mOrderCityList.add(new City("Chandpur"));
        mOrderCityList.add(new City("Chittagong"));
        mOrderCityList.add(new City("Cox's Bazar"));
        mOrderCityList.add(new City("Feni"));
        mOrderCityList.add(new City("Khagrachhari"));
        mOrderCityList.add(new City("Lakshmipur"));
        mOrderCityList.add(new City("Noakhali"));
        mOrderCityList.add(new City("Rangamati"));
        mOrderCityList.add(new City("Faridpur"));
        mOrderCityList.add(new City("Gazipur"));
        mOrderCityList.add(new City("Gopalganj"));
        mOrderCityList.add(new City("Kishoreganj"));
        mOrderCityList.add(new City("Madaripur"));
        mOrderCityList.add(new City("Manikganj"));
        mOrderCityList.add(new City("Munshiganj"));
        mOrderCityList.add(new City("Narayanganj"));
        mOrderCityList.add(new City("Narsingdi"));
        mOrderCityList.add(new City("Rajbari"));
        mOrderCityList.add(new City("Shariatpur"));
        mOrderCityList.add(new City("Tangail"));
        mOrderCityList.add(new City("Bagerhat"));
        mOrderCityList.add(new City("Chuadanga"));
        mOrderCityList.add(new City("Jessore"));
        mOrderCityList.add(new City("Jhenaidah"));
        mOrderCityList.add(new City("Khulna"));
        mOrderCityList.add(new City("Kushtia"));
        mOrderCityList.add(new City("Magura"));
        mOrderCityList.add(new City("Meherpur"));
        mOrderCityList.add(new City("Narail"));
        mOrderCityList.add(new City("Satkhira"));
        mOrderCityList.add(new City("Jamalpur"));
        mOrderCityList.add(new City("Mymensingh"));
        mOrderCityList.add(new City("Netrokona"));
        mOrderCityList.add(new City("Sherpur"));
        mOrderCityList.add(new City("Netrokona"));
        mOrderCityList.add(new City("Joypurhat"));
        mOrderCityList.add(new City("Naogaon"));
        mOrderCityList.add(new City("Natore"));
        mOrderCityList.add(new City("Chapainawabganj"));
        mOrderCityList.add(new City("Pabna"));
        mOrderCityList.add(new City("Rajshahi"));
        mOrderCityList.add(new City("Sirajganj"));
        mOrderCityList.add(new City("Dinajpur"));
        mOrderCityList.add(new City("Sirajganj"));
        mOrderCityList.add(new City("Gaibandha"));
        mOrderCityList.add(new City("Kurigram"));
        mOrderCityList.add(new City("Lalmonirhat"));
        mOrderCityList.add(new City("Nilphamari"));
        mOrderCityList.add(new City("Panchagarh"));
        mOrderCityList.add(new City("Rangpur"));
        mOrderCityList.add(new City("Thakurgaon"));
        mOrderCityList.add(new City("Habiganj"));
        mOrderCityList.add(new City("Moulvibazar"));
        mOrderCityList.add(new City("Sunamganj"));
        mOrderCityList.add(new City("Sylhet"));
    }
    private void initView() {
        previewImage = findViewById(R.id.previewImage);
        minimumBidPrice = findViewById(R.id.bidPriceDemo);
        message = findViewById(R.id.tvBidMessage);
        unitPrice = findViewById(R.id.unitPriceTv);
        stock = findViewById(R.id.stockTv);
        yourBidPrice = findViewById(R.id.bidAmountEt);
        submitbt = findViewById(R.id.submitBidBtn);
        deliverySpinner1 = findViewById(R.id.deliveryFromSpinner);
        deliverySpinner2 = findViewById(R.id.deliveryToSpinner);
        auctionRecyclerView = findViewById(R.id.recyclerViewForBidList);

        mCityAdapter = new CityAdapter(BidProductActivity.this, mOrderCityList);

        deliverySpinner1.setAdapter(mCityAdapter);

        deliverySpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                City clickedCity = (City) parent.getItemAtPosition(position);

                deliverCity1 = clickedCity.getCity();

                Toast.makeText(BidProductActivity.this, deliverCity1 + " is selected !", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mCityAdapter = new CityAdapter(BidProductActivity.this, mOrderCityList);
        deliverySpinner2.setAdapter(mCityAdapter);
        deliverySpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                City clickedCity = (City) parent.getItemAtPosition(position);

                deliveryCity2 = clickedCity.getCity();

                Toast.makeText(BidProductActivity.this, deliveryCity2 + " is selected !", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}