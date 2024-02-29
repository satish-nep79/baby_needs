package com.example.babyneeds.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.babyneeds.model.User;
import com.google.gson.Gson;

public class SharedPref {
    private static final String USER = "USER";

    public static void saveUser(Context context, User user){

        SharedPreferences mSharedPref = context.getSharedPreferences(USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString("User", new Gson().toJson(user));
        editor.apply();
    }

    public static User getUser(Context context){

        SharedPreferences mSharedPref = context.getSharedPreferences(USER, Context.MODE_PRIVATE);
        String userJson = mSharedPref.getString("User", "");
        if(userJson.isEmpty()){
            return null;
        }else{
            User user = new Gson().fromJson(userJson, User.class);
            return user;
        }
    }

    public static void deleteUser(Context context){
        SharedPreferences mSharedPref = context.getSharedPreferences(USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString("User", "");
        editor.apply();

    }

}
