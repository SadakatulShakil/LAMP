package com.example.lamp.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.lamp.Adapter.CityAdapter;
import com.example.lamp.Api.ApiInterface;
import com.example.lamp.Api.RetrofitClient;
import com.example.lamp.FullUserInfo.UpdateUserInfo;
import com.example.lamp.Model.City;
import com.example.lamp.OrderStore.OrderStore;
import com.example.lamp.ProductsInfo.Datum;
import com.example.lamp.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OrderProductActivity extends AppCompatActivity {
    private Toolbar dToolbar;
    private ArrayList<City> mOrderCityList;
    private CityAdapter mCityAdapter;
    private Spinner deliveryCitySpinner1, deliveryCitySpinner2;
    private String deliverCity1, deliveryCity2;
    private SharedPreferences preferences;
    private String retrievedToken;
    public static final String TAG = "OrderNow";
    private Datum productData;
    private EditText name, email, phone, location, city, zip, country, quantity;
    private Button orderPlace;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_product);
        inItCityList();
        inItView();
        Intent intent = getIntent();
        productData = (Datum) intent.getSerializableExtra("productData");
        dToolbar.setTitle(getString(R.string.order));
        preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        retrievedToken = preferences.getString("TOKEN", null);
        getAuthData();

        orderPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                storeOrder();
            }
        });
    }



    private void getAuthData() {
        if (retrievedToken != null) {
            Retrofit retrofit = RetrofitClient.getRetrofitClient();
            ApiInterface api = retrofit.create(ApiInterface.class);

            Call<UpdateUserInfo> call = api.getByAuthQuery("Bearer " + retrievedToken);
            call.enqueue(new Callback<UpdateUserInfo>() {
                @Override
                public void onResponse(Call<UpdateUserInfo> call, Response<UpdateUserInfo> response) {
                    if (response.code() == 200) {
                        UpdateUserInfo updateUserInfo = response.body();

                        name.setText(updateUserInfo.getName());
                        email.setText(updateUserInfo.getEmail());
                        phone.setText(updateUserInfo.getPhone());
                        location.setText(updateUserInfo.getAddress().getLocation());
                        city.setText(updateUserInfo.getAddress().getCity());
                        zip.setText(updateUserInfo.getAddress().getZip());
                        country.setText(updateUserInfo.getAddress().getCountry());
                    }
                }

                @Override
                public void onFailure(Call<UpdateUserInfo> call, Throwable t) {
                    Toast.makeText(OrderProductActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: " + "message: " + t.getMessage());
                }
            });
        }
    }

    private void storeOrder() {
        String oName =  name.getText().toString().trim();
        String oEmail = email.getText().toString().trim();
        String oPhone = phone.getText().toString().trim();
        String oLocation = location.getText().toString().trim();
        String oCity = city.getText().toString().trim();
        String oZip = zip.getText().toString().trim();
        String oCountry = country.getText().toString().trim();
        String oDeliveryFromCity = deliverCity1;
        String oDeliverToCity = deliveryCity2;
        String oProductId = productData.getId();
        int oQuantity = Integer.parseInt(quantity.getText().toString().trim());

        if (retrievedToken != null) {
            Retrofit retrofit = RetrofitClient.getRetrofitClient();
            ApiInterface api = retrofit.create(ApiInterface.class);

            Call<OrderStore> call = api.postByOrderStoreQuery("Bearer " + retrievedToken, oProductId,
                    oName, oEmail, oPhone, oLocation, oCountry, oCity, oZip, oQuantity, oDeliveryFromCity, oDeliverToCity);

            call.enqueue(new Callback<OrderStore>() {
                @Override
                public void onResponse(Call<OrderStore> call, Response<OrderStore> response) {
                    Log.d(TAG, "onResponse: " +response.code());
                    if(response.code() == 200){
                        progressBar.setVisibility(View.GONE);
                        OrderStore orders = response.body();
                        Toast.makeText(OrderProductActivity.this, "Successfully Updated your Order", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(OrderProductActivity.this, UserInterfaceContainerActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    else{
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(OrderProductActivity.this, "Check your Input is Correct", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<OrderStore> call, Throwable t) {
                    Toast.makeText(OrderProductActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void inItView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dToolbar = findViewById(R.id.toolbar);
        }
        name = findViewById(R.id.wholeSellerNameEt);
        email = findViewById(R.id.wholeSellerEmailEt);
        phone = findViewById(R.id.wholeSellerPhoneEt);
        location = findViewById(R.id.wholeSellerLocationEt);
        city = findViewById(R.id.wholeSellerCityEt);
        zip = findViewById(R.id.wholeSellerZipEt);
        country = findViewById(R.id.wholeSellerCountryEt);
        quantity = findViewById(R.id.orderQuantityEt);
        deliveryCitySpinner1 = findViewById(R.id.deliveryFromSpinner);
        deliveryCitySpinner2 = findViewById(R.id.deliveryToSpinner);
        orderPlace = findViewById(R.id.placeOrderBtn);
        progressBar = findViewById(R.id.progressBar);

        mCityAdapter = new CityAdapter(OrderProductActivity.this, mOrderCityList);

        deliveryCitySpinner1.setAdapter(mCityAdapter);

        deliveryCitySpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                City clickedCity = (City) parent.getItemAtPosition(position);

                deliverCity1 = clickedCity.getCity();

                Toast.makeText(OrderProductActivity.this, deliverCity1 +" is selected !", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mCityAdapter = new CityAdapter(OrderProductActivity.this, mOrderCityList);
        deliveryCitySpinner2.setAdapter(mCityAdapter);
        deliveryCitySpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                City clickedCity = (City) parent.getItemAtPosition(position);

                deliveryCity2 = clickedCity.getCity();

                Toast.makeText(OrderProductActivity.this, deliveryCity2 +" is selected !", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}