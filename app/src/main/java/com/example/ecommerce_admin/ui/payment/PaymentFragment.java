package com.example.ecommerce_admin.ui.payment;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce_admin.R;
import com.example.ecommerce_admin.activities.WithdrawalActivity;
import com.example.ecommerce_admin.models.OrderItemModel;
import com.example.ecommerce_admin.ui.order.adapters.OrderItemAdpter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PaymentFragment extends Fragment {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private TextInputLayout textInputLayout;
    private AutoCompleteTextView autoCompleteTextView;
    private TextView totalSelling,totalProfit,dateRecord,test1;
    private ImageView fdBtn1,fdBtn2,bkBtn1,bkBtn2;
    private RecyclerView orderRecycler;
    private String todayDate;
//    public static String dateX;
    private List <String> dateList = new ArrayList<>();
    private List <String> sudoDateList = new ArrayList<>();
    private List<OrderItemModel> itemModelList = new ArrayList<>();
    private OrderItemAdpter itemAdpter ;
    private int listSize;
    public static boolean today;
    public static boolean bacward1;
    public static boolean bacward2;
    int index = 0;
    // 3rd layout
    private TextView myAcBalance,lastWdlDate;
    private Button withdrawBtn;

    List<Integer> sell7List = new ArrayList<>();
    List<Integer> profit7List = new ArrayList<>();
    List<Integer> sell30List = new ArrayList<>();
    List<Integer> profit30List = new ArrayList<>();
    public List<String> last7list = new ArrayList<>();

    public PaymentFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
//        spinner = view.findViewById(R.id.spinner_record);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();


        test1 = view.findViewById(R.id.test1);
        totalProfit = view.findViewById(R.id.total_profit);
        totalSelling = view.findViewById(R.id.total_selling_price);
        dateRecord = view.findViewById(R.id.date_record);
        fdBtn1 = view.findViewById(R.id.forward_btn1);
        fdBtn2 = view.findViewById(R.id.forward_btn2);
        bkBtn1 = view.findViewById(R.id.backward_btn1);
        bkBtn2 = view.findViewById(R.id.backward_btn2);
        orderRecycler = view.findViewById(R.id.order_recycler);
        autoCompleteTextView = view.findViewById(R.id.act_records);
        myAcBalance = view.findViewById(R.id.my_balance);
        lastWdlDate = view.findViewById(R.id.last_wdl_date);
        withdrawBtn = view.findViewById(R.id.withdrawal_btn);



        TodaysDate(dateRecord);
        //        ArrayList<String> arrayList = new ArrayList<>(R.array.record);
        String[] items = {"Today","Last week","Last month","Total"};
        ArrayAdapter<String> recordAdapter = new  ArrayAdapter<String>(getContext(),R.layout.le_spinner_item,items);
        autoCompleteTextView.setText(items[0]);
        //LoadOrder();

        fdBtn1.setEnabled(false);
        fdBtn1.setColorFilter(ContextCompat.getColor(getContext(), R.color.gray), android.graphics.PorterDuff.Mode.MULTIPLY);

        fdBtn2.setEnabled(false);
        fdBtn2.setColorFilter(ContextCompat.getColor(getContext(), R.color.gray), android.graphics.PorterDuff.Mode.MULTIPLY);

        autoCompleteTextView.setAdapter(recordAdapter);



        firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                .collection("SELLER_DATA").document("7_MY_PAYMENT")
                .collection("SELLES_REPORT").document("00_ACTIVE_DATES")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    listSize = Integer.parseInt(task.getResult().get("00_listSize").toString());

                    int m = listSize-1;
                    String lastDate = task.getResult().get("date_No_"+m).toString();
                    for (int i=0;i<listSize;i++){
                        String allDate = task.getResult().get("date_No_"+i).toString();
                        dateList.add(allDate);
                        sudoDateList.add(allDate);
                    }
                    if (!lastDate.equals(todayDate)){

                        totalProfit.setText("Rs. "+0+"/-");
                        totalSelling.setText("Rs. "+0+"/-");
                        today=false;
                    }else {
                        today=true;
                        sudoDateList.remove(listSize);
                    }

                    Last7DaysStat(7);
                    Last30DaysStat(30);

                }

            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        orderRecycler.setLayoutManager(layoutManager);

        itemAdpter = new OrderItemAdpter(itemModelList,4);
        orderRecycler.setAdapter(itemAdpter);
        itemAdpter.notifyDataSetChanged();


        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //use Calender to count date of last week and match thos date with Active_date in firebase
                switch (position){
                    case 0:
                        break;
                    case 1:
                        calculate1(sell7List,profit7List);
                        break;
                    case 2:
                        calculate1(sell30List,profit30List);
                        break;
                    case 3:
                        TotalSell();
                        break;
                }
            }
        });

        MyAccBallance();


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        fdBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Forward 1", Toast.LENGTH_SHORT).show();

            }
        });

        bkBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "backward 1", Toast.LENGTH_SHORT).show();

            }
        });



        bkBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bacward2 = true;
                fdBtn2.setEnabled(true);
                fdBtn2.setColorFilter(ContextCompat.getColor(getContext(), R.color.black), android.graphics.PorterDuff.Mode.MULTIPLY);

                index= index+1;
                String loadDate = "";
                if (index<5){

                    if(!today){
                        loadDate = dateList.get(listSize-index);
                    }else {
                        loadDate = sudoDateList.get(listSize-index);

                    }
                    LoadOrder(loadDate);
                } else {
                    Toast.makeText(getContext(), "only 5 days available", Toast.LENGTH_SHORT).show();
                    bkBtn2.setEnabled(false);
                    bkBtn2.setColorFilter(ContextCompat.getColor(getContext(), R.color.gray), android.graphics.PorterDuff.Mode.MULTIPLY);
                }





            }
        });
        fdBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bkBtn2.setEnabled(true);
                bkBtn2.setColorFilter(ContextCompat.getColor(getContext(), R.color.black), android.graphics.PorterDuff.Mode.MULTIPLY);

                index= index-1;
                String loadDate = "";
                if (bacward2){
                    if (!today) {
                        if (index==0){
                            itemModelList.clear();
                            itemAdpter.notifyDataSetChanged();
                            TodaysDate(dateRecord);

                        }else {
                            loadDate = dateList.get(listSize - index);
                            LoadOrder(loadDate);
                        }

                    } else {
                        loadDate = dateList.get(listSize - index);
                        LoadOrder(loadDate);
                    }

//                    Toast.makeText(getContext(), "only 4 days available", Toast.LENGTH_SHORT).show();

                }
                if (index == 0){
                    bacward2 =false;
                    fdBtn2.setEnabled(false);
                    fdBtn2.setColorFilter(ContextCompat.getColor(getContext(), R.color.gray), android.graphics.PorterDuff.Mode.MULTIPLY);

                }

            }
        });


        withdrawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(), "Withdrawal", Toast.LENGTH_SHORT).show();
                Intent withdrawIntent = new Intent( getActivity(), WithdrawalActivity.class);
                startActivity(withdrawIntent);
            }
        });

    }


    public void LoadOrder(String date){
        itemModelList.clear();


        firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                .collection("SELLER_DATA").document("7_MY_PAYMENT")
                .collection("SELLES_REPORT").document(date)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    dateRecord.setText(date);
                    int listSize = Integer.parseInt(task.getResult().get("listSize").toString());
//                    totalSelling.setText(task.getResult().get("todays_selling").toString());
//                    totalProfit.setText(task.getResult().get("todays_profit").toString());

//                    for (int i = 0;i<listSize;i++){
//                        String orderid = task.getResult().get("order_id_"+i).toString();
//                        itemModelList.add( new OrderItemModel("",orderid));
//                        itemAdpter.notifyDataSetChanged();
//                        //save it into list then pass to adapter
//
//                    }

                }

            }
        });
    }


    public void Last7DaysStat(int diff){



        firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                .collection("SELLER_DATA").document("7_MY_PAYMENT")
                .collection("SELLES_REPORT").document("00_ACTIVE_DATES")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (listSize>diff){
                        for (int i = diff ;i>=0;i--){ ///TODO changed method
                            String dates = task.getResult().get("date_No_"+i).toString();
                            last7list.add(dates);
                        }
                    }else {
                        Toast.makeText(getActivity(), "Not available", Toast.LENGTH_SHORT).show();
                        for (int i = 0;i<listSize;i++){
                            String dates = task.getResult().get("date_No_"+i).toString().trim();
                            last7list.add(dates);
                        }
                    }
                    getLast7Days(last7list,diff);

                }



            }
        });



    }
    private void getLast7Days(List<String> list,int diff){
        List<String> last7Days = new ArrayList<>();
        List<String> tempList = new ArrayList<>();
        StringBuilder appen1 = new StringBuilder();



        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        // get starting date
//        cal.add(Calendar.DAY_OF_YEAR, -6);
        cal.add(Calendar.DAY_OF_YEAR, -diff);

        // loop adding one day in each iteration
        for(int i = 0; i< diff; i++){
            cal.add(Calendar.DAY_OF_YEAR, 1);
            last7Days.add(sdf.format(cal.getTime()).trim());
        }
        for(String tmp1: last7Days) {
            for(String tmp2: list) {
                if(tmp1.contains(tmp2)) {
//                    correctCount++;
                    tempList.add(tmp2);

                } else {
//                    incorrectCount++;
                }
            }
        }

        if (tempList.size()!=0){
            for (int i = 0;i<tempList.size();i++){
                appen1.append(tempList.get(i)+"\n");
                int finalI = i;
                firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                        .collection("SELLER_DATA").document("7_MY_PAYMENT")
                        .collection("SELLES_REPORT").document(tempList.get(i))
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            int sellitem = Math.toIntExact(task.getResult().getLong("todays_selling"));
                            int profititem = Math.toIntExact(task.getResult().getLong("todays_profit"));

                            sell7List.add(sellitem);
                            profit7List.add(profititem);

                        }


                    }
                });
            }

        }

        //test1.setText(appen1.toString());

    }



    public void Last30DaysStat(int diff){
        List<String> list1 = new ArrayList<>();


        firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                .collection("SELLER_DATA").document("7_MY_PAYMENT")
                .collection("SELLES_REPORT").document("00_ACTIVE_DATES")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (listSize>diff){
                        for (int i = 0;i<diff;i++){
                            String dates = task.getResult().get("date_No_"+i).toString();
                            list1.add(dates);
                        }
                    }else {
                        Toast.makeText(getActivity(), "Not available", Toast.LENGTH_SHORT).show();
                        for (int i = 0;i<listSize;i++){
                            String dates = task.getResult().get("date_No_"+i).toString().trim();
                            list1.add(dates);
                        }
                    }
                    getLast30Days(list1,diff);

                }



            }
        });



    }
    private void getLast30Days(List<String> list,int diff){
        List<String> last7Days = new ArrayList<>();
        List<String> tempList = new ArrayList<>();
        StringBuilder appen1 = new StringBuilder();



        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        // get starting date
//        cal.add(Calendar.DAY_OF_YEAR, -6);
        cal.add(Calendar.DAY_OF_YEAR, -diff);

        // loop adding one day in each iteration
        for(int i = 0; i< diff; i++){
            cal.add(Calendar.DAY_OF_YEAR, 1);
            last7Days.add(sdf.format(cal.getTime()).trim());
        }
        for(String tmp1: last7Days) {
            for(String tmp2: list) {
                if(tmp1.contains(tmp2)) {
//                    correctCount++;
                    tempList.add(tmp2);


                } else {
//                    incorrectCount++;
                }
            }
        }

        if (tempList.size()!=0){
            for (int i = 0;i<tempList.size();i++){
                appen1.append(tempList.get(i)+"\n");
                int finalI = i;
                firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                        .collection("SELLER_DATA").document("7_MY_PAYMENT")
                        .collection("SELLES_REPORT").document(tempList.get(i))
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            int sellitem = Math.toIntExact(task.getResult().getLong("todays_selling"));
                            int profititem = Math.toIntExact(task.getResult().getLong("todays_profit"));

                            sell30List.add(sellitem);
                            profit30List.add(profititem);
                            //test1.setText(sell30List.size()+"");

                        }


                    }
                });
            }



        }
