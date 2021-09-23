package com.example.ecommerce_admin.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce_admin.R;
import com.example.ecommerce_admin.models.ProductReviewModel;

import java.util.ArrayList;
import java.util.List;

public class ProductReviewAdapter extends RecyclerView.Adapter<ProductReviewAdapter.ViewHolder> {

    List<ProductReviewModel> list = new ArrayList<>();

    public ProductReviewAdapter(List<ProductReviewModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ProductReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.le_product_review_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductReviewAdapter.ViewHolder holder, int position) {
        String buyerId = list.get(position).getBuyerId();
        String buyerName= list.get(position).getBuyerName();
        int rating= list.get(position).getRating();
        String review= list.get(position).getReview();
        String reviewDate= list.get(position).getReviewDate();


        holder.setData(buyerName ,reviewDate,  review, rating);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView buyerNameTxt,reviewDateTxt,reviewTxt,ratingTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            buyerNameTxt = itemView.findViewById(R.id.buyer_name);
            reviewDateTxt = itemView.findViewById(R.id.review_date);
            reviewTxt = itemView.findViewById(R.id.buyer_review);
            ratingTxt = itemView.findViewById(R.id.mini_rating);
        }

        private void setData(String buyerName ,String reviewDate, String review,int rating){
            buyerNameTxt.setText(buyerName);
            reviewDateTxt.setText(reviewDate);
            reviewTxt.setText(review);
            ratingTxt.setText(rating+"");
        }

    }
}
