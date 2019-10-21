package com.BohdanMilenko;

import com.BohdanMilenko.databaseModel.Artist;
import com.BohdanMilenko.databaseModel.Datasource;
import com.BohdanMilenko.databaseModel.SongArtist;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        Datasource datasource = new Datasource();
        if(!datasource.openDB()){
            System.out.println("Issue with opening a DB");
        }
      // List<Artist> artists = datasource.queryArtists(Datasource.ORDER_ASC);;

//       if(artists == null){
//           System.out.println("No artists in the database");
//       } else {
//           for (Artist artist : artists) {
//               System.out.println(artist.getId() + " " + artist.getName());
//           }
//       }

//       List<String> albums = datasource.queryAlbumsForArtists("Iron Maiden", Datasource.ORDER_ASC);
//
//       if(albums == null){
//           System.out.println("No albums found");
//       } else {
//           for( String album : albums){
//               System.out.println(album);
//           }
//       }

        List<SongArtist> songArtistList = datasource.queryArtistBySong("Freefall", Datasource.ORDER_ASC);

        if(songArtistList != null){
            System.out.println("Such artists has song: " + songArtistList.get(0).getTrackName());
            for( SongArtist songArtist : songArtistList){
                System.out.println( songArtist.getArtistName() + " - " + songArtist.getAlbumName() );
            }
        }else {
            System.out.println("No songs found");
        }
        datasource.getCountMinMax("songs");

        datasource.closeDB();
    }
}
