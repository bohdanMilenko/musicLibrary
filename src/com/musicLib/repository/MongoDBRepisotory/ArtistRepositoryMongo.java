package com.musicLib.repository.MongoDBRepisotory;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.musicLib.entities.Artist;
import com.musicLib.mongoDatabaseModel.AlbumMongo;
import com.musicLib.mongoDatabaseModel.ArtistRecordMongo;
import com.musicLib.mongoUtil.SessionManagerMongo;
import com.musicLib.repository.ArtistRepository;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.musicLib.repository.MongoDBRepisotory.MetaDataMongo.ARTISTS_COLLECTION;
import static com.musicLib.repository.MongoDBRepisotory.MetaDataMongo.ARTIST_NAME;

public class ArtistRepositoryMongo implements ArtistRepository {


    private MongoDatabase mongoDatabase;
    private  MongoCollection<Document> artistCollection ;


    public ArtistRepositoryMongo() {
        mongoDatabase = SessionManagerMongo.getDbFromPropertyFile();
        artistCollection = mongoDatabase.getCollection(ARTISTS_COLLECTION);
    }

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

    //TODO HASH FUNCTION TO INT - TWO WAY
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
        //tempArtist.setId(tempDoc.get(ID).toString());
        tempArtist.setName(tempDoc.getString(ARTIST_NAME));
        return tempArtist;
    }

    //TODO DELETERESULT LOG IT CTRL + Q
    //PUT SYSTEM OUT IN CASES WHEN OBJECT MAY BE CHANGED
    @Override
    public boolean delete(String artistName) {
        Document artistToDelete = new Document();
        artistToDelete.append(ARTIST_NAME,artistName);
        DeleteResult dr = artistCollection.deleteMany(artistToDelete);
        System.out.println(dr.toString());
        return true;
    }




    public List<ArtistRecordMongo> queryArtistByName(MongoCollection<Document> collection, String artistName) {
        List<ArtistRecordMongo> listToReturn = new ArrayList<>();
        try (MongoCursor<Document> cursor = collection.find(new org.bson.Document(MetaDataMongo.ARTIST_NAME, artistName)).iterator()) {
            while (cursor.hasNext()) {
                Document retrievedDocument = cursor.next();
                ArtistRecordMongo tempRecord = createArtistRecord( retrievedDocument);
                Document artistAlbumsDocument = (Document) retrievedDocument.get(MetaDataMongo.ARTIST_ALBUMS_LIST);
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
        Document artistAlbums = (Document) retrievedDocument.get(MetaDataMongo.ARTIST_ALBUMS_LIST);
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
        tempAlbum.setAlbumName((String) documentWithAlbums.get(MetaDataMongo.ARTIST_ALBUM_NAME));
        tempAlbum.setAlbumName((String) documentWithAlbums.get(MetaDataMongo.ARTIST_ALBUM_NAME));
        return tempAlbum;
    }

}
