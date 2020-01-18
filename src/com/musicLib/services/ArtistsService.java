package com.musicLib.services;

import com.musicLib.entities.Artist;
import com.musicLib.repository.ArtistRepository;

import java.util.List;

public class ArtistsService {

    public ArtistsService() {
    }

    public boolean add(ArtistRepository artistRepo, Artist artist) {
        return artistRepo.insert(artist);
    }

    public List<Artist> queryAll(ArtistRepository artistRepo){
        return  artistRepo.queryAllArtists();
    }

    public List<Artist> query(ArtistRepository artistRepo, String artist){
        return artistRepo.queryArtist(artist);
    }

    public boolean delete(ArtistRepository artistRepo, String artistName){
        return artistRepo.deleteArtist(artistName);
    }
}
