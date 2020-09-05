package com.example.lamp.Adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lamp.ProductsInfo.Datum;
import com.example.lamp.ProductsInfo.ProductsInfo;
import com.example.lamp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class productsAdapter extends RecyclerView.Adapter<productsAdapter.viewHolder> {
    private Context context;
    private ArrayList<Datum> productsInfoArrayList;

    public productsAdapter(Context context, ArrayList<Datum> productsInfoArrayList) {
        this.context = context;
        this.productsInfoArrayList = productsInfoArrayList;
    }

    @NonNull
    @Override
    public productsAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_view, parent, false);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull productsAdapter.viewHolder holder, int position) {

        final Datum productsInfo = productsInfoArrayList.get(position);
        holder.productTitle.setText(productsInfo.getTitle());
        String unitPrice = String.valueOf(productsInfo.getUnitPrice());
        String unitMeasure = productsInfo.getUnit();
        holder.productUnitPrice.setText(unitPrice+"৳/"+unitMeasure);
        String minBidPrice = String.valueOf(productsInfo.getMinBidPrice());
        holder.productBidPrice.setText(minBidPrice);
        String productImageUri = productsInfo.getPhotos().getOne();
        Picasso.get().load(productImageUri).into(holder.productImage);

    }

    @Override
    public int getItemCount() {
        return productsInfoArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private TextView productTitle, productUnitPrice, productBidPrice;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.productImage);
            productTitle = itemView.findViewById(R.id.productTitle);
            productUnitPrice = itemView.findViewById(R.id.productUnitPrice);
            productBidPrice = itemView.findViewById(R.id.productBidPrice);
        }
    }
}
