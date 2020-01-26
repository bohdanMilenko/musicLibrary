package com.musicLib.services;

import com.musicLib.entities.Artist;
import com.musicLib.exceptions.QueryException;

import java.sql.SQLException;
import java.util.List;

public interface ArtistService {

    public boolean add(Artist artist);

    public List<Artist> getAll() throws QueryException;

    public List<Artist> getByName(String artist);

    public boolean delete(String artistName) throws SQLException, QueryException;

}
