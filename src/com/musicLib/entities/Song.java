package com.musicLib.entities;

public class Song {

    private int id;
    private int trackNumber;
    private int albumId;
    private String name;

    public Song(int trackNumber, int albumId, String name) {
        this.trackNumber = trackNumber;
        this.albumId = albumId;
        this.name = name;
    }

    public Song() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
