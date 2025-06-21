package com.example.musicapp.Activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.musicapp.Fragment.SharedViewModel;
import com.example.musicapp.R;
import com.example.musicapp.model.Song;
import java.util.ArrayList;
import java.util.List;

public class SongDetailActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private ImageButton playPauseButton;
    private ImageButton backButton;
    private ImageButton favoriteButton;
    private ImageButton prevButton;
    private ImageButton nextButton;
    private Song currentSong;
    private SharedViewModel sharedViewModel;
    private ArrayList<Song> songList;
    private int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_detail);
        // Sử dụng ViewModelProvider với activity cha để đồng bộ dữ liệu
        sharedViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(SharedViewModel.class);

        playPauseButton = findViewById(R.id.playPauseButton);
        backButton = findViewById(R.id.backButton);
        favoriteButton = findViewById(R.id.favoriteButton);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);
        TextView titleTextView = findViewById(R.id.songTitleDetail);
        TextView artistTextView = findViewById(R.id.songArtistDetail);
        ImageView songImageView = findViewById(R.id.songImageDetail);

        // Nhận danh sách bài hát và vị trí hiện tại
        songList = (ArrayList<Song>) getIntent().getSerializableExtra("SONG_LIST");
        currentPosition = getIntent().getIntExtra("SONG_POSITION", 0);
        if (songList == null || songList.isEmpty() || currentPosition < 0 || currentPosition >= songList.size()) {
            Toast.makeText(this, "Không có dữ liệu bài hát!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        currentSong = songList.get(currentPosition);
        // ...set up UI...
        titleTextView.setText(currentSong.getTitle());
        artistTextView.setText(currentSong.getArtist());
        int imageResId = currentSong.getImageResId();
        if (imageResId != 0) {
            songImageView.setImageResource(imageResId);
        } else {
            songImageView.setImageResource(R.drawable.blue);
        }
        sharedViewModel.getFavoriteSongs().observe(this, songs -> {
            updateFavoriteButton();
        });

        playPauseButton.setOnClickListener(v -> {
            if (isPlaying) {
                pauseSong();
            } else {
                playSong();
            }
        });
        backButton.setOnClickListener(v -> finish());
        favoriteButton.setOnClickListener(v -> {
            boolean wasFavorite = false;
            List<Song> currentFavorites = sharedViewModel.getFavoriteSongs().getValue();
            if (currentFavorites != null) {
                for (Song s : currentFavorites) {
                    if (s.getId().equals(currentSong.getId())) {
                        wasFavorite = true;
                        break;
                    }
                }
            }
            if (wasFavorite) {
                sharedViewModel.removeFavorite(currentSong);
                Toast.makeText(this, "Đã xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
            } else {
                sharedViewModel.addFavorite(currentSong);
                Toast.makeText(this, "Đã thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
            }
        });
        prevButton.setOnClickListener(v -> moveToSong(currentPosition - 1));
        nextButton.setOnClickListener(v -> moveToSong(currentPosition + 1));
    }

    private void moveToSong(int newPosition) {
        if (newPosition < 0 || newPosition >= songList.size()) {
            Toast.makeText(this, "Không còn bài nào!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        currentPosition = newPosition;
        currentSong = songList.get(currentPosition);
        TextView titleTextView = findViewById(R.id.songTitleDetail);
        TextView artistTextView = findViewById(R.id.songArtistDetail);
        ImageView songImageView = findViewById(R.id.songImageDetail);
        titleTextView.setText(currentSong.getTitle());
        artistTextView.setText(currentSong.getArtist());
        int imageResId = currentSong.getImageResId();
        if (imageResId != 0) {
            songImageView.setImageResource(imageResId);
        } else {
            songImageView.setImageResource(R.drawable.blue);
        }
        updateFavoriteButton();
        isPlaying = false;
        playPauseButton.setImageResource(android.R.drawable.ic_media_play);
    }

    private void playSong() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, currentSong.getResId());
            if (mediaPlayer == null) {
                // Báo lỗi nếu file nhạc không tồn tại
                Toast.makeText(this, "Không phát được", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        mediaPlayer.start();
        isPlaying = true;
        playPauseButton.setImageResource(android.R.drawable.ic_media_pause);
    }

    private void pauseSong() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        isPlaying = false;
        playPauseButton.setImageResource(android.R.drawable.ic_media_play);
    }

    private void updateFavoriteButton() {
        List<Song> currentFavorites = sharedViewModel.getFavoriteSongs().getValue();
        boolean isFavorite = false;
        if (currentFavorites != null) {
            for (Song s : currentFavorites) {
                if (s.getId().equals(currentSong.getId())) {
                    isFavorite = true;
                    break;
                }
            }
        }
        if (isFavorite) {
            favoriteButton.setImageResource(android.R.drawable.btn_star_big_on);
            favoriteButton.setContentDescription("Xóa khỏi danh sách yêu thích");
        } else {
            favoriteButton.setImageResource(android.R.drawable.btn_star_big_off);
            favoriteButton.setContentDescription("Thêm vào danh sách yêu thích");
        }
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
