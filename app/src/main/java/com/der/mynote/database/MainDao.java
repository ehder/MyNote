package com.der.mynote.database;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.der.mynote.model.Note;

import java.util.List;

@Dao
public interface MainDao {

    @Insert (onConflict = REPLACE)
    void insert(Note note);

    @Query("SELECT * FROM note_table ORDER BY note_id DESC")
    List<Note> getAll();

    @Query("UPDATE note_table SET title = :title, note = :note WHERE note_id = :id")
    void update(long id, String title, String note);

    @Delete
    void delete(Note note);

    @Query("UPDATE note_table SET pin = :isPin WHERE note_id = :id")
    void pin(long id, boolean isPin);



}
