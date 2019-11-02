package com.musicLib.databaseModel;

import java.util.ArrayList;
import java.util.List;

public class SongDAOImplementation implements DAO<Song> {

    private List<Song> songList = new ArrayList<>();

    @Override
    public void addEntity(Song song) {
        songList.add(song);
    }

    @Override
    public Song getEntity(int id) {
        return songList.get(id);
    }

    @Override
    public void editEntity(Song song, String newValue) {

    }

    @Override
    public void deleteEntity(Song song) {

    }

    @Override
    public List<Song> getAllEntities() {
        return songList;
    }
}