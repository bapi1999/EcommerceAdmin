package com.example.ecommerce_admin.ui.products;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ecommerce_admin.R;
import com.example.ecommerce_admin.activities.AddProductActivity;
import com.example.ecommerce_admin.databinding.FragmentDashboardBinding;
import com.example.ecommerce_admin.ui.order.adapters.OrderPagerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;

    private FloatingActionButton floatingActionButton;
    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private String totalProduct;


    private ViewPager2 viewPager2;
    private TabLayout tabLayout;



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        binding = binding.inflate(inflater, container, false);
        View root = binding.getRoot();
        floatingActionButton = root.findViewById(R.id.floatingActionButton);


        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("SELLER_DATA").document("6_ALL_PRODUCT")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    totalProduct = task.getResult().get("listSize").toString();

                }
            }
        });

        viewPager2 =root.findViewById(R.id.viewPager2);
        tabLayout =root.findViewById(R.id.tabLayout);
        FragmentManager fragmentManager = getParentFragmentManager();
        ProductPagerAdapter adapter = new ProductPagerAdapter(fragmentManager,getLifecycle());
        viewPager2.setAdapter(adapter);
        new TabLayoutMediator(tabLayout,viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull @NotNull TabLayout.Tab tab, int position) {
                if (position==0){
                    tab.setText("All");
                    tab.setIcon(R.drawable.ic_view_list_24);
                }else if(position== 1) {
                    tab.setText("Stock Low");
                    tab.setIcon(R.drawable.ic_trending_down_24);
                }else if(position== 2) {
                    tab.setText("Stock Out");
                    tab.setIcon(R.drawable.i_out_stock);
                }
            }
        }).attach();


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent addProductIntent = new Intent(getActivity(), AddProductActivity.class);
                addProductIntent.putExtra("listSize",totalProduct);
                startActivity(addProductIntent);
            }
        });




        return root;
    }






    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }





}