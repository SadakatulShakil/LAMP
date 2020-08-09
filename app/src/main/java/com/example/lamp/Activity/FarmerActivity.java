package com.example.lamp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lamp.Adapter.productTypeAdapter;
import com.example.lamp.Model.productType;
import com.example.lamp.R;

import java.util.ArrayList;

public class FarmerActivity extends AppCompatActivity {
    private Toolbar dToolbar;
    private ArrayList<productType> mProductTypeList;
    private productTypeAdapter  mAdapter;
    private Spinner mSpinner;
    private Button btnUpload;
    private String clickedProductTypeName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer);


        initList();
        initView();
        dToolbar.setTitle(getString(R.string.farmer));

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickedProductTypeName.equals("Select Product Type...")){
                    Toast.makeText(FarmerActivity.this, "please select your right product type", Toast.LENGTH_SHORT).show();
                }
                else{

                    Intent intent = new Intent(FarmerActivity.this, UploadProductActivity.class);
                    intent.putExtra("productType", clickedProductTypeName);
                    startActivity(intent);
                }
            }
        });
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dToolbar = findViewById(R.id.toolbar);
        }
        btnUpload = findViewById(R.id.uploadBt);
        mSpinner = findViewById(R.id.spinner);
        mAdapter = new productTypeAdapter(this, mProductTypeList);

        mSpinner.setAdapter(mAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                productType clickedType = (productType) parent.getItemAtPosition(position);

                clickedProductTypeName = clickedType.getProductType();

                Toast.makeText(FarmerActivity.this, clickedProductTypeName +" is selected !", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initList() {
        mProductTypeList = new ArrayList<>();
        mProductTypeList.add(new productType("Select Product Type..."));
        mProductTypeList.add(new productType("Live Product Fixed"));
        mProductTypeList.add(new productType("Live Product Auction"));
        mProductTypeList.add(new productType("Prebook Future Product"));
    }
}