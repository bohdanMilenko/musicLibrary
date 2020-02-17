package com.musicLib.repository.MongoDBRepisotory;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.musicLib.entities.Album;
import com.musicLib.exceptions.QueryException;
import com.musicLib.mongoUtil.SessionManagerMongo;
import com.musicLib.repository.AlbumRepository;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;

import static com.musicLib.repository.MongoDBRepisotory.MetaDataMongo.*;

public class AlbumRepositoryMongo implements AlbumRepository {

    private MongoDatabase database;
    private MongoCollection<Document> artistsDB;
    private MongoCollection<Document> albumsDB;

    private static BigInteger albumsCounter;

    public AlbumRepositoryMongo() {
        database = SessionManagerMongo.getDbFromPropertyFile();
        artistsDB = database.getCollection(ARTISTS_COLLECTION);
        albumsDB = database.getCollection(ALBUMS_COLLECTION);
    }

    @Override
    public boolean add(Album album) throws SQLException, QueryException {
        Document fullAlbumForAlbumTable = new Document();
       // fullAlbumForAlbumTable.append(ALBUM_NAME, album.getName()).append(ALBUM_YEAR_RELEASED, album.getYearReleased());
        albumsDB.insertOne(fullAlbumForAlbumTable);

        Document albumForArtistTable = new Document();
       // albumForArtistTable.append(ARTIST_ALBUM_NAME,album.getName());
        return true;
    }

    private ObjectId getObjectID(String albumName){
        Document album = new Document();
        album.append(ALBUM_NAME, albumName);
        MongoCursor<Document> foundArtist =  artistsDB.find(album).limit(1).iterator();
        album = foundArtist.next();
        //return  album.getObjectId();
        return null;
    }

    @Override
    public List<Album> getAlbumsByArtistID(int artistID) throws SQLException {
        return null;
    }

    @Override
    public List<Album> getByName(String albumName) throws SQLException {
        return null;
    }

    @Override
    public boolean delete(int albumID, int artistID) throws QueryException, SQLException {
        return false;
    }

    @Override
    public boolean deleteByArtistID(int artistID) throws QueryException {
        return false;
    }

    //Should I catch the exception or let it be thrown? Is it better to throw it in this method or in findArtist() ??
//    public Document insertNewAlbum(MongoCollection collection, String artistName, String albumName, int numberOfSongs, int yearReleased)
//            throws ArtistNotFoundException, DuplicatedRecordException {
//        Document artistRecord;
//        //The method below returns may throw an exception. Should I handle it here or do it at the place of the call??
//        findByArtistName(collection, artistName);
//        if(findByArtistAlbumName(collection,artistName,albumName).isEmpty()) {
//            collection.updateOne(Filters.eq(MetaDataMongo.ARTIST_NAME, artistName),  Updates.combine(Arrays.asList( Updates.set(ARTIST_ALBUM_NAME, albumName),
//                    Updates.set(MetaDataMongo.ALBUM_SONGS_NUMBER, numberOfSongs),
//                    Updates.set(MetaDataMongo.ALBUM_YEAR_RELEASED, yearReleased))));
//            artistRecord = findByArtistName(collection, artistName);
//            return artistRecord;
//        }else {
//            throw new DuplicatedRecordException("Such album already exists");
//        }
//    }
//
//    public Document updateAlbumName(MongoCollection collection, String artistName, String oldName, String newName) throws ArtistNotFoundException {
//        List<Document> foundList = findByArtistAlbumName(collection,artistName,oldName);
//        if(!foundList.isEmpty() && foundList.size() ==1 ) {
//            UpdateResult updatedDoc = collection.updateOne(Filters.and(Filters.eq(MetaDataMongo.ARTIST_NAME, artistName), Filters.eq(ARTIST_ALBUM_NAME, oldName)),
//                    Updates.set(ARTIST_ALBUM_NAME, newName));
//                List<Document> updatedRecord =  findByArtistAlbumName(collection, artistName, newName);
//            return updatedRecord.get(0);
//        }else {
//            //TODO CHANGE LOGIC HERE
//            throw new ArtistNotFoundException("Smth is wrong");
//        }
//
//    }
//
//
//    private List<Document> findByArtistAlbumName(MongoCollection collection,String artistName, String albumName) {
//        List<Document> listToReturn = new ArrayList<>();
//        Document query1 = new Document(ARTIST_ALBUM_NAME, albumName);
//        Document query2 = new Document(MetaDataMongo.ARTIST_NAME, artistName);
//        MongoCursor cursorWithRecords = collection.find(Filters.and(query1,query2)).iterator();
//        while (cursorWithRecords.hasNext()){
//            Document tempDoc = (Document) cursorWithRecords.next();
//            listToReturn.add(tempDoc);
//        }
//        return listToReturn;
//    }
//
//    private Document findByArtistName(MongoCollection collection, String artistName) throws ArtistNotFoundException {
//        Document query = new Document(MetaDataMongo.ARTIST_NAME, artistName);
//        try (MongoCursor<Document> cursor = collection.find(query).iterator()) {
//            if (cursor.hasNext()) {
//                Document foundArtist = cursor.next();
//                return foundArtist;
//            }
//        }
//        throw new ArtistNotFoundException("There is no such artist");
//    }


}
