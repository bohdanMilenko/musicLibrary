package com.musicLib;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.musicLib.entities.Album;
import com.musicLib.entities.Artist;
import com.musicLib.entities.Song;
import com.musicLib.exceptions.ServiceException;
import com.musicLib.repository.AlbumRepository;
import com.musicLib.repository.ArtistRepository;
import com.musicLib.repository.MongoDBRepisotory.AlbumRepositoryMongo;
import com.musicLib.repository.MongoDBRepisotory.ArtistRepositoryMongo;
import com.musicLib.repository.MongoDBRepisotory.SongRepositoryMongo;
import com.musicLib.repository.SongRepository;
import com.musicLib.services.*;
import org.bson.Document;

import javax.print.Doc;
import java.lang.annotation.Documented;
import java.sql.SQLException;
import java.util.List;

public class Main {

    ArtistRepository artistRepositoryMongo;
    AlbumRepository albumRepositoryMongo;
    SongRepository songRepositoryMongo;

    SongService songServiceMongo;
    AlbumService albumServiceMongo;
    ArtistService artistServiceMongo;

//    private static final String mongoClientURIAdmin = "mongodb://root:password123@10.0.75.1:27018/admin";
//    private static final String mongoClientURI = "mongodb://root:password123@10.0.75.1:27018/TOP100SongsUK";
//    //private static MongoDatabase db =
//    private static MongoClient mongoClient;
//    private static MongoClient mongoClient2;
//    private static MongoDatabase actualDB;
//    private static MongoCollection<Document> artistCollection;
//
//    private static boolean loginToMongo(){
//        mongoClient = new MongoClient(new MongoClientURI(mongoClientURIAdmin));
//        MongoDatabase db = mongoClient.getDatabase("admin");
//        MongoCursor<String> dbs = mongoClient.listDatabaseNames().iterator();
//        while (dbs.hasNext()){
//            System.out.println( dbs.next());
//        }
//        actualDB = mongoClient.getDatabase("TOP100SongsUK");
//        artistCollection = actualDB.getCollection("bla");
//        artistCollection.insertOne(new Document().append("name", "Vasiliy"));
//        MongoCursor<String> dbs2 = mongoClient.listDatabaseNames().iterator();
//        while (dbs2.hasNext()){
//            System.out.println( dbs2.next());
//        }
//        return true;
//    }
    public static void main(String[] args) throws SQLException, ServiceException {

        //loginToMongo();




        Artist validArtist = new Artist();
        validArtist.setName("Jack White");
        validArtist.setId(2);

        Album album = new Album();
        album.setArtist(validArtist);
        album.setName("White Blood Cells");

        Song song = new Song();
        song.setArtist(validArtist);
        song.setAlbum(album);
        song.setName("We're gonna be friends");

        Main main = new Main();
        main.initializeServices();

//      TESTING ARTIST SERVICE FOR MONGODB
        main.artistServiceMongo.add(validArtist);

        List<Artist> artists = main.artistServiceMongo.getAll();
        artists.forEach(v -> System.out.println(v.toString()));

        List<Artist> artists2 = main.artistServiceMongo.getByName(validArtist);
        artists2.forEach(v -> System.out.println(v.toString()));

        //main.artistServiceMongo.delete(validArtist);

//      TESTING ALBUM SERVICES FOR MONGODB
        main.albumServiceMongo.add(album);

        List<Album> albums = main.albumServiceMongo.get(album);
        albums.forEach(v -> System.out.println(v.toString() + "\n" + v.getArtist().toString()));

        //main.albumServiceMongo.delete(album);

//      TESTING SONGS SERVICES FOR MONGO
        main.songServiceMongo.add(song);

        List<Song> songs = main.songServiceMongo.getByAlbum(album);
        songs.forEach(v -> System.out.println(v.toString()));

        List<Song> songs2 = main.songServiceMongo.get(song);
        songs2.forEach(v -> System.out.println(v.toString() + "\n" + v.getAlbum().toString() + "\n" + v.getArtist().toString()));

        //main.songServiceMongo.deleteSongsFromAlbum(album);

        //main.songServiceMongo.delete(song);
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

    private ArtistService initializeServices() {
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
