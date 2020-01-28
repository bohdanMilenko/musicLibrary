package com.musicLib.services;

import com.musicLib.entities.Artist;
import com.musicLib.exceptions.ArtistNotFoundException;
import com.musicLib.exceptions.DuplicatedRecordException;
import com.musicLib.exceptions.QueryException;
import com.musicLib.exceptions.ServiceException;
import com.musicLib.repository.ArtistRepository;

import java.sql.SQLException;
import java.util.List;

public class ArtistServiceImpl implements ArtistService {

    private ArtistRepository artistRepo;


    public ArtistServiceImpl(ArtistRepository artistRepo) {
        this.artistRepo = artistRepo;
    }

    public ArtistServiceImpl() {
    }

    public boolean add(Artist artist) throws ServiceException {
        List<Artist> artists = artistRepo.queryArtist(artist.getName());
        if(artists.size()>0){
            throw new DuplicatedRecordException("Such artist already exists!");
        }
        try {
            return artistRepo.add(artist);
        }catch (SQLException e){
            throw new ServiceException("Cannot insert artist to db",e);
        }
    }

    public List<Artist> getAll() throws ServiceException {
        try {
            return artistRepo.queryAllArtists();
        } catch (SQLException e) {
            throw new ServiceException("Issue with getting all Artists", e);
        }
    }

    public List<Artist> getByName(String artist) {
        return artistRepo.queryArtist(artist);
    }

    public boolean delete(String artistName) throws QueryException {
        try {
            return artistRepo.delete(artistName);
        } catch (ArtistNotFoundException e) {
            throw new QueryException("There is no such Artist", e);
        } catch (DuplicatedRecordException e) {
            throw new QueryException("Multiple Artists present, cannot delete a specific one", e);
        } catch (SQLException e) {
            throw new QueryException("Issue with database connectivity", e);
        }
    }
}

