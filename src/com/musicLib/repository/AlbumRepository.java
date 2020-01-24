package com.musicLib.repository;

import com.musicLib.entities.Album;
import com.musicLib.exceptions.ArtistNotFoundException;
import com.musicLib.exceptions.DuplicatedRecordException;

import java.sql.SQLException;
import java.util.List;

public interface AlbumRepository {

    boolean insert(Album album) throws ArtistNotFoundException, DuplicatedRecordException, SQLException;

    List<Album> queryAlbumsByArtistName(String artistName) throws SQLException;

    List<Album> queryByAlbumName(String albumName) throws SQLException;

    boolean delete(String albumName, String artistName) throws ArtistNotFoundException, Exception;


}
