package com.example.ecommerce_admin.ui.order;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ecommerce_admin.R;
import com.example.ecommerce_admin.databinding.FragmentHomeBinding;
import com.example.ecommerce_admin.ui.order.adapters.OrderPagerAdapter;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;

    public HomeFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewPager2 =root.findViewById(R.id.viewPager2);
        tabLayout =root.findViewById(R.id.tabLayout);

        OrderPagerAdapter adapter = new OrderPagerAdapter(getFragmentManager(),getLifecycle());
        viewPager2.setAdapter(adapter);
        new TabLayoutMediator(tabLayout,viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull @NotNull TabLayout.Tab tab, int position) {
                if (position==0){
                    tab.setText("Accepted");
                    tab.setIcon(R.drawable.i_accepted);
//                    WITH OUT NUMBER
                    BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
                    badgeDrawable.setBackgroundColor(getResources().getColor(R.color.brikeRed));
                    badgeDrawable.setVisible(true);
                }else if(position== 1) {
                    tab.setText("Shipped");
                    tab.setIcon(R.drawable.i_delivery_truck);
                }else if(position== 2) {
                    tab.setText("Delivered");
                    tab.setIcon(R.drawable.i_deliverd);
                    // WITH TEXT NUMBER
                    BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
                    badgeDrawable.setBackgroundColor(getResources().getColor(R.color.viewAll));
                    badgeDrawable.setVisible(true);
                    badgeDrawable.setNumber(3);
                    badgeDrawable.setMaxCharacterCount(3);
                }
            }
        }).attach();




        return root;
    }

    private void setupViewPager(ViewPager2 viewPager2) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}