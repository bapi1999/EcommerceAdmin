package com.example.ecommerce_admin.ui.products;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce_admin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class AllProductFragment extends Fragment {
    private RecyclerView allProductRecycler;
    private static TextView productNUmber;
    public static DashboardAdapter dashboardAdapter;
    public static List<String> productlist = new ArrayList<>();

    public static List<DashboardViewModel> productModelList = new ArrayList<>();
    public static List<DashboardViewModel> stockLowlList = new ArrayList<>();
    public static List<DashboardViewModel> stockOutlList = new ArrayList<>();

    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private static Dialog loadingDialog;
    private static String totalProduct;
    public static int listSize;
    private static SwipeRefreshLayout swipeRefreshLayout;

    public AllProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_all_product, container, false);

        allProductRecycler = view.findViewById(R.id.all_product_recycler);
        productNUmber = view.findViewById(R.id.total_product);

        loadingDialog = new Dialog(getActivity());
        loadingDialog.setContentView(R.layout.le_loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.s_bigslider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();


        Myproductlist(getContext(),loadingDialog);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        allProductRecycler.setLayoutManager(layoutManager);

        dashboardAdapter = new DashboardAdapter(productModelList);
        allProductRecycler.setAdapter(dashboardAdapter);
        dashboardAdapter.notifyDataSetChanged();

//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//                swipeRefreshLayout.setRefreshing(true);
//                ReloadPage(getContext());
//            }
//        });

        return view;
    }

    public static void Myproductlist(final Context context, final Dialog dialog)  {
        productlist.clear();
        productModelList.clear();
        stockLowlList.clear();
        stockOutlList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("SELLER_DATA").document("6_ALL_PRODUCT")

                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    totalProduct = task.getResult().get("listSize").toString();
                    listSize = Integer.parseInt(totalProduct);
                    productNUmber.setText(totalProduct+" product");
                    for (int x = 0; x <  Integer.parseInt(totalProduct); x++) {
                        String productID = task.getResult().get(x + "_product_id").toString();
                        productlist.add(productID);
                        productModelList.add(new DashboardViewModel(productID,x));//todo -creat it in product fire base
                        dashboardAdapter.notifyDataSetChanged();
                        //swipeRefreshLayout.setRefreshing(false);
                        int finalX = x;
                        firebaseFirestore.collection("PRODUCTS").document(productID)
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                int stockQuantity = Math.toIntExact(task.getResult().getLong("in_stock_quantity"));
                                if (stockQuantity<=2 && stockQuantity>0){
                                    stockLowlList.add(new DashboardViewModel(productID,finalX));
//                                    StockLowFragment.stockLowAdapter.notifyDataSetChanged();
                                }else if (stockQuantity == 0){
                                    stockOutlList.add(new DashboardViewModel(productID,finalX));
                                }

                            }
                        });
                    }
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();

                }
                dialog.dismiss();
            }
        });
    }
    public static void ReloadPage( Context context) {

        Myproductlist(context,loadingDialog);
//        swipeRefreshLayout.setRefreshing(false);
        loadingDialog.dismiss();

    }
}