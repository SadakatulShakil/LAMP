package com.example.lamp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lamp.AuctionList.Datum;
import com.example.lamp.R;

import java.util.ArrayList;

public class AuctionListAdapter extends RecyclerView.Adapter<AuctionListAdapter.viewHolder> {
    private Context context;
    private ArrayList<Datum> auctionArrayList;
    private String userType;

    public AuctionListAdapter(Context context, ArrayList<Datum> auctionArrayList, String userType) {
        this.context = context;
        this.auctionArrayList = auctionArrayList;
        this.userType = userType;
    }

    @NonNull
    @Override
    public AuctionListAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.auction_view, parent, false);
        return new AuctionListAdapter.viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AuctionListAdapter.viewHolder holder, int position) {
        Datum auctionInfo = auctionArrayList.get(position);
        holder.auctionTimeTv.setText(auctionInfo.getUpdatedAt());
        holder.auctionMessageTv.setText(auctionInfo.getMessage());
        double amount = auctionInfo.getAmount();
        int totalCharge = auctionInfo.getTotalCharge();
        double actualAmount = amount - totalCharge;
        String pActualAmount = String.valueOf(actualAmount);
        String pAmount = String.valueOf(amount);


        if(userType.equals("farmer")){
            holder.acceptBtn.setVisibility(View.VISIBLE);
            holder.auctionAmountTv.setText(pActualAmount);

        }
        else if(userType.equals("whole seller")){
            holder.acceptBtn.setVisibility(View.GONE);
            holder.auctionAmountTv.setText(pAmount);
        }

    }

    @Override
    public int getItemCount() {
        return auctionArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView auctionTimeTv, auctionAmountTv, auctionMessageTv;
        private Button acceptBtn;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            auctionTimeTv = itemView.findViewById(R.id.auctionTime);
            auctionAmountTv = itemView.findViewById(R.id.auctionAmount);
            auctionMessageTv = itemView.findViewById(R.id.auctionMessage);
            acceptBtn = itemView.findViewById(R.id.acceptAuctionBtn);
        }
    }
}
