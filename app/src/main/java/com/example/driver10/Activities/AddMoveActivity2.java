package com.example.driver10.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.driver10.Dialogs.MiddlepointDialog;
import com.example.driver10.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddMoveActivity2 extends AppCompatActivity implements MiddlepointDialog.MiddlepointDialogListener {


    EditText etPlaceStart, etPlaceStop;
    TextView tvMiddlePoint;
    CheckBox cbWsparcie, cbWydanie, cbOdbior, cbRelokacja, cbMycie, cbOdkurzanie, cbNieudane;
    FloatingActionButton floatingActionButton;
    int hour, kmStart, kmStop;
    String car, date, middlePoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_move2);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null){
            hour = (int) bundle.get("HOUR");
            car = (String) bundle.get("CAR");
            date = (String) bundle.get("DATE");
            kmStart = (int) bundle.get("KMSTART");
            kmStop = (int) bundle.get("KMSTOP");
        } else {
            finish();
            Toast.makeText(getApplicationContext(), "errorhere", Toast.LENGTH_LONG).show();
        }

        middlePoint = "brak";

        etPlaceStart = findViewById(R.id.etPlaceStart);
        etPlaceStop = findViewById(R.id.etPlaceStop);
        etPlaceStop.setText("Warszawa");
        etPlaceStart.setText("Warszawa");

        cbWsparcie = findViewById(R.id.cbWsparcie);
        cbWydanie = findViewById(R.id.cbWydanie);
        cbOdbior = findViewById(R.id.cbOdbior);
        cbRelokacja = findViewById(R.id.cbRelokacja);
        cbMycie = findViewById(R.id.cbMycie);
        cbOdkurzanie = findViewById(R.id.cbOdkurzanie);
        cbNieudane = findViewById(R.id.cbNieudane);

        tvMiddlePoint = findViewById(R.id.tvMiddlePoint);
        tvMiddlePoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MiddlepointDialog middlepointDialog = new MiddlepointDialog();
                middlepointDialog.show(getSupportFragmentManager(), "middlepointdialog");
            }
        });


        floatingActionButton = findViewById(R.id.fabStart);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            //!TODO zabezpieczenie przed klikaniem dalej bez wypełnienia et i wybrania celu

            @Override
            public void onClick(View view) {

                boolean[] moveTypes = new boolean[7];
                moveTypes[0] = cbWsparcie.isChecked();
                moveTypes[1] = cbWydanie.isChecked();
                moveTypes[2] = cbOdbior.isChecked();
                moveTypes[3] = cbRelokacja.isChecked();
                moveTypes[4] = cbMycie.isChecked();
                moveTypes[5] = cbOdkurzanie.isChecked();
                moveTypes[6] = cbNieudane.isChecked();

                Intent intent = new Intent(AddMoveActivity2.this, AddMoveActivity3.class);
                intent.putExtra("CAR", car);
                intent.putExtra("PLACESTART", etPlaceStart.getText().toString());
                intent.putExtra("PLACESTOP", etPlaceStop.getText().toString());
                intent.putExtra("MIDDLEPOINT", middlePoint);
                intent.putExtra("HOUR", hour);
                intent.putExtra("DATE", date);
                intent.putExtra("KMSTART", kmStart);
                intent.putExtra("KMSTOP", kmStop);
                intent.putExtra("MOVETYPES", moveTypes);
                startActivityForResult(intent, 2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", 1);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                finish();
            }
        }
    }

    @Override
    public void applyMiddlepoint(String middlepoint) {
        middlePoint = middlepoint;
    }
}
