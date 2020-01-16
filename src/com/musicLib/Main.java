package com.musicLib;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.musicLib.ExceptionsMongoRep.ArtistNotFoundException;
import com.musicLib.ExceptionsMongoRep.DuplicatedRecordException;
import com.musicLib.MongoDBRepisotory.AlbumRepositoryMongo;
import com.musicLib.MongoDBRepisotory.ArtistRepositoryMongo;
import com.musicLib.MongoDBRepisotory.MetaDataMongo;
import com.musicLib.MongoDatabaseModel.ArtistRecordMongo;
import com.musicLib.MongoUtil.SessionManagerMongo;
import org.bson.Document;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        /**
         * Issues: Cannot add a second album to the artist, as albums are not in the List []. Adding new album results in overriding
         * 
         */

        MongoClient mongoClient = SessionManagerMongo.getMongoClient();

        MongoDatabase db = SessionManagerMongo.getDbFromPropertyFile();
        ArtistRepositoryMongo artistRepositoryMongo = new ArtistRepositoryMongo();

        MongoCollection songsDatabase = db.getCollection("Songs");
        try {
            artistRepositoryMongo.insertNewArtist(db.getCollection("Songs"), "Iron Maiden", 1975, "Heavy Metal");
        }catch (DuplicatedRecordException e){
            System.out.println("Such record already exists: " + e);
        }

        try {
            artistRepositoryMongo.insertNewArtist(db.getCollection("Songs"), "Anthrax", 1981, "Thrash Metal");
        }catch (DuplicatedRecordException e){
            System.out.println("Such record already exists: " + e);
        }

        try {
            artistRepositoryMongo.insertNewArtist(db.getCollection("Songs"), "AC/DC", 1973, "Rock and Roll");
        }catch (DuplicatedRecordException e){
            System.out.println("Such record already exists: " + e);
        }

        try {
            artistRepositoryMongo.insertNewArtist(db.getCollection("Songs"), "Led Zeppelin", 1968, "Blues Rock");
        }catch (DuplicatedRecordException e){
            System.out.println("Such record already exists: " + e);
        }




        List<ArtistRecordMongo> list = artistRepositoryMongo.queryArtistByName(songsDatabase,"Iron Maiden");
        for(ArtistRecordMongo record : list){
            System.out.println(record.toString());
        }

        System.out.println();

        AlbumRepositoryMongo albumRepositoryMongo = new AlbumRepositoryMongo();
        try {
            Document insertedNewAlbum = albumRepositoryMongo.insertNewAlbum(songsDatabase, "Anthrax", "Piece of Mind", 9, 1983);
            System.out.println(insertedNewAlbum.toJson());
        }catch (ArtistNotFoundException e){
            System.out.println("There is no such artist: " + e);
        }catch (DuplicatedRecordException e2){
            System.out.println("Such album exists");
            e2.printStackTrace();
        }


        try {
            Document insertedNewAlbum = albumRepositoryMongo.insertNewAlbum(songsDatabase, "Led Zeppelin", "Led Zeppelin 2", 9, 1969);
            System.out.println(insertedNewAlbum.toJson());
        }catch (ArtistNotFoundException e){
            System.out.println("There is no such artist: " + e);
        }catch (DuplicatedRecordException e2){
            System.out.println("Such album exists");
            e2.printStackTrace();
        }

        try {
            Document insertedNewAlbum = albumRepositoryMongo.insertNewAlbum(songsDatabase, "Anthrax", "For All Kings", 12, 2016);
            System.out.println(insertedNewAlbum.toJson());
        }catch (ArtistNotFoundException e){
            System.out.println("There is no such artist: " + e);
        }catch (DuplicatedRecordException e2){
            System.out.println("Such album exists");
            e2.printStackTrace();
        }

        try{
            albumRepositoryMongo.updateAlbumName(songsDatabase,"Led Zeppelin", "Led Zeppelin 2", "Led Zeppelin 4");
        }catch (ArtistNotFoundException e){
            System.out.println("There is no such artist: " + e);
        }

        List<ArtistRecordMongo> allRecords = artistRepositoryMongo.queryAllArtists(songsDatabase);
        //allRecords.forEach(k -> System.out.println(k.toString()));

        ArtistRecordMongo anthrax = allRecords.get(3);
        System.out.println(anthrax.getArtistName());
        System.out.println(anthrax.getAlbum().get(0).getAlbumName());

        MongoCursor databases = mongoClient.listDatabaseNames().iterator();
        while (databases.hasNext()){
            System.out.println(databases.next());
        }
        MongoCursor cursor = songsDatabase.find(new Document(MetaDataMongo.ARTIST_NAME,"Iron Maiden" )).iterator();
        Document foundRecord = (Document) cursor.next();
        System.out.println(foundRecord.get(MetaDataMongo.ARTIST_NAME));


//        SongsRepository songsRepository = new SongsRepository();
//        ArtistsRepository artistsRepository = new ArtistsRepository();
//
//       List<Artist> artists = artistsRepository.queryArtistsTry();;
//       if(artists == null){
//           System.out.println("No artists in the database");
//       } else {
//           for (Artist artist : artists) {
//               System.out.println( artist.getId() + " " + artist.getName());
//           }
//       }
//
//       List<String> albums =  songsRepository.queryAlbumsForArtists("Iron Maiden");
//       if(albums == null){
//           System.out.println("No albums found");
//       } else {
//           for( String album : albums){
//               System.out.println(album);
//           }
//       }
//
//        System.out.println("Please input the song name: ");
//        String songName = UserInput.getUserInput();
//        System.out.println(songName);
//        List<SongArtist> songArtistList = songsRepository.queryArtistBySong(songName);
//        if(!songArtistList.isEmpty()) {
//            // Getting 0 only to print out which song User queries. List of all the possible artists is printed after
//            SongArtist queeredSong =  songArtistList.get(0);
//            System.out.println("Such artists has song: " + queeredSong.getTrackName());
//            printResultSet(songArtistList);
//        }else {
//            System.out.println("No such song!");
//        }
//        System.out.println();
//
//        songsRepository.getCountMinMaxInSongsTable("songs");
//
//        songsRepository.createArtistsListView();
//
//        System.out.println("Please input the song name: ");
//        String newSong = UserInput.getUserInput();
//        System.out.println(newSong);
//        List<SongArtist> songArtistList2 =  songsRepository.queryBySongTitleView(newSong);
//        printResultSet(songArtistList2);
//        System.out.println();
//
//
//        songsRepository.insertSong("DAMN", "Kendrick Lamar", "DAMN", 1);
//
////        Another way to get path
////        Path path = FileSystems.getDefault().getPath("music.db");
////        System.out.println(path.toUri());
//
//
   }
//
//
//    private static void printResultSet(List<SongArtist> anyListToPrint){
//        if(!anyListToPrint.isEmpty()){
//            for( SongArtist  loopingInstance : anyListToPrint){
//                System.out.println( "Artist: " +  loopingInstance.getArtist() + " from album: " + loopingInstance.getAlbum());
//            }
//        } else {
//            System.out.println("No songs found");
//        }
//    }

}
