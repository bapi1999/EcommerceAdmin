package com.example.ecommerce_admin.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ecommerce_admin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class OrderDeatilsActivity extends AppCompatActivity {
    private TextView statusTxt1,statusTxt2,orderIdTxt;
    //product details
    private ImageView productImage;
    private TextView productName,productPrice,productQty , viewDetails;
    //Shipping details
    private TextView buyerName,buyerAddres,buyerMobNo;
    //Fees  and profit
    private TextView sellingPrice,commission,deliveryFee,totalProfit;

    private LinearLayout btnContainer,packedBtn,declineBtn,orderTrakingContainer;
    private Button acceptBtn;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private String orderId,productId,statusintent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_deatils);
        orderId = getIntent().getStringExtra("orderId");
        statusintent = getIntent().getStringExtra("status");

        statusTxt1 = findViewById(R.id.status_txt);
        statusTxt1.setVisibility(View.GONE);//not required

        statusTxt2 = findViewById(R.id.status_txt2);
        orderIdTxt = findViewById(R.id.orderId_txt);
        productImage = findViewById(R.id.product_image);
        productName = findViewById(R.id.title_txt);
        productPrice = findViewById(R.id.price_txt);
        productQty = findViewById(R.id.textView27);
        viewDetails = findViewById(R.id.view_details);
        buyerName = findViewById(R.id.buyer_name);
        buyerAddres = findViewById(R.id.buyer_address);
        buyerMobNo = findViewById(R.id.buyer_MobileNo);
        sellingPrice = findViewById(R.id.selling_price);
        commission = findViewById(R.id.commission_fee);
        deliveryFee = findViewById(R.id.delivery_fee);
        totalProfit = findViewById(R.id.total_profit);
        btnContainer = findViewById(R.id.btn_container);
        acceptBtn = findViewById(R.id.accept_order_btn);
        declineBtn = findViewById(R.id.decline_order_btn);
        packedBtn = findViewById(R.id.packed_order_btn);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        orderTrakingContainer = findViewById(R.id.order_track_container);



        firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                .collection("SELLER_DATA").document("5_ALL_ORDERS")
                .collection("ORDERS").document(orderId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){

//                    String orderedTime = task.getResult().get("1_ordered_time").toString();
//                    String packedTime = task.getResult().get("2_packed_time").toString() ;
//                    String shippedTime = task.getResult().get("3_shipped_time").toString() ;
//                    String deliveredTime = task.getResult().get("4_delivered_time").toString() ;

                    String status1 = task.getResult().get("0_status").toString() ;
                    productId = task.getResult().get("product_id").toString() ;
                    buyerMobNo.setText(task.getResult().get("buyer_mobileNo").toString());
                    buyerName.setText(task.getResult().get("buyer_name").toString());
                    buyerAddres.setText(task.getResult().get("shipping_address").toString());
                    int quantity = Math.toIntExact(task.getResult().getLong("quantity"));


                    productQty.setText("Qty: "+quantity);
                    statusTxt2.setText(status1);
                    statusTxt2.setTextColor(ContextCompat.getColor(OrderDeatilsActivity.this, R.color.brikeRed));
                    orderIdTxt.setText(orderId);

                    firebaseFirestore.collection("PRODUCTS")
                            .document(productId)
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                String uri = task.getResult().get("product_img_0").toString();
                                Glide.with(OrderDeatilsActivity.this).load(uri).apply(new RequestOptions().placeholder(R.drawable.as_square_placeholder)).into(productImage);
                                productName.setText(task.getResult().get("product_title").toString());
                                String price = task.getResult().get("price_Rs").toString();
                                productPrice.setText(price);
                                sellingPrice.setText(price);

                            }

                        }
                    });

                }
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        if (statusintent.equals("new")){
            btnContainer.setVisibility(View.VISIBLE);
            packedBtn.setVisibility(View.GONE);
            orderTrakingContainer.setVisibility(View.GONE);

        }else if (statusintent.equals("accepted")){
            btnContainer.setVisibility(View.GONE);
            packedBtn.setVisibility(View.VISIBLE);
            orderTrakingContainer.setVisibility(View.VISIBLE);
        }else {
            btnContainer.setVisibility(View.GONE);
            packedBtn.setVisibility(View.GONE);
            orderTrakingContainer.setVisibility(View.VISIBLE);
        }

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OrderDeatilsActivity.this, "Accepted", Toast.LENGTH_SHORT).show();
            }
        });
        declineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OrderDeatilsActivity.this, "Declined", Toast.LENGTH_SHORT).show();
            }
        });
        packedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OrderDeatilsActivity.this, "Packed", Toast.LENGTH_SHORT).show();
            }
        });
        viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orderDetailIntent = new Intent(OrderDeatilsActivity.this, ProductDetailActivity.class);
                orderDetailIntent.putExtra("product_ID",productId);
                orderDetailIntent.putExtra("from","ORDER_DETAILS");
                startActivity(orderDetailIntent);
            }
        });

    }
}