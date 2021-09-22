package com.example.ecommerce_admin.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.ecommerce_admin.R;

public class TakeBusinessFragment extends Fragment {

    private FrameLayout parentFrameLayout;
    private Button gotoBankBtn;
    public TakeBusinessFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_take_business, container, false);
        parentFrameLayout = view.findViewById(R.id.take_details_frameLayout);
        gotoBankBtn = view.findViewById(R.id.goto_bank_btn);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gotoBankBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new TakeBankDetailsFragment());
            }
        });

    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slideout_from_left,R.anim.slide_from_right);
        fragmentTransaction.replace(R.id.take_details_frameLayout,fragment);
        fragmentTransaction.commit();

    }
}