package com.musicLib.repository.SQLightRepository;

import com.musicLib.SQLUtil.SessionManagerSQLite;
import com.musicLib.entities.Album;
import com.musicLib.entities.Artist;
import com.musicLib.repository.ArtistRepository;
import com.musicLib.exceptions.ArtistNotFoundException;
import com.musicLib.exceptions.DuplicatedRecordException;

import javax.management.Query;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.musicLib.repository.SQLightRepository.MetaData.*;


public class ArtistRepositorySQL implements ArtistRepository {

    private PreparedStatement insertArtist;
    private PreparedStatement queryArtists;
    private PreparedStatement deleteQuery;
    private PreparedStatement queryByArtistID;
    private SessionManagerSQLite SessionManagerSQLite = new SessionManagerSQLite();
    private static AlbumRepositorySQL albumRepositorySQL = new AlbumRepositorySQL();
    private static SongRepositorySQL songRepository = new SongRepositorySQL();


    private static final String QUERY_ALL_ARTISTS = "SELECT " + COLUMN_ARTISTS_ID + "," + COLUMN_ARTISTS_NAME + " FROM " + TABLE_ARTISTS;



    private static final String DELETE_ARTIST = "DELETE FROM " + TABLE_ARTISTS + " WHERE "
            + TABLE_ARTISTS + "." + COLUMN_ARTISTS_NAME + " = ?";

    @Override
    public boolean add(Artist artist) throws SQLException {
       String query = buildInsertQuery();
        insertArtist = SessionManagerSQLite.getPreparedStatement(query);
        insertArtist.setString(1,artist.getName());
        insertArtist.executeUpdate();
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
    public List<Artist> queryAllArtists() throws SQLException {
        String query = buildQueryAll();
        ResultSet resultSet = getResultSet(query);
        List<Artist> artistsToReturn = resultSetToArtist(resultSet);
        List<Album> albums;
        //TODO MOVE THIS TO RECORDVALIDATOR CLASS ON SERVICE LEVEL
        if (artistsToReturn != null) {
            for (Artist artist : artistsToReturn) {
                albums = albumRepositorySQL.queryAlbumsByArtistID(artist.getName());
                artist.setAlbums(albums);
            }
        }

        return artistsToReturn;
    }

    /**
     * SELECT * FROM artist
     */
    private String buildQueryAll(){
        QueryBuilder qb = new QueryBuilder();
        qb.startQueryAll().queryFrom(TABLE_ARTISTS);
        return qb.toString();
    }

    @Override
    public List<Artist> queryArtist(String artistName) {
        String query = buildQueryByName();
        //PreparedStatement queryArtist = SessionManagerSQLite.getPreparedStatement(QUERY_ARTIST_BY_NAME);
        System.out.println(query);
        PreparedStatement queryArtist = SessionManagerSQLite.getPreparedStatement(query);
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

    /**
     * SELECT artists._id, artists.name FROM artists WHERE artists.name = ?
     */
    private String buildQueryByName(){
        QueryBuilder qb = new QueryBuilder();
        qb.startQuery(TABLE_ARTISTS, COLUMN_ARTISTS_ID).addSelection(TABLE_ARTISTS,COLUMN_ARTISTS_NAME)
                .queryFrom(TABLE_ARTISTS).specifyFirstCondition(TABLE_ARTISTS,COLUMN_ARTISTS_NAME);
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
        List<Artist> artistToDelete = queryArtist(artistName);
        int numberOfArtists = artistToDelete.size();
        System.out.println(numberOfArtists);
        switch (numberOfArtists) {
            case 1:
                Artist artist = artistToDelete.get(0);
                System.out.println(artist.toString());
                List<Album> artistsAlbums = artist.getAlbums();
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
        String query = buildQueryByID();
        queryByArtistID= SessionManagerSQLite.getPreparedStatement(query);
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
    private String buildQueryByID(){
        QueryBuilder qb = new QueryBuilder();
        qb.startQuery(TABLE_ARTISTS,COLUMN_ARTISTS_ID).addSelection(TABLE_ARTISTS,COLUMN_ARTISTS_NAME)
                .queryFrom(TABLE_ARTISTS).specifyFirstCondition(TABLE_ARTISTS,COLUMN_ARTISTS_ID);
        return qb.toString();
    }

    //TODO RETHINK THIS METHOD
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
