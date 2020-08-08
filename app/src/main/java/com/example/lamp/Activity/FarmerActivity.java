package com.example.lamp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lamp.Adapter.productTypeAdapter;
import com.example.lamp.Model.productType;
import com.example.lamp.R;

import java.util.ArrayList;

public class FarmerActivity extends AppCompatActivity {

    private ArrayList<productType> mProductTypeList;
    private productTypeAdapter  mAdapter;
    private Spinner mSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer);

        initList();
        initView();
    }

    private void initView() {
        mSpinner = findViewById(R.id.spinner);
        mAdapter = new productTypeAdapter(this, mProductTypeList);

        mSpinner.setAdapter(mAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                productType clickedType = (productType) parent.getItemAtPosition(position);

                String clickedProductTypeName = clickedType.getProductType();

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