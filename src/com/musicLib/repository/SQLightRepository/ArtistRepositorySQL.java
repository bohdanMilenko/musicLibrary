package com.musicLib.repository.SQLightRepository;

import com.musicLib.repository.SQLUtil.SessionManagerSQLite;
import com.musicLib.entities.Album;
import com.musicLib.entities.Artist;
import com.musicLib.exceptions.ArtistNotFoundException;
import com.musicLib.exceptions.DuplicatedRecordException;
import com.musicLib.repository.ArtistRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.musicLib.repository.SQLightRepository.MetaDataSQL.*;


public class ArtistRepositorySQL implements ArtistRepository {

    private PreparedStatement addArtist;
    private PreparedStatement queryArtists;
    private PreparedStatement deleteQuery;
    private PreparedStatement queryByArtistID;
    private SessionManagerSQLite SessionManagerSQLite = new SessionManagerSQLite();



    @Override
    public boolean add(Artist artist) throws SQLException {
        String query = buildInsertQuery();
        addArtist = SessionManagerSQLite.getPreparedStatement(query);
        addArtist.setString(1, artist.getName());
        addArtist.executeUpdate();
        return false;
    }

    /**
     * INSERT INTO artists (name) VALUES (?);
     */
    private String buildInsertQuery() {
        QueryBuilder qb = new QueryBuilder();
        qb.insertTo(TABLE_ARTISTS).insertSpecifyColumns(COLUMN_ARTISTS_NAME);
        return qb.toString();
    }


    @Override
    public List<Artist> getAll() throws SQLException {
        String query = buildQueryGetAll();
        ResultSet resultSet = getResultSet(query);
        List<Artist> artistsToReturn = resultSetToArtist(resultSet);
        return artistsToReturn;
    }

    /**
     * SELECT * FROM artist
     */
    private String buildQueryGetAll() {
        QueryBuilder qb = new QueryBuilder();
        qb.startQueryAll().queryFrom(TABLE_ARTISTS);
        return qb.toString();
    }

    @Override
    public List<Artist> getByName(String artistName) throws SQLException {
        String query = buildQueryGetByName();
        System.out.println(query);
        PreparedStatement queryArtist = SessionManagerSQLite.getPreparedStatement(query);
        List<Artist> returnList = new ArrayList<>();
        queryArtist.setString(1, artistName);
        ResultSet rs = queryArtist.executeQuery();
        returnList = resultSetToArtist(rs);
        return returnList;
    }

    /**
     * SELECT artists._id, artists.name FROM artists WHERE artists.name = ?
     */
    private String buildQueryGetByName() {
        QueryBuilder qb = new QueryBuilder();
        qb.startQuery(TABLE_ARTISTS, COLUMN_ARTISTS_ID).addSelection(TABLE_ARTISTS, COLUMN_ARTISTS_NAME)
                .queryFrom(TABLE_ARTISTS).specifyFirstCondition(TABLE_ARTISTS, COLUMN_ARTISTS_NAME);
        return qb.toString();
    }

    private List<Artist> resultSetToArtist(ResultSet resultSet) {
        List<Artist> artists = new ArrayList<>();
        if (resultSet != null) {
            try {
                while (resultSet.next()) {
                    Artist artist = new Artist();
                    artist.setId(resultSet.getInt(1));
                    artist.setName(resultSet.getString(2));
                    artists.add(artist);
                }
                return artists;
            } catch (SQLException e) {
                System.out.println("Query was not performed " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    @Override
    public boolean delete(String artistName) throws SQLException, ArtistNotFoundException, DuplicatedRecordException {
        List<Artist> artistToDelete = getByName(artistName);
        int numberOfArtists = artistToDelete.size();
        System.out.println(numberOfArtists);
        switch (numberOfArtists) {
            case 1:
                Artist artist = artistToDelete.get(0);
                System.out.println(artist.toString());
                List<Album> artistsAlbums = artist.getAlbums();
                deleteArtist(artistName);
                return true;
            case 0:
                throw new ArtistNotFoundException("Such artist does not exist");
            default:
                throw new DuplicatedRecordException("Multiple artists with the same name");
        }
    }

    private boolean deleteArtist(String artistName) throws SQLException {
        String query = buildQueryDeleteArtist();
        System.out.println(query);
        deleteQuery = SessionManagerSQLite.getPreparedStatement(query);
        deleteQuery.setString(1, artistName);
        deleteQuery.executeUpdate();
        return true;
    }

    /**
     * DELETE FROM artists WHERE artists.name = ?
     */
    private String buildQueryDeleteArtist() {
        QueryBuilder qb = new QueryBuilder();
        qb.deleteFrom(TABLE_ARTISTS).specifyFirstCondition(TABLE_ARTISTS, COLUMN_ARTISTS_NAME);
        return qb.toString();
    }

    public Artist getById(int id) throws SQLException {
        String query = buildQueryByID();
        queryByArtistID = SessionManagerSQLite.getPreparedStatement(query);
        queryByArtistID.setInt(1, id);
        ResultSet rs = queryByArtistID.executeQuery();
        List<Artist> resultOfQuery = resultSetToArtist(rs);
        if (resultOfQuery.size() == 1) {
            Artist foundArtist = resultOfQuery.get(0);
            return foundArtist;
        }
        return null;
    }

    /**
     * SELECT artists._id, artists.name FROM artists WHERE artists._id = ?
     */
    private String buildQueryByID() {
        QueryBuilder qb = new QueryBuilder();
        qb.startQuery(TABLE_ARTISTS, COLUMN_ARTISTS_ID).addSelection(TABLE_ARTISTS, COLUMN_ARTISTS_NAME)
                .queryFrom(TABLE_ARTISTS).specifyFirstCondition(TABLE_ARTISTS, COLUMN_ARTISTS_ID);
        return qb.toString();
    }

    //TODO RETHINK THIS METHOD
    private ResultSet getResultSet(String query) {
        Connection conn;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            conn = SessionManagerSQLite.getConnection();
            statement = conn.createStatement();
            resultSet = statement.executeQuery(query);
            return resultSet;
        } catch (SQLException e) {
            System.out.println("Query was not performed " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }



    //Old code - left as reference
//    int insertArtist(String name) throws SQLException {
//        queryArtists = SessionManagerSQLite.getPreparedStatement(QUERY_ARTISTS_PREP);
//        queryArtists.setString(1, name);
//        ResultSet rs = queryArtists.executeQuery();
//        if (rs.next()) {
//            System.out.println("Artist already exists in db: id is " + rs.getInt(1));
//            return rs.getInt(1);
//        } else {
//            insertArtist = SessionManagerSQLite.getPreparedStatement(INSERT_ARTIST);
//            insertArtist.setString(1, name);
//            int affectedRows = insertArtist.executeUpdate();
//            if (affectedRows != 1) {
//                throw new SQLException("Problem with inserting Artist");
//            }
//            ResultSet generatedKeys = insertArtist.getGeneratedKeys();
//            if (generatedKeys.next()) {
//                return generatedKeys.getInt(1);
//            } else {
//                throw new SQLException("Problem retrieving an _id (artist)");
//            }
//        }
//    }
}
