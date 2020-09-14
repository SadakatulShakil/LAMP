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

import com.example.lamp.Model.City;
import com.example.lamp.R;

import java.util.ArrayList;

public class CityAdapter extends ArrayAdapter<City> {
    private ArrayList<City> cityArrayList;
    private Context context;

    public CityAdapter(@NonNull Context context, ArrayList<City> cityArrayList) {
        super(context, 0, cityArrayList);
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
        City currentCity = getItem(position);

        if (position == 0) {
            productTypeName.setTextColor(Color.GRAY);
        } else {
            productTypeName.setTextColor(Color.BLACK);

        }
        if (currentCity != null) {
            productTypeName.setText(currentCity.getCity());
        }

        return convertView;
    }
}
