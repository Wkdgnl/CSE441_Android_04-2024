package com.example.book.Adapters;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChapterWebtoonAdapter extends RecyclerView.Adapter<ChapterWebtoonAdapter.ChapterViewHolder> {
    private Context context;
    private List<String[]> chapters;
    private int screenWidth;

    public ChapterWebtoonAdapter(Context context, List<String[]> chapters, int screenWidth) {
        this.context = context;
        this.chapters = chapters;
        this.screenWidth = screenWidth;
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        return new ChapterViewHolder(recyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        ImageAdapter imageAdapter = new ImageAdapter(context, chapters.get(position), screenWidth);
        holder.recyclerView.setAdapter(imageAdapter);
    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }

    public class ChapterViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        public ChapterViewHolder(@NonNull RecyclerView itemView) {
            super(itemView);
            recyclerView = itemView;
        }
    }
}
