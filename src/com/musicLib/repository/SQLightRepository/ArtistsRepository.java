package com.musicLib.repository.SQLightRepository;

import com.musicLib.SQLUtil.SessionManagerSQLite;
import com.musicLib.entities.Artist;
import com.musicLib.repository.ArtistRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.musicLib.repository.SQLightRepository.MetaData.*;


public class ArtistsRepository implements ArtistRepository {

    private PreparedStatement insertArtist;
    private PreparedStatement queryArtists;
    private SessionManagerSQLite SessionManagerSQLite = new SessionManagerSQLite();
    private AlbumRepository albumRepository = new AlbumRepository();


    private static final String QUERY_ALL_ARTISTS = "SELECT " + COLUMN_ARTISTS_ID + "," + COLUMN_ARTISTS_NAME + " FROM " + TABLE_ARTISTS;

    private static final String QUERY_ARTISTS_PREP = "SELECT " + COLUMN_ARTISTS_ID + " FROM " + TABLE_ARTISTS
            + " WHERE " + COLUMN_ARTISTS_NAME + "= ?";

    private static final String QUERY_ARTIST_BY_NAME = "SELECT " + COLUMN_ARTISTS_ID + " FROM " + TABLE_ARTISTS
            + " WHERE " + COLUMN_ARTISTS_NAME + "= ?";


    private static final String INSERT_ARTIST = "INSERT INTO " + TABLE_ARTISTS +
            " (" + COLUMN_ARTISTS_NAME + ") VALUES(?)";

    @Override
    public boolean insert(Artist artist) {
        //TODO COMPLETE THIS METHOD! GET GENERATED KEYS AND SET THEM!
        return false;
    }

    @Override
    public List<Artist> queryAllArtists() {
        ResultSet resultSet = getResultSet(QUERY_ALL_ARTISTS);
        List<Artist> artists = resultSetToArtist(resultSet);
        return artists;
    }

    @Override
    public List<Artist> queryArtist(String artistName) {
        PreparedStatement queryArtist = SessionManagerSQLite.getPreparedStatement(QUERY_ARTIST_BY_NAME);
        List<Artist> returnList = new ArrayList<>();
        try {
            queryArtist.setString(1, artistName);
            ResultSet rs = queryArtist.executeQuery();
            returnList = resultSetToArtist(rs);
            return returnList;
        } catch (SQLException e) {
            System.out.println(e + "\nCannot query by Artist Name");
            return null;
        }
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
    public boolean deleteArtist(String artistName) {
        List<Artist> artistToDelete = queryArtist(artistName);
        if(artistToDelete.size()==1){
           albumRepository.deleteByArtistName(artistName);
           //TODO FINISH THIS METHOD
           return true;
        }else{
            throw new RuntimeException("More than one artist found with the same name");
        }

    }


    private ResultSet getResultSet(String query) {
        List<Artist> artists = new ArrayList<>();
        Connection conn;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            conn = SessionManagerSQLite.getConnection();
            statement = conn.createStatement();
            resultSet = statement.executeQuery(QUERY_ALL_ARTISTS);
            return resultSet;
        } catch (SQLException e) {
            System.out.println("Query was not performed " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    int insertArtist(String name) throws SQLException {
        queryArtists = SessionManagerSQLite.getPreparedStatement(QUERY_ARTISTS_PREP);
        queryArtists.setString(1, name);
        ResultSet rs = queryArtists.executeQuery();
        if (rs.next()) {
            System.out.println("Artist already exists in db: id is " + rs.getInt(1));
            return rs.getInt(1);
        } else {
            insertArtist = SessionManagerSQLite.getPreparedStatement(INSERT_ARTIST);
            insertArtist.setString(1, name);
            int affectedRows = insertArtist.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException("Problem with inserting Artist");
            }
            ResultSet generatedKeys = insertArtist.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Problem retrieving an _id (artist)");
            }
        }
    }
}
