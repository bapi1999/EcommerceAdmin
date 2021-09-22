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


public class StockOutFragment extends Fragment {

    private RecyclerView stockOutRecycler;
    private TextView productNUmber;
    public static DashboardAdapter stockOutAdapter;

    public StockOutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_stock_out, container, false);

        stockOutRecycler = view.findViewById(R.id.stockOut_recycler);
        productNUmber = view.findViewById(R.id.total_product);
        String totalProduct = String.valueOf(AllProductFragment.stockOutlList.size())+" Product";
        productNUmber.setText(totalProduct);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        stockOutRecycler.setLayoutManager(layoutManager);

        stockOutAdapter = new DashboardAdapter(AllProductFragment.stockOutlList);
        stockOutRecycler.setAdapter(stockOutAdapter);
        stockOutAdapter.notifyDataSetChanged();


        return view;
    }
}