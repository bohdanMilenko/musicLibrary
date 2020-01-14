package com.musicLib.MongoDBRepisotory;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Updates;
import com.musicLib.ExceptionsMongoRep.ArtistNotFoundException;
import com.musicLib.SQLightRepository.MetaData;
import org.bson.Document;
import com.mongodb.client.model.Updates;

public class AlbumRepositoryMongo {

    public Document insertNewAlbum(MongoCollection collection, String artistName, String albumName, int numberOfSongs, int yearReleased){
        Document artistRecord = findArtist(collection,artistName);
        if(findAlbum(collection,albumName)){
            collection.updateOne(artistRecord, Updates.combine( Updates.set(MetaDataMongo.ARTIST_ALBUMS + "." + MetaDataMongo.ALBUM_NAME, albumName),
                    Updates.set(MetaDataMongo.ARTIST_ALBUMS + "." + MetaDataMongo.ALBUM_SONGS_NUMBER, numberOfSongs),
                    Updates.set(MetaDataMongo.ARTIST_ALBUMS + "." + MetaDataMongo.ALBUM_YEAR_RELEASED, yearReleased)));
        }else {
            collection.insertOne();
        }
    }

    private boolean findAlbum(MongoCollection collection, String albumName){
        StringBuilder sb = new StringBuilder(MetaDataMongo.ARTIST_ALBUMS);
        sb.append(".").append(MetaDataMongo.ALBUM_NAME);
        Document query = new Document(sb.toString(), albumName);
        try(MongoCursor<Document> cursor = collection.find(query).iterator()){
            if (cursor.hasNext()){
                return true;
            }
        }
        return false;
    }

    private Document findArtist(MongoCollection collection, String artistName){
        MongoCursor<Document> cursor = collection.find(new Document(MetaDataMongo.ARTIST_NAME, artistName)).iterator(){
            if(cursor.hasNext()){
                Document foundArtist = cursor.next();
                return foundArtist;
            }
            throw new ArtistNotFoundException("There is n such artist in DB");
        }
    }


}
