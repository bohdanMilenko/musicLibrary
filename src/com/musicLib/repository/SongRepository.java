package com.musicLib.repository;

import com.musicLib.entities.Song;

import java.util.List;

public interface SongRepository {

    boolean insert(Song song, String artistName, String albumName);

    List<Song> queryBySongName(String songName);

    boolean delete(String artistName, String albumName, String songName);
}
