package com.example.lamp.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lamp.Adapter.AuctionListAdapter;
import com.example.lamp.Adapter.CommentsAdapter;
import com.example.lamp.Adapter.OrdersAdapter;
import com.example.lamp.Api.ApiInterface;
import com.example.lamp.Api.RetrofitClient;
import com.example.lamp.AuctionList.AuctionShowResponse;
import com.example.lamp.Comments.CommentsResponse;
import com.example.lamp.FullUserInfo.UpdateUserInfo;
import com.example.lamp.ProductsInfo.Datum;
import com.example.lamp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProductsDetailsActivity extends AppCompatActivity {

    private ImageView previewImage, thumbnail1, thumbnail2, thumbnail3;
    private TextView delete, title, description, price, stock, startDate, endDate,notify_count;
    private Toolbar dToolbar;
    private String userType = "",productType="";
    private Datum productData;
    private SharedPreferences preferences;
    private String retrievedToken;
    public static final String TAG = "Details";
    private FloatingActionButton update;
    private EditText commentsField;
    private Button sendCommentBtn;
    private RecyclerView commentsRecyclerView;
    private CommentsAdapter mCommentsAdapter;
    private ArrayList<com.example.lamp.Comments.Datum> commentsArrayList;
    private RelativeLayout notificationActionBt;
    private int notificationCount;
    private ArrayList<com.example.lamp.AuctionList.Datum> mAuctionArrayList = new ArrayList<>();

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
        loadAuctionList(productData.getId());
        ClickEvents();

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
                        notificationCount = mAuctionArrayList.size();
                        String countValue = String.valueOf(notificationCount);
                        notify_count.setText(countValue);
                        Log.d(TAG, "onResponse: "+countValue);

                    }
                }

                @Override
                public void onFailure(Call<AuctionShowResponse> call, Throwable t) {
                    Toast.makeText(ProductsDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
                        userType = updateUserInfo.getType();
                        productType = productData.getType();
                        if(userType.equals("farmer") && productType.equals("live_auction")){
                            notificationActionBt.setVisibility(View.VISIBLE);
                            notificationActionBt.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ProductsDetailsActivity.this, AuctionActionActivity.class);
                                    intent.putExtra("productData", productData);
                                    startActivity(intent);
                                }
                            });
                        }
                        else if(userType.equals("whole seller") && productType.equals("live_auction")){
                            delete.setText("Bid Now!");
                            delete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ProductsDetailsActivity.this, BidProductActivity.class);
                                    intent.putExtra("productData", productData);
                                    startActivity(intent);
                                }
                            });
                            update.setImageDrawable(getResources().getDrawable(R.drawable.ic_message));

                            update.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    commentsField.setVisibility(View.VISIBLE);
                                    sendCommentBtn.setVisibility(View.VISIBLE);

                                    sendCommentBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ///////////////////Do comment section task here /////////////////////
                                            if (retrievedToken != null) {
                                                Retrofit retrofit = RetrofitClient.getRetrofitClient();
                                                ApiInterface api = retrofit.create(ApiInterface.class);

                                                Call<com.example.lamp.Comments.Datum> call = api.postByCommentStoreQuery("Bearer " + retrievedToken,
                                                        productData.getId(),commentsField.getText().toString().trim());
                                                call.enqueue(new Callback<com.example.lamp.Comments.Datum>() {
                                                    @Override
                                                    public void onResponse(Call<com.example.lamp.Comments.Datum> call, Response<com.example.lamp.Comments.Datum> response) {
                                                        if(response.code() == 200){
                                                            commentsField.setText("");
                                                            Toast.makeText(ProductsDetailsActivity.this, "Your comment Added Successfully !", Toast.LENGTH_SHORT).show();
                                                            loadCommentsData( productData.getId());
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<com.example.lamp.Comments.Datum> call, Throwable t) {
                                                        Log.d(TAG, "onFailure: "+ t.getMessage());
                                                        Toast.makeText(ProductsDetailsActivity.this, "Failure: "+ t.getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            });
                        }

                        else if(userType.equals("whole seller")){
                            delete.setText("Order now!");
                            delete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ProductsDetailsActivity.this, OrderProductActivity.class);
                                    intent.putExtra("productData", productData);
                                    startActivity(intent);
                                }
                            });

                            update.setImageDrawable(getResources().getDrawable(R.drawable.ic_message));

                            update.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    commentsField.setVisibility(View.VISIBLE);
                                    sendCommentBtn.setVisibility(View.VISIBLE);

                                    sendCommentBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ///////////////////Do comment section task here /////////////////////
                                            if (retrievedToken != null) {
                                                Retrofit retrofit = RetrofitClient.getRetrofitClient();
                                                ApiInterface api = retrofit.create(ApiInterface.class);

                                                Call<com.example.lamp.Comments.Datum> call = api.postByCommentStoreQuery("Bearer " + retrievedToken,
                                                        productData.getId(),commentsField.getText().toString().trim());
                                                call.enqueue(new Callback<com.example.lamp.Comments.Datum>() {
                                                    @Override
                                                    public void onResponse(Call<com.example.lamp.Comments.Datum> call, Response<com.example.lamp.Comments.Datum> response) {
                                                        if(response.code() == 200){
                                                            commentsField.setText("");
                                                            Toast.makeText(ProductsDetailsActivity.this, "Your comment Added Successfully !", Toast.LENGTH_SHORT).show();
                                                            loadCommentsData( productData.getId());
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<com.example.lamp.Comments.Datum> call, Throwable t) {
                                                        Log.d(TAG, "onFailure: "+ t.getMessage());
                                                        Toast.makeText(ProductsDetailsActivity.this, "Failure: "+ t.getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            });
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

        loadCommentsData(productData.getId());

    }

    private void loadCommentsData(String id) {

        if (retrievedToken != null) {
            Retrofit retrofit = RetrofitClient.getRetrofitClient();
            ApiInterface api = retrofit.create(ApiInterface.class);

            Call<CommentsResponse> call = api.getByCommentQuery("Bearer " + retrievedToken, id);

            call.enqueue(new Callback<CommentsResponse>() {
                @Override
                public void onResponse(Call<CommentsResponse> call, Response<CommentsResponse> response) {

                    if (response.code() == 200) {
                        CommentsResponse commentsResponse = response.body();

                        commentsArrayList.clear();

                        commentsArrayList.addAll(commentsResponse.getData());
                        mCommentsAdapter = new CommentsAdapter(ProductsDetailsActivity.this, commentsArrayList);
                        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(ProductsDetailsActivity.this));
                        commentsRecyclerView.setAdapter(mCommentsAdapter);
                        mCommentsAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<CommentsResponse> call, Throwable t) {

                }
            });

        }
    }

    private void inItView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dToolbar = findViewById(R.id.toolbar);
        }
        commentsArrayList = new ArrayList<>();
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
        commentsField = findViewById(R.id.commentsEt);
        sendCommentBtn = findViewById(R.id.sendCommentBtn);
        commentsRecyclerView = findViewById(R.id.recyclerViewForCommentList);
        notificationActionBt = findViewById(R.id.auctionNotificationAction);
        notify_count = findViewById(R.id.notificationCountTv);
    }
}