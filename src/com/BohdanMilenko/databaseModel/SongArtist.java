package com.BohdanMilenko.databaseModel;

public class SongArtist implements MusicLibrary{

    private String trackName;
    private String artistName;
    private String albumName;

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }


    public String getArtist() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }


    public String getAlbum() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }
}
