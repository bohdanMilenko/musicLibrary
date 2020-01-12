package com.musicLib;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.musicLib.MongoUtil.SessionManagerMongo;
import org.bson.Document;

public class Main {

    public static void main(String[] args) {

        MongoClient mongoClient = SessionManagerMongo.getMongoClient();
        MongoDatabase db = SessionManagerMongo.getDbFromPropertyFile();
        MongoCollection<Document> listDbs =  db.getCollection("Songs");
        System.out.println(listDbs.count());


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
