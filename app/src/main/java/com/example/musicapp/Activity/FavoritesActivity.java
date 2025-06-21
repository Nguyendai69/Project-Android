package com.example.musicapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.musicapp.R;
import com.example.musicapp.model.Song;
import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {
    private List<Song> favoriteSongs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        ListView listView = findViewById(R.id.favoritesListView);
        // Lấy danh sách bài hát yêu thích từ Intent
        favoriteSongs = (ArrayList<Song>) getIntent().getSerializableExtra("FAVORITE_SONGS");
        if (favoriteSongs == null) favoriteSongs = new ArrayList<>();

        FavoritesAdapter adapter = new FavoritesAdapter();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Song song = favoriteSongs.get(position);
                Intent intent = new Intent(FavoritesActivity.this, SongDetailActivity.class);
                intent.putExtra("SONG_ID", song.getId());
                intent.putExtra("TITLE", song.getTitle());
                intent.putExtra("ARTIST", song.getArtist());
                intent.putExtra("RES_ID", song.getResId());
                intent.putExtra("IMAGE_RES_ID", song.getImageResId());
                // Truyền danh sách id yêu thích mới nhất sang SongDetailActivity
                ArrayList<String> favoriteIds = new ArrayList<>();
                for (Song s : favoriteSongs) favoriteIds.add(s.getId());
                intent.putStringArrayListExtra("FAVORITE_IDS", favoriteIds);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("FAVORITE_SONGS", new ArrayList<>(favoriteSongs));
        setResult(RESULT_OK, resultIntent);
        super.onBackPressed();
    }

    private class FavoritesAdapter extends BaseAdapter {
        public int getCount() {
            return favoriteSongs.size();
        }

        public Object getItem(int position) {
            return favoriteSongs.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(FavoritesActivity.this).inflate(R.layout.favorites_list_item, parent, false);
            }
            TextView titleTextView = convertView.findViewById(R.id.songTitleTextView);
            ImageButton deleteButton = convertView.findViewById(R.id.deleteButton);
            ImageView songImageView = convertView.findViewById(R.id.songImageView);
            Song song = favoriteSongs.get(position);
            titleTextView.setText(song.getTitle());
            if (song.getImageResId() != 0) {
                songImageView.setImageResource(song.getImageResId());
            } else {
                songImageView.setImageResource(R.drawable.blue);
            }
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    favoriteSongs.remove(position);
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }
    }
}
