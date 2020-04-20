package com.example.driver10.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.driver10.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddMoveActivity1 extends AppCompatActivity {

    EditText etCar, etStartHour, etKmStart, etKmStop;
    TextView tvMoveDate;
    FloatingActionButton floatingActionButton;
    DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_move1);

        etCar = findViewById(R.id.etCarInput);

        etStartHour = findViewById(R.id.etStartHour);
        Calendar rightNow = Calendar.getInstance();
        int currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY);
        String currentHourString = Integer.toString(currentHourIn24Format);
        etStartHour.setText(currentHourString);

        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        tvMoveDate = findViewById(R.id.tvMoveDate);
        tvMoveDate.setText(currentDate);
        tvMoveDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });


        etKmStart = findViewById(R.id.etKmStart);
        etKmStop = findViewById(R.id.etKmStop);

        floatingActionButton = findViewById(R.id.fabCar);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(etStartHour.getText().toString().equals("")|| etCar.getText().toString().equals("") || etKmStart.getText().toString().equals("")
                 || etKmStop.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "Wypełnij wszystkie pola", Toast.LENGTH_SHORT).show();
                else if(Integer.parseInt(etKmStart.getText().toString())>Integer.parseInt(etKmStop.getText().toString())){
                    Toast.makeText(AddMoveActivity1.this, "Błędny przebieg", Toast.LENGTH_SHORT).show();
                }
                else{

                    Intent intent = new Intent(AddMoveActivity1.this, AddMoveActivity2.class);
                    intent.putExtra("CAR", etCar.getText().toString());
                    intent.putExtra("HOUR", Integer.parseInt(etStartHour.getText().toString()));
                    intent.putExtra("DATE", tvMoveDate.getText().toString());
                    intent.putExtra("KMSTART", Integer.parseInt(etKmStart.getText().toString()));
                    intent.putExtra("KMSTOP", Integer.parseInt(etKmStop.getText().toString()));
                    startActivityForResult(intent, 1);
                }
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.DAY_OF_MONTH, day);

                String currentDateString = DateFormat.getDateInstance().format(c.getTime());

                TextView tvDate = findViewById(R.id.tvMoveDate);
                tvDate.setText(currentDateString);
            }
        };


    }

    private void showDateDialog() {
        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(AddMoveActivity1.this, android.R.style.Theme_Holo_Dialog_MinWidth, mDateSetListener ,year, month, day);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", 0);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

}
