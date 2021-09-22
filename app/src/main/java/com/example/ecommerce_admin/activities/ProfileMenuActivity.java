package com.example.ecommerce_admin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.ecommerce_admin.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileMenuActivity extends AppCompatActivity {
    private TextView myProfile,promotions,priceCalculator;
    private CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_menu);

        myProfile = findViewById(R.id.my_profile_text);
        priceCalculator = findViewById(R.id.profit_calculator);
        circleImageView = findViewById(R.id.user_circleImage);

        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myAccountIntent = new Intent(ProfileMenuActivity.this,MyAccountActivity.class);
                startActivity(myAccountIntent);
            }
        });
        priceCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calculatorIntent = new Intent(ProfileMenuActivity.this,PriceCalculatorActivity.class);
                startActivity(calculatorIntent);
            }
        });
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileMenuActivity.this,TakingDetailsActivity.class);
                startActivity(intent);
            }
        });
    }
}