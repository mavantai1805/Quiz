package com.example.sad;

import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CategoryAdapter_Admin  extends RecyclerView.Adapter<CategoryAdapter_Admin.Viewholder> {
    private List<CategoryModel_Admin> categoryModelList;
    private Delete delete;

    public CategoryAdapter_Admin(List<CategoryModel_Admin> categoryModelList,Delete delete) {
        this.categoryModelList = categoryModelList;
        this.delete = delete;
    }

    @NonNull
    @Override
    public CategoryAdapter_Admin.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item_admin,parent,false);
        return  new CategoryAdapter_Admin.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter_Admin.Viewholder holder, int position) {
        holder.setData(categoryModelList.get(position).getUrl(),categoryModelList.get(position).getName(),categoryModelList.get(position).getSets(),categoryModelList.get(position).getKey(),position);

    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    class Viewholder extends RecyclerView.ViewHolder{

        private CircleImageView imageView;
        private TextView title;
        private ImageButton bt_delete;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            title = itemView.findViewById(R.id.title);
            bt_delete= itemView.findViewById(R.id.ic_delete);
        }
        private  void setData(String url, final String title, int sets, String key ,int position){

            Glide.with(itemView.getContext()).load(url).into(imageView);
            this.title.setText(title);

            itemView.setOnClickListener(view -> {
                Intent setIntent = new Intent(itemView.getContext(),SetsActivity_Admin.class);
                setIntent.putExtra("title",title);
                setIntent.putExtra("sets",sets);
                setIntent.putExtra("key",key);
                itemView.getContext().startActivity(setIntent);

            });
            bt_delete.setOnClickListener(new View.OnClickListener()  {
                @Override
                public void onClick(View view)
                {
                    delete.onDelete(key,position);
                }
            });
        }
    }

    public interface Delete{
        public void onDelete(String key, int position);
    }

}
