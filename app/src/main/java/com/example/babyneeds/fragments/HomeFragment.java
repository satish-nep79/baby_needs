package com.example.babyneeds.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babyneeds.R;
import com.example.babyneeds.adapters.ItemAdapter;
import com.example.babyneeds.model.BabyNeedsViewModel;
import com.example.babyneeds.model.Item;
import com.example.babyneeds.screen.AddItemActivity;
import com.example.babyneeds.utils.AppData;
import com.example.babyneeds.utils.ImageHelper;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener, ItemAdapter.OnItemListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static final int SELECT_PHONE_NUMBER = 1;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    LinearLayout itemsTab;
    LinearLayout purchasedTab;
    TextView tvItems;
    TextView tvPurchased;

    //NoData view
    View viewNoData;
    TextView tvNoData;
    View noData;
    LinearLayout btnAddNew;

    //recyclerView
    RecyclerView rvItems;
    RecyclerView rvPurchasedItems;
    ItemAdapter itemAdapter;
    ItemAdapter itemPurchasedAdapter;

    List<Item> items = new ArrayList<Item>();
    List<Item> purchasedItems = new ArrayList<Item>();

    private BabyNeedsViewModel babyNeedsViewModel;
    private String selectedTab = "Items";
    private Item itemToSend;
    private String sendTo = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        initViews();
        initVars();
        listener();
        getItems();
        getPurchasedItems();
        rvPurchasedItems.setVisibility(View.GONE);
        return rootView;


    }


    private void getItems() {

        Observer observer = new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> fetchedItems) {
                babyNeedsViewModel.getUserItems(AppData.user.getId()).removeObserver(this);
                items.clear();
                items.addAll(fetchedItems);
                itemAdapter.notifyDataSetChanged();
                if(!selectedTab.equals("Items")) return;
                if (items.isEmpty()) {
                    viewNoData.setVisibility(View.VISIBLE);
                    rvItems.setVisibility(View.GONE);
                } else {
                    viewNoData.setVisibility(View.GONE);
                    rvItems.setVisibility(View.VISIBLE);
                }

            }
        };

        babyNeedsViewModel.getUserItems(AppData.user.getId()).observe(getActivity(), observer);
    }

    private void getPurchasedItems() {
        Observer observer = new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> fetchedItems) {
                babyNeedsViewModel.getPurchasedItems(AppData.user.getId()).removeObserver(this);
                purchasedItems.clear();
                purchasedItems.addAll(fetchedItems);
                itemPurchasedAdapter.notifyDataSetChanged();
                if(!selectedTab.equals("Purchased")) return;
                if (purchasedItems.isEmpty()) {
                    viewNoData.setVisibility(View.VISIBLE);
                    rvPurchasedItems.setVisibility(View.GONE);
                } else {
                    viewNoData.setVisibility(View.GONE);
                    rvPurchasedItems.setVisibility(View.VISIBLE);
                }

            }
        };

        babyNeedsViewModel.getPurchasedItems(AppData.user.getId()).observe(getActivity(), observer);

    }


    private void initVars() {
        itemAdapter = new ItemAdapter(items, getContext());
        itemAdapter.listener = this;
        rvItems.setAdapter(itemAdapter);
        itemPurchasedAdapter = new ItemAdapter(purchasedItems, getContext());
        itemPurchasedAdapter.listener = this;
        rvPurchasedItems.setAdapter(itemPurchasedAdapter);
        babyNeedsViewModel = new ViewModelProvider(this).get(BabyNeedsViewModel.class);
    }

    private void listener() {

        itemsTab.setOnClickListener(this);
        purchasedTab.setOnClickListener(this);
        btnAddNew.setOnClickListener(this);
    }

    private void initViews() {
        viewNoData = rootView.findViewById(R.id.layout_no_data);
        itemsTab = rootView.findViewById(R.id.tab_items);
        purchasedTab = rootView.findViewById(R.id.tab_purchased);
        tvItems = rootView.findViewById(R.id.tv_items);
        tvPurchased = rootView.findViewById(R.id.tv_purchased);
        noData = rootView.findViewById(R.id.layout_no_data);
        tvNoData = rootView.findViewById(R.id.tv_no_data);
        btnAddNew = rootView.findViewById(R.id.btn_add_new);

        rvItems = rootView.findViewById(R.id.rv_items);
        rvItems.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvItems.setHasFixedSize(true);

        rvPurchasedItems = rootView.findViewById(R.id.rv_purchased_items);
        rvPurchasedItems.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvPurchasedItems.setHasFixedSize(true);

        ItemTouchHelper itemTouch = new ItemTouchHelper(itemCallBack);
        itemTouch.attachToRecyclerView(rvItems);

        ItemTouchHelper purchasedItemTouch = new ItemTouchHelper(purchasedItemCallBack);
        purchasedItemTouch.attachToRecyclerView(rvPurchasedItems);

    }

    private ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Cursor cursor = null;
                        Intent data = result.getData();
                        Uri uri = data.getData();
                        cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
                        cursor.moveToFirst();
                        int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        sendTo = cursor.getString(phoneIndex);
                        sendSms();

                    }
                }
            }
    );

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab_items:
                selectedTab = "Items";
                itemsTab.setBackground(getResources().getDrawable(R.drawable.drb_selected_tab_background));
                tvItems.setTextColor(getResources().getColor(R.color.selected_tab_text));
                purchasedTab.setBackground(null);
                tvPurchased.setTextColor(getResources().getColor(R.color.white));
                tvNoData.setText("No Items Found");
                btnAddNew.setVisibility(View.VISIBLE);
                rvPurchasedItems.setVisibility(View.GONE);
                if (items.isEmpty()) {
                    viewNoData.setVisibility(View.VISIBLE);
                    rvItems.setVisibility(View.GONE);
                } else {
                    viewNoData.setVisibility(View.GONE);
                    rvItems.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tab_purchased:
                selectedTab = "Purchased";
                purchasedTab.setBackground(getResources().getDrawable(R.drawable.drb_selected_tab_background));
                tvPurchased.setTextColor(getResources().getColor(R.color.selected_tab_text));
                itemsTab.setBackground(null);
                tvItems.setTextColor(getResources().getColor(R.color.white));
                tvNoData.setText("No Purchased Items");
                btnAddNew.setVisibility(View.GONE);
                rvItems.setVisibility(View.GONE);
                if (purchasedItems.isEmpty()) {
                    viewNoData.setVisibility(View.VISIBLE);
                    rvPurchasedItems.setVisibility(View.GONE);
                } else {
                    viewNoData.setVisibility(View.GONE);
                    rvPurchasedItems.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_add_new:
                startActivity(new Intent(getActivity(), AddItemActivity.class));
        }
    }

    @Override
    public void onItemClickListener(Item item) {
        Intent intent = new Intent(getContext(), AddItemActivity.class);
        intent.putExtra("Item", new Gson().toJson(item));
        startActivity(intent);
    }

    @Override
    public void onSenClick(Item item) {
        itemToSend = item;
        pickContact();

    }

    private void sendSms() {
        String smsBody = "Name: " + itemToSend.getItemName()
                + "\n\nDescription:" + itemToSend.getItemDesc()
                + "\n\nPrice: Rs. " +itemToSend.getItemPrice();

        Uri uri = Uri.parse("smsto:"+sendTo);
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO, uri);
        smsIntent.putExtra("sms_body", smsBody);

        sendTo = "";
        itemToSend = null;

        try {
            startActivity(smsIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Log.e("SMS", "onSenClick: " + ex);
            Toast.makeText(getContext(), "Sms Fail", Toast.LENGTH_SHORT).show();
        }
    }

    private void pickContact() {
        Intent contactIntent = new Intent(Intent.ACTION_PICK);
        contactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        resultLauncher.launch(contactIntent);
    }

    ItemTouchHelper.SimpleCallback itemCallBack = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAbsoluteAdapterPosition();

            Item item;

            switch (direction) {
                case ItemTouchHelper.LEFT:
                    item = items.get(position);
                    babyNeedsViewModel.delete(item);
                    break;
                case ItemTouchHelper.RIGHT:
                    item = items.get(position);
                    item.setPurchased(true);
                    babyNeedsViewModel.update(item);
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(), R.color.delete_background))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(getContext(), R.color.purchase_background))
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_shopping_cart_24)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        }
    };

    ItemTouchHelper.SimpleCallback purchasedItemCallBack = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAbsoluteAdapterPosition();

            Item item;
            switch (direction) {
                case ItemTouchHelper.LEFT:
                    item = purchasedItems.get(position);
                    babyNeedsViewModel.delete(item);
                    break;
                case ItemTouchHelper.RIGHT:
                    item = purchasedItems.get(position);
                    item.setPurchased(false);
                    babyNeedsViewModel.update(item);
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(), R.color.delete_background))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(getContext(), R.color.purchase_background))
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_remove_shopping_cart_24)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        }
    };


}