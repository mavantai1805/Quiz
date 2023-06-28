package com.example.sad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button startBtn,btn_SignOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        startBtn = findViewById(R.id.start_btn);
        startBtn.setOnClickListener(view -> {
            Intent categoryIntent = new Intent(MainActivity.this,CategoriesActivity.class);
            startActivity(categoryIntent);


        });



        btn_SignOut = findViewById(R.id.btn_Signout);
        btn_SignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent signout = new Intent(MainActivity.this,Login_From.class);
                startActivity(signout);
                finish();

            }
        });

    }
}