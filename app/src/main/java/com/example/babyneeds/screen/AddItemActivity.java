package com.example.babyneeds.screen;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.babyneeds.R;
import com.example.babyneeds.model.BabyNeedsViewModel;
import com.example.babyneeds.model.Item;
import com.example.babyneeds.utils.AppData;
import com.example.babyneeds.utils.ImageHelper;
import com.example.babyneeds.utils.SharedPref;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Objects;

public class AddItemActivity extends AppCompatActivity implements View.OnClickListener {


    ScrollView parent;
    ImageView ivBack;
    EditText etName;
    EditText etDescription;
    EditText etPrice;
    LinearLayout llImage;
    CardView cvImage;
    ImageView ivImage;
    LinearLayout llAttach;
    LinearLayout btnSave;

    private BabyNeedsViewModel babyNeedsViewModel;
    private Bitmap bitmap;
    private Item loadedItem;

    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        initViews();
        initVars();
        listeners();
        handelIntent();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_NORMAL);
            mAccel = 10f;
            mAccelCurrent = SensorManager.GRAVITY_EARTH;
            mAccelLast = SensorManager.GRAVITY_EARTH;
        }

    }

    private final SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            if (mAccel > 12) {
                etName.setText("");
                etPrice.setText("");
                etDescription.setText("");
                ivImage.setImageBitmap(null);
                cvImage.setVisibility(View.GONE);
                llAttach.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private void handelIntent() {
        Intent intent = getIntent();
        if(intent.getExtras() !=null && intent.getExtras().getString("Item") != null){
            ((TextView)findViewById(R.id.title)).setText("Edit Item");
            loadedItem = new Gson().fromJson(intent.getExtras().getString("Item"), Item.class);
            setItemData();
        }
    }

    private void setItemData() {
        etName.setText(loadedItem.getItemName());
        etPrice.setText(String.valueOf(loadedItem.getItemPrice()));
        bitmap = ImageHelper.getImage(loadedItem.getImage());
        ivImage.setImageBitmap(bitmap);
        cvImage.setVisibility(View.VISIBLE);
        llAttach.setVisibility(View.GONE);
        etDescription.setText(loadedItem.getItemDesc());
    }

    private void listeners() {
        btnSave.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        llImage.setOnClickListener(this);

    }

    private void initVars() {
        babyNeedsViewModel = new ViewModelProvider(this).get(BabyNeedsViewModel.class);
    }

    private void initViews() {
        parent = findViewById(R.id.sv_add_item);
        ivBack = findViewById(R.id.iv_back);
        etName = findViewById(R.id.et_item_name);
        etDescription = findViewById(R.id.et_item_desc);
        etPrice = findViewById(R.id.et_item_price);
        llImage = findViewById(R.id.ll_image);
        cvImage = findViewById(R.id.cv_image);
        ivImage = findViewById(R.id.iv_item_image);
        llAttach = findViewById(R.id.ll_attach_image);
        btnSave = findViewById(R.id.btn_save);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_image:
                ImagePicker.with(this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .createIntent(intent -> {
                            resultLauncher.launch(intent);
                            return null;
                        });
                break;
            case R.id.btn_save:
                if(isValidData()){
                    String itemName = etName.getText().toString();
                    String itemDesc = etDescription.getText().toString();
                    Double itemPrice = Double.parseDouble(etPrice.getText().toString());
                    String encodedImage = ImageHelper.getStringImage(bitmap);

                    if(loadedItem != null){
                        System.out.println("DASDASDASDASDASDASD");
                        loadedItem.setItemName(itemName);
                        loadedItem.setItemDesc(itemDesc);
                        loadedItem.setItemPrice(itemPrice);
                        loadedItem.setImage(encodedImage);
                        babyNeedsViewModel.update(loadedItem);
                        finish();
                    }else{
                        Item i = new Item(itemName, itemDesc, itemPrice, encodedImage, AppData.user.getId());
                        babyNeedsViewModel.insert(i);
                        finish();
                    }
                }
                break;
        }
    }

    private boolean isValidData() {
        boolean valid = true;

        String itemName = etName.getText().toString();
        String itemDesc = etDescription.getText().toString();
        String itemPrice = etPrice.getText().toString();

        if(itemName.isEmpty()){
            valid =false;
            etName.setError("Please fill the field");
        }

        if(itemDesc.isEmpty()){
            valid =false;
            etDescription.setError("Please fill the field");
        }

        if(itemPrice.isEmpty()){
            valid =false;
            etPrice.setError("Please fill the field");
        }

        if(bitmap == null){
            valid = false;
            llImage.setBackground(getResources().getDrawable(R.drawable.drb_dotted_error));
        }

        return valid;

    }

    private ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Log.e("GotData", "onActivityResult: ");
                        Intent data = result.getData();

                        Uri fileUri = data.getData();
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUri);
                            ivImage.setImageBitmap(bitmap);
                            cvImage.setVisibility(View.VISIBLE);
                            llAttach.setVisibility(View.GONE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
//                        ivProfile.setImageURI(fileUri);

                    }
                }
            }
    );

}