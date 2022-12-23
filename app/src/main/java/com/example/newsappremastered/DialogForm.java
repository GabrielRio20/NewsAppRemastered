package com.example.newsappremastered;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DialogForm extends DialogFragment {
    String title, author, content, key, user, choose;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("news");
    SharedPreferences getData;

    public DialogForm(String title, String author, String content, String key, String user, String choose) {
        this.title = title;
        this.author = author;
        this.content = content;
        this.key = key;
        this.user = user;
        this.choose = choose;
    }

    EditText etTitle, etAuthor, etContent;
    Spinner etCategory;
    Button updateNews;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_edit_news, container,false);
        etTitle = view.findViewById(R.id.et_title);
        etAuthor = view.findViewById(R.id.et_author);
//        etCategory = view.findViewById(R.id.sp_kategori);
        etContent = view.findViewById(R.id.et_content);
        updateNews = view.findViewById(R.id.btn_update_news);

        etTitle.setText(title);
        etAuthor.setText(author);
        etContent.setText(content);
        updateNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                News news = new News();
                String title = etTitle.getText().toString();
                String author = etAuthor.getText().toString();
//                String category = etCategory.getSelectedItem().toString();
                String content = etContent.getText().toString();
                String user = getData.getString("username", null);

                news.setTitle(title);
                news.setAuthor(author);
                news.setContent(content);
//                news.setCategory(category);
                news.setUser(user);

                if(choose.equals("Update")){
                    databaseReference.push().setValue(news).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(view.getContext(), "Updated", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(view.getContext(), "Failure", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog!= null){
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
