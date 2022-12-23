package com.example.newsappremastered;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NewsList extends AppCompatActivity {

    SharedPreferences getData;
    TextView name;

    RecyclerView recyclerView;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    NewsAdapter newsAdapter;
    ArrayList<News> list;
    Button btnAddNews, btnMyNews;
    TextView kategori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        name = findViewById(R.id.name);
        btnAddNews = findViewById(R.id.btn_add_news);
        btnMyNews = findViewById(R.id.btn_my_news);
        kategori = findViewById(R.id.judul_kategori);

        recyclerView = findViewById(R.id.rev_news);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        list = new ArrayList<>();
        newsAdapter = new NewsAdapter(list, NewsList.this);
        recyclerView.setAdapter(newsAdapter);

        Intent intent = getIntent();
        String message = intent.getStringExtra(DetailUser.MESSAGE_EXTRA);
        int usiaUser = intent.getIntExtra("usiaUser", 1);
        kategori.setText(message);

        databaseReference.child("news").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    News news = dataSnapshot.getValue(News.class);
                    news.setKey(dataSnapshot.getKey());
                    list.add(news);
                    newsAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnAddNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewsList.this, AddNews.class));
            }
        });

        btnMyNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewsList.this, MyNews.class));
            }
        });
    }
}