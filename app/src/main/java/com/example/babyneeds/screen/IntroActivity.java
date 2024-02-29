package com.example.babyneeds.screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.babyneeds.R;

public class IntroActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        LinearLayout signInButton = findViewById(R.id.btn_sign_in);
        LinearLayout signUpButton = findViewById(R.id.btn_sign_up);
        signInButton.setOnClickListener(this);
        signUpButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_sign_in:
                startActivity(new Intent(IntroActivity.this, SignInActivity.class));
                break;

            case R.id.btn_sign_up:
                startActivity(new Intent(IntroActivity.this, SignupActivity.class));
                break;
            default:
                break;

        }
    }
}