package com.example.notes;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class notes extends AppCompatActivity {

    private static final String PREFS_NAME="NotePrefs";
    private static final String KEY_NOTE_COUNT="NoteCount";
    private LinearLayout notesContainer;
    private List<Note> noteList;
    private EditText titleEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notes);

        notesContainer = findViewById(R.id.notesContainer);
        Button saveButton = findViewById(R.id.saveButton);
        titleEditText = findViewById(R.id.titleEditText);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        noteList = new ArrayList<>();
        loadNotesFromPreferences();
        displayNotes();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setDefaultText();
    }
    private void setDefaultText() {
        titleEditText.setText("Canada");
        titleEditText.setEnabled(true); // Enable editing
    }


    private void displayNotes() {
        for(Note note:noteList){
            createNoteView(note);
        }
    }

    private void loadNotesFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences (PREFS_NAME, MODE_PRIVATE);
        int noteCount = sharedPreferences.getInt(KEY_NOTE_COUNT, 0);
        for (int i = 0; i < noteCount; i++) {
            String title = sharedPreferences.getString("note_title_" + i, "");
            String content = sharedPreferences.getString("note_content_" + i, "");
            Note note = new Note();
            note.setTitle(title);
            note.setContent(content);
            noteList.add(note);
        }
    }

    private void saveNote(){
        EditText titleEditText=findViewById(R.id.titleEditText);
        EditText contentEditText=findViewById(R.id.contentEditText);

        String title=titleEditText.getText().toString();
        String content=contentEditText.getText().toString();

        if(!title.isEmpty()&& !content.isEmpty()){
            Note note=new Note();
            note.setTitle(title);
            note.setContent(content);

            noteList.add(note);
            saveNotesToPreferences();

            createNoteView(note);
            clearInputFields();

            setDefaultText();
        }
    }

    private void clearInputFields() {
        EditText titleEditText=findViewById(R.id.titleEditText);
        EditText contentEditText=findViewById(R.id.contentEditText);
        
        titleEditText.getText().clear();
        contentEditText.getText().clear();
    }

    private void createNoteView(final Note note) {
        View noteView=getLayoutInflater().inflate(R.layout.notes_item,null);
        TextView titleTextView = noteView.findViewById(R.id.titleTextView);
        TextView contentTextView= noteView.findViewById(R.id.contentTextView);
        titleTextView.setText(note.getTitle());
        contentTextView.setText(note.getContent());
        noteView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDeleteDialog();
                return true;
            }
        });

        notesContainer.addView(noteView);
    }

    private void showDeleteDialog() {
    }

    private void saveNotesToPreferences(){
        SharedPreferences sharedPreferences=getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        editor.putInt(KEY_NOTE_COUNT,noteList.size());
        for(int i=0;i<noteList.size();i++){
            Note note=noteList.get(i);
            editor.putString("note_title_"+i,note.getTitle());
            editor.putString("note_content_"+i,note.getContent());
        }
        editor.apply();
    }
}