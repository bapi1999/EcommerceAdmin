package com.example.ecommerce_admin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ecommerce_admin.R;

public class MyAccountActivity extends AppCompatActivity {
    private ImageView editProfileBtn,editAddressBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        editProfileBtn = findViewById(R.id.edit_profile_btn);
        editAddressBtn= findViewById(R.id.edit_address_btn);

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyAccountActivity.this, "Profile", Toast.LENGTH_SHORT).show();
            }
        });
        editAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyAccountActivity.this, "Address", Toast.LENGTH_SHORT).show();
            }
        });

    }
}