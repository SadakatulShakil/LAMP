package com.example.lamp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lamp.Activity.UploadProductActivity;
import com.example.lamp.Adapter.productTypeAdapter;
import com.example.lamp.Model.productType;
import com.example.lamp.R;

import java.util.ArrayList;

public class UploadProductsFragment extends Fragment {
    private Toolbar dToolbar;
    private Context context;
    private ArrayList<productType> mProductTypeList;
    private productTypeAdapter mAdapter;
    private Spinner mSpinner;
    private Button btnUpload;
    private String clickedProductTypeName, retrievedToken;
    public UploadProductsFragment() {
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
        return inflater.inflate(R.layout.fragment_upload_products, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList();
        inItView(view);

        dToolbar.setTitle(getString(R.string.upload));

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickedProductTypeName.equals("Select Product Type...")){
                    Toast.makeText(context, "please select your right product type", Toast.LENGTH_SHORT).show();
                }
                else{

                    Intent intent = new Intent(context, UploadProductActivity.class);
                    intent.putExtra("productType", clickedProductTypeName);
                    startActivity(intent);
                }
            }
        });

    }

    private void initList() {
        mProductTypeList = new ArrayList<>();
        mProductTypeList.add(new productType("Select Product Type..."));
        mProductTypeList.add(new productType("live_fixed"));
        mProductTypeList.add(new productType("prebook_fixed"));
        mProductTypeList.add(new productType("live_auction"));
    }

    private void inItView(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dToolbar = view.findViewById(R.id.toolbar);
        }
        btnUpload = view.findViewById(R.id.uploadBt);
        mSpinner = view.findViewById(R.id.spinner);
        mAdapter = new productTypeAdapter(context, mProductTypeList);

        mSpinner.setAdapter(mAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                productType clickedType = (productType) parent.getItemAtPosition(position);

                clickedProductTypeName = clickedType.getProductType();

                Toast.makeText(context, clickedProductTypeName +" is selected !", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}