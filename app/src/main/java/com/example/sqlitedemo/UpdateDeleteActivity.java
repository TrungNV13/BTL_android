package com.example.sqlitedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.sqlitedemo.dal.SQLiteHelper;
import com.example.sqlitedemo.model.Item;

import java.util.Calendar;

public class UpdateDeleteActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner spCategory;
    private EditText tvTitle, tvPrice, tvDate;
    private Button btUpdate, btRemove, btBack;
    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete);
        initView();
        btUpdate.setOnClickListener(this);
        btRemove.setOnClickListener(this);
        btBack.setOnClickListener(this);
        tvDate.setOnClickListener(this);
        getItem();
    }

    private void getItem() {
        Intent intent = getIntent();
        item = (Item) intent.getSerializableExtra("item");
        tvTitle.setText(item.getTitle());
        tvPrice.setText(item.getPrice());
        tvDate.setText(item.getDate());
        for (int i = 0; i < spCategory.getCount(); i++) {
            if (spCategory.getItemAtPosition(i).toString().equalsIgnoreCase(item.getCategory())) {
                spCategory.setSelection(i);
                break;
            }
        }
    }

    private void initView() {
        spCategory = findViewById(R.id.spCategory);
        tvTitle = findViewById(R.id.tvTitle);
        tvPrice = findViewById(R.id.tvPrice);
        tvDate = findViewById(R.id.tvDate);
        btUpdate = findViewById(R.id.btUpdate);
        btRemove = findViewById(R.id.btRemove);
        btBack = findViewById(R.id.btBack);
        spCategory.setAdapter(new ArrayAdapter<String>(this, R.layout.item_spinner,
                getResources().getStringArray(R.array.category)));
    }

    @Override
    public void onClick(View view) {
        if (view == tvDate) {
            final Calendar calendar = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
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
                    tvDate.setText(date);
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        }
        if (view == btBack) {
            finish();
        }
        if (view == btUpdate) {
            int id = item.getId();
            String title = tvTitle.getText().toString();
            String category = spCategory.getSelectedItem().toString();
            String price = tvPrice.getText().toString();
            String date = tvDate.getText().toString();
            Item i = new Item(id, title, category, price, date);
            if (!title.isEmpty() && price.matches("\\d+")) {
                Item item = new Item(title, category, price, date);
                SQLiteHelper db = new SQLiteHelper(this);
                db.updateItem(i);
                finish();
            }
        }
        if (view == btRemove) {
            SQLiteHelper db = new SQLiteHelper(this);
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Thông báo xóa");
            builder.setMessage("Bạn có thật sự muốn xóa '" + item.getTitle() + "' không?");
            builder.setIcon(R.drawable.remove);
            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    db.deleteItem(item.getId());
                    finish();
                }
            });
            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}