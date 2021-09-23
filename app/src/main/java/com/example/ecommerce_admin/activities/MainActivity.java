package com.example.ecommerce_admin.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ecommerce_admin.R;
import com.example.ecommerce_admin.fragments.UiDashbordFragment;
import com.example.ecommerce_admin.ui.order.HomeFragment;
import com.example.ecommerce_admin.ui.payment.PaymentFragment;
import com.example.ecommerce_admin.ui.products.DashboardFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private FirebaseUser currentUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private int new_Notification ;
    private BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Admin");

        firebaseFirestore = FirebaseFirestore.getInstance();



        navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_uidashbord).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(MainActivity.this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        loadFragment(new UiDashbordFragment());


        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("SELLER_DATA").document("NOTIFICATION")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    new_Notification = Integer.parseInt(task.getResult().get("new_listSize").toString());
                    BadgeDrawable orderBadge = navView.getOrCreateBadge(R.id.navigation_notifications);
                    orderBadge.setBackgroundColor(Color.BLUE);
                    orderBadge.setBadgeTextColor(Color.WHITE);
                    orderBadge.setMaxCharacterCount(3);
                    orderBadge.setNumber(new_Notification);
                    orderBadge.setVisible(true);
                }

            }
        });



        //binding.navView.setOnNavigationItemSelectedListener();
        navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch(item.getItemId()){
                    case R.id.navigation_uidashbord:
                        fragment = new UiDashbordFragment();
                        break;
                    case R.id.navigation_home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.navigation_dashboard:
                        fragment = new DashboardFragment();
                        break;
                    case R.id.navigation_payment:
                        fragment = new PaymentFragment();
                        break;
//                    case R.id.navigation_notifications:
//                        BadgeDrawable badgeDrawable = navView.getBadge(R.id.navigation_notifications);
//                        Dbquery.ClearNotification(badgeDrawable);
////                        badgeDrawable.clearNumber();
////                        badgeDrawable.setVisible(true);
//                        navController.navigate(R.id.navigation_notifications);
//
//                        break;
                }
                return loadFragment(fragment);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.topbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
        //return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.topbar_search) {
            Toast.makeText(this, "search", Toast.LENGTH_SHORT).show();
//            Intent searchIntent = new Intent(this,SearchActivity.class);
//            startActivity(searchIntent);
            return true;
        } else if (id == R.id.topbar_profile) {
            if (currentUser == null) {
                //SignInUpDialog();
                Toast.makeText(this, "Not login", Toast.LENGTH_SHORT).show();
            } else {
                Intent i = new Intent(MainActivity.this, ProfileMenuActivity.class);
                startActivity(i);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean loadFragment(Fragment fragment){
        if (fragment!=null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_main,fragment)
                    .addToBackStack(null).commit();
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        if (navView.getSelectedItemId() == R.id.navigation_uidashbord){
            super.onBackPressed();
            finish();
        }else{
            navView.setSelectedItemId(R.id.navigation_uidashbord);
        }
        super.onBackPressed();
    }
}