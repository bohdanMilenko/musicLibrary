package com.musicLib.services;

import com.musicLib.entities.Artist;
import com.musicLib.repository.ArtistRepository;
import com.musicLib.repositoryExceptions.ArtistNotFoundException;
import com.musicLib.repositoryExceptions.DuplicatedRecordException;

import java.sql.SQLException;
import java.util.List;

public interface ArtistService {

    public boolean add(ArtistRepository artistRepo, Artist artist);

    public List<Artist> queryAll(ArtistRepository artistRepo);

    public List<Artist> query(ArtistRepository artistRepo, String artist);

    public boolean delete(ArtistRepository artistRepo, String artistName) throws SQLException, ArtistNotFoundException, DuplicatedRecordException;

}
