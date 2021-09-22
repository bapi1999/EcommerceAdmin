package com.example.ecommerce_admin.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce_admin.R;
import com.example.ecommerce_admin.adapters.PaymentHistoryAdapter;
import com.example.ecommerce_admin.ui.order.adapters.OrderItemAdpter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WithdrawalActivity extends AppCompatActivity {
    //1st layout
    private TextView accNumber, ifscCode, bankingName, aadhaarNo, myEarning;
    private LinearLayout detailsContainer, noAccount;
    //2nd layout
    private AutoCompleteTextView enterAmount;
    private Button confirmBtn;
    private TextView lastDate, clickHere;
    private LinearLayout clickHelpHere;
    //3rd layout
    private TextView noHistory;
    private CheckBox showAndHide;
    private RecyclerView paymentHistoryRecycler;
    // extras
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private List<String> paymentList = new ArrayList<>();
    private PaymentHistoryAdapter adapter;
    private boolean isHistoryAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal);
        accNumber = findViewById(R.id.acc_number);
        ifscCode = findViewById(R.id.ifsc_code);
        bankingName = findViewById(R.id.banking_name);
        aadhaarNo = findViewById(R.id.aadhar_number);
        myEarning = findViewById(R.id.my_earning);
        detailsContainer = findViewById(R.id.details_container);
        noAccount = findViewById(R.id.no_account_add_new);

        enterAmount = findViewById(R.id.enter_amount);
        confirmBtn = findViewById(R.id.confirm_button);
        lastDate = findViewById(R.id.last_wdl_date);
        clickHelpHere = findViewById(R.id.linearLayout8);

        noHistory = findViewById(R.id.textView59);
        showAndHide = findViewById(R.id.show_hide_btn);
        paymentHistoryRecycler = findViewById(R.id.payment_history);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        paymentList.clear();

        showAndHide.setText("Show");
        paymentHistoryRecycler.setVisibility(View.GONE);


        firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                .collection("SELLER_DATA").document("7_MY_PAYMENT")
                .collection("EARNING_DETAILS").document("PAYMENT_HISTORY")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    isHistoryAvailable = task.getResult().getBoolean("00_is_History");
                    if (isHistoryAvailable) {
                        noHistory.setVisibility(View.GONE);
                        showAndHide.setVisibility(View.VISIBLE);
                        int listSize2 = Integer.parseInt(task.getResult().get("00_listSize").toString());
                        for (int i = 0; i < listSize2; i++) {
                            String paymentID = task.getResult().get("payment_" + i).toString();
                            paymentList.add(paymentID);
                        }
                    } else {
                        noHistory.setVisibility(View.VISIBLE);
                        paymentHistoryRecycler.setVisibility(View.GONE);
                        showAndHide.setVisibility(View.GONE);
                    }


                }

            }
        });


        firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                .collection("SELLER_DATA").document("7_MY_PAYMENT")
                .collection("EARNING_DETAILS").document("BANK_DETAILS")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    boolean isBankLinked = task.getResult().getBoolean("is_Bank_Linked");
                    if (isBankLinked) {
                        detailsContainer.setVisibility(View.VISIBLE);
                        noAccount.setVisibility(View.GONE);
                        accNumber.setText(task.getResult().get("account_number").toString());
                        ifscCode.setText(task.getResult().get("ifsc_code").toString());
                        bankingName.setText(task.getResult().get("name").toString());
                        aadhaarNo.setText(task.getResult().get("aadhaar_number").toString());
//                        myEarning = ;

                    } else {
                        detailsContainer.setVisibility(View.GONE);
                        noAccount.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        paymentHistoryRecycler.setLayoutManager(layoutManager);

        adapter = new PaymentHistoryAdapter(paymentList);
        paymentHistoryRecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }

    @Override
    protected void onStart() {
        super.onStart();
        noAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = enterAmount.getText().toString();
                int x = Integer.parseInt(amount);
                if (x < 50) {
                    Toast.makeText(WithdrawalActivity.this, "Not allowed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(WithdrawalActivity.this, "transfer to your bank account within one day", Toast.LENGTH_SHORT).show();

                    Map<String, Object> payRequestMap = new HashMap<>();
                    payRequestMap.put("SELLER_ID", firebaseAuth.getUid());
                    payRequestMap.put("REQUEST_AMOUNT", amount);
                    payRequestMap.put("TIME", FieldValue.serverTimestamp());
                    firebaseFirestore.collection("ADMIN_REQUEST").document("PAYMENT_REQUEST")
                            .collection(TodaysDate()).document(firebaseAuth.getUid()).set(payRequestMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(WithdrawalActivity.this, "transfer to your bank account within one day", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }


            }
        });


        showAndHide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(WithdrawalActivity.this, "show", Toast.LENGTH_SHORT).show();
                    showAndHide.setText("Hide");
                    paymentHistoryRecycler.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(WithdrawalActivity.this, "hide", Toast.LENGTH_SHORT).show();
                    showAndHide.setText("Show");
                    paymentHistoryRecycler.setVisibility(View.GONE);
                }
            }
        });
    }

    public String TodaysDate() {

        Date today = Calendar.getInstance().getTime();//getting date
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");//formating according to my need
        String todayDate = formatter.format(today);
        return todayDate;
    }
}