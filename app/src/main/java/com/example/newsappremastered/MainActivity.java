//package com.example.newsappremastered;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//
//import org.w3c.dom.Text;
//
//import java.util.ArrayList;
//import java.util.Objects;
//
//public class MainActivity extends AppCompatActivity {
//
//    SharedPreferences getData;
//    TextView name;
//
//    private RecyclerView recyclerView;
//    private ArrayList<News> news = new ArrayList<>();
//    TextView kategori;
//    NewsAdapter adapter;
//    Button btnAddNews;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        name = findViewById(R.id.name);
//
//        getData = getSharedPreferences("login_data", MODE_PRIVATE);
//        if(getData.contains("username")){
//            name.setText("Welcome, " + getData.getString("username", null));
//        }
//        else{
//            startActivity(new Intent(MainActivity.this, Login.class));
//            finish();
//        }
//
//        recyclerView = findViewById(R.id.rev_news);
//        kategori = findViewById(R.id.judul_kategori);
//        btnAddNews = findViewById(R.id.btn_add_news);
//
//        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//
//        adapter = new NewsAdapter(news);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(lm);
//        recyclerView.setHasFixedSize(true);
//
//        Intent intent = getIntent();
//        String message = intent.getStringExtra(DetailUser.MESSAGE_EXTRA);
//        int usiaUser = intent.getIntExtra("usiaUser", 1);
//        kategori.setText(message);
//
//        if(Objects.equals(message, "Game") && usiaUser < 17){
//            news.addAll(NewsData.showBeritaGame());
//        }
//        else if(Objects.equals(message, "Game") && usiaUser > 17){
//            news.addAll(NewsData.showBeritaGameAdult());
//            news.addAll(NewsData.showBeritaGame());
//        }
//        else if(Objects.equals(message, "Film") && usiaUser < 17){
//            news.addAll(NewsData.showBeritaFilm());
//        }
//        else if(Objects.equals(message, "Film") && usiaUser > 17){
//            news.addAll(NewsData.showBeritaFilmAdult());
//            news.addAll(NewsData.showBeritaFilm());
//        }
//        else if(Objects.equals(message, "Gadget") && usiaUser < 17){
//            news.addAll(NewsData.showBeritaGadget());
//        }
//        else if(Objects.equals(message, "Film") && usiaUser > 17){
//            news.addAll(NewsData.showBeritaGadgetAdult());
//            news.addAll(NewsData.showBeritaGadget());
//        }
//
//        btnAddNews.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, AddNews.class));
//            }
//        });
//    }
//}