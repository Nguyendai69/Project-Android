package com.example.musicapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.musicapp.R;
import com.example.musicapp.model.Song;
import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    private List<Song> songList;
    private List<String> favoriteIds = new ArrayList<>();
    private int playingPosition = -1;
    private OnSongActionListener actionListener;

    public interface OnSongActionListener {
        void onPlay(Song song, int position);
        void onToggleFavorite(Song song, int position);
        default int getFavoriteIconRes(boolean isFavorite, boolean isFavoriteScreen) {
            return isFavorite ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off;
        }
    }

    public SongAdapter(List<Song> songList, List<String> favoriteIds, int playingPosition, OnSongActionListener actionListener) {
        this.songList = songList;
        this.favoriteIds = favoriteIds;
        this.playingPosition = playingPosition;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songList.get(position);
        holder.titleTextView.setText(song.getTitle());
        holder.artistTextView.setText(song.getArtist());
        holder.playButton.setImageResource(playingPosition == position ? android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play);
        boolean isFavorite = favoriteIds.contains(song.getId());
        boolean isFavoriteScreen = false;
        if (actionListener != null) {
            try {
                isFavoriteScreen = actionListener.getClass().getMethod("getFavoriteIconRes", boolean.class, boolean.class).getDeclaringClass() != OnSongActionListener.class;
            } catch (Exception ignored) {}
        }
        int iconRes = actionListener != null ? actionListener.getFavoriteIconRes(isFavorite, isFavoriteScreen) : (isFavorite ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);
        holder.favoriteButton.setImageResource(iconRes);
        if (song.getImageResId() != 0) {
            holder.songImageView.setImageResource(song.getImageResId());
        } else {
            holder.songImageView.setImageResource(R.drawable.blue);
        }
        holder.playButton.setOnClickListener(v -> actionListener.onPlay(song, position));
        holder.favoriteButton.setOnClickListener(v -> actionListener.onToggleFavorite(song, position));
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public void updateList(List<Song> newList) {
        songList = newList;
        notifyDataSetChanged();
    }

    public void setPlayingPosition(int position) {
        playingPosition = position;
        notifyDataSetChanged();
    }
    public void setFavoriteIds(List<String> ids) {
        this.favoriteIds = ids;
        notifyDataSetChanged();
    }
    public void setSongs(List<Song> songs) {
        this.songList = songs;
        notifyDataSetChanged();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, artistTextView;
        ImageButton playButton, favoriteButton;
        ImageView songImageView;
        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.songTitleTextView);
            artistTextView = itemView.findViewById(R.id.songArtistTextView);
            playButton = itemView.findViewById(R.id.playButton);
            favoriteButton = itemView.findViewById(R.id.favoriteButton);
            songImageView = itemView.findViewById(R.id.songImageView);
        }
    }
}
