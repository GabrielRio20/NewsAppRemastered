package com.example.newsappremastered;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ListViewHolder> {
    private final ArrayList<News> newsList;
    private Activity activity;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public NewsAdapter(ArrayList<News> newsList, Activity activity) {
        this.newsList = newsList;
        this.activity = activity;
    }


    @NonNull
    @Override
    public NewsAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v  = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new ListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ListViewHolder holder, int position) {
        final News news = newsList.get(position);
//        Glide.with(holder.itemView.getContext()).load(news.getImage()).apply(new RequestOptions()
//                .override(100, 100)).into(holder.ivMiniPic);
        holder.tvTitle.setText(news.getTitle());
        holder.tvAuthor.setText(news.getAuthor());
        holder.tvContent.setText(news.getContent());

        //when item onclick
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NewsDetail.class);
                Bundle bundle = new Bundle();
//                bundle.putInt("picture", news.getImage());
                bundle.putString("title", news.getTitle());
                bundle.putString("author", news.getAuthor());
                bundle.putString("content", news.getContent());

                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });

//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                return false;
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class  ListViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvAuthor, tvContent;
        ImageView ivMiniPic;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvAuthor = itemView.findViewById(R.id.tv_author);
            tvContent = itemView.findViewById(R.id.tv_content);
        }
    }
}
