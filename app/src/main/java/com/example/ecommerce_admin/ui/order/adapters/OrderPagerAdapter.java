package com.example.ecommerce_admin.ui.order.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ecommerce_admin.ui.order.AcceptedOrderFragment;
import com.example.ecommerce_admin.ui.order.ReturnedOrderFragment;
import com.example.ecommerce_admin.ui.order.NewOrderFragment;
import com.example.ecommerce_admin.ui.order.ShippedOrderFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OrderPagerAdapter extends FragmentStateAdapter {
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<Integer> tabNumbrList = new ArrayList<>();


    public OrderPagerAdapter(@NonNull @NotNull FragmentManager fragmentManager, @NonNull @NotNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);

    }


    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {

        switch (position){
            case 0:
                return new AcceptedOrderFragment();
            case 1:
                return new ShippedOrderFragment();
            case 2:
                return new ReturnedOrderFragment();
            default:
                return null;
        }

    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
