package com.example.oderingfood.models;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.oderingfood.FcmNotificationsSender;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.time.LocalTime;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class GlobalVariables {
    public static String pathRestaurentID;
    public static String pathMenu;
    public static String pathTable;
    public static int priority;
    public static  int tablePriority;
    public static  String IDUser;
    public static  String DateBooking;
    private static String result = "";
    static public String displayCurrency(Double a) {
        Locale currentLocale = new Locale("vi", "VN");
        Double currencyAmount = a;
        Currency currentCurrency = Currency.getInstance(currentLocale);
        NumberFormat currencyFormatter =
                NumberFormat.getCurrencyInstance(currentLocale);

                return currencyFormatter.format(currencyAmount);
    }

    static  public boolean isDuplicateTime(String s1, String s2, String x1, String x2){
        LocalTime t1 = LocalTime.parse(s1);
        LocalTime t2 = LocalTime.parse(s2);
        LocalTime v1 = LocalTime.parse(x1);
        v1 = v1.minusMinutes(30);
        LocalTime v2 = LocalTime.parse(x2);
        Log.i("Time", t1.toString());
        Log.i("Time", t2.toString());

        Log.i("Time", v1.toString());

        Log.i("Time", v2.toString());

        if (t1.compareTo(v1) >= 0 && t1.compareTo(v2) <=0){
            return true;
        }
        if (t2.compareTo(v1) >= 0 && t2.compareTo(v2) <= 0){

            return true;
        }
        if (v1.compareTo(t1) >= 0 && v1.compareTo(t2) <= 0){

            return true;
        }

        return false;
    }

    public static void SendNotificationToOther(Context context, String idUser, String label, String content){

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference refUser = database.getReference("user/" + idUser);
            refUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String token = snapshot.child("Token").getValue(String.class);
                    FcmNotificationsSender notificationsSender = new FcmNotificationsSender(token,
                            label, content, context.getApplicationContext(), (Activity)context);
                    notificationsSender.SendNotifications();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


    }

    public static void SendNotificationToEmployee(Context context, String idRes, String label, String content){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refUser = database.getReference("/restaurant/" + idRes + "/NhanVien");
        refUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot nhanvienSnapshot : snapshot.getChildren()) {
                    String id = nhanvienSnapshot.getKey();
                    SendNotificationToOther(context, id,label, content);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
