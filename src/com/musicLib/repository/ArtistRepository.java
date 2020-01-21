package com.musicLib.repository;

import com.musicLib.entities.Artist;

import java.sql.SQLException;
import java.util.List;

public interface ArtistRepository {

    boolean insert(Artist artist);

    List<Artist> queryAllArtists();

    List<Artist> queryArtist(String artistName);

    boolean deleteArtist(String artistName) throws SQLException;

}
