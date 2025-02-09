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

    AppCompatButton BT_im_ur;
    AppCompatButton BT_non_im_ur;
    AppCompatButton BT_im_non_ur;
    AppCompatButton BT_non_im_non_ur;
    Button_On_Click_Listener MyListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyListener = new Button_On_Click_Listener(getApplicationContext());

        BT_im_ur = findViewById(R.id.high_left_button);
        BT_non_im_ur = findViewById(R.id.low_left_button);
        BT_im_non_ur = findViewById(R.id.high_right_button);
        BT_non_im_non_ur = findViewById(R.id.low_right_button);

        BT_im_ur.setOnClickListener(MyListener);
        BT_non_im_ur.setOnClickListener(MyListener);
        BT_im_non_ur.setOnClickListener(MyListener);
        BT_non_im_non_ur.setOnClickListener(MyListener);
    }

//    public void onClick(View view) {
//        AppCompatButton BT = findViewById(view.getId());
//        Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
//        Toast.makeText(this, BT.getText().toString(), Toast.LENGTH_SHORT).show();
//        intent.putExtra("block_name", BT.getText().toString());
//        startActivity(intent);
//    }
}