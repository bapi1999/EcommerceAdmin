package com.example.ecommerce_admin.ui.products;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

public class ProductPagerAdapter extends FragmentStateAdapter {

    public ProductPagerAdapter(@NonNull @NotNull FragmentManager fragmentManager, @NonNull @NotNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new AllProductFragment();
            case 1:
                return new StockLowFragment();
            case 2:
                return new StockOutFragment();
            default:
                return null;
        }

    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
