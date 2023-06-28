package com.example.sad;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    private TextView scored,total;
    private Button btn_done;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        scored = findViewById(R.id.scored);
        total =findViewById(R.id.total);
        btn_done = findViewById(R.id.btn_done);

        scored.setText(String.valueOf(getIntent().getIntExtra("score",0)));
        total.setText("/"+String.valueOf(getIntent().getIntExtra("total",0)));
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}