package com.musicLib;

import com.musicLib.entities.Album;
import com.musicLib.entities.Artist;
import com.musicLib.repository.ArtistRepository;
import com.musicLib.repository.MongoDBRepisotory.ArtistRepositoryMongo;
import com.musicLib.repository.SQLightRepository.AlbumRepository;
import com.musicLib.repository.SQLightRepository.ArtistsRepository;
import com.musicLib.services.AlbumService;
import com.musicLib.services.ArtistService;

import java.util.ArrayList;
import java.util.List;

public class ApplicationMain {

    public static void main(String[] args) {

        ArtistRepository artistRepositoryMongo = new ArtistRepositoryMongo();
        ArtistRepository artistRepositorySQLite = new ArtistsRepository();
        ArtistService artistService = new ArtistService();
        AlbumService albumService = new AlbumService();

        Artist artist = new Artist();
        artistService.add( artistRepositoryMongo,artist);

        List<Artist> query = artistService.queryAll(artistRepositorySQLite);

        //query.forEach(v-> System.out.println(v.getName()));

        AlbumRepository albumRepository = new AlbumRepository();
        List<Album> albumList = new ArrayList<>();
        albumList = albumService.queryByAlbumName(albumRepository, "Smth");

        Artist drake = new Artist();
        drake.setName("Drake");

        Album albumDrake = new Album();
        albumDrake.setName("Scorpion");
        albumDrake.setArtist(drake);
        System.out.println(albumService.add(albumRepository,"Drake",albumDrake ));

        albumList.forEach( v -> System.out.println( v.getName()));

        Artist ledZeppelin = new Artist();
        drake.setName("Led Zeppelin");

        Album albumGoingToCalifornia = new Album();
        albumGoingToCalifornia.setName("Going To California 4");
        albumGoingToCalifornia.setArtist(ledZeppelin);
        System.out.println(albumService.add(albumRepository,"Led Zeppelin",albumGoingToCalifornia ));

        albumService.delete
                //TODO FINISH IMPLEMENTING DELETE IN ARTISTS;

    }
}
