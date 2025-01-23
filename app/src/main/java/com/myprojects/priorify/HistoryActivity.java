package com.myprojects.priorify;

import android.os.Bundle;
import android.util.Log;
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

    private static final String TAG = "HistoryActivity";

    ArrayAdapter<String> notes_adapter;
    ArrayList<String> block_of_notes;
    String[] notes;

    String block_name;
    String user_text;

    EditText editText;
    ListView lv_history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        block_name = getIntent().getStringExtra("block_name");

        File[] notes_files = FileHandler.listFilesInDirectory(getApplicationContext());

        block_of_notes = new ArrayList<>();
        for (File file : notes_files) {
            if (file.getName().equals("profileInstalled"))
                continue;
            String note = file.getName().replace(".txt", "");
            block_of_notes.add(note);
        }

        lv_history = findViewById(R.id.history_list);

        Set_Adapter();

        registerForContextMenu(lv_history);
    }

    private void Set_Adapter() {
        String error_handler = FileHandler.readFileContents(getApplicationContext(), block_name);

        if (error_handler.equals("Ошибка при чтении файла")) {
            Toast.makeText(this, "Ошибка при чтении файла", Toast.LENGTH_SHORT).show();
            error_handler = "";
        }
        if (error_handler.equals("Файл не найден.")) {
            error_handler = "";
        }

        notes = error_handler.split("<next note>");

        for (String note : notes) {
            Log.i(TAG, note);
        }
        registerForContextMenu(lv_history);

        notes_adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, notes);

        lv_history.setAdapter(notes_adapter);
    }

    public void SaveButtonOnClick(View view) {
        editText = findViewById(R.id.edit_text);
        user_text = editText.getText().toString().strip();
        if(!user_text.isEmpty()) {

            FileHandler.saveToFile(getApplicationContext(), block_name, user_text);

            Set_Adapter();
        }
        else {
            Log.i(TAG, "user_text пуст");
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, getString(R.string.delete));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.i(TAG, "ItemSelected");
        if (item.getTitle().equals("Удалить")) {
            Log.i(TAG, "Удалить");
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            String fileName = block_of_notes.get(info.position);
            // Удаляем файл через FileHandler
            boolean isDeleted = FileHandler.deleteFile(getApplicationContext(), fileName);

            if (isDeleted) {
                // Убираем элемент из списка и обновляем адаптер
                block_of_notes.remove(info.position);
                notes_adapter.notifyDataSetChanged();
                Toast.makeText(this, getString(R.string.note_deleted), Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, getString(R.string.note_delete_fail) + " " + fileName, Toast.LENGTH_SHORT).show();
            }
        }
        return super.onContextItemSelected(item);
    }
}
