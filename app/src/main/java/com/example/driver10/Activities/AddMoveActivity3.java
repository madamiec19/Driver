package com.example.driver10.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.example.driver10.Dialogs.CommentDialog;
import com.example.driver10.Dialogs.CostsDialog;
import com.example.driver10.Dialogs.ExtrasDialog;
import com.example.driver10.GsheetAPI;
import com.example.driver10.MVVM.Move;
import com.example.driver10.MVVM.MoveViewModel;
import com.example.driver10.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class AddMoveActivity3 extends AppCompatActivity implements CommentDialog.CommentDialogListener,
        CostsDialog.CostsDialogListener, ExtrasDialog.ExtrasDialogListener {

    TextView tvCar, tvHour, tvKmStart, tvKmStop, tvKilometers, tvMoveTypeValue, tvMoveTypes,
            tvKmValue, tvCosts, tvValueSum;
    Button btnCosts, btnComment, btnExtra;
    FloatingActionButton fabStop;
    private MoveViewModel moveViewModel;
    String car, extra, placeStart, placeStop, date, middlePoint, moveComment;
    int hour, kmStart, kmStop, extraValue;
    double costs;
    boolean[] moveTypes;
    Move move;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_move3);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            hour = (int) bundle.get("HOUR");
            car = (String) bundle.get("CAR");
            date = (String) bundle.get("DATE");
            kmStart = (int) bundle.get("KMSTART");
            kmStop = (int) bundle.get("KMSTOP");
            placeStart = (String) bundle.get("PLACESTART");
            placeStop = (String) bundle.get("PLACESTOP");
            middlePoint = (String) bundle.get("MIDDLEPOINT");
            moveTypes = (boolean[]) bundle.get("MOVETYPES");
        } else {
            finish();
            Toast.makeText(getApplicationContext(), "errorhere", Toast.LENGTH_LONG).show();
        }
        moveComment = "";
        extraValue = 0;
        extra = "";

        tvCar = findViewById(R.id.tvCar);
        tvHour = findViewById(R.id.tvHour);
        tvKmStart = findViewById(R.id.tvKmStart);
        tvKmStop = findViewById(R.id.tvKmStop);
        tvKilometers = findViewById(R.id.tvKilometers);
        tvMoveTypeValue = findViewById(R.id.tvMoveTypeValue);
        tvMoveTypes = findViewById(R.id.tvMoveTypes);
        tvKmValue = findViewById(R.id.tvKmValue);
        tvCosts = findViewById(R.id.tvCosts);
        tvValueSum = findViewById(R.id.tvValueSum);

        moveViewModel = ViewModelProviders.of(this).get(MoveViewModel.class);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String driver = firebaseAuth.getCurrentUser().getEmail();

        move = new Move(car, hour, date, kmStart, kmStop, moveTypes, placeStart, middlePoint,
                placeStop, driver, moveComment, costs);

        tvCar.setText(move.getCar());
        tvHour.setText("" + move.getHour());
        tvKmStart.setText("" + move.getKmStart());
        tvKmStop.setText("" + move.getKmStop());
        int kilometers = move.getKmStop() - move.getKmStart();
        tvKilometers.setText("" + kilometers);
        tvMoveTypes.setText(move.getMoveTypesString());
        String s = "+" + move.getTypesValue() + " PLN";
        tvMoveTypeValue.setText(s);
        String s1 = "+" + move.getKilometersValue() + " PLN";
        tvKmValue.setText(s1);
        tvCosts.setText("0 PLN");
        tvValueSum.setText(move.getValue() + " PLN");


        fabStop = findViewById(R.id.fabStop);
        fabStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addItemToDb(move);

                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", 2);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        btnComment = findViewById(R.id.btnComment);
        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentDialog commentDialog = new CommentDialog();
                commentDialog.show(getSupportFragmentManager(), "commentdialog");
            }
        });

        btnCosts = findViewById(R.id.btnCosts);
        btnCosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CostsDialog costsDialog = new CostsDialog();
                costsDialog.show(getSupportFragmentManager(), "costsdialog");
            }
        });

        btnExtra = findViewById(R.id.btnExtra);
        btnExtra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExtrasDialog extrasDialog = new ExtrasDialog();
                extrasDialog.show(getSupportFragmentManager(), "extrasdialog");
            }
        });

    }


    private void addItemToDb(Move inputMove) {
        moveViewModel.insert(inputMove);
        addItemToSheet(inputMove);
    }

    private void addItemToSheet(@NonNull final Move inputMove) {
        //ZROBIĆ ASYNCA Z PROGRESSBAREM
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GsheetAPI.MOVESCRIPTURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parmas = new HashMap<>();

                //here we pass params
                parmas.put("action", "addItem");
                parmas.put("car", inputMove.getCar());
                parmas.put("kmStart", Integer.toString(inputMove.getKmStart()));
                parmas.put("kmStop", Integer.toString(inputMove.getKmStop()));
                parmas.put("placeStart", inputMove.getPlaceStart());
                parmas.put("placeStop", inputMove.getPlaceStop());
                parmas.put("middlePoint", inputMove.getMiddlePoint());
                parmas.put("type", inputMove.getMoveTypesString());
                parmas.put("driver", inputMove.getDriver());
                parmas.put("date", inputMove.getDate());
                parmas.put("hour", Integer.toString(inputMove.getHour()));
                parmas.put("costs", Double.toString(inputMove.getCosts()));
                parmas.put("comment", moveComment);
                parmas.put("extras", extra);
                parmas.put("extrasValue", Integer.toString(extraValue));

                return parmas;
            }
        };

        int socketTimeOut = 50000;// u can change this .. here it is 50 seconds

        //!TODO sprawdzic po co to
        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
    }

    @Override
    public void applyComment(String comment) {
        moveComment = comment;
        Toast.makeText(getApplicationContext(), "Dodano komentarz", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void applyCosts(double mCosts) {
        costs = mCosts;
        move.setCosts(costs);
        String s = costs + "PLN";
        tvCosts.setText(s);
        Toast.makeText(getApplicationContext(), "Dodano koszty", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void applyExtras(String extras, int extrasValue) {
        extra = extras;
        extraValue = extrasValue;
        Toast.makeText(getApplicationContext(), "Dodano sprzedaż dodatków", Toast.LENGTH_SHORT).show();
    }

    //////////////////////////////////////////////////////////////////////////////////////

}
