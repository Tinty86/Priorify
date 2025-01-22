package com.myprojects.priorify;

import android.content.Context;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.InputStreamReader;

public class FileHandler {

    public static void saveToFile(Context context, String block_name, String note) {
        String filename = block_name + ".txt";
        File file = new File(context.getFilesDir(), filename);
        if (file.exists()) {
            for (String saved_apology : readFileContents(context, block_name).split("<next apology>")){
                if (saved_apology.equals(note)) {
                    return;
                }
            }
        }
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
        } catch (IOException e) { e.printStackTrace();}
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
                    fileContents.append(line).append("<next apology>");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            return "Файл не найден.";
        }
        return fileContents.toString().trim();
    }

    public static boolean deleteFile(Context context, String block_name) {
        String filename = block_name + ".txt";
        File file = new File(context.getFilesDir(), filename);

        if (file.exists()) {
            return file.delete();
        }
        else {
            return false; // Файл не найден
        }
    }

    public static File[] listFilesInDirectory(Context context) {
        File directory = context.getFilesDir();
        return directory.listFiles();
    }
}
