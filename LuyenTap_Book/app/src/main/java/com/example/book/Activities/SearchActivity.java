package com.example.book.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book.Adapters.SearchAdapter;
import com.example.book.Domain.Story;
import com.example.book.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    EditText searchEdt;
    ArrayList<Story> stories;
    SearchAdapter searchAdapter;
    ImageView imageBackSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        searchEdt = findViewById(R.id.searchEdt);
        RecyclerView searchView = findViewById(R.id.searchView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        searchView.setLayoutManager(layoutManager);
        stories = new ArrayList<>();
        searchAdapter = new SearchAdapter(stories);
        searchView.setAdapter(searchAdapter);
        imageBackSearch = findViewById(R.id.imageBackSearch);
        searchEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    performSearch();
                }
            }
        });
        imageBackSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Perform initial search based on category
        performSearch();
    }

    private void performSearch() {
        String searchText = searchEdt.getText().toString();

        if (!searchText.isEmpty()) {
            searchText = searchText.substring(0, 1).toUpperCase() + searchText.substring(1);
        }
        Intent intent = getIntent();
        String category = intent.getStringExtra("category");

        if (!searchText.isEmpty() || (category != null && !category.isEmpty())) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            Query query;

            if (category != null && !category.isEmpty()) {
                // Search by category
                query = ref.child("stories").orderByChild("category").startAt(category).endAt(category + "\uf8ff");
            } else {
                // Search by title
                query = ref.child("stories").orderByChild("title").startAt(searchText).endAt(searchText + "\uf8ff");
            }

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    stories.clear();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot storySnapshot: dataSnapshot.getChildren()) {
                            Story story = storySnapshot.getValue(Story.class);
                            stories.add(story);
                        }
                    } else {
                        // Handle case where no records were found
                        Log.e("SearchActivity", "No records found");
                    }
                    searchAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle possible errors.
                }
            });
        }
    }
}