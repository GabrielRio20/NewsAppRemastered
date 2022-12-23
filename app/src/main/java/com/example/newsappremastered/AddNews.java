package com.example.newsappremastered;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.newsappremastered.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

public class AddNews extends AppCompatActivity {
    EditText etTitle, etAuthor, etContent, etFIleName;
    Spinner spKategori;
    Button btnStoreNews, btnAddImage;
    ImageView ivAdd;
    SharedPreferences getData;
    SharedPreferences.Editor setData;

    Uri uri;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("news");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);

        setData  = getSharedPreferences("title", MODE_PRIVATE).edit();
        getData = getSharedPreferences("login_data", MODE_PRIVATE);

        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        etTitle = findViewById(R.id.et_title);
        etAuthor = findViewById(R.id.et_author);
        etContent = findViewById(R.id.et_content);
        spKategori = findViewById(R.id.sp_kategori);
        btnStoreNews = findViewById(R.id.btn_store_news);
//        btnAddImage = findViewById(R.id.btn_add_image);
//        ivAdd = findViewById(R.id.iv_add);
//        etFIleName = findViewById(R.id.et_image_name);

        //array spinner adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.labels_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //jika pada spinner terdapat value, maka set adapter sesuai yg di define di atas
        if(spKategori != null){
            spKategori.setAdapter(adapter);
        }

        btnStoreNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                News news = new News();
                String title = etTitle.getText().toString();
                String author = etAuthor.getText().toString();
                String content = etContent.getText().toString();
                String category = spKategori.getSelectedItem().toString();
                String user = getData.getString("username", null);

                news.setTitle(title);
                news.setAuthor(author);
                news.setContent(content);
                news.setCategory(category);
                news.setUser(user);

                databaseReference.push().setValue(news);
                Toast.makeText(AddNews.this, "Successfully insert data!", Toast.LENGTH_SHORT).show();
//                final String title = etTitle.getText().toString();
//                final String author = etAuthor.getText().toString();
//                final String content = etContent.getText().toString();
//                final String category = spKategori.getSelectedItem().toString();
//                final String user = getData.getString("username", null);
//
//                databaseReference.child("news").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        databaseReference.child("news").child(title).child("title").setValue(title);
//                        databaseReference.child("news").child(title).child("author").setValue(author);
//                        databaseReference.child("news").child(title).child("content").setValue(content);
//                        databaseReference.child("news").child(title).child("category").setValue(category);
//                        databaseReference.child("news").child(title).child("user").setValue(user);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

//                databaseReference.child("userNews").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        databaseReference.child("userNews").child(user).child("title").setValue(title);
//                        databaseReference.child("userNews").child(user).child("author").setValue(author);
//                        databaseReference.child("userNews").child(user).child("content").setValue(content);
//                        databaseReference.child("userNews").child(user).child("category").setValue(category);
//                        databaseReference.child("userNews").child(user).child("user").setValue(user);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
            }
        });
    }

    private void uploadImage(){
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, 101);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(AddNews.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101 && resultCode == RESULT_OK){
            uri = data.getData();
            ivAdd.setImageURI(uri);
        }
    }
}