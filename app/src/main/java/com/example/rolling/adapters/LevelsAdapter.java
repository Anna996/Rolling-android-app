package com.example.rolling.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rolling.items.Level;
import com.example.rolling.ItemClickedListener;
import com.example.rolling.R;

import java.util.List;

public class LevelsAdapter extends RecyclerView.Adapter<LevelsAdapter.LevelsViewHolder> {

    private List<Level> levels;
    private ItemClickedListener listener;

    public LevelsAdapter(List<Level> levels) {
        this.levels = levels;
    }

    public void setListener(ItemClickedListener listener) {
        this.listener = listener;
    }

    public class LevelsViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public LevelsViewHolder(@NonNull final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.level_ImageView);
            textView = itemView.findViewById(R.id.level_TextView);

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
    public LevelsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.level_cell, parent, false);
        return new LevelsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LevelsViewHolder holder, int position) {
        Level level = levels.get(position);
        holder.textView.setText(level.getName());
        holder.imageView.setImageResource(level.getResId());
    }

    @Override
    public int getItemCount() {
        return levels.size();
    }
}
