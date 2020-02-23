package com.musicLib.entities;

public class Song {

    private int id;
    private int trackNumber;
    private String name;
    private Album album;
    private Artist artist;

    public Song(int trackNumber, String name) {
        this.trackNumber = trackNumber;
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

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Song " +
                "\n\tID: " + id +
                "\n\tName: " + name +
                "\n\tTrack Number: " + trackNumber;
    }
}
