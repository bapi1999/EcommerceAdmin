package com.example.ecommerce_admin.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.ecommerce_admin.R;
import com.example.ecommerce_admin.adapters.CalculatePagerAdapter;
import com.example.ecommerce_admin.ui.order.adapters.OrderPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

public class PriceCalculatorActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_calculator);

        viewPager2 =findViewById(R.id.viewPager2);
        tabLayout =findViewById(R.id.tabLayout);

        FragmentManager fm = getSupportFragmentManager();
        CalculatePagerAdapter adapter = new CalculatePagerAdapter(this);
        viewPager2.setAdapter(adapter);
        new TabLayoutMediator(tabLayout,viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull @NotNull TabLayout.Tab tab, int position) {
                if (position==0){
                    tab.setText("Instruction");
                    tab.setIcon(R.drawable.i_info_button);
                }else if(position== 1) {
                    tab.setText("Calculator");
                    tab.setIcon(R.drawable.ic_baseline_calculate_24);
                }
            }
        }).attach();
    }
}