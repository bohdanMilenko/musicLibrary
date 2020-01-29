package com.musicLib.services;

import com.musicLib.entities.Artist;
import com.musicLib.exceptions.ArtistNotFoundException;
import com.musicLib.exceptions.DuplicatedRecordException;
import com.musicLib.exceptions.ServiceException;
import com.musicLib.repository.ArtistRepository;

import java.sql.SQLException;
import java.util.List;

public class ArtistServiceImpl implements ArtistService {

    private ArtistRepository artistRepo;
    private RecordValidator recordValidator;

    public ArtistServiceImpl(ArtistRepository artistRepo, RecordValidator recordValidator) {
        this.artistRepo = artistRepo;
        this.recordValidator = recordValidator;
    }

    public ArtistServiceImpl() {
    }

    public boolean add(Artist artist) throws ServiceException {
        List<Artist> artists = artistRepo.queryArtist(artist.getName());
        if (artists.size() > 0) {
            throw new DuplicatedRecordException("Such artist already exists!");
        }
        try {
            return artistRepo.add(artist);
        } catch (SQLException e) {
            throw new ServiceException("Cannot insert artist to db", e);
        }
    }

    public List<Artist> getAll() throws ServiceException {
        try {
            List<Artist> artists = artistRepo.queryAll();
            artists = addDependantAlbums(artists);
            return artists;
        } catch (SQLException e) {
            throw new ServiceException("Issue with getting all Artists", e);
        }
    }

    private List<Artist> addDependantAlbums(List<Artist> artists) throws ServiceException {
        artists = recordValidator.addAlbumsToArtist(artists);
        return artists;
    }

    public List<Artist> getByName(String artist) throws ServiceException {
        try {
            List<Artist> artists = artistRepo.queryArtist(artist);
            artists = addDependantAlbums(artists);
            return artists;
        } catch (SQLException e) {
            throw new ServiceException("Issue with getting all Artists", e);
        }
    }

    public boolean delete(String artistName) throws ServiceException {
        try {
            return artistRepo.delete(artistName);
        } catch (ArtistNotFoundException e) {
            throw new ServiceException("There is no such Artist", e);
        } catch (DuplicatedRecordException e) {
            throw new ServiceException("Multiple Artists present, cannot delete a specific one", e);
        } catch (SQLException e) {
            throw new ServiceException("Issue with database connectivity", e);
        }
    }
}

