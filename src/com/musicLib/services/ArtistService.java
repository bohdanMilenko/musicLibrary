package com.musicLib.services;

import com.musicLib.entities.Artist;
import com.musicLib.exceptions.QueryException;
import com.musicLib.exceptions.ServiceException;

import java.sql.SQLException;
import java.util.List;

public interface ArtistService {

    boolean add(Artist artist) throws QueryException, ServiceException;

    List<Artist> getAll() throws QueryException, ServiceException;

    List<Artist> getByName(Artist artist) throws ServiceException;

    boolean delete(Artist artist) throws SQLException, QueryException, ServiceException;

    void setRecordValidator(RecordValidator recordValidator);

    void setAlbumService(AlbumService albumService);

    Artist updateArtistID(Artist artist) throws ServiceException;

}

