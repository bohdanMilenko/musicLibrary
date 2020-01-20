package com.musicLib.repository;

import com.musicLib.entities.Album;
import com.musicLib.repositoryExceptions.ArtistNotFoundException;
import com.musicLib.repositoryExceptions.DuplicatedRecordException;

import java.sql.SQLException;
import java.util.List;

public interface AlbumRepository {

    boolean insert(Album album, String artistName) throws ArtistNotFoundException, DuplicatedRecordException, SQLException;

    List<Album> queryByArtistName(String artistName);

    boolean delete(String albumName, String artistName);


}
