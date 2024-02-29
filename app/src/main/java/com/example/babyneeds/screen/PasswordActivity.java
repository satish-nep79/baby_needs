package com.example.babyneeds.screen;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.babyneeds.R;
import com.example.babyneeds.model.BabyNeedsViewModel;
import com.example.babyneeds.model.User;
import com.example.babyneeds.utils.AESCrypt;
import com.example.babyneeds.utils.AppData;
import com.example.babyneeds.utils.Helper;
import com.example.babyneeds.utils.SharedPref;
import com.google.android.material.snackbar.Snackbar;

public class PasswordActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout parent;
    ImageView ivBack;
    EditText etOldPwd;
    EditText etNewPwd;
    EditText etConfirmPwd;
    LinearLayout btnChange;

    private BabyNeedsViewModel babyNeedsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        intViews();
        initVars();
        listeners();
    }

    private void listeners() {
        ivBack.setOnClickListener(this);
        btnChange.setOnClickListener(this);
    }

    private void initVars() {
        babyNeedsViewModel = new ViewModelProvider(this).get(BabyNeedsViewModel.class);
    }

    private void intViews() {
        parent = findViewById(R.id.ll_password);
        ivBack = findViewById(R.id.iv_back);
        etOldPwd = findViewById(R.id.et_old_password);
        etNewPwd = findViewById(R.id.et_new_password);
        etConfirmPwd = findViewById(R.id.et_confirm_password);
        btnChange = findViewById(R.id.btn_save_password);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_save_password:
                if(isValidData()){
                    etOldPwd.setError(null);
                    etNewPwd.setError(null);
                    etConfirmPwd.setError(null);

                    String oldPwd = etOldPwd.getText().toString();
                    String newPwd = etNewPwd.getText().toString();

                    try {
                        String password = AESCrypt.decrypt(AppData.user.getPassword());

                        if(password.equals(oldPwd)){
                            User user = AppData.user;
                            String encryptPwd = AESCrypt.encrypt(newPwd);
                            user.setPassword(encryptPwd);
                            babyNeedsViewModel.update(user);
                            SharedPref.saveUser(PasswordActivity.this, user);
                            AppData.user = user;
                            finish();
                        }else{
                            Snackbar snackbar = Snackbar
                                    .make(parent, "Sorry password does not match", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Snackbar snackbar = Snackbar
                                .make(parent, "Sorry something went wrong!", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }

                }
                break;
        }
    }

    private boolean isValidData() {
        boolean valid = true;

        String oldPwd = etOldPwd.getText().toString();
        String newPwd = etNewPwd.getText().toString();
        String confirmPwd = etConfirmPwd.getText().toString();

        if (oldPwd.isEmpty()) {
            valid = false;
            etOldPwd.setError("Please fill the field");
        }
        if (newPwd.isEmpty()) {
            valid = false;
            etNewPwd.setError("Please fill the field");
        } else if (newPwd.length() < 8) {
            valid = false;
            etNewPwd.setError("Must have 8 characters");
        }
        if (confirmPwd.isEmpty()) {
            valid = false;
            etConfirmPwd.setError("Please fill the field");
        } else if (newPwd.length() < 8) {
            valid = false;
            etConfirmPwd.setError("Must have 8 characters");
        }

        if(!confirmPwd.equals(newPwd)){
            valid = false;
            etNewPwd.setError("Password does not match");
            etConfirmPwd.setError("Password does not match");
        }

        return valid;
    }

}