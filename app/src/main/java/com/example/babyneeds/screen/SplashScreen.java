package com.example.babyneeds.screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.example.babyneeds.R;
import com.example.babyneeds.model.BabyNeedsViewModel;
import com.example.babyneeds.model.User;
import com.example.babyneeds.utils.AppData;
import com.example.babyneeds.utils.SharedPref;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        AppData.user = SharedPref.getUser(this);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                if(AppData.user == null){
                    Intent intent = new Intent(SplashScreen.this, IntroActivity.class);
                    startActivity(intent);
                    finishAffinity();//finish all previous activities
                }else if (AppData.user.getMotherName() == null || AppData.user.getMotherName().isEmpty()){
                    Intent intent = new Intent(SplashScreen.this, SignUpCompleteActivity.class);
                    startActivity(intent);
                    finishAffinity();//finish all previous activities
                }else{
                    Intent intent = new Intent(SplashScreen.this, HomeActivity.class);
                    startActivity(intent);
                    finishAffinity();//finish all previous activities
                }
            }
        }, 2000);

    }
}