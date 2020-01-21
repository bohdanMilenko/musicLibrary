package com.musicLib;

import com.musicLib.entities.Album;
import com.musicLib.entities.Artist;
import com.musicLib.repository.ArtistRepository;
import com.musicLib.repository.MongoDBRepisotory.ArtistRepositoryMongo;
import com.musicLib.repository.SQLightRepository.AlbumRepository;
import com.musicLib.repository.SQLightRepository.ArtistsRepository;
import com.musicLib.services.ArtistService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ApplicationMain {

    public static void main(String[] args) {

        ArtistRepository artistRepositoryMongo = new ArtistRepositoryMongo();
        ArtistRepository artistRepositorySQLite = new ArtistsRepository();
        ArtistService artistService = new ArtistService();

        Artist artist = new Artist();
        artistService.add( artistRepositoryMongo,artist);

        List<Artist> query = artistService.queryAll(artistRepositorySQLite);

        //query.forEach(v-> System.out.println(v.getName()));

        AlbumRepository albumRepository = new AlbumRepository();
        List<Album> albumList = new ArrayList<>();
        try{
            albumList = albumRepository.queryByAlbumName("Pulse");
        }catch (SQLException e){
            System.out.println("Cannot query Albums by name: " +e.getMessage());
            e.printStackTrace();
        }

        albumList.forEach( v -> System.out.println( v.getName()));
    }
}
