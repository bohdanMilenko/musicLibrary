package com.musicLib.services;

import com.musicLib.entities.Album;
import com.musicLib.exceptions.ArtistNotFoundException;
import com.musicLib.exceptions.DuplicatedRecordException;
import com.musicLib.exceptions.QueryException;
import com.musicLib.repository.AlbumRepository;

import java.sql.SQLException;
import java.util.List;

public class AlbumServiceImpl implements AlbumService {

    private AlbumRepository albumRepo;
    private ArtistService artistService;
    private RecordValidator recordValidator;

    public AlbumServiceImpl(AlbumRepository albumRepo, ArtistService artistService) {
        this.albumRepo = albumRepo;
        this.artistService = artistService;
        this.recordValidator = new RecordValidator(artistService);;
    }

    public AlbumServiceImpl() {
    }

    public boolean add(Album album) throws QueryException {
        try {
            return albumRepo.add(album);
        } catch (ArtistNotFoundException e) {
            throw new QueryException("Cannot find such artist in db", e);
        } catch (DuplicatedRecordException e) {
            throw new QueryException("Multiple albums with the same name, unable to define a correct one", e);
        } catch (SQLException e) {
            throw new QueryException("Issue with db connectivity", e);
        }
    }

    public List<Album> getByArtistName(String artistName) throws QueryException {
        try {
            return albumRepo.queryAlbumsByArtistName(artistName);
        } catch (SQLException e) {
            throw new QueryException("Issue with db connectivity", e);
        }
    }

    public List<Album> getByName(String albumName)throws QueryException {
        try {
            return albumRepo.queryByAlbumName(albumName);
        } catch (SQLException e) {
            throw new QueryException("Issue with db connectivity", e);
        }
    }

    public boolean delete(String artistName, String albumName) throws QueryException {
        try {
            return albumRepo.delete(albumName, artistName);
        } catch (ArtistNotFoundException e) {
            throw new QueryException("Cannot find such artist in db", e);
        }catch (SQLException e) {
            throw new QueryException("Issue with db connectivity", e);
        }
    }
}
