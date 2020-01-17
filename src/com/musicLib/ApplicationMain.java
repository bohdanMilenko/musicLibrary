package com.musicLib;

import com.musicLib.entities.Artist;
import com.musicLib.repository.ArtistRepository;
import com.musicLib.repository.MongoDBRepisotory.ArtistRepositoryMongo;
import com.musicLib.services.ArtistsService;

public class ApplicationMain {

    public static void main(String[] args) {

        ArtistRepository artistRepositoryMongo = new ArtistRepositoryMongo();
        ArtistsService artistsService = new ArtistsService(artistRepositoryMongo);

        Artist artist = new Artist();
        artistsService.add(artist);

    }
}
