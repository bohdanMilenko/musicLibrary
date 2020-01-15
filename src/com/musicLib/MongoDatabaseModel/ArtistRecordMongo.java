package com.musicLib.MongoDatabaseModel;

import java.util.List;

public class ArtistRecordMongo {

    private String artistName;
    private int dateFounded;
    private String genre;
    private List<AlbumMongo> album;

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public int getDateFounded() {
        return dateFounded;
    }

    public void setDateFounded(int dateFounded) {
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

    private String getAllAlbumNames() {
        StringBuilder sb = new StringBuilder();
        album.forEach(album -> sb.append(album.toString()).append("\n"));
        return sb.toString();
    }

    @Override
    public String toString() {
        String returnString = artistName + " was founded in " + dateFounded + "\nAlbums are: ";
        return returnString + getAllAlbumNames();
    }
}
