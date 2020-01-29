package com.musicLib.repository;

import com.musicLib.entities.Artist;
import com.musicLib.exceptions.ArtistNotFoundException;
import com.musicLib.exceptions.DuplicatedRecordException;

import java.sql.SQLException;
import java.util.List;

public interface ArtistRepository {

    boolean add(Artist artist) throws SQLException;

    List<Artist> queryAll() throws SQLException;

    List<Artist> queryArtist(String artistName) throws SQLException;

    boolean delete(String artistName) throws SQLException, ArtistNotFoundException, DuplicatedRecordException;

}
