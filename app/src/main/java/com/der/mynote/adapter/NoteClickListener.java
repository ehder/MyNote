package com.der.mynote.adapter;

import android.view.View;

import androidx.cardview.widget.CardView;

import com.der.mynote.model.Note;

public interface NoteClickListener {

    void onClick(Note note);
    void onLongClick(Note note, CardView cardView);

}
