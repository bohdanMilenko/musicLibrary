package com.musicLib;

import com.musicLib.entities.Album;
import com.musicLib.entities.Artist;
import com.musicLib.entities.Song;
import com.musicLib.repository.AlbumRepository;
import com.musicLib.repository.ArtistRepository;
import com.musicLib.repository.SQLightRepository.AlbumRepositorySQL;
import com.musicLib.repository.SQLightRepository.ArtistRepositorySQL;
import com.musicLib.repository.SQLightRepository.SongRepositorySQL;
import com.musicLib.repository.SongRepository;
import com.musicLib.services.*;

import java.util.ArrayList;
import java.util.List;

public class ApplicationMain {

    public static void main(String[] args) {

        ArtistRepository artistRepositorySQLite = new ArtistRepositorySQL();
        AlbumRepository albumRepositorySQLite = new AlbumRepositorySQL();
        SongRepository songRepositorySQLite = new SongRepositorySQL();

        RecordValidator recordValidatorSQLite = new RecordValidator()

        ArtistService artistServiceImpl = new ArtistServiceImpl(artistRepositorySQLite);
        AlbumService albumServiceImpl = new AlbumServiceImpl(albumRepositorySQLite, artistServiceImpl);
        SongService songServiceSQL = new SongServiceImpl(songRepositorySQLite, artistServiceImpl, albumServiceImpl);


        List<Artist> query = artistServiceImpl.getAll();

        //query.forEach(v-> System.out.println(v.getName()));

        AlbumRepository albumRepository = new AlbumRepositorySQL();
        SongRepository songRepositorySQL = new SongRepositorySQL();

        List<Album> albumList = new ArrayList<>();
        albumList = albumServiceImpl.getByName("Smth");

        Artist drake = new Artist();
        drake.setName("Drake");
        Album albumDrake = new Album();
        albumDrake.setName("Scorpion");
        albumDrake.setArtist(drake);
        System.out.println(albumServiceImpl.add(albumDrake));

        albumList.forEach(v -> System.out.println(v.getName()));

        Artist ledZeppelin = new Artist();
        drake.setName("Led Zeppelin");
        Album albumGoingToCalifornia = new Album();
        albumGoingToCalifornia.setName("Going To California 4");
        albumGoingToCalifornia.setArtist(ledZeppelin);
        System.out.println(albumServiceImpl.add(albumGoingToCalifornia));

        artistServiceImpl.delete("Kendrick Lamar");

        SongService songService = new SongServiceImpl();
        List<Song> foundSongs = songService.getByName("Flaming Telepaths");
        foundSongs.forEach(v -> System.out.println(v.getName()));
        Artist tempArtist = foundSongs.get(0).getArtist();
        Album tempAlbum = foundSongs.get(0).getAlbum();

        System.out.println(tempArtist.toString());
        System.out.println(tempAlbum.toString());

        System.out.println(artistServiceImpl.delete("Blue Öyster Cult"));


    }

    private static void workWithSQLDb(){



    }
}
