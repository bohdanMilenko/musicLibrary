package com.musicLib.MongoDatabaseModel;

import java.time.LocalDate;
import java.util.List;

public class MongoArtistRecord {

    private String artistName;
    private LocalDate dateFounded;
    private String genre;
    private List<AlbumMongo> album;

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public LocalDate getDateFounded() {
        return dateFounded;
    }

    public void setDateFounded(LocalDate dateFounded) {
        this.dateFounded = dateFounded;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public List<AlbumMongo> getAlbum() {
        return album;
    }

    public void setAlbum(List<AlbumMongo> album) {
        this.album = album;
    }
}
