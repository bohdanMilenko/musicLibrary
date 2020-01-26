package com.musicLib.repository;

import com.musicLib.entities.Album;
import com.musicLib.exceptions.QueryException;

import java.sql.SQLException;
import java.util.List;

public interface AlbumRepository {

    boolean add(Album album) throws SQLException, QueryException;

    List<Album> queryAlbumsByArtistName(String artistName) throws SQLException;

    List<Album> queryByAlbumName(String albumName) throws SQLException;

    boolean delete(String albumName, String artistName) throws  QueryException, SQLException;


}
