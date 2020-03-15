package com.musicLib.serviceTests;

import com.musicLib.entities.Artist;
import com.musicLib.exceptions.QueryException;
import com.musicLib.repository.ArtistRepository;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class ArtistRepositoryMongoTestStub implements ArtistRepository {

    @Override
    public boolean add(Artist artist) throws SQLException, QueryException {
        return false;
    }

    @Override
    public List<Artist> getAll() throws SQLException {
        return Arrays.asList(new Artist(), new Artist());
    }

    @Override
    public List<Artist> getByName(String artistName) throws SQLException {
        return null;
    }

    @Override
    public boolean delete(String artistName) throws SQLException, QueryException {
        return false;
    }
}
