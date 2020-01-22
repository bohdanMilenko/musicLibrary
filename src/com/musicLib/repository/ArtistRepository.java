package com.musicLib.repository;

import com.musicLib.entities.Artist;
import com.musicLib.repositoryExceptions.ArtistNotFoundException;
import com.musicLib.repositoryExceptions.DuplicatedRecordException;

import java.sql.SQLException;
import java.util.List;

public interface ArtistRepository {

    boolean insert(Artist artist);

    List<Artist> queryAllArtists() throws SQLException;

    List<Artist> queryArtist(String artistName);

    boolean deleteArtist(String artistName) throws SQLException, ArtistNotFoundException, DuplicatedRecordException;

}
