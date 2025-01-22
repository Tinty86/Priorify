package com.myprojects.priorify;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    ArrayAdapter<String> notes_adapter;
    ArrayList<String> notes;

    String block_name;
    String edittext_text;

    EditText editText;
    ListView lv_history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        editText = findViewById(R.id.edit_text);
        edittext_text = editText.getText().toString();

        block_name = getIntent().getStringExtra("block_name");

        File[] notes_files = FileHandler.listFilesInDirectory(getApplicationContext());

        notes = new ArrayList<>();
        for (File file : notes_files) {
            if (file.getName().equals("profileInstalled"))
                continue;
            String prompt = file.getName().replace(".txt", "");
            notes.add(prompt);
        }

        lv_history = findViewById(R.id.history_list);

        registerForContextMenu(lv_history);

        notes_adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, notes);

        lv_history.setAdapter(notes_adapter);
    }

    public void SaveButtonOnClick(View view) {
        FileHandler.saveToFile(getApplicationContext(), block_name, edittext_text);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, getString(R.string.delete));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals("Удалить")) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            String fileName = notes.get(info.position);
            // Удаляем файл через FileHandler
            boolean isDeleted = FileHandler.deleteFile(getApplicationContext(), fileName);

            if (isDeleted) {
                // Убираем элемент из списка и обновляем адаптер
                notes.remove(info.position);
                notes_adapter.notifyDataSetChanged();
                Toast.makeText(this, getString(R.string.note_deleted), Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, getString(R.string.note_delete_fail) + " " + fileName, Toast.LENGTH_SHORT).show();
            }
        }
        return super.onContextItemSelected(item);
    }
}
