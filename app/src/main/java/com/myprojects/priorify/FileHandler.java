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

public class FileHandler {

    private static final String TAG = "FileHandler";

    public static void saveToFile(Context context, String block_name, String note) {
        Log.i(TAG, "block_name:" + block_name + "\nnote: " + note);
        String filename = block_name + ".txt";
        File file = new File(context.getFilesDir(), filename);
        if (file.exists()) {
            for (String saved_note : readFileContents(context, block_name).split("<next note>")){
                if (saved_note.equals(note)) {
                    Log.i(TAG, "Уже есть заметка, поэтому сохранение пропущено");
                    Toast.makeText(context, "Уже есть заметка, поэтому сохранение пропущено", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        Log.i(TAG, note);
        try {
            FileOutputStream outputStream;
            if (file.exists()) {
                outputStream = new FileOutputStream(file, true);
                outputStream.write(("\n" + note).getBytes());
            }
            else {
                outputStream = new FileOutputStream(file);
                outputStream.write(note.getBytes());
            }
            outputStream.close();
        } catch (IOException e) {
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
        Log.i(TAG, fileContents.toString().trim());
        return fileContents.toString().trim();
    }

    public static boolean deleteFile(Context context, String block_name) {
        String filename = block_name + ".txt";
        File file = new File(context.getFilesDir(), filename);
        Log.i(TAG, block_name);

        if (file.exists()) {
            return file.delete();
        }
        else {
            Log.w(TAG, "Попытка удалить несуществующий файл: " + filename);
            return false; // Файл не найден
        }
    }
    public static File[] listFilesInDirectory(Context context) {
        File directory = context.getFilesDir();

        return directory.listFiles();
    }
}
