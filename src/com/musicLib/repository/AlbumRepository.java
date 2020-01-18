package com.musicLib.repository;

import com.musicLib.entities.Album;

import java.util.List;

public interface AlbumRepository {

    boolean insert(Album album, String artistName);

    List<Album> queryByArtistName(String artistName);

    boolean delete(String albumName, String artistName);


}
