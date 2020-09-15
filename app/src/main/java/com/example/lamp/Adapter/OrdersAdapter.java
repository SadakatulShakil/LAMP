package com.example.lamp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lamp.Activity.OrderDetailsActivity;
import com.example.lamp.Orders.Datum;
import com.example.lamp.R;

import java.util.ArrayList;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.viewHolder> {
    private Context context;
    private ArrayList<Datum> orderArrayList;

    public OrdersAdapter(Context context, ArrayList<Datum> orderArrayList) {
        this.context = context;
        this.orderArrayList = orderArrayList;
    }

    @NonNull
    @Override
    public OrdersAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_view, parent, false);
        return new OrdersAdapter.viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.viewHolder holder, int position) {

        final Datum orderInfo = orderArrayList.get(position);
        String unit = orderInfo.getUnit();
        String quantity = String.valueOf(orderInfo.getQuantity());
        String totalPrice = String.valueOf(orderInfo.getTotalPrice());
        String fromDestination = orderInfo.getFrom();
        String toDestination = orderInfo.getTo();
        holder.orderName.setText(orderInfo.getName());
        holder.orderQuantity.setText(quantity+" "+unit);
        holder.orderTotalAmount.setText(totalPrice);
        holder.orderDestination.setText(fromDestination+" to "+toDestination);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderDetailsActivity.class);
                intent.putExtra("orderInfo", orderInfo);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private TextView orderName, orderQuantity, orderTotalAmount, orderDestination;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            orderName = itemView.findViewById(R.id.orderName);
            orderQuantity = itemView.findViewById(R.id.orderQuantity);
            orderTotalAmount = itemView.findViewById(R.id.orderTotalAmount);
            orderDestination = itemView.findViewById(R.id.orderdestination);
        }
    }
}
