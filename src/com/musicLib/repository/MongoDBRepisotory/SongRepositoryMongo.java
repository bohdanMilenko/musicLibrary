package com.musicLib.repository.MongoDBRepisotory;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.musicLib.entities.Album;
import com.musicLib.entities.Artist;
import com.musicLib.entities.Song;
import com.musicLib.mongoUtil.SessionManagerMongo;
import com.musicLib.repository.SongRepository;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

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
        return true;
    }

    @Override
    public List<Song> getByName(String songName) {
        Document query = new Document();
        query.append(SONG_NAME, songName);
        try (MongoCursor<Document> cursor = songsCollection.find(query).iterator()) {
            return cursorToSongs(cursor);
        }

    }

    @Override
    public List<Song> getByAlbumId(int albumId) {
        Document query = new Document();
        query.append(SONG_ALBUM_ID, albumId);
        try (MongoCursor<Document> cursor = songsCollection.find(query).iterator()) {
            return cursorToSongs(cursor);
        }
    }

    private List<Song> cursorToSongs(MongoCursor<Document> cursor) {
        List<Song> songsToReturn = new ArrayList<>();
        while (cursor.hasNext()) {
            Document tempDoc = cursor.next();
            Song tempSong = documentToSong(tempDoc);
            songsToReturn.add(tempSong);
        }
        return songsToReturn;
    }

    private Song documentToSong(Document passedDoc) {
        Song songToReturn = new Song();
        Artist artist = new Artist();
        Album album = new Album();

//        artist.setId(passedDoc.getInteger(SONG_ARTIST_ID));
        artist.setName(passedDoc.getString(SONG_ARTIST_NAME));

//        album.setId(passedDoc.getInteger(SONG_ALBUM_ID));
        album.setName(passedDoc.getString(SONG_ALBUM_NAME));

        songToReturn.setId(passedDoc.getInteger(SONG_ID));
        songToReturn.setName(passedDoc.getString(SONG_NAME));
        songToReturn.setArtist(artist);
        songToReturn.setAlbum(album);

        return songToReturn;
    }


    @Override
    public boolean delete(Song song) {
        Document deleteQuery = new Document();
        deleteQuery.append(SONG_NAME, song.getName()).append(SONG_ARTIST_ID, song.getArtist().getId())
                .append(SONG_ALBUM_ID, song.getAlbum().getId());
        DeleteResult deleteResult = songsCollection.deleteOne(deleteQuery);
        System.out.println(deleteResult.toString());
        return true;
    }

    @Override
    public boolean deleteByAlbumId(int albumId) {
        Document deleteQuery = new Document();
        deleteQuery.append(SONG_ALBUM_ID, albumId);
        DeleteResult deleteResult = songsCollection.deleteMany(deleteQuery);
        System.out.println(deleteResult);
        return true;
    }


}
