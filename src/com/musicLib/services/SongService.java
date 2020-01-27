package com.musicLib.services;

import com.musicLib.entities.Song;
import com.musicLib.exceptions.QueryException;
import com.musicLib.exceptions.ServiceException;

import java.util.List;

public interface SongService {

    public boolean add(Song song) throws  ServiceException;

    public List<Song> getByName(String songName) throws QueryException;

    public boolean delete(String artistName, String albumName, String songName) throws QueryException;
}
