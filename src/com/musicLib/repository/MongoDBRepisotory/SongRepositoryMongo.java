package com.musicLib.repository.MongoDBRepisotory;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.musicLib.entities.Album;
import com.musicLib.entities.Artist;
import com.musicLib.entities.Song;
import com.musicLib.repository.mongoUtil.SessionManagerMongo;
import com.musicLib.repository.SongRepository;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;
import static com.musicLib.repository.MongoDBRepisotory.MetaDataMongo.*;

public class SongRepositoryMongo implements SongRepository {


    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> songsCollection;


    public SongRepositoryMongo() {
        mongoDatabase = SessionManagerMongo.getDbFromPropertyFile();
        songsCollection = mongoDatabase.getCollection(SONGS_COLLECTION);
    }


    @Override
    public boolean add(Song song) {
        Document albumForSong = new Document();
        albumForSong.append(ALBUM_ID, song.getAlbum().getId()).append(ALBUM_NAME, song.getAlbum().getName());
        Document artistForSong = new Document();
        artistForSong.append(ARTIST_ID, song.getArtist().getId()).append(ARTIST_NAME, song.getArtist().getName());
        Document songToAdd = new Document();
        songToAdd.append(SONG_ID, MetaDataMongo.getNextSequence(songsCollection)).append(SONG_NAME, song.getName())
                .append(SONG_ARTIST_INFO, artistForSong).append(SONG_ALBUM_INFO, albumForSong);
        songsCollection.insertOne(songToAdd);
        System.out.println("Inserted: " + song.toString());
        return true;
    }

    @Override
    public List<Song> getByName(String songName) {
        try (MongoCursor<Document> cursor = songsCollection.find(eq(SONG_NAME, songName)).iterator()) {
            return cursorToSongs(cursor);
        }

    }

    @Override
    public List<Song> getByAlbumId(int albumId) {
        System.out.println(albumId);
        try (MongoCursor<Document> cursor = songsCollection.find(eq(SONG_QUERY_ALBUM_ID, albumId)).iterator()) {
            return cursorToSongs(cursor);
        }
    }

    private List<Song> cursorToSongs(MongoCursor<Document> cursor) {
        List<Song> songsToReturn = new ArrayList<>();
        while (cursor.hasNext()) {
            Document songFromDB = cursor.next();
            Document artistFromDB = (Document) songFromDB.get(SONG_ARTIST_INFO);
            Document albumFromDB = (Document) songFromDB.get(SONG_ALBUM_INFO);
            Song tempSong = documentToSong(artistFromDB, albumFromDB, songFromDB);
            songsToReturn.add(tempSong);
        }
        return songsToReturn;
    }

    private Song documentToSong(Document artistDoc, Document albumDoc, Document songDoc) {
        Song songToReturn = new Song();
        Artist artist = new Artist();
        Album album = new Album();

        artist.setId(artistDoc.getInteger(ARTIST_ID));
        artist.setName(artistDoc.getString(ARTIST_NAME));

        album.setId(albumDoc.getInteger(ALBUM_ID));
        album.setName(albumDoc.getString(ALBUM_NAME));

        songToReturn.setId(songDoc.getInteger(SONG_ID));
        songToReturn.setName(songDoc.getString(SONG_NAME));
        songToReturn.setArtist(artist);
        songToReturn.setAlbum(album);
        return songToReturn;
    }


    @Override
    public boolean delete(Song song) {
        DeleteResult deleteResult = songsCollection.deleteOne(and(eq(SONG_NAME, song.getName()),
                eq(SONG_QUERY_ARTIST_ID, song.getArtist().getId()),
                eq(SONG_QUERY_ALBUM_ID, song.getAlbum().getId())));
        System.out.println("Songs Repo: " + deleteResult.toString());
        return true;
    }

    @Override
    public boolean deleteByAlbumId(int albumId) {
        DeleteResult deleteResult = songsCollection.deleteMany(eq(SONG_QUERY_ALBUM_ID, albumId));
        System.out.println("Songs Repo: " + deleteResult.toString());
        return true;
    }


}
