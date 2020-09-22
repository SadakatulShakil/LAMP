package com.example.lamp.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.PluralsRes;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lamp.Activity.SignUpActivity;
import com.example.lamp.Adapter.productsAdapter;
import com.example.lamp.Api.ApiInterface;
import com.example.lamp.Api.RetrofitClient;
import com.example.lamp.FullUserInfo.UpdateUserInfo;
import com.example.lamp.ProductsInfo.Datum;
import com.example.lamp.ProductsInfo.ProductsInfo;
import com.example.lamp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeFragment extends Fragment {

    private Context context;
    private Toolbar dToolbar;
    private RecyclerView productRecyclerView, live_fixed_Rv, live_auction_Rv, preBooked_Rv;
    private productsAdapter productsAdapter;
    private ScrollView scViewOfProduct;
    private ProgressBar progressBar;
    private String retrievedToken;
    SharedPreferences preferences;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Datum> datumArrayList = new ArrayList<>();

    public static final String TAG = "Home";

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: " + "On create ready");
        inItView(view);
        dToolbar.setTitle(getString(R.string.home));

        progressBar.setVisibility(View.VISIBLE);

        preferences = context.getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        retrievedToken = preferences.getString("TOKEN", null);


        getAuthUserData();
        getProductsData();

    }


    /////Functionality///////
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
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
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
                    progressBar.setVisibility(View.GONE);
                    ProductsInfo productsData = response.body();

                    datumArrayList.clear();
                    /*live_fixedArrayList.clear();
                    prebook_fixedArrayList.clear();
                    live_auctionArrayList.clear();*/

                    datumArrayList.addAll(productsData.getData());
                    Log.d(TAG, "onResponse: " + productsData.getData().size());

                    productsAdapter = new productsAdapter(context, datumArrayList);
                    layoutManager = new GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false);
                    productRecyclerView.setLayoutManager(layoutManager);
                    productRecyclerView.setAdapter(productsAdapter);
                    /*for (Datum datum : datumArrayList) {/////for each loop is " object of ArrayList : that ArrayList/////

                        if (datum.getType().equals("live_fixed")) {
                            live_fixedArrayList.add(datum);
                            ////get live_fixed/////////
                            productsAdapter = new productsAdapter(context, live_fixedArrayList);
                            live_fixed_Rv.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, true));
                            live_fixed_Rv.setAdapter(productsAdapter);
                            productsAdapter.notifyDataSetChanged();
                            Log.d(TAG, "onResponse: Fixed" + live_fixedArrayList.size());
                        } else if (datum.getType().equals("live_auction")) {
                            live_auctionArrayList.add(datum);
                            ////get prebook_fixed//////
                            productsAdapter = new productsAdapter(context, prebook_fixedArrayList);
                            preBooked_Rv.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, true));
                            preBooked_Rv.setAdapter(productsAdapter);
                            productsAdapter.notifyDataSetChanged();
                            Log.d(TAG, "onResponse: Auction" + live_auctionArrayList.size());
                        } else if (datum.getType().equals("prebook_fixed")) {
                            prebook_fixedArrayList.add(datum);
                            ///////get live_auction////
                            productsAdapter = new productsAdapter(context, live_auctionArrayList);
                            live_auction_Rv.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, true));
                            live_auction_Rv.setAdapter(productsAdapter);
                            productsAdapter.notifyDataSetChanged();
                            Log.d(TAG, "onResponse: Prebook" + prebook_fixedArrayList.size());
                        }
                    }*/


                }
                else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(context, "Please Check your Internet connection!", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ProductsInfo> call, Throwable t) {
                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: "+t.getLocalizedMessage());
            }
        });

    }

    private void inItView(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dToolbar = view.findViewById(R.id.toolbar);
        }
        productRecyclerView = view.findViewById(R.id.recyclerViewForProducts);
        /*live_auction_Rv = view.findViewById(R.id.recyclerViewForAuction);
        preBooked_Rv = view.findViewById(R.id.recyclerViewForPreBooked);*/

        scViewOfProduct = view.findViewById(R.id.productView);
        progressBar = view.findViewById(R.id.progressBar);
    }
}