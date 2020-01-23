package com.musicLib.entities;

import java.util.List;

public class Artist {

    private int id;
    private String name;
    private List<Album> albumList;


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

    public List<Album> getAlbumList() {
        return albumList;
    }

    public void setAlbumList(List<Album> albumList) {
        this.albumList = albumList;
    }

    @Override
    public String toString() {
        return "Artist Name: " + name;
    }
}
