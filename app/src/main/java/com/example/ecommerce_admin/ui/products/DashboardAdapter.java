package com.example.ecommerce_admin.ui.products;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ecommerce_admin.R;
import com.example.ecommerce_admin.activities.ProductDetailActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {

    private List<DashboardViewModel> list = new ArrayList<>();

    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser user;
    private String state;

    public DashboardAdapter(List<DashboardViewModel> list) {
        this.list = list;
        firebaseFirestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @NotNull
    @Override
    public DashboardAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.le_product_horizontal_item, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DashboardAdapter.ViewHolder holder, int position) {
        String product_id = list.get(position).getProductId();
        int itemPosition = list.get(position).getPosition();

        holder.setProductData(product_id, position, itemPosition);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImg;
        TextView productTitle;
        TextView priceView;
        TextView retingTxt;
        TextView stockState;
        ImageButton deleteBtn;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            productImg = itemView.findViewById(R.id.product_image);
            productTitle = itemView.findViewById(R.id.product_name);
            priceView = itemView.findViewById(R.id.product_price);
            deleteBtn = itemView.findViewById(R.id.delete_btn);
            retingTxt = itemView.findViewById(R.id.mini_rating);
            stockState = itemView.findViewById(R.id.stock_quantity);

        }

        private void setProductData(final String product_id, final int index, int itemPosition) {

//            Glide.with(itemView.getContext()).load(R.drawable.as_square_placeholder).into(productImg);


            firebaseFirestore.collection("PRODUCTS").document(product_id)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        int stock = Math.toIntExact(task.getResult().getLong("in_stock_quantity"));
                        String image = task.getResult().get("product_thumbnail").toString();

                        Glide.with(itemView.getContext()).load(image).apply(new RequestOptions().placeholder(R.drawable.as_square_placeholder)).into(productImg);
                        productTitle.setText(task.getResult().get("book_title").toString());
                        String price = task.getResult().get("price_Rs").toString();
                        String rating = task.getResult().get("rating_avg").toString();
                        if (rating.length()>0){
                            retingTxt.setText(rating.substring(0,4));
                        }else {
                            retingTxt.setText(rating.length()+" ");
                        }


                        priceView.setText("Rs." + itemPosition + "/-");
                        if (stock>2){
                            stockState.setText(stock+" in Stock");
                        }else if (stock<=2 && stock>0){
                            stockState.setText(stock+" in Stock");
                            stockState.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.yellow));
//                            stockState.setTextColor(itemView.getColor(R.color.brikeRed));

                        }else {
                            productTitle.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.brikeRed));
                            stockState.setText("Out of Stock");
                            stockState.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.brikeRed));
                        }




                    }

                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), ProductDetailActivity.class);
                    intent.putExtra("product_ID", product_id);
                    intent.putExtra("position", itemPosition);
                    intent.putExtra("from","PRODUCT_LIST");

                    itemView.getContext().startActivity(intent);
                }
            });
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(itemView.getContext(), " btn clicked", Toast.LENGTH_SHORT).show();
                    Delete(index);
                }
            });

        }

        public void Delete(int index) {
            AllProductFragment.productlist.remove(index);
            AllProductFragment.productModelList.remove(index);


            AllProductFragment.listSize = AllProductFragment.productlist.size();

            Map<String, Object> map = new HashMap<>();

            map.put("listSize", AllProductFragment.productlist.size());

            firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                    .collection("SELLER_DATA").document("6_ALL_PRODUCT")
                    .set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        for (int i = 0; i < AllProductFragment.productlist.size(); i++) {
                            Map<String, Object> productDeleteMap = new HashMap<>();
                            productDeleteMap.put(i + "_product_id", AllProductFragment.productModelList.get(i).getProductId());

                            int finalI = i;
                            firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                                    .collection("SELLER_DATA").document("6_ALL_PRODUCT")
                                    .update(productDeleteMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if (task.isSuccessful()) {
//                                        DashboardFragment.dashboardAdapter.notifyDataSetChanged();
                                        if (finalI == AllProductFragment.productlist.size() - 1) {
                                            Toast.makeText(itemView.getContext(), "Successfully Deleted", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }
                            });
                        }
                    }
                }
            });


        }


    }
}
