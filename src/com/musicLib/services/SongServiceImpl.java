package com.musicLib.services;

import com.musicLib.entities.Song;
import com.musicLib.repository.SongRepository;

import java.sql.SQLException;
import java.util.List;

public class SongServiceImpl implements SongService {

    public boolean add(SongRepository songRepo, Song song, String artistName, String albumName) {
        return songRepo.insert(song, artistName, albumName);
    }

    public List<Song> queryBySongName(SongRepository songRepo, String songName) {
        try {
            return songRepo.queryBySongName(songName);
        }catch (SQLException e){
            System.out.println("Cannot query songs by song name: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean delete(SongRepository songRepo, String artistName, String albumName, String songName) {
        return songRepo.delete(artistName, albumName, songName);

    }

}
