package com.example.ecommerce_admin.ui.order;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecommerce_admin.R;
import com.example.ecommerce_admin.models.OrderItemModel;
import com.example.ecommerce_admin.ui.order.adapters.OrderItemAdpter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ShippedOrderFragment extends Fragment {
//TODO: ORDE CODE =============================================
    public Integer NEW_ORDER = 0;
    public Integer ACCEPTED_ORDER = 1;
    public Integer PACKED_ORDER = 2;
    public Integer RETURNED_ORDER = 3;
    public Integer SHIPPED_ORDER = 4;
    public Integer DELIVERED_ORDER = 5;
//TODO=============================================

    private RecyclerView orderRecycler;
    private OrderItemAdpter itemAdpter ;
    private List<OrderItemModel> itemModelList = new ArrayList<>();

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String totalProduct;
    int listSize;
    String orderID;

    public ShippedOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_shipped_order, container, false);
        orderRecycler = view.findViewById(R.id.shippedOrder_recycler);

        MyOrder();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        orderRecycler.setLayoutManager(layoutManager);

        itemAdpter = new OrderItemAdpter(itemModelList,SHIPPED_ORDER);
        orderRecycler.setAdapter(itemAdpter);
        itemAdpter.notifyDataSetChanged();

        return view;
    }
    private void MyOrder() {

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("SELLER_DATA").document("5_ALL_ORDERS")
                .collection("ORDERS").whereGreaterThanOrEqualTo("1_ORDER_CODE",SHIPPED_ORDER).limit(10)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                    orderID = documentSnapshot.getId();
                    String statusS = documentSnapshot.getString("0_status") ;
                    int quantity = Math.toIntExact(documentSnapshot.getLong("quantity"));
                    String productId = documentSnapshot.getString("product_id") ;
                    String orderPrice =documentSnapshot.getString("order_price");
                    itemModelList.add(new OrderItemModel( productId,orderID,statusS,quantity,orderPrice));
                    itemAdpter.notifyDataSetChanged();
                }

            }
        });
    }
}