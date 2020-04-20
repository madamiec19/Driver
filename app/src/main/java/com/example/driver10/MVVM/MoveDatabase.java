package com.example.driver10.MVVM;


import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = Move.class, version = 6, exportSchema = false)
public abstract class MoveDatabase extends RoomDatabase {

    private static MoveDatabase instance;

    public abstract MoveDao moveDao();

    public static synchronized MoveDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    MoveDatabase.class, "move_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }


    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{
        private MoveDao moveDao;

        private PopulateDbAsyncTask(MoveDatabase db){
            moveDao = db.moveDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            //moveDao.insert(new Move());
            return null;
        }
    }

}
