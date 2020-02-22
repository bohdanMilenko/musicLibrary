package com.musicLib;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.musicLib.entities.Album;
import com.musicLib.entities.Artist;
import com.musicLib.entities.Song;
import com.musicLib.exceptions.ServiceException;
import com.musicLib.mongoUtil.SessionManagerMongo;
import com.musicLib.repository.AlbumRepository;
import com.musicLib.repository.ArtistRepository;
import com.musicLib.repository.MongoDBRepisotory.AlbumRepositoryMongo;
import com.musicLib.repository.MongoDBRepisotory.ArtistRepositoryMongo;
import com.musicLib.repository.MongoDBRepisotory.SongRepositoryMongo;
import com.musicLib.repository.SongRepository;
import com.musicLib.services.*;
import org.mockito.Mock;

import java.sql.SQLException;
import java.util.List;

public class Main {

    ArtistRepository artistRepositoryMongo;
    AlbumRepository albumRepositoryMongo;
    SongRepository songRepositoryMongo;

    SongService songServiceMongo;
    AlbumService albumServiceMongo;
    ArtistService artistServiceMongo;

    public static void main(String[] args) throws SQLException, ServiceException {




        MongoClient mongoClient = SessionManagerMongo.getMongoClient();

        MongoDatabase db = SessionManagerMongo.getDbFromPropertyFile();
        ArtistRepositoryMongo artistRepositoryMongo = new ArtistRepositoryMongo();
        AlbumRepositoryMongo albumRepositoryMongo = new AlbumRepositoryMongo();
        SongRepositoryMongo songRepositoryMongo = new SongRepositoryMongo();
        Artist validArtist = new Artist();
        validArtist.setName("Led Zeppelin");
        validArtist.setId(2);

        Album album = new Album();
        album.setArtist(validArtist);
        album.setName("Led Zeppelin IV");

        Song song = new Song();
        song.setArtist(validArtist);
        song.setAlbum(album);
        song.setName("We Appreciate Power");
        //songRepositoryMongo.add(song);

//        List<Song> songs = songRepositoryMongo.getByName("War machine");
//        songs.forEach(v -> System.out.println(v.toString()));


        Main main = new Main();
        main.initializeServices();

        //TESTING ARTIST SERVICE FOR MONGODB
        //main.artistServiceMongo.add(validArtist);
       // System.out.println("artist added\n");
//        List<Artist> artists =  main.artistServiceMongo.getAll();
//        artists.forEach(v -> System.out.println(v.toString()));
//
//        List<Artist> artists2 = main.artistServiceMongo.getByName(validArtist);
//        artists2.forEach(v -> System.out.println(v.toString()));
//
        main.artistServiceMongo.delete(validArtist);
//
//        artists =  main.artistServiceMongo.getAll();
//        artists.forEach(v -> System.out.println(v.toString()));

        //TESTING ALBUM SERVICES FOR MONGODB
        //main.albumServiceMongo.add(album);
//        List<Album> albums = main.albumServiceMongo.get(album);
//        albums.forEach(v -> System.out.println(v.toString()));
       // main.albumServiceMongo.delete(album);

    }

    private static byte[] parseHexString(final String s) {
        byte[] b = new byte[16];
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16);
        }
        return b;
    }

    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    private  ArtistService initializeServices() {
        artistRepositoryMongo = new ArtistRepositoryMongo();
        albumRepositoryMongo = new AlbumRepositoryMongo();
        songRepositoryMongo = new SongRepositoryMongo();

        songServiceMongo = new SongServiceImpl(songRepositoryMongo);
        albumServiceMongo = new AlbumServiceImpl(albumRepositoryMongo);
        artistServiceMongo = new ArtistServiceImpl(artistRepositoryMongo);

        RecordValidator recordValidator = new RecordValidator(artistServiceMongo, albumServiceMongo, songServiceMongo);

        songServiceMongo.setAlbumService(albumServiceMongo);
        songServiceMongo.setRecordValidator(recordValidator);

        artistServiceMongo.setAlbumService(albumServiceMongo);
        artistServiceMongo.setRecordValidator(recordValidator);

        albumServiceMongo.setSongService(songServiceMongo);
        albumServiceMongo.setArtistService(artistServiceMongo);
        albumServiceMongo.setRecordValidator(recordValidator);

        return artistServiceMongo;
    }


}
