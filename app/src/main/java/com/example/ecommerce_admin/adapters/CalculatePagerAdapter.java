package com.example.ecommerce_admin.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ecommerce_admin.fragments.PriceCalculatorFragment;
import com.example.ecommerce_admin.fragments.SellerInstractionFragment;


import org.jetbrains.annotations.NotNull;

public class CalculatePagerAdapter extends FragmentStateAdapter {

    public CalculatePagerAdapter(@NonNull @NotNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new SellerInstractionFragment();
            case 1:
                return new PriceCalculatorFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
