package com.example.ecommerce_admin.activities;

import android.app.Dialog;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ecommerce_admin.R;
import com.example.ecommerce_admin.ui.products.DashboardFragment;
import com.example.ecommerce_admin.ui.products.DashboardViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dbquery {

    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();;

    public static List<String> productlist = new ArrayList<>();
    public static List<DashboardViewModel> productModelList = new ArrayList<>();

    public static List<String> stockOutlist = new ArrayList<>();


    public static void Myproductlist(final Context context, final Dialog dialog, final boolean loadProductData)  {
        productlist.clear();
        productModelList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("SELLER_DATA").document("6_ALL_PRODUCT")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {

                    String  totalProduct = task.getResult().get("listSize").toString();
                    int listSize = Integer.parseInt(totalProduct);

                    for (int x = 0; x <  listSize; x++) {
                        String productID = task.getResult().get(x + "_product_id").toString();
                        productlist.add(productID);
                        productModelList.add(new DashboardViewModel(productID,6));//todo -creat it in product fire base
                       // DashboardFragment.dashboardAdapter.notifyDataSetChanged();
//                        DashboardFragment.swipeRefreshLayout.setRefreshing(false);
                    }
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();

                }
                dialog.dismiss();
            }
        });
    }

    public static void OutofStockMetod(List<String> productlist){
        String product_id ="";
        firebaseFirestore.collection("PRODUCTS").document(product_id)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
//                    String image = task.getResult().get("product_img_0").toString();
//                    Glide.with().load(image).apply(new RequestOptions().placeholder(R.drawable.as_square_placeholder)).into(productImg);
                    boolean instock = task.getResult().getBoolean("in_stock");
                    if (!instock){
                        stockOutlist.add(product_id);
                    }
                }

            }
        });

    }

    public static void ClearNotification(BadgeDrawable badgeDrawable){
        Map<String,Object> clearMap = new HashMap<>();
        clearMap.put("new_listSize",0);
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("SELLER_DATA").document("NOTIFICATION")
                .update(clearMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    badgeDrawable.clearNumber();
                    badgeDrawable.setVisible(false);

                }
            }
        });
    }

}
