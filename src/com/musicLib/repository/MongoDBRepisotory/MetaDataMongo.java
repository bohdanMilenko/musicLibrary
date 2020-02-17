package com.musicLib.repository.MongoDBRepisotory;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import org.bson.Document;

public class MetaDataMongo {

    static final String ARTISTS_COLLECTION = "artists";
    static final String ALBUMS_COLLECTION = "albums";
    static final String SONGS_COLLECTION = "songs";


    static final String ARTIST_ID = "_id";
    static final String ARTIST_NAME = "artistName";
    static final String ARTIST_ALBUMS_LIST = "albumList";


    static final String ALBUM_ID = "_id";
    static final String ALBUM_NAME = "albumName";
    static final String ALBUM_ARTIST_INFO = "artistInfo";
    static final String ALBUM_ARTIST_ID = ALBUM_ARTIST_INFO + "._id";
    static final String ALBUM_ARTIST_NAME = ALBUM_ARTIST_INFO + ".artistName";

    

    static final String SONG_ID = "_id";
    static final String SONG_NAME = "songName";
    static final String SONG_TRACK_NUMBER = "trackNumber";
    static final String SONG_ALBUM_INFO = "albumInfo";
    static final String SONG_ALBUM_ID = SONG_ALBUM_INFO + "._id";
    static final String SONG_ALBUM_NAME = SONG_ALBUM_INFO +".albumName";
    static final String SONG_ARTIST_INFO = "artistInfo";
    static final String SONG_ARTIST_ID = SONG_ARTIST_INFO + "._id";
    static final String SONG_ARTIST_NAME = SONG_ARTIST_INFO +".artistName";


    static final String ID_PARAM = "idParam";
    static final String ID_SEQ = "seq";

    static int getNextSequence(MongoCollection<Document> collection){
        Document findRequest = new Document();
        findRequest.append("_id", ID_PARAM);
        Document updateRequest =  new Document();
        updateRequest.append("$inc", new Document(ID_SEQ,1));
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
        options.upsert(true);
        options.returnDocument(ReturnDocument.AFTER);
        Document returnDoc =  collection.findOneAndUpdate(findRequest,updateRequest,options);
        return (int) returnDoc.get(ID_SEQ);
    }



    
}
