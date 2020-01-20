package com.musicLib.repository.MongoDBRepisotory;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.musicLib.repositoryExceptions.DuplicatedRecordException;
import com.musicLib.MongoDatabaseModel.AlbumMongo;
import com.musicLib.MongoDatabaseModel.ArtistRecordMongo;
import com.musicLib.entities.Artist;
import com.musicLib.repository.ArtistRepository;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class ArtistRepositoryMongo implements ArtistRepository {

    @Override
    public boolean insert(Artist artist){
        //TODO Crate functionality to write to the db
        return true;
    }

    @Override
    public List<Artist> queryAllArtists() {
        return null;
    }

    @Override
    public List<Artist> queryArtist(String artistName) {
        return null;
    }

    @Override
    public boolean deleteArtist(String artistName) {
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
                .append(MetaDataMongo.ARTIST_YEAR_FOUNDED, yearFounded)
                .append(MetaDataMongo.ARTIST_GENRE, genre);
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
        tempRecord.setGenre((String) retrievedDocument.get(MetaDataMongo.ARTIST_GENRE));
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