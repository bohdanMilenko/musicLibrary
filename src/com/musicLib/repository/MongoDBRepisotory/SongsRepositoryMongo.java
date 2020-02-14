package com.musicLib.repository.MongoDBRepisotory;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.musicLib.entities.Song;
import com.musicLib.mongoUtil.SessionManagerMongo;
import com.musicLib.repository.SongRepository;
import org.bson.Document;

import java.sql.SQLException;
import java.util.List;

import static com.musicLib.repository.MongoDBRepisotory.MetaDataMongo.ALBUMS_COLLECTION;

public class SongsRepositoryMongo implements SongRepository {


    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> albumsCollection ;


    public SongsRepositoryMongo() {
        mongoDatabase = SessionManagerMongo.getDbFromPropertyFile();
        albumsCollection = mongoDatabase.getCollection(ALBUMS_COLLECTION);
    }


    @Override
    public boolean add(Song song) throws SQLException {
        Document songToAdd = new Document();
        songToAdd.append()
    }

    @Override
    public List<Song> getByName(String songName) throws SQLException {
        return null;
    }

    @Override
    public boolean delete(Song song) throws SQLException {
        return false;
    }

    @Override
    public List<Song> getByAlbumId(int albumId) throws SQLException {
        return null;
    }

    @Override
    public boolean deleteByAlbumId(int albumId) throws SQLException {
        return false;
    }

    //    public static Song insertSong(MongoCollection collection, String artistName, String albumName, String songName, String songLength) {
//       try(MongoCursor<Document> cursor = collection.find(new Document("artistName", artistName)).iterator()){
//           while (cursor.hasNext()){
//               Document artist = cursor.next();
//               System.out.println(artist.);
//           }
//       }
//
//    }

}
