package com.myprojects.priorify;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class FileHandler {

    private static final String TAG = "FileHandler";

    public static void saveToFile(Context context, String block_name, String note) {
        String filename = block_name + ".txt";
        File file = new File(context.getFilesDir(), filename);
        if (file.exists()) {
            for (String saved_note : readFileContents(context, block_name).split("<next note>")){
                if (saved_note.equals(note)) {
                    Log.i(TAG, "Уже есть заметка, поэтому сохранение пропущено");
                    Toast.makeText(context, R.string.note_saving_skipped, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        try {
            FileOutputStream outputStream;
            if (file.exists()) {
                outputStream = new FileOutputStream(file, true);
            }
            else {
                outputStream = new FileOutputStream(file);
            }
            outputStream.write((note + "<next note>").getBytes());
            outputStream.close();
            Log.d(TAG, "note till saving:\n" + note);
        } catch (IOException e) {
            Toast.makeText(context, R.string.note_saving_failed, Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Ошибка при сохранения в файл: " + e.getMessage(), e);
        }
    }

    public static String readFileContents(Context context, String block_name) {
        String filename = block_name + ".txt";
        File file = new File(context.getFilesDir(), filename);
        StringBuilder fileContents = new StringBuilder();

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = "\n" + line;
                    fileContents.append(line).append("<next note>");
                }
            } catch (IOException e) {
                Log.e(TAG, "Ошибка при чтении файла: " + e.getMessage(), e);
                return "Ошибка при чтении файла";
            }
        }
        else {
            Log.w(TAG, "Файл не найден: " + filename);
            return "Файл не найден.";
        }
        return fileContents.toString().trim();
    }

    private static boolean deleteFile(Context context, String block_name) {
        File file = new File(context.getFilesDir(), block_name + ".txt");
        if (file.exists()) {
            return file.delete();
        }
        else {
            Log.w(TAG, "Попытка удалить несуществующий файл: " + file.getName());
            Toast.makeText(context, R.string.attempt_to_delete_non_existing_file, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public static boolean deleteNote(Context context, String block_name, String note) {
        Log.d(TAG, "block name:\n" + block_name + "\nnote:\n" + note);
        File file_block = new File(context.getFilesDir(), block_name + ".txt");
        if (!file_block.exists()) {
            Log.wtf(TAG, "Attempt to delete note from " + block_name + ".txt" + " has failed " +
                    "because of non-existent " + block_name + ".txt");
            return false;
        }
        ArrayList<String> file_content = new ArrayList<>(Arrays.asList(readFileContents(context, block_name).split("<next note>")));

        Log.d(TAG, "file_content:\n" + file_content);

        file_content.remove(note);

        Log.d(TAG, "file_content after removal:\n" + file_content);

        if (!deleteFile(context, block_name)) {
            return false;
        }

        StringBuilder note_to_save = new StringBuilder();
        for (String s : file_content) {
            if (s.isEmpty()) continue;
            Log.d(TAG, "s:\n" + s);
            note_to_save.append(s).append("<next note>");
            Log.d(TAG, "note_to_save:\n" + note_to_save);
        }
        if (!note_to_save.toString().isEmpty()) {
            saveToFile(context, block_name, String.valueOf(note_to_save));
        }
        return true;
    }
//    public static File[] listFilesInDirectory(Context context) {
//        File directory = context.getFilesDir();
//
//        return directory.listFiles();
//    }
}
