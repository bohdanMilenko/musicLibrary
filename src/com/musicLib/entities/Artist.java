package com.musicLib.entities;

import java.util.List;

public class Artist {

    private int id;
    private String name;
    private List<Album> albums;


    public Artist(String name) {
        this.name = name;
    }

    public Artist() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    @Override
    public String toString() {
        return "Artist "
                +"\n\tID: " + id
                +"\n\tName: " + name;
    }
}
