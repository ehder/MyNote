package com.der.mynote.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.der.mynote.NoteTakerActivity;
import com.der.mynote.R;
import com.der.mynote.model.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    List<Note> notes;
    Context context;
    NoteClickListener listener;

    public NoteAdapter(Context context,List<Note> notes, NoteClickListener listener) {
        this.context = context;
        this.notes = notes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(notes.get(position).getTitle());
        holder.title.setSelected(true);
        holder.date.setText(notes.get(position).getDate());
        holder.note.setText(notes.get(position).getNote().trim());

        if (notes.get(position).isPin()){
            holder.pin.setImageResource(R.drawable.ic_baseline_push_pin_24);
        }else {
            holder.pin.setImageResource(0);
        }

        int colorCode = randomColorPicker();
        holder.container.setCardBackgroundColor(holder.itemView.getResources().getColor(colorCode, null));

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int position = holder.getAdapterPosition();
                listener.onClick(notes.get(position));
            }
        });


        holder.container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                listener.onLongClick(notes.get(holder.getAdapterPosition()), holder.container );
                return true;
            }
        });


    }


    private int randomColorPicker(){
        List<Integer> colorCode = new ArrayList<>();
        colorCode.add(R.color.color1);
        colorCode.add(R.color.color2);
        colorCode.add(R.color.color3);
        colorCode.add(R.color.color4);
        colorCode.add(R.color.color5);

        Random random = new Random();
        int randomColor = random.nextInt(colorCode.size());
        return colorCode.get(randomColor);

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void filterList(List<Note> notes){
        this.notes = notes;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView title, date, note;
        ImageView pin;
        CardView container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            pin = itemView.findViewById(R.id.image_pin);
            note = itemView.findViewById(R.id.note);
            container = itemView.findViewById(R.id.note_container);
        }
    }
}
