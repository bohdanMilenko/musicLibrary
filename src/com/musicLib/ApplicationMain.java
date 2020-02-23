package com.musicLib;

import com.musicLib.entities.Album;
import com.musicLib.entities.Artist;
import com.musicLib.entities.Song;
import com.musicLib.exceptions.ServiceException;
import com.musicLib.repository.AlbumRepository;
import com.musicLib.repository.ArtistRepository;
import com.musicLib.repository.SQLightRepository.AlbumRepositorySQL;
import com.musicLib.repository.SQLightRepository.ArtistRepositorySQL;
import com.musicLib.repository.SQLightRepository.SongRepositorySQL;
import com.musicLib.repository.SongRepository;
import com.musicLib.services.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ApplicationMain {

    static SongService songServiceSQL;
    static AlbumService albumServiceSQL;
    static ArtistService artistServiceSQL;

    static ArtistRepositorySQL artistRepositorySQLite;
    static AlbumRepository albumRepositorySQLite;
    static SongRepository songRepositorySQLite;

    public static void main(String[] args) throws ServiceException, SQLException {

        initializeServices();

        Artist artistDrake = new Artist();
        artistDrake.setName("Drake");
        Album albumDrake = new Album();
        albumDrake.setName("Scorpion");
        albumDrake.setArtist(artistDrake);

        Artist artistLedZeppelin = new Artist();
        artistLedZeppelin.setName("Led Zeppelin");
        Album albumGoingToCalifornia = new Album();
        albumGoingToCalifornia.setName("Going To California 4");
        albumGoingToCalifornia.setArtist(artistLedZeppelin);

        Song songLedZeppelin = new Song();
        songLedZeppelin.setArtist(artistLedZeppelin);
        songLedZeppelin.setAlbum(albumGoingToCalifornia);
        songLedZeppelin.setName("Black Dog");

        artistServiceSQL.add(artistDrake);
        artistServiceSQL.add(artistLedZeppelin);

        List<Artist> artistList = artistServiceSQL.getByName(artistDrake);
        artistList.forEach(v -> System.out.println(v.getName()));

        List<Artist> artistList2 = artistServiceSQL.getAll();
        artistList2.forEach(v -> System.out.println(v.getName()));

        artistServiceSQL.delete(artistDrake);
        artistServiceSQL.delete(artistLedZeppelin);


//      TESTING ALBUM SERVICE FOR SQLITE
        albumServiceSQL.add(albumDrake);
        albumServiceSQL.add(albumGoingToCalifornia);
        List<Album> albumList = albumServiceSQL.getByArtist(artistDrake);
        albumList.forEach(v -> System.out.println(v.getName()));

        List<Album> albumList2 = albumServiceSQL.get(albumGoingToCalifornia);
        albumList2.forEach(v -> System.out.println(v.getName()));

        albumServiceSQL.delete(albumGoingToCalifornia);
        albumServiceSQL.deleteAlbumsFromArtist(artistDrake);



//      TESTING SONGS SERVICE FOR SQLITE
        songServiceSQL.add(songLedZeppelin);
        List<Song> foundSongs = songServiceSQL.get(songLedZeppelin);
        foundSongs.forEach(v -> System.out.println(v.toString()));

        List<Song> foundSongs2 = songServiceSQL.getByAlbum(albumGoingToCalifornia);
        foundSongs2.forEach(v -> System.out.println(v.toString()));

        songServiceSQL.delete(songLedZeppelin);


    }

    private static void initializeServices() {
        ArtistRepository artistRepositorySQLite = new ArtistRepositorySQL();
        AlbumRepository albumRepositorySQLite = new AlbumRepositorySQL();
        SongRepository songRepositorySQLite = new SongRepositorySQL();

        songServiceSQL = new SongServiceImpl(songRepositorySQLite);
        albumServiceSQL = new AlbumServiceImpl(albumRepositorySQLite);
        artistServiceSQL = new ArtistServiceImpl(artistRepositorySQLite);

        RecordValidator recordValidator = new RecordValidator(artistServiceSQL, albumServiceSQL, songServiceSQL);

        songServiceSQL.setAlbumService(albumServiceSQL);
        songServiceSQL.setRecordValidator(recordValidator);

        artistServiceSQL.setAlbumService(albumServiceSQL);
        artistServiceSQL.setRecordValidator(recordValidator);

        albumServiceSQL.setSongService(songServiceSQL);
        albumServiceSQL.setArtistService(artistServiceSQL);
        albumServiceSQL.setRecordValidator(recordValidator);
    }


}
