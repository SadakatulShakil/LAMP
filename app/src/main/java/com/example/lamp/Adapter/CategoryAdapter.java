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

import com.example.lamp.Model.CategoryType;
import com.example.lamp.R;

import java.util.ArrayList;

public class CategoryAdapter extends ArrayAdapter<CategoryType> {

    private ArrayList<CategoryType> categoryArrayList;
    private Context context;

    public CategoryAdapter(@NonNull Context context, ArrayList<CategoryType> categoryArrayList) {
        super(context, 0, categoryArrayList);
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
        CategoryType currentCategoryType = getItem(position);

        if (position == 0) {
            productTypeName.setTextColor(Color.GRAY);
        } else {
            productTypeName.setTextColor(Color.BLACK);

        }
        if (currentCategoryType != null) {
            productTypeName.setText(currentCategoryType.getCategory());
        }

        return convertView;
    }
}
