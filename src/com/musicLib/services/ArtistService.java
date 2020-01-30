package com.musicLib.services;

import com.musicLib.entities.Artist;
import com.musicLib.exceptions.QueryException;
import com.musicLib.exceptions.ServiceException;

import java.sql.SQLException;
import java.util.List;

public interface ArtistService {

    public boolean add(Artist artist) throws QueryException, ServiceException;

    public List<Artist> getAll() throws QueryException, ServiceException;

    public List<Artist> getByName(String artist) throws ServiceException;

    public boolean delete(String artistName) throws SQLException, QueryException, ServiceException;

}
