package com.myprojects.priorify;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;

public class Button_On_Click_Listener implements View.OnClickListener{

    Context context;

    public Button_On_Click_Listener(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View view) {
        AppCompatButton BT = (AppCompatButton) view;
        Intent intent = new Intent(context, HistoryActivity.class);
        intent.putExtra("block_name", BT.getText().toString());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
