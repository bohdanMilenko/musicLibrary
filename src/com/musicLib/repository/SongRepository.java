package com.musicLib.repository;

import com.musicLib.entities.Song;

import java.sql.SQLException;
import java.util.List;

public interface SongRepository {

    boolean add(Song song) throws SQLException;

    List<Song> queryByName(String songName) throws SQLException;

    boolean delete(String artistName, String albumName, String songName);
}
