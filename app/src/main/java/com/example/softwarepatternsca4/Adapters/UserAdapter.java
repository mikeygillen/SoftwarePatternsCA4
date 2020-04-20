package com.example.softwarepatternsca4.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softwarepatternsca4.Classes.User;
import com.example.softwarepatternsca4.R;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private static final String TAG = "UserRecyclerAdapter";

    private ArrayList<User> userItems;
    private ArrayList<User> filteredItems;
    private OnUserListener mOnUserListener;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new ViewHolder(v, mOnUserListener);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User currentItem = userItems.get(position);

        holder.email.setText(currentItem.getEmail());
        holder.address.setText(currentItem.getAddress());
        holder.payment.setText(currentItem.getPayment());
        //holder.orders.setText(currentItem.getOrders() + " Orders");
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView email, address, payment;
        public int orders;
        OnUserListener onUserListener;

        public ViewHolder(@NonNull View itemView, OnUserListener onUserListener) {
            super(itemView);
            email = itemView.findViewById(R.id.email_user);
            address = itemView.findViewById(R.id.address_user);
            payment = itemView.findViewById(R.id.payment_user);
            //orders = itemView.findViewById(R.id.orders_user);
            this.onUserListener = onUserListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: " + getAdapterPosition());
            mOnUserListener.onUserClick(getAdapterPosition());
        }
    }

    public UserAdapter(OnUserListener onUserListener, ArrayList<User> userList) {
        this.userItems = userList;
        //filteredItems = (ArrayList<User>) userItems.clone();
        this.mOnUserListener = onUserListener;
    }

    @Override
    public int getItemCount() {
        return userItems.size();
    }

    public interface OnUserListener{
        void onUserClick(int position);
    }
}

