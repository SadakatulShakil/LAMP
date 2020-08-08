package com.example.lamp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.lamp.Model.productType;
import com.example.lamp.R;

import java.util.ArrayList;

public class productTypeAdapter extends ArrayAdapter<productType> {
    private ArrayList<productType> productTypeArrayList;
    private Context context;

    public productTypeAdapter(@NonNull Context context, ArrayList<productType> productTypeArrayList) {
        super(context, 0, productTypeArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }


    @Override
    public boolean isEnabled(int position) {
        return position == 0 ? false : true;
    }

    private View initView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.product_type, parent, false);

        }

        TextView productTypeName = convertView.findViewById(R.id.productType);
        productType currentProductType = getItem(position);

        if (position == 0) {
            productTypeName.setTextColor(Color.GRAY);
        } else {
            productTypeName.setTextColor(Color.BLACK);
          
        }
        if (currentProductType != null) {
            productTypeName.setText(currentProductType.getProductType());
        }

        return convertView;
    }
}