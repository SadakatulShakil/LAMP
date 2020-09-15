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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lamp.Orders.Datum;
import com.example.lamp.Orders.Orders;
import com.example.lamp.Adapter.OrdersAdapter;
import com.example.lamp.Api.ApiInterface;
import com.example.lamp.Api.RetrofitClient;
import com.example.lamp.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OrdersFragment extends Fragment {
    private Context context;
    private RecyclerView orderRecyclerView;
    private OrdersAdapter mOrderAdapter;
    private ArrayList<Datum> datumArrayList = new ArrayList<>();
    private SharedPreferences preferences;
    private String retrievedToken;
    private ProgressBar progressBar;
    public static final String TAG = "Order";
    private Toolbar dToolbar;

    public OrdersFragment() {
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
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inItView(view);
        dToolbar.setTitle(getString(R.string.order));
        progressBar.setVisibility(View.VISIBLE);
        preferences = context.getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        retrievedToken = preferences.getString("TOKEN", null);

        getOrderList();
    }

    private void getOrderList() {
        if (retrievedToken != null) {
            Retrofit retrofit = RetrofitClient.getRetrofitClient();
            ApiInterface api = retrofit.create(ApiInterface.class);

            Call<Orders> call = api.getByOrdersQuery("Bearer " + retrievedToken);

            call.enqueue(new Callback<Orders>() {
                @Override
                public void onResponse(Call<Orders> call, Response<Orders> response) {
                    Log.d(TAG, "onResponse: " + response.code());
                    if(response.code() == 200){
                        progressBar.setVisibility(View.GONE);
                        Orders orderInfo = response.body();

                        datumArrayList.clear();

                        datumArrayList.addAll(orderInfo.getData());
                        mOrderAdapter = new OrdersAdapter(context, datumArrayList);
                        orderRecyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, true));
                        orderRecyclerView.setAdapter(mOrderAdapter);
                        mOrderAdapter.notifyDataSetChanged();
                        Log.d(TAG, "onResponse: order " + datumArrayList.size());
                    }
                }

                @Override
                public void onFailure(Call<Orders> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: " + "message: " + t.getMessage());
                }
            });

        }


    }
    private void inItView (View view){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dToolbar = view.findViewById(R.id.toolbar);
        }
        progressBar = view.findViewById(R.id.progressBar);
        orderRecyclerView = view.findViewById(R.id.recyclerViewForOrders);
    }
}