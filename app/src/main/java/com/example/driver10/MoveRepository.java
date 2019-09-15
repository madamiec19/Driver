package com.example.driver10;


import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MoveRepository {
    private static final String TAG = "MoveRepository";

    private MoveDao moveDao;
    private LiveData<List<Move>> allMoves;
    private LiveData<Double> valueSum;


    public MoveRepository(Application application) {
        MoveDatabase database = MoveDatabase.getInstance(application);
        moveDao = database.moveDao();
        allMoves = moveDao.getAllMoves();
        valueSum = moveDao.getValueSum();
    }

    public void insert(Move move) {
        new InsertMoveAsyncTask(moveDao).execute(move);
    }

    public void update(Move move) {
        new UpdateMoveAsyncTask(moveDao).execute(move);
    }

    public void delete(Move move) {
        new DeleteMoveAsyncTask(moveDao).execute(move);
    }

    public void deleteAllMoves() {
        new DeleteAllMovesAsyncTask(moveDao).execute();
    }

    public LiveData<List<Move>> getAllMoves() {
        return allMoves;
    }

    public LiveData<Double> getValueSum() {
        return valueSum;
    }


    private static class InsertMoveAsyncTask extends AsyncTask<Move, Void, Void> {
        private MoveDao moveDao;

        private InsertMoveAsyncTask(MoveDao moveDao) {
            this.moveDao = moveDao;
        }

        @Override
        protected Void doInBackground(Move... moves) {
            moveDao.insert(moves[0]);
            return null;
        }
    }

    private static class UpdateMoveAsyncTask extends AsyncTask<Move, Void, Void> {
        private MoveDao moveDao;

        private UpdateMoveAsyncTask(MoveDao moveDao) {
            this.moveDao = moveDao;
        }

        @Override
        protected Void doInBackground(Move... moves) {
            moveDao.update(moves[0]);
            return null;
        }
    }

    private static class DeleteMoveAsyncTask extends AsyncTask<Move, Void, Void> {
        private MoveDao moveDao;

        private DeleteMoveAsyncTask(MoveDao moveDao) {
            this.moveDao = moveDao;
        }

        @Override
        protected Void doInBackground(Move... moves) {
            moveDao.delete(moves[0]);
            return null;
        }
    }

    private static class DeleteAllMovesAsyncTask extends AsyncTask<Void, Void, Void> {
        private MoveDao moveDao;

        private DeleteAllMovesAsyncTask(MoveDao moveDao) {
            this.moveDao = moveDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            moveDao.deleteAllMoves();
            return null;
        }
    }

}