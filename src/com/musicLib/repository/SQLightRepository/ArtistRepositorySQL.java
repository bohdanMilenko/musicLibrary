package com.musicLib.repository.SQLightRepository;

import com.musicLib.SQLUtil.SessionManagerSQLite;
import com.musicLib.entities.Album;
import com.musicLib.entities.Artist;
import com.musicLib.repository.ArtistRepository;
import com.musicLib.repositoryExceptions.ArtistNotFoundException;
import com.musicLib.repositoryExceptions.DuplicatedRecordException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.musicLib.repository.SQLightRepository.MetaData.*;


public class ArtistRepositorySQL implements ArtistRepository {

    private PreparedStatement insertArtist;
    private PreparedStatement queryArtists;
    private PreparedStatement deleteQuery;
    private SessionManagerSQLite SessionManagerSQLite = new SessionManagerSQLite();
    private static AlbumRepositorySQL albumRepositorySQL = new AlbumRepositorySQL();
    private static SongRepositorySQL songRepository = new SongRepositorySQL();


    private static final String QUERY_ALL_ARTISTS = "SELECT " + COLUMN_ARTISTS_ID + "," + COLUMN_ARTISTS_NAME + " FROM " + TABLE_ARTISTS;

    private static final String QUERY_ARTISTS_PREP = "SELECT " + COLUMN_ARTISTS_ID + " FROM " + TABLE_ARTISTS
            + " WHERE " + COLUMN_ARTISTS_NAME + "= ?";

    private static final String QUERY_ARTIST_BY_NAME = "SELECT " + COLUMN_ARTISTS_ID + ", " + COLUMN_ARTISTS_NAME + " FROM " + TABLE_ARTISTS
            + " WHERE " + COLUMN_ARTISTS_NAME + "= ?";


    /*
  SELECT artists._id , artists.name, albums._id , albums.name, songs._id, songs.title, songs.track
  FROM artists INNER JOIN albums ON albums.artist = artists._id
  INNER JOIN songs ON songs.album = albums._id
  WHERE artists.name = "ZZ Top"
   */
    private static final String QUERY_BY_ARTIST_NAME = "SELECT " + TABLE_ARTISTS + "." + COLUMN_ARTISTS_ID + ", "
            + TABLE_ARTISTS + "." + COLUMN_ARTISTS_NAME + ", " + TABLE_ALBUMS + "." + COLUMN_ALBUMS_ID + ", " +
            TABLE_ALBUMS + "." + COLUMN_ALBUMS_NAME + ", " + TABLE_ALBUMS + "." + COLUMN_ALBUMS_ARTIST + ", " +
            TABLE_SONGS + "." + COLUMN_SONGS_ID + ", " + TABLE_SONGS + "." + COLUMN_SONGS_TITLE + ", " +
            TABLE_SONGS + "." + COLUMN_SONGS_TRACK + " FROM " + TABLE_ARTISTS +
            " INNER JOIN " + TABLE_ALBUMS + " ON " + TABLE_ARTISTS + "." + COLUMN_ARTISTS_ID + " = " +
            TABLE_ALBUMS + "." + COLUMN_ALBUMS_ARTIST +
            "INNER JOIN " + TABLE_SONGS + "." + COLUMN_SONGS_ALBUM + " = " + TABLE_ALBUMS + "." + COLUMN_ARTISTS_ID +
            " WHERE " + TABLE_ARTISTS + "." + COLUMN_ARTISTS_NAME + " = ?";


    private static final String QUERY_ARTIST_BY_ID = "SELECT " + COLUMN_ARTISTS_ID + ", " + COLUMN_ARTISTS_NAME + " FROM " + TABLE_ARTISTS
            + " WHERE " + COLUMN_ARTISTS_ID + "= ?";

    private static final String INSERT_ARTIST = "INSERT INTO " + TABLE_ARTISTS +
            " (" + COLUMN_ARTISTS_NAME + ") VALUES(?)";

    private static final String DELETE_ARTIST = "DELETE FROM " + TABLE_ARTISTS + " WHERE "
            + TABLE_ARTISTS + "." + COLUMN_ARTISTS_NAME + " = ?";

    @Override
    public boolean insert(Artist artist) {
        //TODO COMPLETE THIS METHOD! GET GENERATED KEYS AND SET THEM!
        return false;
    }

    @Override
    public List<Artist> queryAllArtists() throws SQLException {
        ResultSet resultSet = getResultSet(QUERY_ALL_ARTISTS);
        List<Artist> artistsToReturn = resultSetToArtist(resultSet);
        List<Album> albums;
        if (artistsToReturn != null) {
            for (Artist artist : artistsToReturn) {
                albums = albumRepositorySQL.queryByArtistName(artist.getName());
                artist.setAlbumList(albums);
            }
        }

        return artistsToReturn;
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
    public boolean delete(String artistName) throws SQLException, ArtistNotFoundException, DuplicatedRecordException {
        List<Artist> artistToDelete = queryArtist(artistName);
        int numberOfArtists = artistToDelete.size();
        System.out.println(numberOfArtists);
        switch (numberOfArtists) {
            case 1:
                Artist artist = artistToDelete.get(0);
                System.out.println(artist.toString());
                List<Album> artistsAlbums = artist.getAlbumList();
                albumRepositorySQL.deleteAlbumsByArtistName(artistName);
                deleteArtist(artistName);
                return true;
            case 0:
                throw new ArtistNotFoundException("Such artist does not exist");
            default:
                throw new DuplicatedRecordException("Multiple artists with the same name");
        }

    }

    private boolean deleteArtist(String artistName) throws SQLException{
        deleteQuery = SessionManagerSQLite.getPreparedStatement(DELETE_ARTIST);
        deleteQuery.setString(1,artistName);
        deleteQuery.executeUpdate();
        return true;
    }

    public Artist queryById(int id) throws SQLException {
        PreparedStatement query = SessionManagerSQLite.getPreparedStatement(QUERY_ARTIST_BY_NAME);
        query.setInt(1, id);
        ResultSet rs = query.executeQuery();
        List<Artist> resultOfQuery = resultSetToArtist(rs);
        if (resultOfQuery.size() == 1) {
            Artist foundArtist = resultOfQuery.get(0);
            return foundArtist;
        }
        return null;
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


    //Old code - left as reference
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