package com.musicLib;

import com.musicLib.databaseModel.*;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


public class Main {

    private static Scanner scanner = new Scanner((System.in));
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
        String songName = getUserInput();
        System.out.println(songName);
        int order = ordering();
        List<MusicLibrary> songArtistList = datasource.queryArtistBySong(songName, order);
        if(!songArtistList.isEmpty()) {
            // Getting 0 only to print out which song User queries. List of all the possible artists is printed after
            SongArtist queeredSong = (SongArtist) songArtistList.get(0);
            System.out.println("Such artists has song: " + queeredSong.getTrackName());
            printResultSet(songArtistList);
        }else {
            System.out.println("No such song!");
        }
        System.out.println();

        datasource.getCountMinMaxInSongsTable("songs");

        datasource.createArtistsListView();
        System.out.println("Please input the song name: ");

        String newSong = getUserInput();
        System.out.println(newSong);
        List<MusicLibrary> songArtistList2 =  datasource.queryBySongTitleView(newSong);
        printResultSet(songArtistList2);
        System.out.println();

        datasource.insertSong("Humble", "Kendrick Lamar", "DAMN", 1);

        scanner.close();
        datasource.closeDB();
    }

    private static String getUserInput() {
        String songName = "";

        try {
            if (scanner.hasNextLine()) {
                songName = scanner.nextLine();
            } else {
                songName = scanner.nextLine();
            }
            return songName;
        } catch (InputMismatchException e) {
            System.out.println("Please enter a valid input");
            return songName;
//        Commented out as code does not work otherwise
//        }finally {
//            scanner.close();
//        }
        }
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

    private static int ordering() {
        System.out.println("How would you like the query sorted?\n" +
                "1 - Ascending\n" +
                "2 - Descending\n" +
                "3 - No Sorting");
        int order;
        int option;
        try {
                option = scanner.nextInt();
                scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Please enter 1, 2 or 3:");
            scanner.nextLine();
            option = scanner.nextInt();
            scanner.nextLine();
        }

        switch (option){
            case 1:
                    order = Datasource.ORDER_ASC;
                    break;
            case 2:
                    order = Datasource.ORDER_DESC;
                    break;
            default:
                order = Datasource.ORDER_NONE;
        }
        return order;
    }
}
