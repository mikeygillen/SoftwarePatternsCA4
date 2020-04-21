package com.example.softwarepatternsca4.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softwarepatternsca4.Classes.Review;
import com.example.softwarepatternsca4.R;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private static final String TAG = "ReviewAdapter";

    private ArrayList<Review> reviewItems;
    private Context mContext;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review currentItem = reviewItems.get(position);

        holder.comment.setText("'" + currentItem.getComment() + "'");
        holder.ratingBar.setNumStars((int) currentItem.getRating());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView comment;
        public RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            comment = itemView.findViewById(R.id.comment_comment);
            ratingBar = itemView.findViewById(R.id.comment_rating);
        }
    }

    @Override
    public int getItemCount() {
        return reviewItems.size();
    }

    public ReviewAdapter(Context context, ArrayList<Review> reviewList){
        mContext = context;
        this.reviewItems = reviewList;
    }
}
