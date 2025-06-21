package com.example.musicapp.Fragment;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.text.Editable;
import android.text.TextWatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.musicapp.R;
import com.example.musicapp.adapter.SongAdapter;
import com.example.musicapp.model.Song;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private List<Song> songList = new ArrayList<>();
    private List<String> favoriteIds = new ArrayList<>();
    private SongAdapter songAdapter;
    private SharedViewModel sharedViewModel;
    private MediaPlayer mediaPlayer;
    private int playingPosition = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.songRecyclerView);
        EditText editTextSearch = view.findViewById(R.id.editTextSearchHome);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        // Thêm dữ liệu mẫu
        if (songList.isEmpty()) {
            songList.add(new Song("1", "Chưa Từng Thương Ai Đến Vậy", "Toàn Thắng", R.raw.chuatungthuongaidenvay, R.drawable.chuatungthuongaidenvay));
            songList.add(new Song("2", "Có Lẽ Anh Chưa Từng", "Vkey", R.raw.coleanhchuatung, R.drawable.coleanhchuatung));
            songList.add(new Song("3", "Mưa Tháng Sáu (Remix)", "Nam Con", R.raw.muathangsau, R.drawable.muathangsau));
            songList.add(new Song("4", "Sa Vào Nguy Hiểm (Remix)", "China", R.raw.savaonguyhiem, R.drawable.savaonguyhiem));
        }
        songAdapter = new SongAdapter(songList, favoriteIds, playingPosition, new SongAdapter.OnSongActionListener() {
            @Override
            public void onPlay(Song song, int position) {
                playSong(song, position);
            }
            @Override
            public void onToggleFavorite(Song song, int position) {
                if (favoriteIds.contains(song.getId())) {
                    favoriteIds.remove(song.getId());
                    sharedViewModel.removeFavorite(song);
                } else {
                    favoriteIds.add(song.getId());
                    sharedViewModel.addFavorite(song);
                }
                songAdapter.setFavoriteIds(favoriteIds);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(songAdapter);

        // Lắng nghe thay đổi text để tìm kiếm
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterSongs(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        sharedViewModel.getFavoriteSongs().observe(getViewLifecycleOwner(), songs -> {
            favoriteIds.clear();
            if (songs != null) {
                for (Song song : songs) {
                    favoriteIds.add(song.getId());
                }
            }
            songAdapter.setFavoriteIds(favoriteIds);
        });
        return view;
    }

    private void filterSongs(String query) {
        List<Song> filteredList = new ArrayList<>();
        for (Song song : songList) {
            if (song.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                song.getArtist().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(song);
            }
        }
        songAdapter.setSongs(filteredList);
    }

    private void playSong(Song song, int position) {
        // Chuyển sang SongDetailActivity thay vì phát nhạc trực tiếp
        Intent intent = new Intent(getContext(), com.example.musicapp.Activity.SongDetailActivity.class);
        intent.putExtra("SONG_LIST", new ArrayList<>(songList));
        intent.putExtra("SONG_POSITION", position);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
