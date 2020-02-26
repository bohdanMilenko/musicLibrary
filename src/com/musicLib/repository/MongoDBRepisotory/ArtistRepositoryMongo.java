package com.musicLib.repository.MongoDBRepisotory;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.musicLib.entities.Artist;
import com.musicLib.exceptions.QueryException;
import com.musicLib.repository.mongoUtil.SessionManagerMongo;
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


    /**
     * Returning true, as Mongo does not allow autoincrement for _id field and it cannot be disabled since Mongo 4.0,
     * ObjectId cannot be used as entities are using int to hold id filed.
     */
    @Override
    public boolean add(Artist artist) throws QueryException {
        Document artistToInsert = new Document();
        //artistToInsert.append(ARTIST_ID, assignArtistID()).append(ARTIST_NAME, artist.getName());
        artistToInsert.append(ARTIST_ID, MetaDataMongo.getNextSequence(artistCollection)).append(ARTIST_NAME, artist.getName());
        boolean validOperation = false;
            try {
                addArtist(artistToInsert);
                System.out.println("Artist is added to db: " + artistToInsert.toJson());
                return true;
            } catch (MongoWriteException e) {
                System.out.println("Duplicated ID for Artist " + artistToInsert.toJson());
                throw new QueryException(e);
            }

    }

    private boolean addArtist(Document artistToInsert) {
        artistCollection.insertOne(artistToInsert);
        return true;
    }

    /**
     * cursor.next() is used to drop Sequence document from parsing
     * (Collection is using AutoIncrement for _id field and getNextSequence() to assign it)
     */
    @Override
    public List<Artist> getAll() {
        try (MongoCursor<Document> cursor = artistCollection.find().iterator()) {
            cursor.next();
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

    /**
     * Maps Documents to Artist entity
     * @param tempDoc
     * @return
     */
    private Artist documentToArtist(Document tempDoc) {
        Artist tempArtist = new Artist();
        tempArtist.setId(tempDoc.getInteger(ARTIST_ID));
        tempArtist.setName(tempDoc.getString(ARTIST_NAME));
        return tempArtist;
    }

    @Override
    public boolean delete(String artistName) {
        Document artistToDelete = new Document();
        artistToDelete.append(ARTIST_NAME, artistName);
        DeleteResult dr = artistCollection.deleteMany(artistToDelete);
        System.out.println("Artist Repo: " + dr.toString());
        return true;
    }

}
