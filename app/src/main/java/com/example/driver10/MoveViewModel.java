package com.example.driver10;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MoveViewModel extends AndroidViewModel {
    private MoveRepository repository;
    private LiveData<List<Move>> allMoves;
    private LiveData<Double> valueSum;

    public MoveViewModel(@NonNull Application application) {
        super(application);
        repository = new MoveRepository(application);
        allMoves = repository.getAllMoves();
        valueSum = repository.getValueSum();
    }

    public void insert(Move move){
        repository.insert(move);
    }

    public void update(Move move){
        repository.update(move);
    }

    public void delete(Move move){
        repository.delete(move);
    }

    public void deleteAllMoves(){
        repository.deleteAllMoves();
    }

    public LiveData<List<Move>> getAllMoves(){
        return allMoves;
    }

    public LiveData<Double> getValueSum() {return valueSum;}
}
