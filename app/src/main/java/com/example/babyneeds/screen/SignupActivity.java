package com.example.babyneeds.screen;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.babyneeds.R;
import com.example.babyneeds.model.BabyNeedsViewModel;
import com.example.babyneeds.model.User;
import com.example.babyneeds.utils.AESCrypt;
import com.example.babyneeds.utils.AppData;
import com.example.babyneeds.utils.SharedPref;
import com.google.android.material.snackbar.Snackbar;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {


    RelativeLayout parent;
    EditText etName;
    EditText etEmail;
    EditText etPassword;
    LinearLayout btnSignUp;
    TextView tvSignIn;

    private BabyNeedsViewModel babyNeedsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initViews();
        initVars();


    }

    private void initVars() {
        babyNeedsViewModel = new ViewModelProvider(this).get(BabyNeedsViewModel.class);
    }

    private void initViews() {

        parent = findViewById(R.id.rl_signup);
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnSignUp = findViewById(R.id.btn_sign_up);
        tvSignIn = findViewById(R.id.tv_sign_in);

        SpannableString mSpannableString = new SpannableString("Sign in");
        mSpannableString.setSpan(new UnderlineSpan(), 0, mSpannableString.length(), 0);
        tvSignIn.setText(mSpannableString);

        btnSignUp.setOnClickListener(this);
        tvSignIn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sign_up:
                if (isValidData()) {
                    String name = etName.getText().toString();
                    String email = etEmail.getText().toString();
                    String password = etPassword.getText().toString();

                    try {
                        String encryptPassword = AESCrypt.encrypt(password);

                        User user = new User(name, email, encryptPassword);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Boolean isUserExists = babyNeedsViewModel.isUserExist(email);
                                if(!isUserExists){
                                    babyNeedsViewModel.insert(user);
                                    AppData.user = user;
                                    SharedPref.saveUser(SignupActivity.this, user);
                                    startActivity(new Intent(SignupActivity.this, SignUpCompleteActivity.class));
                                    finishAffinity();
                                }else{
                                    Snackbar snackbar = Snackbar
                                            .make(parent, "Email already in use", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }
                            }
                        }).start();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Snackbar snackbar = Snackbar
                                .make(parent, "Sorry something went wrong!", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }

                }

                break;

            case R.id.tv_sign_in:
                startActivity(new Intent(SignupActivity.this, SignInActivity.class));
                finish();
                break;
            default:
                break;

        }
    }

    private boolean isValidData() {
        boolean valid = true;

        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (name.isEmpty()) {
            valid = false;
            etName.setError("Please fill the field");
        }
        if (email.isEmpty()) {
            valid = false;
            etEmail.setError("Please fill the field");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            valid = false;
            etEmail.setError("Please enter valid mail");
        }

        if (password.isEmpty()) {
            valid = false;
            etPassword.setError("Please fill the field");
        } else if (password.length() < 8) {
            valid = false;
            etPassword.setError("Must have at lest 8 characters");
        }

        return valid;
    }

}