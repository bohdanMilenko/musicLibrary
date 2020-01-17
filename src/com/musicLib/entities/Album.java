package com.musicLib.entities;

import com.musicLib.databaseModel.Song;

import java.time.LocalDate;
import java.util.List;

public class Album {

    private int id;
    private String name;
    private LocalDate yearReleased;
    private Artist artist;
    private List<Song> songs;

    public Album(String name, LocalDate yearReleased, Artist artist, List<Song> songs) {
        this.name = name;
        this.yearReleased = yearReleased;
        this.artist = artist;
        this.songs = songs;
    }

    //TODO Create interface for service as well

    public Album(){

    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getYearReleased() {
        return yearReleased;
    }

    public void setYearReleased(LocalDate yearReleased) {
        this.yearReleased = yearReleased;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }
}
