package com.example.rolling.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rolling.ItemClickedListener;
import com.example.rolling.R;
import com.example.rolling.items.ShopItem;

import java.util.List;
import java.util.Locale;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder> {

    private List<ShopItem> shopItems;
    private ItemClickedListener listener;

    public ShopAdapter(List<ShopItem> shopItems) {
        this.shopItems = shopItems;
    }

    public void setListener(ItemClickedListener listener) {
        this.listener = listener;
    }

    public class ShopViewHolder extends RecyclerView.ViewHolder {

        ImageView firstImageView;
        ImageView secondImageView;
        ImageView thirdImageView;
        TextView textView;
        LinearLayout linearLayout;

        public ShopViewHolder(@NonNull View itemView) {
            super(itemView);
            firstImageView = itemView.findViewById(R.id.first_item_imageView);
            secondImageView = itemView.findViewById(R.id.second_item_imageView);
            thirdImageView = itemView.findViewById(R.id.third_item_imageView);
            textView = itemView.findViewById(R.id.price_textView);
            linearLayout = itemView.findViewById(R.id.shop_linearLayout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClicked(getAdapterPosition(), v);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_cell, parent, false);
        return new ShopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        ShopItem item = shopItems.get(position);
        holder.firstImageView.setImageResource(item.getResIdFirst());
        holder.secondImageView.setImageResource(item.getResIdSecond());
        holder.thirdImageView.setImageResource(item.getResIdThird());
        holder.textView.setText(String.format(Locale.getDefault(), "$%d", item.getPrice()));
        holder.linearLayout.setEnabled(item.getAvailable());
        holder.linearLayout.setBackgroundResource(item.getAcquired() ? R.drawable.shop_cell_shape : R.drawable.shop_cell_selector);
    }

    @Override
    public int getItemCount() {
        return shopItems.size();
    }
}