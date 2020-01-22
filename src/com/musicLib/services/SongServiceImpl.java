package com.musicLib.services;

import com.musicLib.entities.Song;
import com.musicLib.repository.SongRepository;

import java.util.List;

public class SongServiceImpl {

    public boolean add(SongRepository songRepo, Song song, String artistName, String albumName) {
        return songRepo.insert(song, artistName, albumName);
    }

    public List<Song> queryBySongName(SongRepository songRepo, String songName) {
        return songRepo.queryBySongName(songName);
    }

    public boolean delete(SongRepository songRepo, String artistName, String albumName, String songName) {
        return songRepo.delete(artistName, albumName, songName);

    }

}
