package com.example.ecommerce_admin.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce_admin.R;
import com.example.ecommerce_admin.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignupFragment extends Fragment {


    public SignupFragment() {
        // Required empty public constructor
    }
    private String pattern="[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    public static boolean disableCloseBtn = false;

    private TextView alreadyHaveAcc;
    private FrameLayout parentframeLayout;
    private EditText email,name,pass,conf_pass;
    private ImageButton closeButton;
    private Button signupBtn;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        alreadyHaveAcc = view.findViewById(R.id.sign_in_forgot_pass);
        parentframeLayout=getActivity().findViewById(R.id.reg_frame_layout);

        email=view.findViewById(R.id.sign_up_email);
        name=view.findViewById(R.id.sign_up_name);
        pass=view.findViewById(R.id.sign_up_password);
        conf_pass=view.findViewById(R.id.sign_up_confirm_password);

//        closeButton=view.findViewById(R.id.sign_up_close_btn);
        signupBtn=view.findViewById(R.id.sign_up_btn);
        progressBar=view.findViewById(R.id.sign_up_progressBar);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

//        if(disableCloseBtn){
//            closeButton.setVisibility(View.GONE);
//
//        }else {
//            closeButton.setVisibility(View.VISIBLE);
//        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        alreadyHaveAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new SigninFragment());
            }
        });

//        closeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getActivity(), MainActivity.class));
//                getActivity().finish();
//            }
//        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        conf_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEmailandPass();
            }
        });
    }

    private void checkEmailandPass() {
        if(email.getText().toString().matches(pattern)){
            if(pass.getText().toString().equals(conf_pass.getText().toString())){

                progressBar.setVisibility(View.VISIBLE);
                signupBtn.setEnabled(false);
                signupBtn.setTextColor(Color.argb(50,255,255,255));

                mAuth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    final Map<String,Object> userdata= new HashMap<>();
                                    userdata.put("name",name.getText().toString());
                                    userdata.put("email",email.getText().toString());
                                    userdata.put("profile","");
                                    userdata.put("Is_seller",(boolean)true);
                                    userdata.put("Both_Seller_User",false);
                                    userdata.put("Is_user",false);

                                    firebaseFirestore.collection("USERS").document(mAuth.getUid())
                                            .set(userdata)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){

                                                        CollectionReference userDataReference=firebaseFirestore.collection("USERS")
                                                                .document(mAuth.getUid()).collection("SELLER_DATA");

                                                        Map<String,Object> newOrderMap= new HashMap<>();
                                                        newOrderMap.put("listSize", (long) 0);

                                                        Map<String,Object> acceptedOrderMap= new HashMap<>();
                                                        acceptedOrderMap.put("listSize", (long) 0);

                                                        Map<String,Object> shippingMap= new HashMap<>();
                                                        shippingMap.put("listSize", (long) 0);

                                                        Map<String,Object> returnedMap= new HashMap<>();
                                                        returnedMap.put("listSize", (long) 0);


                                                        Map<String,Object> allProductMap= new HashMap<>();
                                                        allProductMap.put("listSize", (long) 0);

                                                        Map<String,Object> notificationMap= new HashMap<>();
                                                        notificationMap.put("listSize", (long) 0);
                                                        notificationMap.put("new_listSize",(long) 0);


                                                        List<String> documentNames = new ArrayList<>();
                                                        documentNames.add("1_NEW_ORDERS"); //newOrderMap
                                                        documentNames.add("2_ACCEPTED_ORDERS"); //acceptedOrderMap
                                                        documentNames.add("3_SHIPPED_ORDERS"); //shippingMap
                                                        documentNames.add("4_RETURNED_ORDER"); //returnedMap
                                                        documentNames.add("6_ALL_PRODUCT"); //allProductMap
                                                        documentNames.add("NOTIFICATION"); //notificationMap


                                                        final List<Map<String,Object>> documentFields = new ArrayList<>();
                                                        documentFields.add(newOrderMap);
                                                        documentFields.add(acceptedOrderMap);
                                                        documentFields.add(shippingMap);
                                                        documentFields.add(returnedMap);
                                                        documentFields.add(allProductMap);
                                                        documentFields.add(notificationMap);

                                                        for (int x=0; x<documentNames.size() ;x++){
                                                            final int finalX = x;
                                                            userDataReference.document(documentNames.get(x))
                                                                    .set(documentFields.get(x))
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if(task.isSuccessful()){
                                                                                if(finalX == documentFields.size()-1) {
                                                                                    if (disableCloseBtn) {
                                                                                        disableCloseBtn = false;
                                                                                    } else {
                                                                                        startActivity(new Intent(getActivity(), MainActivity.class));
                                                                                    }
                                                                                    getActivity().finish();
                                                                                }
                                                                            }else {
                                                                                progressBar.setVisibility(View.INVISIBLE);
                                                                                signupBtn.setEnabled(true);
                                                                                signupBtn.setTextColor(Color.rgb(255,255,255));
                                                                                String error=task.getException().getMessage();
                                                                                Toast.makeText(getActivity(), error,Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                    });
                                                        }
                                                    }
                                                    else {
                                                        String error=task.getException().getMessage();
                                                        Toast.makeText(getActivity(), error,Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                                else {
                                    // If sign in fails, display a message to the user.
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getActivity(), "Authentication failed.",Toast.LENGTH_SHORT).show();
                                    signupBtn.setEnabled(true);
                                    signupBtn.setTextColor(Color.rgb(255,255,255));
                                }
                            }
                        });
            }else {
                conf_pass.setError("Password doesn't match !");
            }
        }else {
            email.setError("Invalid Email!");
        }
    }


    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_right);
        fragmentTransaction.replace(parentframeLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    private void checkInputs() {
        if(!TextUtils.isEmpty(email.getText())){
            if(!TextUtils.isEmpty(name.getText())){
                if(!TextUtils.isEmpty(pass.getText()) && pass.length()>=8){
                    if(!TextUtils.isEmpty(conf_pass.getText())){
                        signupBtn.setEnabled(true);
                        signupBtn.setTextColor(Color.rgb(255,255,255));
                    }else{
                        signupBtn.setEnabled(false);
                        signupBtn.setTextColor(Color.argb(50,255,255,255));
                    }
                }else{
                    signupBtn.setEnabled(false);
                    signupBtn.setTextColor(Color.argb(50,255,255,255));
                }
            }else{
                signupBtn.setEnabled(false);
                signupBtn.setTextColor(Color.argb(50,255,255,255));
            }
        }else{
            signupBtn.setEnabled(false);
            signupBtn.setTextColor(Color.argb(50,255,255,255));
        }
    }



}