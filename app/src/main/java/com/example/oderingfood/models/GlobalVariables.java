package com.example.oderingfood.models;

import java.text.NumberFormat;
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

    static public String displayCurrency(Double a) {
        Locale currentLocale = new Locale("vi", "VN");
        Double currencyAmount = a;
        Currency currentCurrency = Currency.getInstance(currentLocale);
        NumberFormat currencyFormatter =
                NumberFormat.getCurrencyInstance(currentLocale);

                return currencyFormatter.format(currencyAmount);
    }
}
