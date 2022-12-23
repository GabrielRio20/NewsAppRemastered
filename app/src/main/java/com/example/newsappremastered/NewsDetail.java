package com.example.newsappremastered;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class NewsDetail extends AppCompatActivity {

    TextView tvTitle, tvAuthor, tvContent;
    String title, author, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        tvTitle = findViewById(R.id.tv_title_detail);
        tvAuthor = findViewById(R.id.tv_author_detail);
        tvContent = findViewById(R.id.tv_content_detail);

        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("title");
        author = bundle.getString("author");
        content = bundle.getString("content");

        tvTitle.setText(title);
        tvAuthor.setText(author);
        tvContent.setText(content);

    }
}