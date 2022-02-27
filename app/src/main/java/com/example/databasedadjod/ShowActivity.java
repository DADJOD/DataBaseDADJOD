package com.example.databasedadjod;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class ShowActivity extends AppCompatActivity {
    private TextView textViewName, textViewSecName, textViewEmail;
    private ImageView imageViewBD;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_layout);
        init();
        getIntentMain();
    }

    private void init() {
        textViewName = findViewById(R.id.textViewName);
        textViewSecName = findViewById(R.id.textViewSecondName);
        textViewEmail = findViewById(R.id.textViewEmail);
        imageViewBD = findViewById(R.id.imageViewBD);
    }

    private void getIntentMain() {
        Intent intent = getIntent();

        if (intent != null) {
            Picasso.get().load(intent.getStringExtra(Constant.USER_IMAGE)).into(imageViewBD);
            textViewName.setText(intent.getStringExtra(Constant.USER_NAME));
            textViewSecName.setText(intent.getStringExtra(Constant.USER_SECOND_NAME));
            textViewEmail.setText(intent.getStringExtra(Constant.USER_EMAIL));
        }
    }
}
