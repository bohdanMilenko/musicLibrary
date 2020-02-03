package com.musicLib.repository;

import com.musicLib.entities.Song;

import java.sql.SQLException;
import java.util.List;

public interface SongRepository {

    boolean add(Song song) throws SQLException;

    List<Song> queryByName(String songName) throws SQLException;

    boolean delete(Song song) throws SQLException;

    List<Song> queryByAlbumId(int albumId) throws SQLException;

    boolean deleteByAlbumId(int albumId) throws SQLException;
}
