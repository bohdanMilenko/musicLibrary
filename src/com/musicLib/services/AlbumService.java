package com.musicLib.services;

import com.musicLib.entities.Album;
import com.musicLib.repository.AlbumRepository;

import java.util.List;

public interface AlbumService {

    public boolean add(AlbumRepository albumRepo, String artistName, Album album);

    public List<Album> queryByArtist(AlbumRepository albumRepo, String artistName);

    public List<Album> queryByName(AlbumRepository albumRepo, String albumName);

    public boolean delete(AlbumRepository albumRepo, String artistName, String albumName);
}
