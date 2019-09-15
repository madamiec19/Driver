package com.example.driver10;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MoveDao {

    @Insert
    void insert(Move move);

    @Update
    void update(Move move);

    @Delete
    void delete(Move move);

    @Query("DELETE FROM move_table")
    void deleteAllMoves();

    @Query("SELECT * FROM move_table ORDER BY id DESC")
    LiveData<List<Move>> getAllMoves();

    @Query("SELECT COUNT(id) FROM move_table")
    LiveData<Integer> getRowCount();

    @Query("SELECT sum(value) From move_table")
    LiveData<Double> getValueSum();

    @Query("DELETE FROM move_table WHERE id = :moveID")
    void deleteByUserId(long moveID);
}
