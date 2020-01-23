package com.musicLib;

import com.musicLib.entities.Album;
import com.musicLib.entities.Artist;
import com.musicLib.entities.Song;
import com.musicLib.repository.AlbumRepository;
import com.musicLib.repository.ArtistRepository;
import com.musicLib.repository.MongoDBRepisotory.ArtistRepositoryMongo;
import com.musicLib.repository.SQLightRepository.AlbumRepositorySQL;
import com.musicLib.repository.SQLightRepository.ArtistRepositorySQL;
import com.musicLib.repository.SQLightRepository.SongRepositorySQL;
import com.musicLib.repository.SongRepository;
import com.musicLib.services.AlbumServiceImpl;
import com.musicLib.services.ArtistServiceImpl;
import com.musicLib.services.SongService;
import com.musicLib.services.SongServiceImpl;

import java.util.List;

public class ApplicationMain {

    public static void main(String[] args) {

        ArtistRepository artistRepositoryMongo = new ArtistRepositoryMongo();
        ArtistRepository artistRepositorySQLite = new ArtistRepositorySQL();
        ArtistServiceImpl artistServiceImpl = new ArtistServiceImpl();
        AlbumServiceImpl albumServiceImpl = new AlbumServiceImpl();

        Artist artist = new Artist();
       // artistServiceImpl.add( artistRepositoryMongo,artist);

        //List<Artist> query = artistServiceImpl.queryAll(artistRepositorySQLite);

        //query.forEach(v-> System.out.println(v.getName()));

        AlbumRepository albumRepository = new AlbumRepositorySQL();
        SongRepository   songRepositorySQL = new SongRepositorySQL();
//        List<Album> albumList = new ArrayList<>();
//        albumList = albumServiceImpl.queryByAlbumName(albumRepository, "Smth");
//
//        Artist drake = new Artist();
//        drake.setName("Drake");
//
//        Album albumDrake = new Album();
//        albumDrake.setName("Scorpion");
//        albumDrake.setArtist(drake);
//        System.out.println(albumServiceImpl.add(albumRepository,"Drake",albumDrake ));
//
//        albumList.forEach( v -> System.out.println( v.getName()));
//
//        Artist ledZeppelin = new Artist();
//        drake.setName("Led Zeppelin");
//
//        Album albumGoingToCalifornia = new Album();
//        albumGoingToCalifornia.setName("Going To California 4");
//        albumGoingToCalifornia.setArtist(ledZeppelin);
//        System.out.println(albumServiceImpl.add(albumRepository,"Led Zeppelin",albumGoingToCalifornia ));
//
//            artistServiceImpl.delete(artistRepositorySQLite, "Kendrick Lamar");

        SongService songService = new SongServiceImpl();
        List<Song> foundSongs = songService.queryBySongName(songRepositorySQL, "Flaming Telepaths");
        foundSongs.forEach(v -> System.out.println(v.getName()));
        Artist tempArtist = foundSongs.get(0).getArtist();
        Album tempAlbum = foundSongs.get(0).getAlbum();

        System.out.println(tempArtist.toString());
        System.out.println(tempAlbum.toString());
    }
}
