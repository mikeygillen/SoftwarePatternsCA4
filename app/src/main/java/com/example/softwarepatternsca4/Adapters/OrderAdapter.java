package com.example.softwarepatternsca4.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softwarepatternsca4.Classes.Order;
import com.example.softwarepatternsca4.R;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private static final String TAG = "OrderAdapter";

    private ArrayList<Order> orderItems;
    private Context mContext;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order currentItem = orderItems.get(position);

        holder.name.setText(currentItem.getUser());
        holder.address.setText(currentItem.getShipping());
        holder.payment.setText("$" + currentItem.getCost() + " by " + currentItem.getPayment());
        holder.products.setText(currentItem.getProducts());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, address, payment, products;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_order);
            address = itemView.findViewById(R.id.address_order);
            payment = itemView.findViewById(R.id.payment_order);
            products = itemView.findViewById(R.id.products_order);
        }
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public OrderAdapter(Context context, ArrayList<Order> orderList){
        mContext = context;
        this.orderItems = orderList;
    }
}
