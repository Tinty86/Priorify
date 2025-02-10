// TODO: Change the deleting mechanism. Instead of complete removal, func can strike through the text and add the week-timer that
//  updates every day until it will achieve the 7-th day (for example, (3/7) <- timer on the third day).
//  Add feature to copy notes. This feature should be an option in menu when user select item from history list

// TODO: Fix bug. After deleting a note extra empty note adds to the above from deleted note (transformed to -->
//  Bug. Random deleting notes. Delete func either removes non-existing note or removes previous note instead of deleting chosen note)

package com.myprojects.priorify;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class HistoryActivity extends AppCompatActivity {

    private static final String TAG = "HistoryActivity";

    ArrayAdapter<String> notes_adapter;
    ArrayList<String> list_notes;
    String[] notes;

    String error_handler;

    String block_name;
    String user_text;

    EditText editText;
    ListView lv_history;
    TextView top_block_name;
    Button save_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        top_block_name = findViewById(R.id.top_block_name);

        block_name = getIntent().getStringExtra("block_name");

        top_block_name.setText(block_name);

        list_notes = new ArrayList<>();

        lv_history = findViewById(R.id.history_list);

        Set_Adapter();

        registerForContextMenu(lv_history);

        save_bt = findViewById(R.id.save_button);

        save_bt.setOnClickListener(v -> {
            editText = findViewById(R.id.edit_text);
            user_text = editText.getText().toString().strip();
            if(!user_text.isEmpty()) {
                FileHandler.saveToFile(getApplicationContext(), block_name, user_text);
                Set_Adapter();
            }
            else {
                Log.i(TAG, "user_text пуст");
            }
        });
    }

    private void Set_Adapter() {
        error_handler = FileHandler.readFileContents(getApplicationContext(), block_name);

        if (error_handler.equals("Ошибка при чтении файла")) {
            Toast.makeText(this, "Ошибка при чтении файла", Toast.LENGTH_SHORT).show();
            error_handler = "";
        }
        if (error_handler.equals("Файл не найден.")) {
            error_handler = "";
        }

        String[] raw_notes = error_handler.split("<next note>");

        int count_of_empty_strings = 0;
        for (int i = 0; i < raw_notes.length; i++) {
            if (raw_notes[i].isEmpty() || raw_notes[i].equals(" ")) {
                count_of_empty_strings ++;
            }
        }
        int index = 0;
        notes = new String[raw_notes.length - count_of_empty_strings];
        for (int i = 0; i < raw_notes.length; i++) {
            if (raw_notes[i].isEmpty() || raw_notes[i].equals(" ")) {
                continue;
            }
            notes[index] = raw_notes[i];
            index++;
        }
//        for (int i = 0; i < raw_notes.length; i++) {
//            if (notes[i].isEmpty() || notes[i].equals(" ")) {
//                notes[i] = "<empty>";
//            }
//        }

        Log.d(TAG, "notes:\n" + Arrays.toString(notes));

        registerForContextMenu(lv_history);

        notes_adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, notes);

        lv_history.setAdapter(notes_adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, getString(R.string.delete));
        menu.add(1, v.getId(), 1, getString(R.string.copy_option));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.i(TAG, "ItemSelected");
        CharSequence title = item.getTitle();
        if (title.equals(getString(R.string.delete))) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            list_notes.addAll(Arrays.asList(notes));

            String note = list_notes.get(info.position);
            // Удаляем файл через FileHandler
            boolean isDeleted = FileHandler.deleteNote(getApplicationContext(), block_name, note);

            if (isDeleted) {
                Log.i(TAG, "File has been deleted");
                // Убираем элемент из списка и обновляем адаптер
                list_notes.remove(info.position);
                Set_Adapter();
                Toast.makeText(this, getString(R.string.note_deleted), Toast.LENGTH_SHORT).show();
            } else {
                Log.w(TAG, "File delete fail");
                Toast.makeText(this, getString(R.string.note_delete_fail) + " " + note, Toast.LENGTH_SHORT).show();
            }
        }
        // else if (title.equals(getString(R.string.copy_option))) {
        //    ...
        // }
        return super.onContextItemSelected(item);
    }
}
