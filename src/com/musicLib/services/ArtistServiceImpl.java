package com.musicLib.services;

import com.musicLib.entities.Artist;
import com.musicLib.repository.ArtistRepository;
import com.musicLib.exceptions.ArtistNotFoundException;
import com.musicLib.exceptions.DuplicatedRecordException;

import java.sql.SQLException;
import java.util.List;

public class ArtistServiceImpl implements ArtistService {

    public ArtistServiceImpl() {
    }

    public boolean add(ArtistRepository artistRepo, Artist artist) {
        return artistRepo.insert(artist);
    }

    public List<Artist> queryAll(ArtistRepository artistRepo) {
        try {
            return artistRepo.queryAllArtists();
        } catch (SQLException e3) {
            System.out.println("Cannot perform query (Add Album): " + e3.getMessage());
            e3.printStackTrace();
        }
        return null;
    }

    public List<Artist> query(ArtistRepository artistRepo, String artist) {
        return artistRepo.queryArtist(artist);
    }

    public boolean delete(ArtistRepository artistRepo, String artistName) {
        try {
            return artistRepo.delete(artistName);
        } catch (ArtistNotFoundException e) {
            System.out.println("There is no such artist to add an album: " + e.getMessage());
            e.printStackTrace();
        } catch (DuplicatedRecordException e2) {
            System.out.println("Such album already exists: " + e2.getMessage());
            e2.printStackTrace();
        } catch (SQLException e3) {
            System.out.println("Cannot perform query (Add Album): " + e3.getMessage());
            e3.printStackTrace();
        }
        return false;
    }
}

