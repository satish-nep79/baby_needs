package com.example.babyneeds.screen;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.babyneeds.R;
import com.example.babyneeds.model.BabyNeedsViewModel;
import com.example.babyneeds.model.User;
import com.example.babyneeds.utils.AppData;
import com.example.babyneeds.utils.SharedPref;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

public class SignUpCompleteActivity extends AppCompatActivity implements View.OnClickListener {


    private RelativeLayout parent;
    private EditText etName;
    private TextView tvDob;
    private TextView tvDeliveryDate;
    private User user;

    private BabyNeedsViewModel babyNeedsViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_complete);

        initViews();
        initVars();

    }

    private void initVars() {
        user = new User(AppData.user.getName(), AppData.user.getEmail(), AppData.user.getPassword());
        babyNeedsViewModel = new ViewModelProvider(this).get(BabyNeedsViewModel.class);
    }

    private void initViews() {
        parent = findViewById(R.id.rl_signup_complete);
        etName = findViewById(R.id.et_mother_name);
        tvDob = findViewById(R.id.tv_dob);
        tvDeliveryDate = findViewById(R.id.tv_delivery);

        findViewById(R.id.ll_dob).setOnClickListener(this);
        findViewById(R.id.ll_delivery).setOnClickListener(this);
        findViewById(R.id.btn_continue).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_dob:
                Log.e("Clicked", "onClick: ");
                Calendar calendar = Calendar.getInstance();
                new DatePickerDialog(SignUpCompleteActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        user.setMotherDob(year + "/" + month + "/" + day);
                        tvDob.setText(user.getMotherDob());
                        tvDob.setTextColor(getResources().getColor(R.color.white));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.ll_delivery:
                calendar = Calendar.getInstance();
                new DatePickerDialog(SignUpCompleteActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        user.setDeliveryDate(year + "/" + month + "/" + day);
                        tvDeliveryDate.setText(user.getDeliveryDate());
                        tvDeliveryDate.setTextColor(getResources().getColor(R.color.white));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.btn_continue:
                saveData();
                break;
        }
    }

    private void saveData() {
        if (isValidData()) {
            String name = etName.getText().toString();
            user.setMotherName(name);

            Observer observer = new Observer<User>() {
                @Override
                public void onChanged(User u) {
                    babyNeedsViewModel.getUser(user.getEmail()).removeObserver(this);
                    if (u == null) {
                        Snackbar snackbar = Snackbar
                                .make(parent, "Sorry something went wrong!", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        return;
                    }
                    if(u.getEmail().equals(user.getEmail())){
                        if(u.getMotherName() != null) return;
                        user.setId(u.getId());
                        babyNeedsViewModel.update(user);
                        AppData.user = user;
                        SharedPref.saveUser(SignUpCompleteActivity.this, AppData.user);
                        startActivity(new Intent(SignUpCompleteActivity.this, HomeActivity.class));
                        finishAffinity();
                    }else{
                        Snackbar snackbar = Snackbar
                                .make(parent, "Sorry something went wrong", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }

                }
            };

            babyNeedsViewModel.getUser(user.getEmail()).observe(this, observer);
        }
    }

    private boolean isValidData() {
        boolean valid = true;

        String name = etName.getText().toString();

        if (name.isEmpty()) {
            valid = false;
            etName.setError("Please fill the field");
        }

        if (user.getMotherDob() == null || user.getDeliveryDate() == null) {
            valid = false;
            Snackbar snackbar = Snackbar
                    .make(parent, "Please fill all fields", Snackbar.LENGTH_LONG);
            snackbar.show();
        }

        return valid;
    }

}