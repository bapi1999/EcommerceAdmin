package com.example.ecommerce_admin.adapters;

import android.icu.text.SimpleDateFormat;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce_admin.R;
import com.example.ecommerce_admin.models.OrderItemModel;
import com.example.ecommerce_admin.ui.order.adapters.OrderItemAdpter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PaymentHistoryAdapter  extends RecyclerView.Adapter<PaymentHistoryAdapter.ViewHolder> {
    List<String> list = new ArrayList<>();
    public FirebaseFirestore firebaseFirestore ;
    private FirebaseAuth firebaseAuth;

    public PaymentHistoryAdapter(List<String> list) {
        this.list = list;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public PaymentHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.le_payment_histor_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentHistoryAdapter.ViewHolder holder, int position) {
        String paymentID = list.get(position).toString();
        holder.setData(paymentID);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTime,paidAmount,payingAgent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTime = itemView.findViewById(R.id.date_time);
            paidAmount = itemView.findViewById(R.id.paid_amount);
            payingAgent = itemView.findViewById(R.id.paying_agent);
        }
        public void setData(String paymentId){
            firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                    .collection("SELLER_DATA").document("7_MY_PAYMENT")
                    .collection("EARNING_HISTORY").document(paymentId)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        Timestamp time = task.getResult().getTimestamp("time");
//                        long timeStamp  = Long.parseLong(time);
                        dateTime.setText(getDate3(time.toDate()));
                        paidAmount.setText(task.getResult().get("paid_amount").toString());
                        payingAgent.setText(task.getResult().get("paying_agent").toString());
                    }
                }
            });

        }
        private String getDate3(Date date) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy   HH:mm a");//formating according to my need
            String myFormat = "dd/MM/yy";

             String todayDate = formatter.format(date);
             return todayDate;

        }
    }
}
