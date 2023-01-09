package com.example.oderingfood.models;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.oderingfood.models.Account;

public class SessionManagement {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String SHARED_PREF_NAME="session";
    String SESSION_KEY="session_user";

    public SessionManagement(Context context){
        sharedPreferences=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }

    public void saveSession(Account account){
        String id= account.getId();
        editor.putString(SESSION_KEY,id).commit();
    }

    public String getSession(){
        return  sharedPreferences.getString(SESSION_KEY,"emptyString");
    }

    public void removeSession(){
        editor.putString(SESSION_KEY,"emptyString").commit();
    }
}
