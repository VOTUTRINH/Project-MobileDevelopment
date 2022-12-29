package com.example.oderingfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.example.oderingfood.models.GlobalVariables;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Bottomnavigation extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();

    ChatActivity chatActivity = new ChatActivity();
    Toolbar toolbar;

    String user, idRes;

    FirebaseDatabase database;
    DatabaseReference myRef;
    String role;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottomnavigation);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("restaurant/" + idRes);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            user = bundle.getString("user");
            idRes = bundle.getString("restaurant");
            role = bundle.getString("role");
            GlobalVariables.pathRestaurentID = idRes;
        }

        bottomNavigationView=findViewById(R.id.buttom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        switch (role)
        {
            case "KhachHang":
//                bottomNavigationView.setVisibility(View.INVISIBLE);
                menu.getItem(2).setVisible(false);
                menu.getItem(3).setVisible(false);
                break;
            case "NhanVien":
                menu.getItem(3).setVisible(false);
                break;
            case "ChuQuan":
                break;
            default:
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:

                        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
                        return true;
                    case R.id.booking:
                        BookingFragment bookingFragment = new BookingFragment();
                        bookingFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, bookingFragment).commit();
                        return true;
                    case R.id.order:
                        TablesActivity orderFragment = new TablesActivity();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, orderFragment).commit();
                        return true;
                    case R.id.employee:
                        EmployeeManageActivity employeeManagerFragment = new EmployeeManageActivity();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, employeeManagerFragment).commit();
                        return true;

                    case R.id.menu:
                        TuyChon_Fragment tuyChon_fragment = new TuyChon_Fragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, tuyChon_fragment).commit();
                        return true;
                }

                return false;
            }
        });
        toolbar = findViewById(R.id.bna_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!role.equals("KhachHang"))
        {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.toolbar_menu, menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.notification: {
                FragmentNotification noticeFragment = new FragmentNotification();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, noticeFragment).commit();
                break;
            }
            case R.id.message: {
                Intent intent = new Intent(bottomNavigationView.getContext(), ChatActivity.class);
                intent.putExtra("idRes", idRes);
                intent.putExtra("user", user);
                intent.putExtra("role", role);
                startActivity(intent);
                break;
            }
            case R.id.camera: {

                Intent intent = new Intent(bottomNavigationView.getContext(), ScanQRCode.class);
                Bundle bundle = new Bundle();
                bundle.putString("idRes", idRes);
                bundle.putString("idUser", user);
                bundle.putString("role",role);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    public String getUser() {
        return user;
    }

    public String getIdRes() {
        return idRes;
    }

    public String getRole() {
        return role;
    }
}
