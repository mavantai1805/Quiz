package com.example.sad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.NoCopySpan;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class QuestionsActivity extends AppCompatActivity {


    private TextView question,noIndicator;
    private FloatingActionButton bookmarkBtn;
    private LinearLayout optionsContainer;
    private Button shareBtn,nextBtn;

    private List<QusetionModel> list;

    private int position =0;

    private int score =0;

    private  int count = 0;
    private  String category;
    private int setNo;
    private Dialog load;



    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        question = findViewById(R.id.question);
        noIndicator = findViewById(R.id.no_indicator);

        optionsContainer = findViewById(R.id.options_container);

        nextBtn = findViewById(R.id.next);






        category = getIntent().getStringExtra("category");
        setNo = getIntent().getIntExtra("setNo",1);

        load = new Dialog(this);
        load.setContentView(R.layout.load);

        load.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        load.setCancelable(false);



        list = new ArrayList<>();
        load.show();
        databaseReference.child("SETS").child(category).child("questions").orderByChild("setNo")
                .equalTo(setNo).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot :dataSnapshot.getChildren()){
                                list.add(snapshot.getValue(QusetionModel.class));
                            }
                            if (list.size()>0){

                                for (int i = 0; i < 4; i++){
                                    optionsContainer.getChildAt(i).setOnClickListener(view -> {
                                        checkAnswer((Button)view);
                                    });
                                }
                                play(question,0,list.get(position).getQuestion());
                                nextBtn.setOnClickListener(view -> {
                                    nextBtn.setEnabled(false);
                                    nextBtn.setAlpha(0.7f);
                                    enableOption(true);
                                    position++;
                                    if (position == list.size()) {
                                        Intent Socre = new Intent(QuestionsActivity.this,ScoreActivity.class);
                                        Socre.putExtra("score",score);
                                        Socre.putExtra("total",list.size());
                                        startActivity(Socre);
                                        finish();
                                        return;
                                    }
                                    count = 0;
                                    play(question,0,list.get(position).getQuestion());
                                });
                            }else {
                                finish();
                                Toast.makeText(QuestionsActivity.this, "No Questions", Toast.LENGTH_SHORT).show();
                            }
                            load.dismiss();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(QuestionsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        load.dismiss();
                        finish();
                    }
                });

    }




    private void play(View view, int value, String data){
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(@NonNull Animator animator) {
                        if(value == 0 && count  < 4){
                            String option = "";
                            if (count == 0){
                                option = list.get(position).getOptionA();
                            }else if (count ==1){
                                option = list.get(position).getOptionB();
                            }else if (count ==2){
                                option = list.get(position).getOptionC();
                            }else if (count ==3){
                                option = list.get(position).getOptionD();
                            }
                            play(optionsContainer.getChildAt(count),0,option);
                            count++;
                        }
                    }

                    @Override
                    public void onAnimationEnd(@NonNull Animator animator) {
                        if(value == 0){
                            try {
                                ((TextView)view).setText(data);
                                noIndicator.setText(position+1+"/"+list.size());

                            }catch (ClassCastException ex) {
                                ((Button)view).setText(data);
                            }

                            view.setTag(data);
                            play(view,1,data);
                        }
                    }

                    @Override
                    public void onAnimationCancel(@NonNull Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(@NonNull Animator animator) {

                    }
                });
    }
    private void checkAnswer(Button selectedOption){
        enableOption(false);
        nextBtn.setEnabled(true);
        nextBtn.setAlpha(1);
        if (selectedOption.getText().toString().equals(list.get(position).getANS())) {
            //correct
            score++;
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#008000")));
        }else {

            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FA7074")));
            Button correctoption = (Button) optionsContainer.findViewWithTag(list.get(position).getANS());
            correctoption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#008000")));
        }

    }

    private void enableOption(boolean enable){
        for (int i = 0; i < 4; i++){
            optionsContainer.getChildAt(i).setEnabled(enable);
            if (enable){
                optionsContainer.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#989898")));
            }
        }
    }

}
