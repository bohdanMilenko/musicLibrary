package com.musicLib.MongoDBRepisotory;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Updates;
import com.musicLib.ExceptionsMongoRep.ArtistNotFoundException;
import org.bson.Document;

public class AlbumRepositoryMongo {

    public Document insertNewAlbum(MongoCollection collection, String artistName, String albumName, int numberOfSongs, int yearReleased){
        Document artistRecord = findArtist(collection,artistName);
        if(artistRecord != null){
            collection.updateOne(artistRecord, Updates.combine( Updates.set(MetaDataMongo.ARTIST_ALBUMS + "." + MetaDataMongo.ALBUM_NAME, albumName),
                    Updates.set(MetaDataMongo.ARTIST_ALBUMS + "." + MetaDataMongo.ALBUM_SONGS_NUMBER, numberOfSongs),
                    Updates.set(MetaDataMongo.ARTIST_ALBUMS + "." + MetaDataMongo.ALBUM_YEAR_RELEASED, yearReleased)));
        }else {
            throw new ArtistNotFoundException("There is no such artist");
        }
        return artistRecord;
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
        Document query = new Document(MetaDataMongo.ARTIST_NAME, artistName);
        try( MongoCursor<Document> cursor = collection.find(query).iterator()){
            if(cursor.hasNext()){
                Document foundArtist = cursor.next();
                return foundArtist;
            }
        }
        return null;
    }


}
