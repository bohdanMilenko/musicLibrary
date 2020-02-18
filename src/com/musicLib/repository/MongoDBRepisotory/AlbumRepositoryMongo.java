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
        artistInfo.append(ALBUM_ARTIST_ID, album.getArtist().getId())
                .append(ARTIST_NAME, album.getArtist().getName());
        Document albumToInsert = new Document();
        albumToInsert.append(ALBUM_ID, MetaDataMongo.getNextSequence(albumsCollection)).append(ALBUM_NAME, album.getName())
                .append(ALBUM_ARTIST_INFO, artistInfo);
        System.out.println(albumToInsert.toJson());
        albumsCollection.insertOne(albumToInsert);
        return true;
    }


    @Override
    public List<Album> getAlbumsByArtistID(int artistID) {
        Document artistCondition = new Document();
        artistCondition.append(ALBUM_ARTIST_ID,artistID);
        try(MongoCursor<Document> cursor = albumsCollection.find(artistCondition).iterator()){
            return cursorToAlbum(cursor);
        }
    }

    @Override
    public List<Album> getByName(String albumName) {
        Document condition = new Document();
        condition.append(ALBUM_NAME, albumName);
        try(MongoCursor<Document> cursor = albumsCollection.find(condition).iterator()){
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
        System.out.println(ALBUM_ARTIST_ID);
        System.out.println(ALBUM_ARTIST_NAME);
        //TODO WHY IT HAS NULL POINTER????
        //System.out.println( passedDoc.get(ALBUM_ARTIST_ID).getClass());
        tempArtist.setId(passedDoc.getInteger(ALBUM_ARTIST_ID));
        tempArtist.setName(passedDoc.getString(ALBUM_ARTIST_NAME));
        tempAlbum.setArtist(tempArtist);
        return tempAlbum;
    }

    @Override
    public boolean delete(int albumID, int artistID) {
        Document albumCondition = new Document();
        albumCondition.append(ALBUM_ID,albumID);
        Document artistCondition = new Document();
        artistCondition.append(ALBUM_ARTIST_ID,artistID);
        DeleteResult result =  albumsCollection.deleteOne(Filters.and(artistCondition,albumCondition));
        System.out.println(result.toString());
        return false;
    }

    @Override
    public boolean deleteByArtistID(int artistID) {
        Document deleteDoc = new Document();
        deleteDoc.append(ALBUM_ARTIST_ID,artistID);
        albumsCollection.deleteMany(deleteDoc);
        return true;
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
