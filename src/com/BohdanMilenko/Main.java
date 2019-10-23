package com.BohdanMilenko;

import com.BohdanMilenko.databaseModel.*;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Datasource datasource = new Datasource();
        if(!datasource.openDB()){
            System.out.println("Issue with opening a DB");
        }
//       List<MusicLibrary> artists = datasource.queryArtists(Datasource.ORDER_ASC);;
//
//       if(artists == null){
//           System.out.println("No artists in the database");
//       } else {
//           for (MusicLibrary artist : artists) {
//               System.out.println( (Artist) artist.getId() + " " + artist.getName());
//           }
//       }

       List<String> albums = datasource.queryAlbumsForArtists("Iron Maiden", Datasource.ORDER_ASC);

       if(albums == null){
           System.out.println("No albums found");
       } else {
           for( String album : albums){
               System.out.println(album);
           }
       }

        System.out.println("Please input the song name: ");
        Scanner scanner = new Scanner(System.in);
        String songName = scanner.nextLine();
        List<MusicLibrary> songArtistList = datasource.queryArtistBySong(songName, Datasource.ORDER_ASC);
        SongArtist queeredSong = (SongArtist) songArtistList.get(0);
        System.out.println("Such artists has song: " +  queeredSong.getTrackName());
        printResultSet(songArtistList);
        System.out.println();

        datasource.getCountMinMax("songs");

        datasource.createArtistsListView();
        System.out.println("Please input the song name: ");

        songName = scanner.nextLine();
        List<MusicLibrary> songArtistList2 =  datasource.queryBySongTitleView(songName);
        printResultSet(songArtistList2);
        System.out.println();

        datasource.insertSong("Humble", "Kendrick Lamar", "DAMN", 1);

        datasource.closeDB();
    }

    private static void printResultSet(List<MusicLibrary> anyListToPrint){
        if(!anyListToPrint.isEmpty()){
            for( MusicLibrary  loopingInstance : anyListToPrint){
                SongArtist loopingInstance2 = (SongArtist)loopingInstance;
                System.out.println( "Artist: " +  loopingInstance2.getArtist() + " from album: " + loopingInstance2.getAlbum());
            }
        } else {
            System.out.println("No songs found");
        }
    }
}
