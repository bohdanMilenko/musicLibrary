package com.musicLib.services;

import com.musicLib.entities.Song;
import com.musicLib.exceptions.AlbumNotFoundException;
import com.musicLib.repository.SongRepository;
import com.musicLib.exceptions.ArtistNotFoundException;

import java.util.List;

public interface SongService {

    public boolean add(SongRepository songRepo, Song song, String artistName, String albumName) throws ArtistNotFoundException, AlbumNotFoundException;

    public List<Song> queryByName(SongRepository songRepo, String songName);

    public boolean delete(SongRepository songRepo, String artistName, String albumName, String songName);
}
