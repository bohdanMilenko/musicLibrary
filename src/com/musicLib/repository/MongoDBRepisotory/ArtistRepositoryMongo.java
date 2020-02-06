package com.musicLib.repository.MongoDBRepisotory;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.musicLib.entities.Artist;
import com.musicLib.exceptions.DuplicatedRecordException;
import com.musicLib.mongoDatabaseModel.AlbumMongo;
import com.musicLib.mongoDatabaseModel.ArtistRecordMongo;
import com.musicLib.mongoUtil.SessionManagerMongo;
import com.musicLib.repository.ArtistRepository;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.musicLib.repository.MongoDBRepisotory.MetaDataMongo.*;

public class ArtistRepositoryMongo implements ArtistRepository {


    private MongoDatabase mongoDatabase = SessionManagerMongo.getDbFromPropertyFile();
    private  MongoCollection<Document> artistCollection = mongoDatabase.getCollection(ARTISTS_COLLECTION);


    @Override
    public boolean add(Artist artist){
        Document artistToInsert = new Document();
        artistToInsert.append(ARTIST_NAME,artist.getName());
        artistCollection.insertOne(artistToInsert);
        return true;
    }

    @Override
    public List<Artist> getAll() {
        return null;
    }

    @Override
    public List<Artist> getByName(String artistName) {
        Document artistToFind= new Document();
        artistToFind.append(ARTIST_NAME,artistName);
        try(MongoCursor<Document> cursor = artistCollection.find(artistToFind).iterator()){
            return cursorToArtist(cursor);
        }
    }

    private List<Artist> cursorToArtist(MongoCursor<Document> cursor){
        List<Artist> artists = new ArrayList<>();
        while (cursor.hasNext()){
            Document tempDoc = cursor.next();
            Artist tempArtist = documentToArtist(tempDoc);
            artists.add(tempArtist);
        }
        return artists;
    }

    private Artist documentToArtist(Document tempDoc) {
        Artist tempArtist = new Artist();
        //todo think how to store objectID
        //tempArtist.setId((ObjectId) tempDoc.get(ID));
        tempArtist.setName(tempDoc.getString(ARTIST_NAME));
        return tempArtist;
    }

    @Override
    public boolean delete(String artistName) {
        return false;
    }

    /**
     * Use this method to insert new Artist.
     * @throws DuplicatedRecordException - in case such Artist already exists
     */
    public Document insertNewArtist(MongoCollection<Document> collection, String artistName, int yearFounded, String genre) throws  DuplicatedRecordException {
        Document recordToInsert = null;
        try (MongoCursor<Document> cursor = collection.find(new Document(MetaDataMongo.ARTIST_NAME, artistName)).iterator()) {
            if (cursor.hasNext()) {
                throw new DuplicatedRecordException("Such artist is already present in db");
            }
        }
        recordToInsert = new Document();
        recordToInsert.append(MetaDataMongo.ARTIST_NAME, artistName)
                .append(MetaDataMongo.ARTIST_YEAR_FOUNDED, yearFounded);
        collection.insertOne(recordToInsert);
        System.out.println("Inserted a record");
        return recordToInsert;
    }




    public List<ArtistRecordMongo> queryArtistByName(MongoCollection<Document> collection, String artistName) {
        List<ArtistRecordMongo> listToReturn = new ArrayList<>();
        try (MongoCursor<Document> cursor = collection.find(new org.bson.Document(MetaDataMongo.ARTIST_NAME, artistName)).iterator()) {
            while (cursor.hasNext()) {
                Document retrievedDocument = cursor.next();
                ArtistRecordMongo tempRecord = createArtistRecord( retrievedDocument);
                Document artistAlbumsDocument = (Document) retrievedDocument.get(MetaDataMongo.ARTIST_ALBUMS);
                if (artistAlbumsDocument != null) {
                    AlbumMongo tempAlbum = createAlbumRecord(artistAlbumsDocument);
                    listToReturn.add(tempRecord);
                }
            }
            return listToReturn;
        }
    }

    public List<ArtistRecordMongo> queryAllArtists(MongoCollection<Document> collection) {
        List<ArtistRecordMongo> listToReturn = new ArrayList<>();
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document retrievedDocument = cursor.next();
                ArtistRecordMongo tempRecord = createArtistRecord( retrievedDocument);
                listToReturn.add(tempRecord);
            }
        }
        return listToReturn;
    }

    private ArtistRecordMongo createArtistRecord( Document retrievedDocument) {
        ArtistRecordMongo tempRecord = new ArtistRecordMongo();
        tempRecord.setArtistName((String) retrievedDocument.get(MetaDataMongo.ARTIST_NAME));
        tempRecord.setDateFounded((int) retrievedDocument.get(MetaDataMongo.ARTIST_YEAR_FOUNDED));
        Document artistAlbums = (Document) retrievedDocument.get(MetaDataMongo.ARTIST_ALBUMS);
        if (artistAlbums != null) {
            List<AlbumMongo> albumMongoList = new ArrayList<>();
            AlbumMongo tempAlbum = createAlbumRecord(artistAlbums);
            albumMongoList.add(tempAlbum);
            tempRecord.setAlbum(albumMongoList);
        }
        return tempRecord;
    }

    private AlbumMongo createAlbumRecord(Document documentWithAlbums) {
        AlbumMongo tempAlbum = new AlbumMongo();
        tempAlbum.setAlbumName((String) documentWithAlbums.get(MetaDataMongo.ALBUM_NAME));
        tempAlbum.setAlbumName((String) documentWithAlbums.get(MetaDataMongo.ALBUM_NAME));
        return tempAlbum;
    }

}
