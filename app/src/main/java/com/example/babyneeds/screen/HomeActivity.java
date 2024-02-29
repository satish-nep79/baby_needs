package com.example.babyneeds.screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.babyneeds.R;
import com.example.babyneeds.fragments.HomeFragment;
import com.example.babyneeds.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeActivity extends AppCompatActivity {

    TextView tvTitle;
    BottomNavigationView bottomNav;
    FloatingActionButton favBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        listeners();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_view, new HomeFragment()).commit();
    }

    private void listeners() {

        //Bottom Navigation listener
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();
            if(itemId == R.id.home){
                selectedFragment = new HomeFragment();
                tvTitle.setText(getResources().getString(R.string.app_name));
                favBtn.setVisibility(View.VISIBLE);
            }else if (itemId == R.id.profile){
                selectedFragment = new ProfileFragment();
                tvTitle.setText("Profile");
                favBtn.setVisibility(View.GONE);
            }

            if(selectedFragment != null){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_view, selectedFragment).commit();
            }
            return true;
        });

        //Floating button listener
        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, AddItemActivity.class));
            }
        });

    }

    private void initViews() {
        tvTitle = findViewById(R.id.tv_home_activity_title);
        bottomNav = findViewById(R.id.bottom_bar);
        favBtn = findViewById(R.id.fav_add);
    }

}