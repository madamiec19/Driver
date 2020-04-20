package com.example.driver10.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.driver10.GsheetAPI;
import com.example.driver10.MVVM.Move;
import com.example.driver10.RecyclerView.MoveAdapter;
import com.example.driver10.MVVM.MoveViewModel;
import com.example.driver10.R;
import com.example.driver10.RecyclerView.RecyclerItemClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.math.BigDecimal;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private MoveViewModel moveViewModel;
    TextView tvMoneyToday;
    ImageView btnWorkday;
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Ruchy");

        btnWorkday = findViewById(R.id.btnWorkday);
        btnWorkday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WorkdayActivity.class);
                startActivity(intent);
            }
        });

        floatingActionButton = findViewById(R.id.floatingAddMove);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddMoveActivity1.class);
                startActivityForResult(intent, 0);
            }
        });


        mAuth = FirebaseAuth.getInstance();

        //setting recyclerview with list of moves
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final MoveAdapter moveAdapter = new MoveAdapter();
        recyclerView.setAdapter(moveAdapter);

        moveViewModel = new ViewModelProvider(this).get(MoveViewModel.class);
        moveViewModel.getAllMoves().observe(this, new Observer<List<Move>>() {
            @Override
            public void onChanged(List<Move> moves) {
                moveAdapter.setMoves(moves);
            }
        });
        ////////////////////////

        //gettin sum of move's values
        moveViewModel.getValueSum().observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                tvMoneyToday = findViewById(R.id.tvMoneyToday);
                if(aDouble == null) tvMoneyToday.setText("0 PLN");
                else{

                    //TODO! to trzeba przenieść do convertera w klasie i wgl użyć BigDecimal w klasie
                    BigDecimal tempBig = new BigDecimal(Double.toString(aDouble));
                    tempBig = tempBig.setScale(2, BigDecimal.ROUND_HALF_EVEN);
                    tvMoneyToday.setText(tempBig + " PLN");
                }

            }
        });


        // onclick dla poszczególnych ruchów
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {

                    @Override public void onItemClick(View view, int position) {
                        //TODO! po kliknieciu otwiera sie dialog informacyjny
                        Toast.makeText(MainActivity.this,
                                moveAdapter.getMoveAt(position).getDate()+ " godzina: " + moveAdapter.getMoveAt(position).getHour() +
                                        "\ntrasa: " + moveAdapter.getMoveAt(position).getCosts(),
                                Toast.LENGTH_LONG)
                                .show();
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Usunąć ruch?")
                                .setPositiveButton("TAK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(MainActivity.this, "Ruch usunięty", Toast.LENGTH_LONG).show();
                                        moveViewModel.delete(moveAdapter.getMoveAt(position));
                                    }
                                })
                                .setNegativeButton("NIE", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                })
        );



    }

    @Override
    public void onStart(){
        super.onStart();
        //Check if user is signed in
        FirebaseUser currentUser = mAuth.getCurrentUser();


        if(currentUser != null) {
            //zalogowany
        }
        if(currentUser == null){

            //niezalogowany
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
    }


    //Menu rozwijane w głównej aktywności, prawy górny róg
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.move_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.showEveryMove:
                Intent everyMovesSheet = new Intent(Intent.ACTION_VIEW, Uri.parse(GsheetAPI.MOVESSHEET));
                startActivity(everyMovesSheet);
                return super.onOptionsItemSelected(item);
            case R.id.logOut:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);

        }

    }


    //reakcja na result od innej aktywnosci
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                Toast.makeText(this, "Dodano ruch!" + result, Toast.LENGTH_SHORT).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

}
