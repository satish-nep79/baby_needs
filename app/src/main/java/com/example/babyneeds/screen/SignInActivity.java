package com.example.babyneeds.screen;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.babyneeds.R;
import com.example.babyneeds.model.BabyNeedsViewModel;
import com.example.babyneeds.model.User;
import com.example.babyneeds.utils.AESCrypt;
import com.example.babyneeds.utils.AppData;
import com.example.babyneeds.utils.SharedPref;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {


    RelativeLayout parent;
    LinearLayout btnSignIn;
    TextView tvSignup;
    EditText etEmail;
    EditText etPassword;

    private BabyNeedsViewModel babyNeedsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initViews();
        initVars();
    }

    private void initVars() {
        babyNeedsViewModel = new ViewModelProvider(this).get(BabyNeedsViewModel.class);
    }

    private void initViews() {

        parent = findViewById(R.id.rl_signIn);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnSignIn = findViewById(R.id.btn_sign_in);
        tvSignup = findViewById(R.id.tv_sign_up);

        SpannableString mSpannableString = new SpannableString("Sign up");
        mSpannableString.setSpan(new UnderlineSpan(), 0, mSpannableString.length(), 0);
        tvSignup.setText(mSpannableString);

        btnSignIn.setOnClickListener(this);
        tvSignup.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sign_in:
                if(isValidData()){
                    String email = etEmail.getText().toString();
                    String password = etPassword.getText().toString();

                    babyNeedsViewModel.getUser(email).observe(this, new Observer<User>() {
                        @Override
                        public void onChanged(User user) {
                            if(user == null){
                                Snackbar snackbar = Snackbar
                                        .make(parent, "Sorry user does not exist for this email", Snackbar.LENGTH_LONG);
                                snackbar.show();
                                return;
                            }

                            try {
                                String decryptPwd = AESCrypt.decrypt(user.getPassword());
                                if(decryptPwd.equals(password)){
                                    SharedPref.saveUser(SignInActivity.this, user);
                                    AppData.user = user;
                                    if(user.getMotherName() == null || user.getMotherName().isEmpty()){
                                        startActivity(new Intent(SignInActivity.this, SignUpCompleteActivity.class));
                                        finishAffinity();
                                    }else{
                                        startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                                        finishAffinity();
                                    }
                                }else{
                                    Snackbar snackbar = Snackbar
                                            .make(parent, "Sorry user does not exist for this email", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Snackbar snackbar = Snackbar
                                        .make(parent, "Sorry something went wrong!", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }

                        }
                    });

                }
                break;

            case R.id.tv_sign_up:
                startActivity(new Intent(SignInActivity.this, SignupActivity.class));
                finish();
                break;
            default:
                break;

        }
    }

    private boolean isValidData() {
        boolean valid = true;

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

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
        }

        return valid;
    }
}