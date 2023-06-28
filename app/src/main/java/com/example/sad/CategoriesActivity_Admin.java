package com.example.sad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoriesActivity_Admin extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();


    private Dialog load,categoty_add;
    private RecyclerView recyclerView;
    private List<CategoryModel_Admin> list;
    private CircleImageView addImage;
    private EditText category_name;
    private Button btn_add;
    private Uri img;
    private String downloadUil;
    private  CategoryAdapter_Admin adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_admin);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Categories");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        load = new Dialog(this);
        load.setContentView(R.layout.load);
        load.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        load.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        load.setCancelable(false);

        setCategotyDialog();


        recyclerView = findViewById(R.id.rv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);

        list = new ArrayList<>();

        adapter = new CategoryAdapter_Admin(list, new CategoryAdapter_Admin.Delete() {
            @Override
            public void onDelete(String key, int position) {
                new AlertDialog.Builder(CategoriesActivity_Admin.this)
                        .setTitle("Delete Category")
                        .setMessage("Are your sure , you want to delete this categogy ?")
                        .setPositiveButton("Delete ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                load.show();
                                databaseReference.child("Categories").child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            databaseReference.child("SETS").child(list.get(position).getName()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        list.remove(position);
                                                        adapter.notifyDataSetChanged();

                                                    }else {
                                                        Toast.makeText(CategoriesActivity_Admin.this, "Đã xảy ra sự cố delete !", Toast.LENGTH_SHORT).show();
                                                    }
                                                    load.dismiss();
                                                }
                                            });

                                        }else{
                                            Toast.makeText(CategoriesActivity_Admin.this, "Đã xảy ra sự cố delete !", Toast.LENGTH_SHORT).show();
                                            load.dismiss();
                                        }

                                    }
                                });
                            }
                              })
                        .setNegativeButton("Cancel",null)
                        .setIcon(android.R.drawable.ic_dialog_alert).show();

            }
        });
        recyclerView.setAdapter(adapter);


        load.show();
        databaseReference.child("Categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    try{
                        list.add(new CategoryModel_Admin(dataSnapshot1.child("name").getValue().toString(),
                                Integer.parseInt(dataSnapshot1.child("sets").getValue().toString()),
                                dataSnapshot1.child("url").getValue().toString(),
                                dataSnapshot.getKey()
                        ));
                    }catch (NullPointerException ex){
                        ex.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
                load.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CategoriesActivity_Admin.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                load.dismiss();
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.add){
                categoty_add.show();
        }

        return super.onOptionsItemSelected(item);
    }
    private void setCategotyDialog(){
        categoty_add = new Dialog(this);
        categoty_add.setContentView(R.layout.add_cattegort_dialog);
        categoty_add.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_box));
        categoty_add.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        categoty_add.setCancelable(true);

        addImage = categoty_add.findViewById(R.id.img);
        category_name = categoty_add.findViewById(R.id.category_name);
        btn_add = categoty_add.findViewById(R.id.btn_add);

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imgIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(imgIntent,111);

            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(category_name.getText().toString().isEmpty()){
                        category_name.setError("Chưa nhập tên thể loại !");
                        return;
                }
                for(CategoryModel_Admin model_admin : list){
                   if( category_name.getText().toString().equals(model_admin.getName())){
                       category_name.setError("Category fail !");
                       return;
                   }
                }
                if (img == null){
                    Toast.makeText(CategoriesActivity_Admin.this, "Vui lòng thêm ảnh", Toast.LENGTH_SHORT).show();
                    return;
                }
                categoty_add.dismiss();
                uploadData();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 111){
            if(resultCode == RESULT_OK){
                 img = data.getData();
                addImage.setImageURI(img);
            }
        }
    }
    private void uploadData(){
        load.show();

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference image_StorageReference = storageReference.child("categories").child(img.getLastPathSegment());

       UploadTask uploadTask = image_StorageReference.putFile(img);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return image_StorageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            downloadUil = task.getResult().toString();
                            uploadData_categories();
                        }
                        else {
                            Toast.makeText(CategoriesActivity_Admin.this, "Đã xảy ra sự cố !", Toast.LENGTH_SHORT).show();
                            load.dismiss();
                        }
                    }
                });
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                } else {
                    Toast.makeText(CategoriesActivity_Admin.this, "Đã xảy ra sự cố !", Toast.LENGTH_SHORT).show();
                    load.dismiss();
                }
            }
        });
    }
    private void uploadData_categories(){
        Map<String,Object> map = new HashMap<>();
        map.put("name",category_name.getText().toString());
        map.put("sets",0);
        map.put("url",downloadUil);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().child("Categories").child("category" +(list.size()+1))
                .setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                               list.add(new CategoryModel_Admin(category_name.getText().toString(),0,downloadUil,"category" +(list.size()+1)));
                                adapter.notifyDataSetChanged();

                        }
                        else {
                            Toast.makeText(CategoriesActivity_Admin.this, "Đã xảy ra sự cố !", Toast.LENGTH_SHORT).show();
                        }
                        load.dismiss();
                    }
                });
    }


}