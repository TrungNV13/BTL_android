package com.example.sqlitedemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqlitedemo.R;
import com.example.sqlitedemo.model.Item;

import java.util.ArrayList;
import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.RecycleViewHolder> {
    private List<Item> list;
    private ItemListener itemListener;

    public RecycleViewAdapter() {
        this.list = new ArrayList<>();
    }

    public void setList(List<Item> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public Item getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new RecycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewHolder holder, int position) {
        Item item = list.get(position);
        if (item == null)
            return;
        holder.title.setText(item.getTitle());
        holder.category.setText(item.getCategory());
        holder.price.setText(item.getPrice());
        holder.date.setText(item.getDate());
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class RecycleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title, category, price, date;

        public RecycleViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            category = itemView.findViewById(R.id.tvCategory);
            price = itemView.findViewById(R.id.tvPrice);
            date = itemView.findViewById(R.id.tvDate);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemListener != null)
                itemListener.onItemClick(view, getAdapterPosition());
        }
    }

    public interface ItemListener {
        public void onItemClick(View view, int position);
    }
}
