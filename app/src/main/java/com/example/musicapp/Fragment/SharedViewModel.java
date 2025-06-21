package com.example.musicapp.Fragment;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.musicapp.model.Song;
import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Song>> favoriteSongs = new MutableLiveData<>(new ArrayList<>());

    public SharedViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Song>> getFavoriteSongs() {
        return favoriteSongs;
    }

    public void setFavoriteSongs(List<Song> songs) {
        favoriteSongs.setValue(songs);
    }

    public void addFavorite(Song song) {
        List<Song> current = favoriteSongs.getValue();
        if (current == null) current = new ArrayList<>();
        if (!current.contains(song)) {
            current.add(song);
            favoriteSongs.setValue(current);
        }
    }

    public void removeFavorite(Song song) {
        List<Song> current = favoriteSongs.getValue();
        if (current != null && current.contains(song)) {
            current.remove(song);
            favoriteSongs.setValue(current);
        }
    }
}
