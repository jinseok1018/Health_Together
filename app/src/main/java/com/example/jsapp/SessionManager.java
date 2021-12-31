package com.example.jsapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


import java.util.HashMap;

public class SessionManager {
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";

    // 변수명을 대문자로 작성해야함
    public static final String USERID = "userID";
    public static final String AGE = "Age";
    public static final String SEX = "Sex";
    public static final String GYM_NAME = "GymName";

    public SessionManager(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String userID, String Age, String Sex, String GymName){
        editor.putBoolean(LOGIN, true);
        editor.putString(USERID, userID);
        editor.putString(AGE, Age);
        editor.putString(SEX, Sex);
        editor.putString(GYM_NAME, GymName);
        editor.apply();
    }
    public void createSession(String GymName){
        editor.putBoolean(LOGIN, true);
        editor.putString(GYM_NAME, GymName);
        editor.apply();
    }



    public boolean isLoggin(){
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin(){
        if (!this.isLoggin()){
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);
            ((ListActivity) context).finish();
        }
    }

    public HashMap<String, String> getUserDetail()    {
        HashMap<String, String> user = new HashMap<>();
        user.put(USERID, sharedPreferences.getString(USERID, null));
        user.put(AGE, sharedPreferences.getString(AGE, null));
        user.put(SEX, sharedPreferences.getString(SEX, null));
        user.put(GYM_NAME, sharedPreferences.getString(GYM_NAME, null));

        return user;
    }

    public void logout(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        context.startActivity(i);
        ((ListActivity) context).finish();
    }

    public void editorClear(){
        editor.clear();
        editor.commit();
    }

}
