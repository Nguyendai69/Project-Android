package com.example.musicapp.model;

public class Song implements java.io.Serializable {
    private String id;
    private String title;
    private String artist;
    private String url;
    private int resId; // resource id for local audio file (in res/raw)
    private int imageResId; // resource id for song image

    public Song(String id, String title, String artist, String url, int imageResId) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.url = url;
        this.imageResId = imageResId;
    }

    public Song(String id, String title, String artist, int resId, int imageResId) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.resId = resId;
        this.url = null;
        this.imageResId = imageResId;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getUrl() { return url; }
    public int getResId() { return resId; }
    public int getImageResId() { return imageResId; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Song song = (Song) obj;
        return id != null && id.equals(song.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
