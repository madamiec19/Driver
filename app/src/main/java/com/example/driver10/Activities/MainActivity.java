package com.example.driver10.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.driver10.GsheetAPI;
import com.example.driver10.Move;
import com.example.driver10.MoveAdapter;
import com.example.driver10.MoveViewModel;
import com.example.driver10.R;
import com.example.driver10.RecyclerItemClickListener;
import com.example.driver10.SwipeToDeleteCallback;
import com.example.driver10.WorkdayActivity;
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

        setFloatingButton();
        mAuth = FirebaseAuth.getInstance();

        //setting recyclerview with list of moves
        setUpRecyclerView();





    }


    private void setUpRecyclerView(){

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(moveAdapter));
//        itemTouchHelper.attachToRecyclerView(recyclerView);
        final MoveAdapter moveAdapter = new MoveAdapter();
        recyclerView.setAdapter(moveAdapter);

        moveViewModel = new ViewModelProvider(this).get(MoveViewModel.class);//ViewModelProviders.of(this).get(MoveViewModel.class);
        moveViewModel.getAllMoves().observe(this, new Observer<List<Move>>() {
            @Override
            public void onChanged(List<Move> moves) {
                moveAdapter.setMoves(moves);
            }
        });


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

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Toast.makeText(MainActivity.this, moveAdapter.getMoveAt(position).getRoute()+" /  " +moveAdapter.getMoveAt(position).getMoveTypesString(), Toast.LENGTH_LONG).show();
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

    private void setFloatingButton() {
        FloatingActionButton btnAddMove = findViewById(R.id.floatingAddMove);
        btnAddMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddMoveActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.move_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_moves:
                moveViewModel.deleteAllMoves();
                Toast.makeText(this, "Wszystkie ruchy usunięte", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
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

}
