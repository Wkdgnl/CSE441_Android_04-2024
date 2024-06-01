package com.example.book.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.book.Activities.DetailActivity;
import com.example.book.Domain.Story;
import com.example.book.R;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private List<Story> stories;
    private Context context;

    public SearchAdapter(List<Story> stories) {
        this.stories = stories;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_search, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        Story story = stories.get(position);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(20));

        Glide.with(context)
                .load(story.getImage())
                .apply(requestOptions)
                .into(holder.imageBook);
        holder.categorySearchTxt.setText("#"+story.getCategory());
        holder.titleSearchTxt.setText(story.getTitle());
        holder.authorSearchTxt.setText(story.getAuthor());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
            intent.putExtra("id", story.getId());
            // Kiểm tra loại dữ liệu và truyền tham số tương ứng
            intent.putExtra("readType", "Best");
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }
    public class SearchViewHolder extends RecyclerView.ViewHolder {
        ImageView imageBook;
        TextView categorySearchTxt, titleSearchTxt, authorSearchTxt;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            imageBook = itemView.findViewById(R.id.imageBook);
            categorySearchTxt = itemView.findViewById(R.id.categorySearchTxt);
            titleSearchTxt = itemView.findViewById(R.id.titleSearchTxt);
            authorSearchTxt = itemView.findViewById(R.id.authorSearchTxt);
        }
    }
}
