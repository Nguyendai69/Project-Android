package com.example.musicapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.SearchView;
import com.example.musicapp.R;
import com.example.musicapp.adapter.SongAdapter;
import com.example.musicapp.model.Song;
import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {
    private List<Song> favoriteSongs = new ArrayList<>();
    private List<Song> filteredSongs = new ArrayList<>();
    private SongAdapter songAdapter;
    private SharedViewModel sharedViewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.favoritesRecyclerView);
        SearchView searchView = view.findViewById(R.id.searchViewFavorites);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        songAdapter = new SongAdapter(filteredSongs, new ArrayList<>(), -1, new SongAdapter.OnSongActionListener() {
            @Override
            public void onPlay(Song song, int position) {
                // Mở SongDetailActivity khi ấn phát bài hát trong danh sách yêu thích
                Intent intent = new Intent(getContext(), com.example.musicapp.Activity.SongDetailActivity.class);
                intent.putExtra("SONG_LIST", new ArrayList<>(filteredSongs));
                intent.putExtra("SONG_POSITION", position);
                startActivity(intent);
            }
            @Override
            public void onToggleFavorite(Song song, int position) {
                sharedViewModel.removeFavorite(song);
            }
            public int getFavoriteIconRes(boolean isFavorite, boolean isFavoriteScreen) {
                // Nếu là màn hình yêu thích thì trả về icon thùng rác
                return isFavoriteScreen ? android.R.drawable.ic_menu_delete : (isFavorite ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(songAdapter);
        sharedViewModel.getFavoriteSongs().observe(getViewLifecycleOwner(), new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                favoriteSongs = songs != null ? songs : new ArrayList<>();
                filteredSongs.clear();
                filteredSongs.addAll(favoriteSongs);
                songAdapter.updateList(filteredSongs);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterSongs(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filterSongs(newText);
                return true;
            }
            private void filterSongs(String query) {
                filteredSongs.clear();
                if (query == null || query.trim().isEmpty()) {
                    filteredSongs.addAll(favoriteSongs);
                } else {
                    String lower = query.toLowerCase();
                    for (Song song : favoriteSongs) {
                        if (song.getTitle().toLowerCase().contains(lower) || song.getArtist().toLowerCase().contains(lower)) {
                            filteredSongs.add(song);
                        }
                    }
                }
                songAdapter.updateList(filteredSongs);
            }
        });
        return view;
    }
}
