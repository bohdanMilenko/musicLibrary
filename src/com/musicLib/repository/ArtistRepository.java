package com.musicLib.repository;

import com.musicLib.entities.Artist;
import com.musicLib.exceptions.QueryException;

import java.sql.SQLException;
import java.util.List;

public interface ArtistRepository {

    boolean add(Artist artist) throws SQLException;

    List<Artist> getAll() throws SQLException;

    List<Artist> getByName(String artistName) throws SQLException;

    boolean delete(String artistName) throws SQLException, QueryException;

}
