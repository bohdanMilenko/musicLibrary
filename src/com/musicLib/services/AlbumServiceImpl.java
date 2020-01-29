package com.musicLib.services;

import com.musicLib.entities.Album;
import com.musicLib.entities.Artist;
import com.musicLib.exceptions.QueryException;
import com.musicLib.exceptions.ServiceException;
import com.musicLib.repository.AlbumRepository;

import java.sql.SQLException;
import java.util.List;

public class AlbumServiceImpl implements AlbumService {

    private AlbumRepository albumRepo;
    private RecordValidator recordValidator;

    public AlbumServiceImpl(AlbumRepository albumRepo, RecordValidator recordValidator) {
        this.albumRepo = albumRepo;
        this.recordValidator = recordValidator;
    }

    public AlbumServiceImpl() {
    }

    public boolean add(Album album) throws ServiceException {
        int artistID = getArtistID(album);
        album = updateWithArtistID(album, artistID);
        try {
            return albumRepo.add(album);
        } catch (SQLException e) {
            throw new ServiceException("Issue with adding album to db", e);
        }
    }

    private int getArtistID(Album album) throws ServiceException {
        return getArtistID(album.getName());
    }

    private int getArtistID(String album) throws ServiceException {
        try {
            return recordValidator.getAlbumID(album);
        } catch (QueryException e) {
            throw new ServiceException("Cannot get artist ID", e);
        }
    }

    private Album updateWithArtistID(Album album, int artistID) {
        Artist artist = album.getArtist();
        artist.setId(artistID);
        album.setArtist(artist);
        return album;
    }

    public List<Album> getByArtistName(String artistName) throws ServiceException {
        int artistID = getArtistIDByArtistName(artistName);
        try {
            return albumRepo.queryAlbumsByArtistID(artistID);
        } catch (SQLException e) {
            throw new ServiceException("Unable to query albums by artist name", e);
        }
    }

    private int getArtistIDByArtistName(String artistName) throws ServiceException {
        try {
            return recordValidator.getArtistID(artistName);
        } catch (QueryException e) {
            throw new ServiceException("Cannot get artist ID by artist name", e);
        }
    }

    public List<Album> getByName(String albumName) throws ServiceException {
        try {
            return albumRepo.queryByName(albumName);
        } catch (SQLException e) {
            throw new ServiceException("Issue with db connectivity", e);
        }
    }

    public boolean delete(String artistName, String albumName) throws ServiceException {
        int artistID = getArtistID(albumName);
        int albumID = recordValidator.getAlbumID(albumName);
        try {
//            songRepository.deleteByAlbumId(albumID);
            return albumRepo.delete(albumID, artistID);
        } catch (SQLException e) {
            throw new ServiceException("Unable to delete album", e);
        }
    }
}
