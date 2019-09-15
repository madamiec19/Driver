package com.example.driver10;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class WorkdayActivity extends AppCompatActivity {

    private MoveViewModel moveViewModel;
    TextView tvMovesValue, tvMovesCount, tvHoursValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workday);
    }
}
