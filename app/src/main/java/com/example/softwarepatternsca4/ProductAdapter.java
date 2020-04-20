package com.example.softwarepatternsca4;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private static final String TAG = "ProductRecyclerAdapter";

    private ArrayList<Product> productItems;
    private ArrayList<Product> filteredItems;
    private OnProductListener mOnProductListener;


    //private OnItemClickListener mListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ViewHolder(v, mOnProductListener);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product currentItem = productItems.get(position);

        holder.name.setText(currentItem.getName());
        holder.manufacturer.setText(currentItem.getManufacturer());
        holder.category.setText(currentItem.getName());
        holder.price.setText("$" + currentItem.getPrice());
        Picasso.get().load(currentItem.getImage()).fit().centerInside().into(holder.picture);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name, manufacturer, price, category;
        public ImageView picture;
        OnProductListener onProductListener;

        public ViewHolder(@NonNull View itemView, OnProductListener onProductListener) {
            super(itemView);
            name = itemView.findViewById(R.id.name_stock);
            manufacturer = itemView.findViewById(R.id.manufacturer_stock);
            picture = itemView.findViewById(R.id.image_stock);
            price = itemView.findViewById(R.id.price_stock);
            category = itemView.findViewById(R.id.category_stock);
            this.onProductListener = onProductListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: " + getAdapterPosition());
            mOnProductListener.onProductClick(getAdapterPosition());
        }
    }

    public ProductAdapter(OnProductListener onProductListener, ArrayList<Product> productList) {
        this.productItems = productList;
        //filteredItems = (ArrayList<Product>) productItems.clone();
        this.mOnProductListener = onProductListener;
    }

    @Override
    public int getItemCount() {
        return productItems.size();
    }

    public interface OnProductListener{
        void onProductClick(int position);
    }

/*
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

 */
}
