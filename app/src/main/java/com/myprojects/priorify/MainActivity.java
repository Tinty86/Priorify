package com.myprojects.priorify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        AppCompatButton BT = findViewById(view.getId());
        Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
        Toast.makeText(this, BT.getText().toString(), Toast.LENGTH_SHORT).show();
        intent.putExtra("block_name", BT.getText().toString());
        startActivity(intent);
    }
}