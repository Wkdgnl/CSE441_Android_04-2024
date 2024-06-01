package com.example.book.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder>{
    private List<Story> stories;
    private Context context;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public BookmarkAdapter(List<Story> stories) {
        this.stories = stories;
    }

    @NonNull
    @Override
    public BookmarkAdapter.BookmarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_bookmark, parent, false);
        return new BookmarkAdapter.BookmarkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkAdapter.BookmarkViewHolder holder, int position) {
        Story story = stories.get(position);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(20));

        if (story.getImage() != null) {
            Glide.with(context)
                    .load(story.getImage())
                    .apply(requestOptions)
                    .into(holder.imageBook);
        } else {

        }
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

        // Thêm OnClickListener cho imageX
        holder.imageX.setOnClickListener(v -> {
            // Xóa truyện khỏi danh sách
            stories.remove(position);
            // Cập nhật RecyclerView
            notifyDataSetChanged();

            // Cập nhật SharedPreferences
            SharedPreferences sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            String readStories = sharedPreferences.getString("ReadStories", "");
            String[] storyIds = readStories.split(",");
            StringBuilder updatedReadStories = new StringBuilder();
            for (String storyId : storyIds) {
                if (!storyId.equals(String.valueOf(story.getId()))) {
                    updatedReadStories.append(storyId).append(",");
                }
            }
            Toast.makeText(context, "Đã xóa truyện khỏi Tủ truyện", Toast.LENGTH_SHORT).show();
            sharedPreferences.edit().putString("ReadStories", updatedReadStories.toString()).apply();
        });
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }
    public class BookmarkViewHolder extends RecyclerView.ViewHolder {
        ImageView imageBook, imageX;
        TextView categorySearchTxt, titleSearchTxt, authorSearchTxt;

        public BookmarkViewHolder(@NonNull View itemView) {
            super(itemView);
            imageBook = itemView.findViewById(R.id.imageBookmark);
            imageX = itemView.findViewById(R.id.imageX);
            categorySearchTxt = itemView.findViewById(R.id.categoryBookmarkTxt);
            titleSearchTxt = itemView.findViewById(R.id.titleBookmarkTxt);
            authorSearchTxt = itemView.findViewById(R.id.authorBookmarkTxt);
        }
    }
}
