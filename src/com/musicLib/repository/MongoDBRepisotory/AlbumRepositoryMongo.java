package com.musicLib.repository.MongoDBRepisotory;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.musicLib.entities.Album;
import com.musicLib.entities.Artist;
import com.musicLib.mongoUtil.SessionManagerMongo;
import com.musicLib.repository.AlbumRepository;
import org.bson.Document;


import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;
import static com.musicLib.repository.MongoDBRepisotory.MetaDataMongo.*;

public class AlbumRepositoryMongo implements AlbumRepository {

    private MongoDatabase database;
    private MongoCollection<Document> albumsCollection;

    public AlbumRepositoryMongo() {
        database = SessionManagerMongo.getDbFromPropertyFile();
        albumsCollection = database.getCollection(ALBUMS_COLLECTION);
    }


    @Override
    public boolean add(Album album) {
        Document artistInfo = new Document();
        artistInfo.append(ALBUM_INSERT_ARTIST_ID, album.getArtist().getId())
                .append(ALBUM_INSERT_ARTIST_NAME, album.getArtist().getName());
        Document albumToInsert = new Document();
        albumToInsert.append(ALBUM_ID, MetaDataMongo.getNextSequence(albumsCollection)).append(ALBUM_NAME, album.getName())
                .append(ALBUM_ARTIST_INFO, artistInfo);
        System.out.println(albumToInsert.toJson());
        albumsCollection.insertOne(albumToInsert);
        return true;
    }


    @Override
    public List<Album> getAlbumsByArtistID(int artistID) {
        try (MongoCursor<Document> cursor = albumsCollection.find(eq(ALBUM_QUERY_ARTIST_ID, artistID)).iterator()) {
            return cursorToAlbum(cursor);
        }
    }

    @Override
    public List<Album> getByName(String albumName) {
        try (MongoCursor<Document> cursor = albumsCollection.find(eq(ALBUM_NAME,albumName)).iterator()) {
            return cursorToAlbum(cursor);
        }
    }

    private List<Album> cursorToAlbum(MongoCursor<Document> cursor) {
        List<Album> albums = new ArrayList<>();
        while (cursor.hasNext()) {
            Document docToAlbum = cursor.next();
            Album tempAlbum = documentToAlbum(docToAlbum);
            albums.add(tempAlbum);
        }
        return albums;
    }

    private Album documentToAlbum(Document passedDoc) {
        System.out.println(passedDoc.toJson());
        Album tempAlbum = new Album();
        tempAlbum.setId(passedDoc.getInteger(ALBUM_ID));
        tempAlbum.setName(passedDoc.getString(ALBUM_NAME));
        Artist tempArtist = new Artist();
        tempArtist.setId(passedDoc.getInteger(ALBUM_INSERT_ARTIST_ID));
        tempArtist.setName(passedDoc.getString(ALBUM_INSERT_ARTIST_NAME));
        tempAlbum.setArtist(tempArtist);
        return tempAlbum;
    }

    @Override
    public boolean delete(int albumID, int artistID) {
        DeleteResult dr = albumsCollection.deleteOne(and(eq(ALBUM_ID, albumID), eq(ALBUM_QUERY_ARTIST_ID, artistID)));
        System.out.println("Album Repo: " + dr.toString());
        return true;
    }
static int counter;
    @Override
    public boolean deleteByArtistID(int artistID) {
        DeleteResult dr = albumsCollection.deleteMany(eq(ALBUM_QUERY_ARTIST_ID,artistID));
        System.out.println("Album Repo: " + dr.toString());
        return true;
    }

}
