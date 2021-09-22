package com.example.ecommerce_admin.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ecommerce_admin.R;
import com.example.ecommerce_admin.models.OrderItemModel;
import com.example.ecommerce_admin.ui.order.adapters.OrderItemAdpter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class UiDashbordFragment extends Fragment {

    public static Integer NEW_ORDER = 0;
    public Integer ACCEPTED_ORDER = 1;
    public Integer PACKED_ORDER = 2;
    public Integer RETURNED_ORDER = 3;
    public Integer SHIPPED_ORDER = 4;
    public Integer DELIVERED_ORDER = 5;

    private TextView totalNEWorder,totalRETURNEDorder,newViewAll,returnedViewAll;

    private RecyclerView newOrderRecycler;
    private RecyclerView returnedOrderRecycler;
    private OrderItemAdpter itemAdpter ;
    private OrderItemAdpter itemAdpter2 ;
    public List<OrderItemModel> newOrderList = new ArrayList<>();
    public List<OrderItemModel> returdedOrderList = new ArrayList<>();


    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String orderID;

    public UiDashbordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ui_dashbord, container, false);

        newOrderRecycler = view.findViewById(R.id.newOrder_recycler);
        returnedOrderRecycler = view.findViewById(R.id.returned_order_recycler);
        totalNEWorder =view.findViewById(R.id.total_new_order);
        totalRETURNEDorder  = view.findViewById(R.id.total_return_order);

        MyOrder(newOrderList,NEW_ORDER,totalNEWorder);
        MyOrder(returdedOrderList,RETURNED_ORDER,totalRETURNEDorder);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        newOrderRecycler.setLayoutManager(layoutManager);
        itemAdpter = new OrderItemAdpter(newOrderList,NEW_ORDER);
        newOrderRecycler.setAdapter(itemAdpter);
        itemAdpter.notifyDataSetChanged();

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        returnedOrderRecycler.setLayoutManager(layoutManager2);
        itemAdpter2 = new OrderItemAdpter(returdedOrderList,RETURNED_ORDER);
        returnedOrderRecycler.setAdapter(itemAdpter2);
        itemAdpter2.notifyDataSetChanged();



        return view;
    }

    private void MyOrder( List<OrderItemModel> list,Integer orderCode , TextView textView) {

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("SELLER_DATA").document("5_ALL_ORDERS")
                .collection("ORDERS").whereEqualTo("1_ORDER_CODE",orderCode).limit(7)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                    if (documentSnapshot.exists()){
                        orderID = documentSnapshot.getId();
                        String statusS = documentSnapshot.getString("0_status") ;
                        int quantity = Math.toIntExact(documentSnapshot.getLong("quantity"));
                        String productId = documentSnapshot.getString("product_id").trim() ;
                        String orderPrice =documentSnapshot.getString("order_price");
                        list.add(new OrderItemModel( productId,orderID,statusS,quantity,orderPrice));
                        itemAdpter.notifyDataSetChanged();

                    }else {

                    }



                }
                if (orderCode == NEW_ORDER){
                    textView.setText(list.size()+" new order");
                }else  if (orderCode == RETURNED_ORDER){
                    textView.setText(list.size()+" order returned");
                }


            }
        });
    }
}