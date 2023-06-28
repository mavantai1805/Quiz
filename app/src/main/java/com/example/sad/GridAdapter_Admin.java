package com.example.sad;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GridAdapter_Admin extends BaseAdapter {

    public int sets =0;
    private  String category;
    private ADDSets addSets;


    public GridAdapter_Admin(int sets ,String category,ADDSets addSets) {

        this.sets = sets;
        this.category = category;
        this.addSets = addSets;
    }


    @Override
    public int getCount() {
        return sets+1;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, final ViewGroup parent) {

        View view;

        if (convertView == null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_set_iteam,parent,false);
        }
        else {
            view = convertView;

        }
        if(i==0){
            ((TextView)view.findViewById(R.id.textview)).setText("+");
        }else {
            ((TextView) view.findViewById(R.id.textview)).setText(String.valueOf((i)));
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(i==0){
                        addSets.addSet();
                }else {


                    Intent questionIntent = new Intent(parent.getContext(),Create_Question_Admin.class);
                      questionIntent.putExtra("category",category);
                     questionIntent.putExtra("setNo",i+1);
                     parent.getContext().startActivity(questionIntent);
                }
            }
        });


        return view;
    }
    public interface ADDSets{
        public void addSet();
    }

}