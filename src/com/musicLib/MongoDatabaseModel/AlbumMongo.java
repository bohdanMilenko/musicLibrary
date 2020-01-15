package com.musicLib.MongoDatabaseModel;

import java.util.List;

public class AlbumMongo {

    private String albumName;
    private int numberOfSongs;
    private int yearReleased;
    private List<SongMongo> songMongoList;

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public int getNumberOfSongs() {
        return numberOfSongs;
    }

    public void setNumberOfSongs(int numberOfSongs) {
        this.numberOfSongs = numberOfSongs;
    }

    public int getYearReleased() {
        return yearReleased;
    }

    public void setYearReleased(int yearReleased) {
        this.yearReleased = yearReleased;
    }

    public List<SongMongo> getSongMongoList() {
        return songMongoList;
    }

    public void setSongMongoList(List<SongMongo> songMongoList) {
        this.songMongoList = songMongoList;
    }

    @Override
    public String toString() {
        return albumName;
    }
}
