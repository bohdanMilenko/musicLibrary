package com.musicLib.repository.MongoDBRepisotory;

import com.mongodb.DuplicateKeyException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.musicLib.entities.Artist;
import com.musicLib.mongoUtil.SessionManagerMongo;
import com.musicLib.repository.ArtistRepository;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.musicLib.repository.MongoDBRepisotory.MetaDataMongo.*;


public class ArtistRepositoryMongo implements ArtistRepository {


    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> artistCollection;

    private static Random intGenerator = new Random();


    public ArtistRepositoryMongo() {
        mongoDatabase = SessionManagerMongo.getDbFromPropertyFile();
        artistCollection = mongoDatabase.getCollection(ARTISTS_COLLECTION);
    }

    @Override
    public boolean add(Artist artist) {
        Document artistToInsert = new Document();
        //artistToInsert.append(ARTIST_ID, assignArtistID()).append(ARTIST_NAME, artist.getName());
        artistToInsert.append(ARTIST_ID, MetaDataMongo.getNextSequence(artistCollection)).append(ARTIST_NAME, artist.getName());
        boolean validOperation = false;
        int attempts = 0;
        do {
            try {
                addArtist(artistToInsert);
                validOperation = true;
            } catch (DuplicateKeyException e) {
                attempts++;
                System.out.println("Incorrect ID for Artist");
            }
        } while (!validOperation || attempts < 3);
        if (attempts == 3) {
            throw new RuntimeException("Unable to add Artist - Mongo");
        }
        return true;
    }

    private boolean addArtist(Document artistToInsert) {
        artistCollection.insertOne(artistToInsert);
        return true;
    }

    @Override
    public List<Artist> getAll() {
        try (MongoCursor<Document> cursor = artistCollection.find().iterator()) {
            return cursorToArtist(cursor);
        }
    }


    @Override
    public List<Artist> getByName(String artistName) {
        Document artistToFind = new Document();
        artistToFind.append(ARTIST_NAME, artistName);
        try (MongoCursor<Document> cursor = artistCollection.find(artistToFind).iterator()) {
            return cursorToArtist(cursor);
        }
    }

    private List<Artist> cursorToArtist(MongoCursor<Document> cursor) {
        List<Artist> artists = new ArrayList<>();
        while (cursor.hasNext()) {
            Document tempDoc = cursor.next();
            Artist tempArtist = documentToArtist(tempDoc);
            artists.add(tempArtist);
        }
        return artists;
    }

    private Artist documentToArtist(Document tempDoc) {
        Artist tempArtist = new Artist();
        tempArtist.setId(Integer.parseInt(tempDoc.get(ARTIST_ID).toString()));
        tempArtist.setName(tempDoc.getString(ARTIST_NAME));
        return tempArtist;
    }

    @Override
    public boolean delete(String artistName) {
        Document artistToDelete = new Document();
        artistToDelete.append(ARTIST_NAME, artistName);
        DeleteResult dr = artistCollection.deleteMany(artistToDelete);
        System.out.println(dr.toString());
        return true;
    }

    private int assignArtistID() {
        return Math.abs(intGenerator.nextInt());
    }


}
