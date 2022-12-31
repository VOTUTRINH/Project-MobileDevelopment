package com.example.oderingfood.models;

import android.util.Log;

import java.text.NumberFormat;
import java.time.LocalTime;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class GlobalVariables {
    public static String pathRestaurentID;
    public static String pathMenu;
    public static List<Food> menu;
    public static String pathTable;
    public static int priority;
    public static  int tablePriority;
    public static  String IDUser;
    public static  String DateBooking;

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
}
