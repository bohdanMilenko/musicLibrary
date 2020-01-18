package com.musicLib;

import com.musicLib.entities.Artist;
import com.musicLib.repository.ArtistRepository;
import com.musicLib.repository.MongoDBRepisotory.ArtistRepositoryMongo;
import com.musicLib.services.ArtistService;

public class ApplicationMain {

    public static void main(String[] args) {

        ArtistRepository artistRepositoryMongo = new ArtistRepositoryMongo();
        ArtistService artistService = new ArtistService();

        Artist artist = new Artist();
        artistService.add( artistRepositoryMongo,artist);

    }
}
