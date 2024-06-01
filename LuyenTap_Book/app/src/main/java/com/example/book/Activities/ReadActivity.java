package com.example.book.Activities;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.book.Adapters.ChapterPagerAdapter;
import com.example.book.Database.ReadingProgressDbHelper;
import com.example.book.Fragments.ChapterFragment;
import com.example.book.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;

public class ReadActivity extends AppCompatActivity {
    TextView titleChapterTxt, percentReadTxt;
    TextToSpeech t1;
    ImageView imagetimepastnhat, imagepausenhat, imagetimenhat, imagerightarrownhat, imageleftarrownhat1, imageleftarrownhat0, imagetimearrownhat, imageBack;
    String content, read;
    String[] chunks;
    ViewPager2 viewPager;
    boolean isPause = false;
    SeekBar seekBar;
    ChapterPagerAdapter adapter;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_read);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        seekBar = findViewById(R.id.seekBar);
        imagetimepastnhat = findViewById(R.id.imagetimepastnhat);
        imagepausenhat = findViewById(R.id.imagepausenhat);
        imageleftarrownhat0 = findViewById(R.id.imageleftarrownhat0);
        imageleftarrownhat1 = findViewById(R.id.imageleftarrownhat1);
        imagerightarrownhat = findViewById(R.id.imagerightarrownhat);
        imagetimenhat = findViewById(R.id.imagetimenhat);
        imagetimearrownhat = findViewById(R.id.imagetimearrownhat);
        imageBack = findViewById(R.id.imageBack);
        viewPager = findViewById(R.id.view_pager);
        titleChapterTxt = findViewById(R.id.titleChapterTxt);
        percentReadTxt = findViewById(R.id.percentReadTxt);


        viewPager.setOffscreenPageLimit(2);

        Intent intent = getIntent();
        int currentChapterIndex = intent.getIntExtra("CURRENT_CHAPTER_INDEX", 0);
        int storyId = intent.getIntExtra("STORY_ID", -1);
        read = intent.getStringExtra("READ_TYPE"); // lấy giá trị của biến read
        int currentReadingPosition = intent.getIntExtra("CURRENT_READING_POSITION", 0);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(read + "/story" + storyId + "/chapters");

        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String readStories = sharedPreferences.getString("ReadStories", "");
        if (!readStories.contains(String.valueOf(storyId))) {
            readStories += storyId + ",";
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ReadStories", readStories);
        editor.apply();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> chapterContentsList = new ArrayList<>();
                ArrayList<String> chapterTitleList = new ArrayList<>();
                for (DataSnapshot chapterSnapshot : dataSnapshot.getChildren()) {
                    String chapterContent = chapterSnapshot.child("content").getValue(String.class);
                    String chapterTitle = chapterSnapshot.child("title").getValue(String.class);
                    chapterContentsList.add(chapterContent);
                    chapterTitleList.add(chapterTitle);
                }

                // Cài đặt adapter cho viewPager với danh sách nội dung và tiêu đề chương
                adapter = new ChapterPagerAdapter(ReadActivity.this, chapterContentsList, chapterTitleList, read + storyId, currentReadingPosition);
                viewPager.setAdapter(adapter);
                viewPager.setCurrentItem(currentChapterIndex, false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Stop the current TextToSpeech
                if (t1 != null) {
                    t1.stop();
                }
                // Get the content of the current chapter
                String currentChapterContent = adapter.getChapterContent(position);
                content = currentChapterContent.replace("\\n", "\n");
                chunks = content.split("\\.");

                // Update the chapter title
                String chapterTitle = "Chương " + (position + 1) + ": " + adapter.getChapterTitle(position);
                titleChapterTxt.setText(chapterTitle);

                // If the TextToSpeech is currently speaking, start speaking the new content
                if (isPause) {
                    for (int i = 0; i < chunks.length; i++) {
                        String chunk = chunks[i];
                        t1.speak(chunk, TextToSpeech.QUEUE_ADD, null, Integer.toString(i));
                    }
                }

                ReadingProgressDbHelper dbHelper = new ReadingProgressDbHelper(ReadActivity.this);
                if (dbHelper.isReadingProgressExist(read + storyId)) {
                    dbHelper.updateCurrentChapterIndex(read + storyId, position);
                } else {
                    dbHelper.insertReadingProgress(read + storyId, position, 0);
                }
            }
        });
        t1 = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(new Locale("vi", "VN"));
                    t1.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onStart(String utteranceId) {
                            // Code to execute when the utterance starts
                        }

                        @Override
                        public void onDone(String utteranceId) {
                            // Code to execute when the utterance is done
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    seekBar.setProgress(seekBar.getProgress() + 1);
                                }
                            });
                        }

                        @Override
                        public void onError(String utteranceId) {
                            // Code to execute in case of an error
                        }
                    });
                    imagepausenhat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (status != TextToSpeech.ERROR) {
                                if (isPause == false) {
                                    t1.setLanguage(new Locale("vi", "VN"));
                                    seekBar.setMax(chunks.length);
                                    for (int i = seekBar.getProgress(); i < chunks.length; i++) {
                                        String chunk = chunks[i];
                                        t1.speak(chunk, TextToSpeech.QUEUE_ADD, null, Integer.toString(i));
                                    }
                                    isPause = true;
                                    imagepausenhat.setImageResource(R.drawable.pausedam);
                                    imageleftarrownhat0.setImageResource(R.drawable.leftarrowdam);
                                    imageleftarrownhat1.setImageResource(R.drawable.leftarrowdam);
                                    imagetimepastnhat.setImageResource(R.drawable.timepastdam);
                                    imagetimenhat.setImageResource(R.drawable.timedam);
                                    imagetimearrownhat.setImageResource(R.drawable.rightarrowdam);
                                    imagerightarrownhat.setImageResource(R.drawable.rightarrowdam);
                                } else {
                                    t1.stop();
                                    isPause = false;
                                    imagepausenhat.setImageResource(R.drawable.playdam);
                                    imageleftarrownhat0.setImageResource(R.drawable.leftarrownhat);
                                    imageleftarrownhat1.setImageResource(R.drawable.leftarrownhat);
                                    imagetimepastnhat.setImageResource(R.drawable.timepastnhat);
                                    imagetimenhat.setImageResource(R.drawable.timenhat);
                                    imagetimearrownhat.setImageResource(R.drawable.rightarrownhat);
                                    imagerightarrownhat.setImageResource(R.drawable.rightarrownhat);
                                }
                            } else {
                                Log.e("TTS", "TextToSpeech is not ready to speak.");
                            }
                        }
                    });
                }
            }
        });

        imagetimepastnhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seekBar.getProgress() > 0) { // chỉ thực hiện nếu SeekBar không ở vị trí đầu tiên
                    t1.stop(); // dừng TextToSpeech
                    seekBar.setProgress(seekBar.getProgress() - 2); // giảm giá trị của SeekBar xuống 1
                    if (isPause) {
                        for (int i = seekBar.getProgress(); i < chunks.length; i++) { // bắt đầu từ đoạn văn bản tương ứng với giá trị mới của SeekBar
                            String chunk = chunks[i];
                            t1.speak(chunk, TextToSpeech.QUEUE_ADD, null, Integer.toString(i));
                        }
                    }
                }
            }
        });

        imageleftarrownhat0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seekBar.getProgress() > 0) { // chỉ thực hiện nếu SeekBar không ở vị trí đầu tiên
                    t1.stop(); // dừng TextToSpeech
                    seekBar.setProgress(seekBar.getProgress() - 1); // giảm giá trị của SeekBar xuống 1
                    if (isPause) {
                        for (int i = seekBar.getProgress(); i < chunks.length; i++) { // bắt đầu từ đoạn văn bản tương ứng với giá trị mới của SeekBar
                            String chunk = chunks[i];
                            t1.speak(chunk, TextToSpeech.QUEUE_ADD, null, Integer.toString(i));
                        }
                    }
                }
            }
        });

        imageleftarrownhat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seekBar.getProgress() > 0) { // chỉ thực hiện nếu SeekBar không ở vị trí đầu tiên
                    t1.stop(); // dừng TextToSpeech
                    seekBar.setProgress(seekBar.getProgress() - 1); // giảm giá trị của SeekBar xuống 1
                    if (isPause) {
                        for (int i = seekBar.getProgress(); i < chunks.length; i++) { // bắt đầu từ đoạn văn bản tương ứng với giá trị mới của SeekBar
                            String chunk = chunks[i];
                            t1.speak(chunk, TextToSpeech.QUEUE_ADD, null, Integer.toString(i));
                        }
                    }
                }
            }
        });

        imagetimenhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seekBar.getProgress() < seekBar.getMax()) { // chỉ thực hiện nếu SeekBar không ở vị trí cuối cùng
                    t1.stop(); // dừng TextToSpeech
                    seekBar.setProgress(seekBar.getProgress() + 2); // tăng giá trị của SeekBar lên 1
                    if (isPause) {
                        for (int i = seekBar.getProgress(); i < chunks.length; i++) { // bắt đầu từ đoạn văn bản tương ứng với giá trị mới của SeekBar
                            String chunk = chunks[i];
                            t1.speak(chunk, TextToSpeech.QUEUE_ADD, null, Integer.toString(i));
                        }
                    }
                }
            }
        });

        imagerightarrownhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seekBar.getProgress() < seekBar.getMax()) { // chỉ thực hiện nếu SeekBar không ở vị trí cuối cùng
                    t1.stop(); // dừng TextToSpeech
                    seekBar.setProgress(seekBar.getProgress() + 1); // tăng giá trị của SeekBar lên 1
                    if (isPause) {
                        for (int i = seekBar.getProgress(); i < chunks.length; i++) { // bắt đầu từ đoạn văn bản tương ứng với giá trị mới của SeekBar
                            String chunk = chunks[i];
                            t1.speak(chunk, TextToSpeech.QUEUE_ADD, null, Integer.toString(i));
                        }
                    }
                }
            }
        });

        imagetimearrownhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seekBar.getProgress() < seekBar.getMax()) { // chỉ thực hiện nếu SeekBar không ở vị trí cuối cùng
                    t1.stop(); // dừng TextToSpeech
                    seekBar.setProgress(seekBar.getProgress() + 1); // tăng giá trị của SeekBar lên 1
                    if (isPause) {
                        for (int i = seekBar.getProgress(); i < chunks.length; i++) { // bắt đầu từ đoạn văn bản tương ứng với giá trị mới của SeekBar
                            String chunk = chunks[i];
                            t1.speak(chunk, TextToSpeech.QUEUE_ADD, null, Integer.toString(i));
                        }
                    }
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) { // chỉ xử lý khi người dùng kéo SeekBar
                    t1.stop(); // dừng TextToSpeech
                    if (isPause) {
                        for (int i = progress; i < chunks.length; i++) { // bắt đầu từ đoạn văn bản tương ứng với giá trị mới của SeekBar
                            String chunk = chunks[i];
                            t1.speak(chunk, TextToSpeech.QUEUE_ADD, null, Integer.toString(i));
                        }
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Các hành động để thực hiện khi bắt đầu kéo SeekBar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Các hành động để thực hiện khi dừng kéo SeekBar
            }
        });
    }

    @Override
    protected void onPause() {
        if (t1 != null) {
            t1.stop();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (t1 != null) {
            t1.stop();
            t1.shutdown();
        }
        super.onDestroy();
    }

    private RecyclerView getRecyclerView(ViewPager2 viewPager2) {
        return (RecyclerView) viewPager2.getChildAt(0);
    }
}