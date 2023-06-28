package com.example.sad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.bumptech.glide.load.resource.gif.GifBitmapProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class SetsActivity_Admin extends AppCompatActivity {
    private GridView gridView;
    private Dialog load;
    private GridAdapter_Admin adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sets_admin);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        load = new Dialog(this);
        load.setContentView(R.layout.load);
        load.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        load.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        load.setCancelable(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));

        gridView =  findViewById(R.id.gridview);

         adapter = new GridAdapter_Admin(getIntent().getIntExtra("sets", 0), getIntent().getStringExtra("title"), new GridAdapter_Admin.ADDSets() {
            @Override
            public void addSet() {
                load.show();
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                firebaseDatabase.getReference().child("Categories").child(getIntent().getStringExtra("key")).child("sets").setValue(getIntent().getIntExtra("sets", 0)+1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            adapter.sets++;
                            adapter.notifyDataSetChanged();
                        }else{}
                        load.dismiss();
                    }

                });


            }
        });
        gridView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}