package com.musicLib.MongoDBRepisotory;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.musicLib.ExceptionsMongoRep.DuplicatedRecordException;

public class AlbumRepositoryMongo {

    public org.bson.Document insertNewArtist(MongoCollection<org.bson.Document> collection, String artistName, int yearFounded, String genre){
        org.bson.Document recordToInsert = null;
       try( MongoCursor<org.bson.Document> cursor = collection.find(new org.bson.Document(MetaDataMongo.ARTIST_NAME, artistName)).iterator()){
           if (cursor.hasNext()){
                   throw new DuplicatedRecordException("Such artist is already present in db");
           }
       }
        recordToInsert = new org.bson.Document();
        recordToInsert.append(MetaDataMongo.ARTIST_NAME,artistName)
                .append(MetaDataMongo.ARTIST_YEAR_FOUNDED, yearFounded)
                .append(MetaDataMongo.ARTIST_GENRE, genre);
        collection.insertOne(recordToInsert);
        System.out.println("Inserted a record");
       return recordToInsert;
    }
}
