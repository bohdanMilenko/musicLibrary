package com.musicLib.MongoDBRepisotory;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import com.musicLib.ExceptionsMongoRep.ArtistNotFoundException;
import com.musicLib.ExceptionsMongoRep.DuplicatedRecordException;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlbumRepositoryMongo {

    //Should I catch the exception or let it be thrown? Is it better to throw it in this method or in findArtist() ??
    public Document insertNewAlbum(MongoCollection collection, String artistName, String albumName, int numberOfSongs, int yearReleased)
            throws ArtistNotFoundException, DuplicatedRecordException {
        Document artistRecord;
        //The method below returns may throw an exception. Should I handle it here or do it at the place of the call??
        findByArtistName(collection, artistName);
        if(findByArtistAlbumName(collection,artistName,albumName).isEmpty()) {
            collection.updateOne(Filters.eq(MetaDataMongo.ARTIST_NAME, artistName),  Updates.combine(Arrays.asList( Updates.set(MetaDataMongo.ALBUM_NAME, albumName),
                    Updates.set(MetaDataMongo.ALBUM_SONGS_NUMBER, numberOfSongs),
                    Updates.set(MetaDataMongo.ALBUM_YEAR_RELEASED, yearReleased))));
            artistRecord = findByArtistName(collection, artistName);
            return artistRecord;
        }else {
            throw new DuplicatedRecordException("Such album already exists");
        }
    }

    public Document updateAlbumName(MongoCollection collection, String artistName, String oldName, String newName) throws ArtistNotFoundException {
        List<Document> foundList = findByArtistAlbumName(collection,artistName,oldName);
        if(!foundList.isEmpty() && foundList.size() ==1 ) {
            UpdateResult updatedDoc = collection.updateOne(Filters.and(Filters.eq(MetaDataMongo.ARTIST_NAME, artistName), Filters.eq(MetaDataMongo.ALBUM_NAME, oldName)),
                    Updates.set(MetaDataMongo.ALBUM_NAME, newName));
                List<Document> updatedRecord =  findByArtistAlbumName(collection, artistName, newName);
            return updatedRecord.get(0);
        }else {
            //TODO CHANGE LOGIC HERE
            throw new ArtistNotFoundException("Smth is wrong");
        }

    }


    private List<Document> findByArtistAlbumName(MongoCollection collection,String artistName, String albumName) {
        List<Document> listToReturn = new ArrayList<>();
        Document query1 = new Document(MetaDataMongo.ALBUM_NAME, albumName);
        Document query2 = new Document(MetaDataMongo.ARTIST_NAME, artistName);
        MongoCursor cursorWithRecords = collection.find(Filters.and(query1,query2)).iterator();
        while (cursorWithRecords.hasNext()){
            Document tempDoc = (Document) cursorWithRecords.next();
            listToReturn.add(tempDoc);
        }
        return listToReturn;
    }

    private Document findByArtistName(MongoCollection collection, String artistName) throws ArtistNotFoundException {
        Document query = new Document(MetaDataMongo.ARTIST_NAME, artistName);
        try (MongoCursor<Document> cursor = collection.find(query).iterator()) {
            if (cursor.hasNext()) {
                Document foundArtist = cursor.next();
                return foundArtist;
            }
        }
        throw new ArtistNotFoundException("There is no such artist");
    }


}
