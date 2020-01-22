package com.musicLib.services;

import com.musicLib.entities.Song;
import com.musicLib.repository.SongRepository;

import java.util.List;

public interface SongService {

    public boolean add(SongRepository songRepo, Song song, String artistName, String albumName);

    public List<Song> queryBySongName(SongRepository songRepo, String songName);

    public boolean delete(SongRepository songRepo, String artistName, String albumName, String songName);
}
