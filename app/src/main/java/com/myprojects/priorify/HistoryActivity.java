// TODO: Change the deleting mechanism. Instead of complete removal, func can strike through the text and add the week-timer that will
//  update every day until it will achieve the 7-th day (for example, (3/7) <- timer on the third day).

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

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

        notes = error_handler.split("<next note>");

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
        if (item.getTitle().equals(getString(R.string.delete))) {
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
        return super.onContextItemSelected(item);
    }
}
