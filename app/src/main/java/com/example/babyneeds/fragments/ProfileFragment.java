package com.example.babyneeds.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.babyneeds.R;
import com.example.babyneeds.model.BabyNeedsViewModel;
import com.example.babyneeds.screen.IntroActivity;
import com.example.babyneeds.screen.PasswordActivity;
import com.example.babyneeds.utils.AppData;
import com.example.babyneeds.utils.Helper;
import com.example.babyneeds.utils.ImageHelper;
import com.example.babyneeds.utils.SharedPref;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    View rootView;
    ScrollView parent;
    CardView cvProfile;
    ImageView ivProfile;
    TextView tvName;
    TextView tvEmail;
    TextView tvTotalItems;
    TextView tvPurchasedItems;
    TextView tvMotherName;
    TextView tvDob;
    TextView tvDueDate;
    LinearLayout btnPassword;
    LinearLayout btnLogout;

    private BabyNeedsViewModel babyNeedsViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        babyNeedsViewModel = new ViewModelProvider(this).get(BabyNeedsViewModel.class);

        intViews();
        intiListeners();
        setData();

        return rootView;
    }

    private void setData() {
        if (AppData.user.getImage() != null) {
            Bitmap bitmap = ImageHelper.getImage(AppData.user.getImage());
            ivProfile.setImageBitmap(bitmap);
        }
        tvName.setText(Helper.capitalize(AppData.user.getName()));
        tvEmail.setText(AppData.user.getEmail());
        tvMotherName.setText(Helper.capitalize(AppData.user.getMotherName()));
        tvDob.setText("(" + AppData.user.getMotherDob() + ")");
        tvDueDate.setText("Due Date: - " + AppData.user.getDeliveryDate());
        setUserItems();
        setPurchasedItems();
    }

    private void setUserItems() {
        Observer observer = new Observer<Integer>() {
            @Override
            public void onChanged(Integer count) {
                babyNeedsViewModel.countUserItems(AppData.user.getId()).removeObserver(this);
                tvTotalItems.setText(count.toString());
            }
        };

        babyNeedsViewModel.countUserItems(AppData.user.getId()).observe(getActivity(), observer);
    }

    private void setPurchasedItems() {
        Observer observer = new Observer<Integer>() {
            @Override
            public void onChanged(Integer count) {
                babyNeedsViewModel.countPurchasedItems(AppData.user.getId()).removeObserver(this);
                tvPurchasedItems.setText(count.toString());
            }
        };

        babyNeedsViewModel.countPurchasedItems(AppData.user.getId()).observe(getActivity(), observer);
    }

    private void intiListeners() {
        cvProfile.setOnClickListener(this);
        btnPassword.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
    }

    private void intViews() {
        parent = rootView.findViewById(R.id.sv_parent);
        cvProfile = rootView.findViewById(R.id.cv_profile);
        ivProfile = rootView.findViewById(R.id.iv_profile);
        tvName = rootView.findViewById(R.id.tv_name);
        tvEmail = rootView.findViewById(R.id.tv_email);
        tvTotalItems = rootView.findViewById(R.id.tv_total_items);
        tvPurchasedItems = rootView.findViewById(R.id.tv_purchased_items);
        tvMotherName = rootView.findViewById(R.id.tv_mother_name);
        tvDob = rootView.findViewById(R.id.tv_mother_dob);
        tvDueDate = rootView.findViewById(R.id.tv_due_date);
        btnPassword = rootView.findViewById(R.id.btn_password);
        btnLogout = rootView.findViewById(R.id.btn_logout);
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
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), fileUri);
                            String encodedImage = ImageHelper.getStringImage(bitmap);
                            AppData.user.setImage(encodedImage);
                            babyNeedsViewModel.update(AppData.user);
                            SharedPref.saveUser(getContext(), AppData.user);
                            ivProfile.setImageURI(fileUri);
                            Snackbar snackbar = Snackbar
                                    .make(parent, "Profile updated successfully", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
//                        ivProfile.setImageURI(fileUri);

                    }
                }
            }
    );


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cv_profile:
                ImagePicker.with(this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .createIntent(intent -> {
                            resultLauncher.launch(intent);
                            return null;
                        });
                break;
            case R.id.btn_password:
                startActivity(new Intent(getContext(), PasswordActivity.class));
                break;
            case R.id.btn_logout:
                SharedPref.deleteUser(getContext());
                AppData.user = null;
                startActivity(new Intent(getContext(), IntroActivity.class));
                getActivity().finishAffinity();
                break;
        }
    }


}