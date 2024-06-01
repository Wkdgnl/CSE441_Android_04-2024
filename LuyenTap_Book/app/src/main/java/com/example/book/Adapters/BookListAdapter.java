package com.example.book.Adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
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
import com.example.book.Domain.ListBook;
import com.example.book.Domain.Stories;
import com.example.book.Domain.Story;
import com.example.book.R;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder> {
    private Stories stories;
    private Context context;
    private String readType;

    public BookListAdapter(Stories stories, String readType) {
        this.stories = stories;
        this.readType = readType;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_book, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Story story = (Story) stories.getStories().values().toArray()[position];
        holder.titleTxt.setText(story.getTitle());
        holder.categoryTxt.setText(story.getCategory());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));

        Glide.with(context)
                .load(story.getImage())
                .apply(requestOptions)
                .into(holder.pic);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
            intent.putExtra("id", story.getId());
            // Kiểm tra loại dữ liệu và truyền tham số tương ứng
            intent.putExtra("readType", readType);

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return stories.getStories().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTxt, categoryTxt;
        ImageView pic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            categoryTxt = itemView.findViewById(R.id.categoryTxt);
            pic = itemView.findViewById(R.id.pic);
        }
    }
}