package com.example.lamp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lamp.Fragment.HomeFragment;
import com.example.lamp.Fragment.ProductsDetailsFragment;
import com.example.lamp.ProductsInfo.Datum;
import com.example.lamp.R;
import com.squareup.picasso.Picasso;

public class ProductsDetailsActivity extends AppCompatActivity {

    private ImageView previewImage, thumbnail1, thumbnail2, thumbnail3;
    private TextView update, delete, title, description, price, stock, startDate, endDate;
    private Toolbar dToolbar;
    private Datum productData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_details);

        inItView();
        dToolbar.setTitle(getString(R.string.details));

        Intent intent = getIntent();
        productData = (Datum) intent.getSerializableExtra("productData");

        loadProductData();
        ClickEvents();

    }

    private void ClickEvents() {
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        stock.setText("Stock Available: "+pStock);
        startDate.setText("Start At: "+pStartDate);
        endDate.setText("Expire At: "+pEndDate);
        price.setText(pPrice+"৳/"+unit);

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

        update = findViewById(R.id.updateTv);
        delete = findViewById(R.id.deleteTv);
        title = findViewById(R.id.titleTv);
        description = findViewById(R.id.productDescriptionTv);
        stock = findViewById(R.id.stockTv);
        startDate = findViewById(R.id.startDateTv);
        endDate = findViewById(R.id.endDateTv);
        price = findViewById(R.id.priceTv);
    }
}