package com.example.babyneeds.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babyneeds.R;
import com.example.babyneeds.model.Item;
import com.example.babyneeds.utils.ImageHelper;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private final List<Item> items;
    private final Context mContext;
    public OnItemListener listener;

    public ItemAdapter(List<Item> items, Context mContext) {
        this.items = items;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder holder, int position) {

        Item item = items.get(position);

        holder.itemTitle.setText(item.getItemName());
        holder.itemDesc.setText(item.getItemDesc());
        holder.itemPrice.setText("Rs. " + item.getItemPrice());

        Bitmap bitmap = ImageHelper.getImage(item.getImage());
        holder.imageView.setImageBitmap(bitmap);

        holder.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSenClick(item);
            }
        });

        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClickListener(item);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView itemTitle;
        public TextView itemDesc;
        public TextView itemPrice;
        public LinearLayout btnSend;
        public LinearLayout llItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.iv_image);
            itemTitle = itemView.findViewById(R.id.tv_item_title);
            itemDesc = itemView.findViewById(R.id.tv_item_desc);
            itemPrice = itemView.findViewById(R.id.tv_item_price);
            btnSend = itemView.findViewById(R.id.btn_send);
            llItem = itemView.findViewById(R.id.ll_item);

        }
    }

    public interface OnItemListener{
        public void onItemClickListener(Item item);
        public void onSenClick(Item item);
    }


}


