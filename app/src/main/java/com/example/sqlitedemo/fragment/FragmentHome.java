package com.example.sqlitedemo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqlitedemo.R;
import com.example.sqlitedemo.UpdateDeleteActivity;
import com.example.sqlitedemo.adapter.RecycleViewAdapter;
import com.example.sqlitedemo.dal.SQLiteHelper;
import com.example.sqlitedemo.model.Item;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FragmentHome extends Fragment implements RecycleViewAdapter.ItemListener {
    private RecyclerView recyclerView;
    private RecycleViewAdapter adapter;
    private SQLiteHelper db;
    private TextView tvTong;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycleView);
        tvTong = view.findViewById(R.id.tvTong);
        adapter = new RecycleViewAdapter();
        adapter.setItemListener(this);
        db = new SQLiteHelper(getContext());
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        List<Item> list = db.getByDate(f.format(date));
        adapter.setList(list);
        tvTong.setText("Tổng tiền: " + tingTong(list));
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    public int tingTong(List<Item> list) {
        int t = 0;
        for (Item i : list) {
            t += Integer.parseInt(i.getPrice());
        }
        return t;
    }

    @Override
    public void onItemClick(View view, int position) {
        Item item = adapter.getItem(position);
        Intent intent = new Intent(getActivity(), UpdateDeleteActivity.class);
        intent.putExtra("item", item);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        List<Item> list = db.getByDate(f.format(date));
        adapter.setList(list);
        tvTong.setText("Tổng tiền: " + tingTong(list));
    }
}
