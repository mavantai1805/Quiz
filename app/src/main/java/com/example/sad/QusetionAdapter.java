package com.example.sad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QusetionAdapter extends RecyclerView.Adapter<QusetionAdapter.ViewHolder> {


    private List<QusetionModel> list;

    public QusetionAdapter(List<QusetionModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item_admin,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String question = list.get(position).getQuestion();
        String ANS = list.get(position).getANS();
        holder.setData(question,ANS,position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
    private TextView question,ANS;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            question = itemView.findViewById(R.id.Question);
            ANS = itemView.findViewById(R.id.Answer);
        }
        private void setData(String qusetion, String ANS , int position){
            this.question.setText(position+1+", "+qusetion);
            this.ANS.setText("Ans "+ANS);
        }
    }
}
