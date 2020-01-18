package com.musicLib.services;

import com.musicLib.entities.Album;
import com.musicLib.repository.AlbumRepository;

import java.util.List;

public class AlbumService {

    public AlbumService() {
    }

    public boolean add(AlbumRepository albumRepo, String artistName, Album album) {
        return albumRepo.insert(album, artistName);
    }

    public List<Album> queryByArtist(AlbumRepository albumRepo, String artistName) {
        return albumRepo.queryByArtistName(artistName);
    }

    public boolean delete(AlbumRepository albumRepo, String artistName, String albumName) {
        return albumRepo.delete(albumName, artistName);
    }
}
