package com.example.ecommerce_admin.ui.order.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ecommerce_admin.R;
import com.example.ecommerce_admin.activities.OrderDeatilsActivity;
import com.example.ecommerce_admin.models.OrderItemModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class
OrderItemAdpter  extends RecyclerView.Adapter<OrderItemAdpter.ViewHolder> {

    List<OrderItemModel> list = new ArrayList<>();
    public FirebaseFirestore firebaseFirestore ;
    int orderCode;

    public Integer NEW_ORDER = 0;
    public Integer ACCEPTED_ORDER = 1;
    public Integer PACKED_ORDER = 2;
    public Integer RETURNED_ORDER = 3;

    public OrderItemAdpter(List<OrderItemModel> list,int orderCode) {
        this.list = list;
        this.orderCode = orderCode;

        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @NotNull
    @Override
    public OrderItemAdpter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.le_order_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull OrderItemAdpter.ViewHolder holder, int position) {

        String orderId = list.get(position).getOrderID();
        String productId = list.get(position).getProductID();
        String orderStatus= list.get(position).getOrderStatus();
        int quantity= list.get(position).getQuantity();
        String orderPrice= list.get(position).getOrderPrice();
        holder.setProduct(orderId,orderStatus, quantity,productId,orderPrice);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title,price,quantityTxt,viewDetails,productstatus;
        Button acceptBtn;
        LinearLayout declineBtn,buttonContainer,packedBtn;
        View devider;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.product_image);
            title = itemView.findViewById(R.id.title_txt);
            price = itemView.findViewById(R.id.price_txt);
            quantityTxt = itemView.findViewById(R.id.textView27);
            productstatus = itemView.findViewById(R.id.status_txt);
            acceptBtn = itemView.findViewById(R.id.accept_product_btn);
            declineBtn = itemView.findViewById(R.id.decline_product_btn);
            packedBtn = itemView.findViewById(R.id.packed_btn);
            buttonContainer = itemView.findViewById(R.id.linearLayout4);
            viewDetails = itemView.findViewById(R.id.view_details);
            devider = itemView.findViewById(R.id.divider12);

        }
        private void setProduct( String orderId,final String orderStatus, int quantity,String productId,String orderPrice){

            firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                    .collection("SELLER_DATA").document("5_ALL_ORDERS")
                    .collection("ORDERS").document(orderId)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){

                        productstatus.setText(orderStatus);
                        quantityTxt.setText("Qty:"+quantity);
                        price.setText(orderPrice);

                        if (orderStatus.equals("new")){
                            productstatus.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.brikeRed));
                        }else if (orderStatus.equals("accepted")){
                            productstatus.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.successGreen));
                        }else if(orderStatus.equals("shipped")){

                            productstatus.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.light_black));
                        }else if(orderStatus.equals("delivered")){
                            productstatus.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.successGreen));
                        }else if(orderStatus.equals("returned")){
                            productstatus.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.brikeRed));
                            title.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.brikeRed));
                        }


                        firebaseFirestore.collection("PRODUCTS")
                                .document(productId)
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    title.setText(task.getResult().get("book_title").toString());
                                    String uri = task.getResult().get("product_thumbnail").toString();
                                    Glide.with(itemView.getContext()).load(uri).apply(new RequestOptions().placeholder(R.drawable.as_square_placeholder)).into(image);



                                }

                            }
                        });
                    }
                }
            });



            if (orderCode==NEW_ORDER){
                devider.setVisibility(View.GONE);
                buttonContainer.setVisibility(View.VISIBLE);
                packedBtn.setVisibility(View.GONE);
            }else if (orderCode==ACCEPTED_ORDER){
                devider.setVisibility(View.GONE);
                buttonContainer.setVisibility(View.GONE);
                packedBtn.setVisibility(View.VISIBLE);
            }else {
                devider.setVisibility(View.VISIBLE);
                buttonContainer.setVisibility(View.GONE);
                packedBtn.setVisibility(View.GONE);
            }

            acceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), "Accepted", Toast.LENGTH_SHORT).show();
                }
            });
            declineBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), "Declined", Toast.LENGTH_SHORT).show();
                }
            });
            packedBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), "Packed", Toast.LENGTH_SHORT).show();
                }
            });
            viewDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent orderDetailIntent = new Intent(itemView.getContext(), OrderDeatilsActivity.class);
//                    orderDetailIntent.putExtra("productID",productID);
                    orderDetailIntent.putExtra("orderId",orderId);
                    orderDetailIntent.putExtra("ORDER_CODE", orderCode);
                    itemView.getContext().startActivity(orderDetailIntent);
                }
            });

        }

    }
}
