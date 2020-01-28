package com.musicLib.repository;

import com.musicLib.entities.Album;
import com.musicLib.exceptions.QueryException;

import java.sql.SQLException;
import java.util.List;

public interface AlbumRepository {

    boolean add(Album album) throws SQLException, QueryException;

    List<Album> queryAlbumsByArtistID(int artistID) throws SQLException;

    List<Album> queryByName(String albumName) throws SQLException;

    boolean delete(int albumID, int artistID) throws  QueryException, SQLException;


}
