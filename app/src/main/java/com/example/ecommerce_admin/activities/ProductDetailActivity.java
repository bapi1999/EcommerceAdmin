package com.example.ecommerce_admin.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce_admin.R;
import com.example.ecommerce_admin.adapters.ProductImgAdapter;
import com.example.ecommerce_admin.ui.products.AllProductFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDetailActivity extends AppCompatActivity {
    //layout 1

    private ViewPager productImgViewPager;
    private TabLayout productImgIndicator;
    private TextView productName, mini_avg_rating, mini_total_ratings, product_price
            ,productState,countWishlist,stockQuantity,stockState;

    //2nd layout
    private TextView productDetailsText;
    //3rd layout

    //4th layout //rating

    public static LinearLayout rateNowContainer;
    private TextView average_rating, totalRating;
    private LinearLayout ratings_number_container, rating_bar_container;
    public static int initialRating;
    private int mainPosition = -1;


    // main layout
    private Button editBtn;
    private LinearLayout deleteBtn,btnContainer;

    // other variable
    public static boolean fromSearch = false;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser currentUser;
    public static String productID;
    private String cameFrom;

    private Dialog loadingDialog;

    private DocumentSnapshot documentSnapshot;
    private TextView sellingPrice,commissionFee,deliverycharge, estmdProfit;
//    private String totalProduct;
//    private int index;
//    public static MenuItem cartItem;
//
//    public static Activity productDetailsActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        productName = findViewById(R.id.product_name);
        mini_avg_rating = findViewById(R.id.mini_product_rating);
        mini_total_ratings = findViewById(R.id.mini_totalNumberOf_ratings);
        product_price = findViewById(R.id.product_price);
        productDetailsText = findViewById(R.id.product_details_text);
        productState= findViewById(R.id.product_state);
        countWishlist=findViewById(R.id.count_wishlist);
        stockQuantity=findViewById(R.id.stock_quantity);
        stockState=findViewById(R.id.stockState);

        average_rating = findViewById(R.id.average_rating_text);
        totalRating = findViewById(R.id.totalRating);

        ratings_number_container = findViewById(R.id.ratings_number_container);
        rating_bar_container = findViewById(R.id.rating_bar_containter);
        editBtn = findViewById(R.id.edit_product_btn);
        deleteBtn = findViewById(R.id.delete_product_btn);
        btnContainer = findViewById(R.id.linearLayout4);

        productImgViewPager = findViewById(R.id.product_Img_viewPager);
        productImgIndicator = findViewById(R.id.product_Img_indicator);
        productImgIndicator.setupWithViewPager(productImgViewPager, true);
        initialRating = -2;
        rateNowContainer = findViewById(R.id.rate_now_container);
        sellingPrice = findViewById(R.id.selling_price);
        commissionFee = findViewById(R.id.commission_fee);
        deliverycharge  = findViewById(R.id.delivery_fee);
        estmdProfit = findViewById(R.id.total_profit);

        //loading dialog
        loadingDialog = new Dialog(ProductDetailActivity.this);
        loadingDialog.setContentView(R.layout.le_loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(AppCompatResources.getDrawable(this,R.drawable.s_bigslider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        //loading dialog

        firebaseFirestore = FirebaseFirestore.getInstance();
        final List<String> productImgList = new ArrayList<>();
        productID = getIntent().getStringExtra("product_ID");
        cameFrom = getIntent().getStringExtra("from");
        //TODO-  from    NOTIFICATION/ PRODUCT_LIST/ SEARCH/ ORDER_DETAILS

        if (cameFrom.equals("NOTIFICATION") || cameFrom.equals("SEARCH") ){
            getDeleteBtnPositon(productID);
        }else if(cameFrom.equals("PRODUCT_LIST") ){
            mainPosition = getIntent().getIntExtra("position",-1);
        }else if(cameFrom.equals("ORDER_DETAILS") ){
            mainPosition = 0;
            // DISABLE DELETE/EDIT BUTTON
            btnContainer.setVisibility(View.GONE);
        }

//        totalProduct = getIntent().getStringExtra("listSize");
//        index = getIntent().getIntExtra("index",-1);






//TODO- Main query###################################################################################################

        firebaseFirestore.collection("PRODUCTS").document(productID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isComplete()) {
                    documentSnapshot = task.getResult();
                    for (long x = 0; x < (long) documentSnapshot.get("no_of_img") ; x++) {
                        productImgList.add(documentSnapshot.get("product_img_" + x).toString());
                    }
                    ProductImgAdapter adapter = new ProductImgAdapter(productImgList);
                    productImgViewPager.setAdapter(adapter);

                    String avgRating = documentSnapshot.get("rating_avg").toString();
                    String totalRatinginput = documentSnapshot.get("rating_total").toString();

                    productName.setText(documentSnapshot.get("book_title").toString());
                    mini_avg_rating.setText(avgRating);

                    mini_total_ratings.setText(new StringBuilder().append("(").append(totalRatinginput).append(") ratings").toString());
                    String price = documentSnapshot.get("price_Rs").toString();
                    product_price.setText(new StringBuilder().append("Rs.").append(price).append("/-").toString());
                    sellingPrice.setText("Rs." + price+ "/-");
                    productDetailsText.setText(documentSnapshot.get("book_details").toString());
//                    int stock_quantity = Integer.parseInt(documentSnapshot.get("stock_quantity").toString());

                    average_rating.setText(avgRating);
                    totalRating.setText(totalRatinginput);

                    for (int x = 0; x < 5; x++) {
                        TextView rating = (TextView) ratings_number_container.getChildAt(x);
                        rating.setText(documentSnapshot.get("rating_Star_" + (5 - x)).toString());

                        ProgressBar progressBar = (ProgressBar) rating_bar_container.getChildAt(x);
                        int maxProgress = Integer.parseInt(documentSnapshot.get("rating_total").toString());
                        progressBar.setMax(maxProgress);
                        String perccing = documentSnapshot.get("rating_Star_" + (5 - x)).toString();
                        int progress = Integer.valueOf(perccing);
                        progressBar.setProgress(progress);
                    }
                    loadingDialog.dismiss();
                    ProfitCalculator(price);


//todo = Edit btn ##################################################################
                    editBtn.setOnClickListener(new View.OnClickListener()  {
                        @Override
                        public void onClick(View view) {

                            Map<String, Object> addProduct = new HashMap<>();


                            firebaseFirestore.collection("USERS").document(currentUser.getUid())
                                    .collection("USER_DATA").document("MY_CART")
                                    .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {


                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(ProductDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });

                    deleteBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            if (cameFrom.equals("NOTIFICATION") || cameFrom.equals("SEARCH") ){
//                                getDeleteBtnPositon(productID);
//                            }else if(cameFrom.equals("PRODUCT_LIST") ){
//                                mainPosition = getIntent().getIntExtra("position",-1);
//
//                            }
                            Toast.makeText(ProductDetailActivity.this, mainPosition+" position", Toast.LENGTH_SHORT).show();


                        }
                    });



                } else {
                    loadingDialog.dismiss();
                    String error = task.getException().getMessage();
                    Toast.makeText(ProductDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    private Integer getDeleteBtnPositon(String productId){
        int position = 0;
        for (int i = 0;i< AllProductFragment.productlist.size();i++){
            String ids = AllProductFragment.productlist.get(i);
            if (productId.equals(ids)){
                position = i;
                mainPosition = position;
                break;

            }
        }
//        Toast.makeText(ProductDetailActivity.this, mainPosition+" position", Toast.LENGTH_SHORT).show();

        return position;
    }
    public void ProfitCalculator(String pricetxt){
        //not fully correct. after adding shipping Api it will re calculate
        int productPrice = Integer.parseInt(pricetxt);
        int commission = 0;
        int result1 = 0;
        if (productPrice<=250){
            commission = productPrice*2/100;
            result1 = productPrice - commission;
        }else if( productPrice > 250 && productPrice<=500 ){

            commission = productPrice*4/100;
            result1 = productPrice - commission;
        }else if( productPrice > 500 ){
            commission = productPrice*5/100;
            result1 = productPrice - commission;
        }
        commissionFee.setText("Rs. "+commission);
        estmdProfit.setText("Rs. "+result1);

    }
}