package com.example.ecommerce_admin.ui.products;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ecommerce_admin.R;


public class StockLowFragment extends Fragment {
    private RecyclerView stockLowRecycler;
    private TextView productNUmber;
    public static DashboardAdapter stockLowAdapter;
    public StockLowFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_stock_low, container, false);

        stockLowRecycler = view.findViewById(R.id.stockLow_recycler);
        productNUmber = view.findViewById(R.id.total_product);
        String totalProduct = String.valueOf(AllProductFragment.stockLowlList.size())+" Product";
        productNUmber.setText(totalProduct);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        stockLowRecycler.setLayoutManager(layoutManager);

        stockLowAdapter = new DashboardAdapter(AllProductFragment.stockLowlList);
        stockLowRecycler.setAdapter(stockLowAdapter);
        stockLowAdapter.notifyDataSetChanged();

        return view;
    }
}