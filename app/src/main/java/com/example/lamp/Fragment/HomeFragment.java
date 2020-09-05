package com.example.lamp.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private RecyclerView live_fixed_Rv, live_auction_Rv, preBooked_Rv;
    private productsAdapter productsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String retrievedToken;
    SharedPreferences preferences;
    private ArrayList<Datum> datumArrayList;
    public static final String TAG ="Home";
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

        inItView(view);
        datumArrayList = new ArrayList<>();
        productsAdapter = new productsAdapter(context, datumArrayList);
        live_fixed_Rv.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,true));
        live_fixed_Rv.setAdapter(productsAdapter);

        preferences = context.getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        retrievedToken  = preferences.getString("TOKEN",null);

        getAuthUserData();
        getProductsData();
    }

    /////Functionality///////
    private void getAuthUserData() {

        if(retrievedToken!= null){
            Retrofit retrofit = RetrofitClient.getRetrofitClient();
            ApiInterface api = retrofit.create(ApiInterface.class);

            Call<UpdateUserInfo> call = api.getByAuthQuery("Bearer "+retrievedToken);
            call.enqueue(new Callback<UpdateUserInfo>() {
                @Override
                public void onResponse(Call<UpdateUserInfo> call, Response<UpdateUserInfo> response) {
                    if(response.code() == 200){
                        UpdateUserInfo updateUserInfo = response.body();


                    }
                }

                @Override
                public void onFailure(Call<UpdateUserInfo> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: "+"message: "+ t.getMessage());
                }
            });
        }
    }

    private void getProductsData() {
        Retrofit retrofit = RetrofitClient.getRetrofitClient();
        ApiInterface api = retrofit.create(ApiInterface.class);

        Call<ProductsInfo>call = api.getByProductsQuery("Bearer "+retrievedToken);

        call.enqueue(new Callback<ProductsInfo>() {
            @Override
            public void onResponse(Call<ProductsInfo> call, Response<ProductsInfo> response) {
                if(response.code() == 200){
                    ProductsInfo productsData = response.body();
                    datumArrayList.addAll(productsData.getData());
                    Log.d(TAG, "onResponse: " + productsData.getData().size());

                    /*Log.d(TAG, "onResponse: "+ datumArrayList.get(0).getData().size());*/
                }
                    productsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ProductsInfo> call, Throwable t) {

            }
        });

    }

    private void inItView(View view) {

        live_fixed_Rv = view.findViewById(R.id.recyclerViewForLiveFixed);
        live_auction_Rv = view.findViewById(R.id.recyclerViewForAuction);
        preBooked_Rv = view.findViewById(R.id.recyclerViewForPreBooked);
    }
}