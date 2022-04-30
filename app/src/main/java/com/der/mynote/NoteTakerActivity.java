package com.der.mynote;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.der.mynote.database.RoomDB;
import com.der.mynote.model.Note;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NoteTakerActivity extends AppCompatActivity {

    private EditText txt_title, txt_note;
    private TextView txt_save_date;
    private ImageView save;
    private Note note;
    boolean isOldNote = false;
    private String title;
    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_taker);
        initId();
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        note = new Note();
        try {
            note = (Note) getIntent().getSerializableExtra("old_note");
            txt_title.setText(note.getTitle());
            txt_note.setText(note.getNote());
            txt_save_date.setText(note.getDate());
            isOldNote = true;
        }catch (Exception e){
            e.printStackTrace();
        }

        if (note == null){
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote(view);
            }
        });

    }

    private void saveNote(View view){
            if (!isOldNote){
                note = new Note();
                Toast.makeText(this, "new note created.", Toast.LENGTH_SHORT).show();
            }

            String date = formatDate();

            if (txt_note.getText().toString().isEmpty()){
                Toast.makeText(NoteTakerActivity.this, "Please input Note", Toast.LENGTH_SHORT).show();
            }

            if (txt_title.getText().toString().isEmpty() ){
                int limitWords = 25;
                 title = txt_note.getText().toString();
                if (title.length() < limitWords){
                    txt_title.setText(title);
                    note.setTitle(title);
                    Toast.makeText(NoteTakerActivity.this,"TITLE LENGTH: "+ title.length(), Toast.LENGTH_SHORT).show();
                }else if (title.length() > 25){
                    String str = title.substring(0, 25);
                    txt_title.setText(str);
                    note.setTitle(str);
                    Toast.makeText(NoteTakerActivity.this,"TITLE LENGTH: "+ str.length(), Toast.LENGTH_SHORT).show();
                }
            }else if (!txt_title.getText().toString().isEmpty()){
                note.setTitle(txt_title.getText().toString());
            }

                note.setNote(txt_note.getText().toString());
                note.setDate(date);
                note.setPin(false);

                Intent intent = new Intent();
                intent.putExtra("note", note);
                setResult(Activity.RESULT_OK, intent);
                finish();
    }

    private void initId(){
        txt_title = findViewById(R.id.title);
        txt_note = findViewById(R.id.note);
        save = findViewById(R.id.save);
        txt_save_date = findViewById(R.id.save_date);
    }

    private String formatDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.applyPattern("EEE, MMM dd, HH:mm, yyyy");
        String date = simpleDateFormat.format(Calendar.getInstance().getTime());
        return date;
    }


}