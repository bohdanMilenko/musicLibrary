package com.musicLib.MongoDBRepisotory;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.musicLib.databaseModel.Song;
import org.bson.Document;

public class SongsRepositoryMongo {

    public static Song insertSong(MongoCollection collection, String artistName, String albumName, String songName, String songLength) {
       try(MongoCursor<Document> cursor = collection.find(new Document("artistName", artistName)).iterator()){
           while (cursor.hasNext()){
               Document artist = cursor.next();
               System.out.println(artist.);
           }
       }

    }

}
