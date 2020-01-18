package com.musicLib.repository;

import com.musicLib.entities.Artist;

import java.util.List;

public interface ArtistRepository {

    boolean insert(Artist artist);

    List<Artist> getAllArtists();

    Artist queryArtist(String artistName);

    boolean deleteArtist(String artistName);

}