//        test1.setText(appen1.toString());

    }

    public void calculate1(List<Integer> sellList,List<Integer> profitList ){
        int sellCounter = 0;
        int profitCounter = 0;

        if (sellList.size()!=0 ){
            for (int i=0;i<sellList.size();i++){
                sellCounter = sellCounter +sellList.get(i);
                profitCounter = profitCounter + profitList.get(i);
            }

            totalSelling.setText("Rs. "+ sellCounter +"/-");
            totalProfit.setText("Rs. "+ profitCounter +"/-");
        }else {
            Toast.makeText(getActivity(), "fuck off", Toast.LENGTH_SHORT).show();
            totalSelling.setText("Rs. "+ 0 +"/-");
            totalProfit.setText("Rs. "+ 0 +"/-");
        }

    }

    private void TotalSell(){

        firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                .collection("SELLER_DATA").document("7_MY_PAYMENT")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    totalSelling.setText(task.getResult().get("total_selling").toString());
                    totalProfit.setText(task.getResult().get("total_profit").toString());


                }

            }
        });
    }


    public void TodaysDate(TextView view){

        Date today = Calendar.getInstance().getTime();//getting date
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");//formating according to my need
        todayDate = formatter.format(today);
        view .setText(todayDate);
    }

    private void MyAccBallance(){

        firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                .collection("SELLER_DATA").document("7_MY_PAYMENT")
                .collection("EARNING_DETAILS").document("ACCOUNT_BALANCE")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    int mainBallance = Math.toIntExact(task.getResult().getLong("account_balance"));
                    myAcBalance.setText("Rs. "+mainBallance+"/-");
                }
            }
        });

        lastWdlDate.setText(getCalculatedDate("",-3)+"\n"+getCalculatedDate("",-7));

//        for (String temp: last7list){ // todo = this method not working
//            if (temp.contains(getCalculatedDate("",-3).trim())){
//                Toast.makeText(getActivity(), "in 3 days", Toast.LENGTH_SHORT).show();
//            }
////            else if (temp.contains(getCalculatedDate("",-7).trim())){
////                Toast.makeText(getActivity(), "in 7 days", Toast.LENGTH_SHORT).show();
////            }
//
////
//        }



    }


    public static Date getDateWithOffset(int offset, Date date){
        Calendar calendar = calendar = Calendar.getInstance();;
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, offset);
        return calendar.getTime();
    }

//    Date weekAgoDate = getDateWithOffset(-7, new Date());

    public static String getCalculatedDate(String date, int days) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy");
        if (!date.isEmpty()) {
            try {
                cal.setTime(s.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        cal.add(Calendar.DAY_OF_YEAR, days);
        return s.format(new Date(cal.getTimeInMillis()));
    }
}