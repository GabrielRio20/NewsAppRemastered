package com.example.newsappremastered;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyNews extends AppCompatActivity {

    SharedPreferences getData1;
    SharedPreferences getData2;

    DatabaseReference databaseReference;
    DatabaseReference newsTitle;
    DatabaseReference userCreator;

    RecyclerView revMyNews;
    NewsAdapterMy newsAdapter;
    ArrayList<News> list;

    Button btnEdit, btnDelete;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_news);

        getData1 = getSharedPreferences("login_data", MODE_PRIVATE);
        getData2 = getSharedPreferences("title", MODE_PRIVATE);

        String user = getData1.toString();
        String title = getData2.toString();

        databaseReference = FirebaseDatabase.getInstance().getReference("news");
//        newsTitle = databaseReference.child(title);
//        userCreator = newsTitle.child(user);

        revMyNews = findViewById(R.id.rev_my_news);
        revMyNews.setHasFixedSize(true);
        revMyNews.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        list = new ArrayList<>();
        newsAdapter = new NewsAdapterMy(list, MyNews.this);
        revMyNews.setAdapter(newsAdapter);

        Intent intent = getIntent();
        String message = intent.getStringExtra(DetailUser.MESSAGE_EXTRA);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    News news = dataSnapshot.child("user").getValue().toString();
//                    String userUploader = dataSnapshot.child("user").getValue().toString();
                    News news = dataSnapshot.getValue(News.class);
                    if (news != null){
                        list.add(news);
                    }

//                    if(dataSnapshot.equals(user)){
//                        final News news = dataSnapshot.getValue(News.class);
//                        if (news != null){
//                            list.add(news);
//                        }
//                    }



//                    if(dataSnapshot.equals(user)){
//                        News news = dataSnapshot.getValue(News.class);
//                        list.add(news);
//                    }
                    newsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}