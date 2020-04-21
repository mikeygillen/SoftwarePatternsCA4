package com.example.softwarepatternsca4.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softwarepatternsca4.Classes.Product;
import com.example.softwarepatternsca4.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private static final String TAG = "ProductRecyclerAdapter";

    private ArrayList<Product> productItems;
    private Context mContext;
    private OnItemClickListener mListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product currentItem = productItems.get(position);

        holder.name.setText(currentItem.getName());
        holder.manufacturer.setText(currentItem.getManufacturer());
        holder.category.setText(currentItem.getCategory());
        holder.price.setText("$" + currentItem.getPrice());
        holder.quantity.setText(currentItem.getQuantity() + " Still in stock");
        Picasso.get().load(currentItem.getImage()).fit().centerInside().into(holder.picture);
    }


    public void filterlist(ArrayList<Product> filteredItems){
        productItems = filteredItems;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, manufacturer, price, category, quantity;
        public ImageView picture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_stock);
            manufacturer = itemView.findViewById(R.id.manufacturer_stock);
            picture = itemView.findViewById(R.id.image_stock);
            price = itemView.findViewById(R.id.price_stock);
            category = itemView.findViewById(R.id.category_stock);
            quantity = itemView.findViewById(R.id.quantity_stock);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener !=null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);}
                    }}});
        }
    }

    @Override
    public int getItemCount() {
        return productItems.size();
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public ProductAdapter(Context context, ArrayList<Product> productList){
        mContext = context;
        this.productItems = productList;
    }
}
