package com.example.sqlitedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
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

public class AddActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner spCategory;
    private EditText tvTitle, tvPrice, tvDate;
    private Button btUpdate, btCancle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initView();
        btUpdate.setOnClickListener(this);
        btCancle.setOnClickListener(this);
        tvDate.setOnClickListener(this);
    }

    private void initView() {
        spCategory = findViewById(R.id.spCategory);
        tvTitle = findViewById(R.id.tvTitle);
        tvPrice = findViewById(R.id.tvPrice);
        tvDate = findViewById(R.id.tvDate);
        btUpdate = findViewById(R.id.btUpdate);
        btCancle = findViewById(R.id.btCancle);
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
        if (view == btCancle) {
            finish();
        }
        if (view == btUpdate) {
            String title = tvTitle.getText().toString();
            String category = spCategory.getSelectedItem().toString();
            String price = tvPrice.getText().toString();
            String date = tvDate.getText().toString();
            if (!title.isEmpty() && price.matches("\\d+")) {
                Item item = new Item(title, category, price, date);
                SQLiteHelper db = new SQLiteHelper(this);
                db.addItem(item);
                finish();
            }
        }
    }
}