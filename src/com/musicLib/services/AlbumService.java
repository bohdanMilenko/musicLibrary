package com.musicLib.services;

import com.musicLib.entities.Album;
import com.musicLib.exceptions.QueryException;
import com.musicLib.exceptions.ServiceException;

import java.util.List;

public interface AlbumService {

    public boolean add(Album album) throws QueryException, ServiceException;

    public List<Album> getByArtistName(String artistName) throws QueryException, ServiceException;

    public List<Album> getByName(String albumName) throws QueryException;

    public boolean delete(String artistName, String albumName) throws QueryException;
}
