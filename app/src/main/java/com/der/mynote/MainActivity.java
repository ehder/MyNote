package com.der.mynote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.der.mynote.adapter.NoteAdapter;
import com.der.mynote.adapter.NoteClickListener;
import com.der.mynote.database.RoomDB;
import com.der.mynote.model.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private NoteAdapter adapter;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private List<Note> noteList;
    private RoomDB database;
    private SearchView searchView;
    private Note selectedNote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab = findViewById(R.id.fab);
        searchView = findViewById(R.id.search);
        recyclerView = findViewById(R.id.recycler_home);
        database = RoomDB.getInstance(this);
        noteList = new ArrayList<>();

        noteList = database.mainDao().getAll();
        updateRecycler(noteList);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NoteTakerActivity.class);
                startActivityForResult(intent, 101);
            }
        });

    }

    private void filter(String newText) {
        List<Note> filterList = new ArrayList<>();

        for (Note note : noteList) {
            if (note.getNote().contains(newText)){
                filterList.add(note);
            }else if (note.getTitle().contains(newText)){
                filterList.add(note);
            }
        }

        adapter.filterList(filterList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Save
        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                Note newNote = (Note) data.getSerializableExtra("note");
                database.mainDao().insert(newNote);
                noteList.clear();
                noteList.addAll(database.mainDao().getAll());
                adapter.notifyDataSetChanged();
            }
        }

        //Update
        if (requestCode == 102) {
            if (resultCode == Activity.RESULT_OK) {
                Note newNote = (Note) data.getSerializableExtra("note");
                database.mainDao().update(newNote.getId(), newNote.getTitle(), newNote.getNote());
                noteList.clear();
                noteList.addAll(database.mainDao().getAll());
                adapter.notifyDataSetChanged();
            }


        }
    }

    private void updateRecycler(List<Note> noteList) {
        recyclerView.setHasFixedSize(true);
        adapter = new NoteAdapter(this, noteList, noteClickListener);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
    }

    private final NoteClickListener noteClickListener = new NoteClickListener() {
        @Override
        public void onClick(Note note) {
            Intent intent = new Intent(MainActivity.this, NoteTakerActivity.class);
            intent.putExtra("old_note", note);
            startActivityForResult(intent, 102);
        }

        @Override
        public void onLongClick(Note note, CardView cardView) {
            Toast.makeText(MainActivity.this, "Recycler View Was long CliCk..", Toast.LENGTH_SHORT).show();
            selectedNote = new Note();
            selectedNote = note;
            showPopup(cardView);

        }

    };

    private void showPopup(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.pin:
                if (selectedNote.isPin()){
                    database.mainDao().pin(selectedNote.getId(), false);
                    Toast.makeText(MainActivity.this,"unPined", Toast.LENGTH_SHORT).show();
                }else {
                    database.mainDao().pin(selectedNote.getId(), true);
                    Toast.makeText(MainActivity.this,"pin", Toast.LENGTH_SHORT).show();
                }
                noteList.clear();
                noteList.addAll(database.mainDao().getAll());
                adapter.notifyDataSetChanged();
                return true;


            case R.id.delete:
                database.mainDao().delete(selectedNote);
                noteList.clear();
                noteList.addAll(database.mainDao().getAll());
                adapter.notifyDataSetChanged();
                return true;

            default:
                return false;

        }
    }
}