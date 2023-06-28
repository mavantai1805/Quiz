package com.example.sad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class Create_Question_Admin extends AppCompatActivity {

    private Button add;
    private RecyclerView recyclerView;
    private QusetionAdapter qusetionAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question_admin);

        Toolbar toolbar = findViewById(R.id.toolbarQ);
        setSupportActionBar(toolbar);
        String Category_name = getIntent().getStringExtra("category");
        int set = getIntent().getIntExtra("setNo",1);
        getSupportActionBar().setTitle(Category_name+"/set "+set);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        add = findViewById(R.id.btn_add_Question);
       recyclerView = findViewById(R.id.recyclerviewQ);

        LinearLayoutManager layoutManager =  new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);


        List<QusetionModel> list =  new ArrayList<>();



        qusetionAdapter = new QusetionAdapter(list);
        recyclerView.setAdapter(qusetionAdapter);



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}