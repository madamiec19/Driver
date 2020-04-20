package com.example.driver10.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.driver10.Day;
import com.example.driver10.GsheetAPI;
import com.example.driver10.MVVM.MoveViewModel;
import com.example.driver10.R;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class WorkdayActivity extends AppCompatActivity {

    private MoveViewModel moveViewModel;
    TextView tvMovesValue, tvMovesCount, tvHoursValue, tvSum, btnEndDay, tvDayDate;
    EditText etHours, etStartHour;
    int movesCount;
    double dayValue, movesValue, hoursValue;
    DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workday);

        moveViewModel = new ViewModelProvider(this).get(MoveViewModel.class);

        //USTAWIANIE WARTOŚCI RUCHÓW
        moveViewModel.getValueSum().observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                tvMovesValue = findViewById(R.id.tvMovesValue);
                if(aDouble == null) tvMovesValue.setText("0 PLN");
                else{

                    //TODO! to trzeba przenieść do convertera w klasie i wgl użyć BigDecimal w klasie
                    BigDecimal tempBig = new BigDecimal(Double.toString(aDouble));
                    tempBig = tempBig.setScale(2, BigDecimal.ROUND_HALF_EVEN);
                    movesValue = aDouble;
                    tvMovesValue.setText(tempBig + " PLN");
                }
            }
        });

        //USTAWIANIE ILOŚCI RUCHÓW
        moveViewModel.getRowCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                tvMovesCount = findViewById(R.id.tvMovesCount);
                if(integer == null) tvMovesCount.setText("0");
                else {
                    tvMovesCount.setText(integer + "");
                    movesCount = integer;
                }
            }
        });

        //USTAWIANIE ILOŚCI I WARTOŚCI GODZIN
        tvSum = findViewById(R.id.tvSum);
        tvSum.setText(movesValue+" PLN");
        tvHoursValue = findViewById(R.id.tvHoursValue);
        etHours = findViewById(R.id.etHours);
        etHours.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(etHours.getText().toString().isEmpty()) {
                    tvHoursValue.setText("0 PLN");
                    hoursValue = 0.0;
                }
                else{
                    String text = Integer.parseInt(etHours.getText().toString())*12 + " PLN";
                    tvHoursValue.setText(text);
                    hoursValue = Double.parseDouble(etHours.getText().toString())*12.0;
                }
                dayValue = hoursValue + movesValue;
                tvSum.setText(dayValue + " PLN");

            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });

        //ewidoncjonowanie dnia
        btnEndDay = findViewById(R.id.btnEndDay);
        btnEndDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etHours.getText() == null) Toast.makeText(getApplicationContext(),"Wpisz ilość godzin!", Toast.LENGTH_SHORT).show();
                else addHoursToSheet();
                finish();

            }
        });


        //ustawiene pickera do daty
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.DAY_OF_MONTH, day);

                String currentDateString = DateFormat.getDateInstance().format(c.getTime());

                TextView tvDate = findViewById(R.id.tvDayDate);
                tvDate.setText(currentDateString);
            }
        };

        //ustawienie daty aktualnej jako domyślnej
        Calendar cal = Calendar.getInstance();
        tvDayDate = findViewById(R.id.tvDayDate);
        String currentDateString = DateFormat.getDateInstance().format(cal.getTime());
        tvDayDate.setText(currentDateString);
        tvDayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();

                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(WorkdayActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth, mDateSetListener ,year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });


    }

    private void addHoursToSheet() {
        etHours = findViewById(R.id.etHours);
        etStartHour = findViewById(R.id.etStartHour);
        Day day = new Day(tvDayDate.getText().toString(), etStartHour.getText().toString(), Double.parseDouble(etHours.getText().toString()), movesCount, movesValue);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GsheetAPI.HOURSSCRIPT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Dodano godziny", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "błąd dodawania godzina", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parmas = new HashMap<>();

                //here we pass params
                parmas.put("action", "addHours");
                parmas.put("date", day.getDate());
                parmas.put("startHour", day.getStartHour());
                parmas.put("hours", Double.toString(day.getHours()));
                parmas.put("moves", Integer.toString(day.getMoves()));
                parmas.put("movesValue", Double.toString(day.getMovesValue()));


                return parmas;
            }
        };

        int socketTimeOut = 50000;// u can change this .. here it is 50 seconds

        //!TODO sprawdzic po co to
        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
        moveViewModel.deleteAllMoves();
    }

}
