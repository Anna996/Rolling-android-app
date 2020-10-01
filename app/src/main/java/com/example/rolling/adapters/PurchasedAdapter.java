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
import com.example.rolling.items.PurchasedItem;

import java.util.List;

public class PurchasedAdapter extends RecyclerView.Adapter<PurchasedAdapter.ItemViewHolder> {

    private List<PurchasedItem> items;
    private ItemClickedListener listener;

    public PurchasedAdapter(List<PurchasedItem> items) {
        this.items = items;
    }

    public void setListener(ItemClickedListener listener) {
        this.listener = listener;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView firstImageView;
        ImageView secondImageView;
        ImageView thirdImageView;
        TextView textView;
        LinearLayout linearLayout;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            firstImageView = itemView.findViewById(R.id.first_purchased_imageView);
            secondImageView = itemView.findViewById(R.id.second_purchased_imageView);
            thirdImageView = itemView.findViewById(R.id.third_purchased_imageView);
            textView = itemView.findViewById(R.id.purchased_textView);
            linearLayout = itemView.findViewById(R.id.purchased_linerLayout);

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
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.purchased_cell, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        PurchasedItem item = items.get(position);
        holder.firstImageView.setImageResource(item.getResIdFirst());
        holder.secondImageView.setImageResource(item.getResIdSecond());
        holder.thirdImageView.setImageResource(item.getResIdThird());
        holder.textView.setText(item.isDefault() ? R.string.default_text : R.string.available_text);
        holder.linearLayout.setEnabled(item.isSelected());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
