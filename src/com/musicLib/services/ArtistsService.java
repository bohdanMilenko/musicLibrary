package com.musicLib.services;

import com.musicLib.entities.Artist;
import com.musicLib.repository.ArtistRepository;

public class ArtistsService {

    private ArtistRepository artistRepo;

    public ArtistsService(ArtistRepository artistRepo) {
        this.artistRepo = artistRepo;
    }

    public boolean add(Artist artist) {
        return artistRepo.insert(artist);
    }

    
}
