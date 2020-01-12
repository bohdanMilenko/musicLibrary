package com.musicLib.Repository;

import com.musicLib.databaseModel.Artist;
import com.musicLib.util.SessionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.musicLib.Repository.MetaData.*;



public class ArtistsRepository {

    private PreparedStatement insertArtist;
    private  PreparedStatement queryArtists;
    private SessionManager sessionManager = new SessionManager();

    private static final String INSERT_ARTIST = "INSERT INTO " + TABLE_ARTISTS +
            " (" + COLUMN_ARTISTS_NAME + ") VALUES(?)";

    private static final String QUERY_ARTISTS = "SELECT " + COLUMN_ARTISTS_ID + " FROM " + TABLE_ARTISTS
            + " WHERE " + COLUMN_ARTISTS_NAME + "= ?";
    private static final String QUERY_ARTISTS_PREP = "SELECT " + COLUMN_ARTISTS_ID + " FROM " + TABLE_ARTISTS
            + " WHERE " + COLUMN_ARTISTS_NAME + "= ?";

    public List<Artist> queryArtistsTable() {
        List<Artist> artists = new ArrayList<>();
        Connection conn;
        Statement statement = null;
        ResultSet resultSet = null;
        try { conn = sessionManager.getConnection();
            statement = conn.createStatement();
            resultSet = statement.executeQuery(QUERY_ARTISTS);
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
            return artists;
        }
    }


    public List<Artist> queryArtistsTry() {
        List<Artist> artists2 = new ArrayList<>();
        //No need to close as it is Try with resources;
        try (Connection conn = sessionManager.getConnection();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_ARTISTS);) {
            while (resultSet.next()) {
                Artist artist = new Artist();
                artist.setId(resultSet.getInt(COLUMN_ARTISTS_ID));
                artist.setName(resultSet.getString(COLUMN_ARTISTS_NAME));
                artists2.add(artist);
            }
            return artists2;

        } catch (SQLException e) {
            System.out.println("Query was not performed " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    int insertArtist(String name) throws SQLException {
        queryArtists = sessionManager.getPreparedStatement(QUERY_ARTISTS_PREP);
        queryArtists.setString(1, name);
        ResultSet rs = queryArtists.executeQuery();
        if (rs.next()) {
            System.out.println("Artist already exists in db: id is " + rs.getInt(1));
            return rs.getInt(1);
        } else {
            insertArtist = sessionManager.getPreparedStatement(INSERT_ARTIST);
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
