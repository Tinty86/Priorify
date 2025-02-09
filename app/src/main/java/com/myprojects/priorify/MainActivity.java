package com.myprojects.priorify;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

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
}