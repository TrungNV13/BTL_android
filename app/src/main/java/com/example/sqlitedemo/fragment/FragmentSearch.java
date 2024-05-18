package com.example.sqlitedemo.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqlitedemo.R;
import com.example.sqlitedemo.adapter.RecycleViewAdapter;
import com.example.sqlitedemo.dal.SQLiteHelper;
import com.example.sqlitedemo.model.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class FragmentSearch extends Fragment implements View.OnClickListener {
    private RecyclerView recyclerView;
    private TextView tvTong;
    private Button btSearch;
    private SearchView searchView;
    private EditText eFrom, eTo;
    private Spinner spCategory;
    private RecycleViewAdapter adapter;
    private SQLiteHelper db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        List<Item> list = db.getAll();
        adapter.setList(list);
        tvTong.setText("Tổng tiền: " + tinhTong(list));
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Item> lSearch = db.searchByTitle(newText);
                tvTong.setText("Tổng tiền: " + tinhTong(lSearch));
                adapter.setList(lSearch);
                return true;
            }
        });
        eFrom.setOnClickListener(this);
        eTo.setOnClickListener(this);
        btSearch.setOnClickListener(this);
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String cate = spCategory.getItemAtPosition(position).toString();
                List<Item> lSearch;
                if (!cate.equalsIgnoreCase("All")) {
                    lSearch = db.searchByCategory(cate);
                } else {
                    lSearch = db.getAll();
                }
                tvTong.setText("Tổng tiền: " + tinhTong(lSearch));
                adapter.setList(lSearch);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public int tinhTong(List<Item> list) {
        int t = 0;
        for (Item i : list) {
            t += Integer.parseInt(i.getPrice());
        }
        return t;
    }

    public void initView(View view) {
        recyclerView = view.findViewById(R.id.recycleView);
        tvTong = view.findViewById(R.id.tvTong);
        btSearch = view.findViewById(R.id.btSearch);
        searchView = view.findViewById(R.id.search);
        eFrom = view.findViewById(R.id.eFrom);
        eTo = view.findViewById(R.id.eTo);
        spCategory = view.findViewById(R.id.spCategory);
        List<String> list = new ArrayList<>();
        list.add("All");
        list.addAll(Arrays.asList(getResources().getStringArray(R.array.category)));
        spCategory.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.item_spinner, list.toArray(new String[list.size()])));
        adapter = new RecycleViewAdapter();
        db = new SQLiteHelper(getContext());
    }

    @Override
    public void onClick(View view) {
        if (view == eFrom) {
            final Calendar calendar = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                    String date = "";
                    if (d > 9) {
                        date = d + "/";
                    } else {
                        date = "0" + d + "/";
                    }
                    if (m > 8) {
                        date += (1 + m) + "/";
                    } else {
                        date += "0" + (1 + m) + "/";
                    }
                    date += y;
                    eFrom.setText(date);
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        }
        if (view == eTo) {
            final Calendar calendar = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                    String date = "";
                    if (d > 9) {
                        date = d + "/";
                    } else {
                        date = "0" + d + "/";
                    }
                    if (m > 8) {
                        date += (1 + m) + "/";
                    } else {
                        date += "0" + (1 + m) + "/";
                    }
                    date += y;
                    eTo.setText(date);
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        }
        if (view == btSearch) {
            String from = eFrom.getText().toString();
            String to = eTo.getText().toString();
            if (!from.isEmpty() && !to.isEmpty()) {
                List<Item> lSearch = db.searchByDateFromTo(from, to);
                tvTong.setText("Tổng tiền: " + tinhTong(lSearch));
                adapter.setList(lSearch);
            }
        }
    }
}
