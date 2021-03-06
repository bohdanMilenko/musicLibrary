package com.musicLib.repository.SQLightRepository;

import com.musicLib.repository.SQLUtil.SessionManagerSQLite;
import com.musicLib.entities.Album;
import com.musicLib.entities.Artist;
import com.musicLib.exceptions.QueryException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.musicLib.repository.SQLightRepository.MetaDataSQL.*;

public class AlbumRepositorySQL implements com.musicLib.repository.AlbumRepository {

    private PreparedStatement insertAlbum;
    private PreparedStatement queryAlbums;
    private PreparedStatement queryByArtistID;
    private PreparedStatement deleteAlbumByID;
    private PreparedStatement deleteByArtistID;
    private SessionManagerSQLite sessionManagerSQLite = new SessionManagerSQLite();

    @Override
    public boolean add(Album album) throws SQLException {
        String query = buildInsertQuery();
        insertAlbum = sessionManagerSQLite.getPreparedStatement(query);
        insertAlbum.setString(1, album.getName());
        insertAlbum.setInt(2, album.getArtist().getId());
        insertAlbum.executeUpdate();
        return true;
    }

    /**
     * INSERT INTO albums (album,artist) = VALUES (?,?)
     */
    private String buildInsertQuery() {
        QueryBuilder qb = new QueryBuilder();
        qb.insertTo(TABLE_ALBUMS).insertSpecifyColumns(COLUMN_ALBUMS_NAME, COLUMN_ALBUMS_ARTIST);
        return qb.toString();
    }


    @Override
    public List<Album> getAlbumsByArtistID(int artistID) throws SQLException {
        String query = buildQueryByArtistID();
        System.out.println(query);
        queryByArtistID = sessionManagerSQLite.getPreparedStatement(query);
        // queryByArtistName = SessionManagerSQLite.getPreparedStatement(QUERY_ALBUMS_BY_ARTIST_NAME);
        queryByArtistID.setInt(1, artistID);
        ResultSet rs = queryByArtistID.executeQuery();
        List<Album> albums = resultSetToAlbum(rs);
        return albums;
    }

    /**
     * SELECT artists._id , artists.name, albums._id , albums.name
     * FROM artists INNER JOIN albums ON albums.artist = artists._id
     * WHERE artists.name = "?"
     */

    private String buildQueryByArtistID() {
        QueryBuilder generalQuery = buildGeneralQueryNoConditions();
        generalQuery.specifyFirstCondition(TABLE_ARTISTS, COLUMN_ARTISTS_NAME);
        return generalQuery.toString();
    }

    @Override
    public List<Album> getByName(String albumName) throws SQLException {
        List<Album> returnList = new ArrayList<>();
        //queryAlbums = SessionManagerSQLite.getPreparedStatement(QUERY_BY_ALBUM_NAME);
        String query = buildQueryByName();
        queryAlbums = sessionManagerSQLite.getPreparedStatement(query);
        queryAlbums.setString(1, albumName);
        ResultSet rs = queryAlbums.executeQuery();
        returnList = resultSetToAlbum(rs);
        return returnList;
    }

    /**
     * SELECT artists._id , artists.name, albums._id , albums.name
     * FROM artists INNER JOIN albums ON albums.artist = artists._id
     * WHERE albums.name = "?"
     */
    private String buildQueryByName() {
        QueryBuilder qb = buildGeneralQueryNoConditions();
        qb.specifyFirstCondition(TABLE_ALBUMS, COLUMN_ALBUMS_NAME);
        return qb.toString();
    }

    private QueryBuilder buildGeneralQueryNoConditions() {
        QueryBuilder qb = new QueryBuilder();
        qb.startQuery(TABLE_ARTISTS, COLUMN_ARTISTS_ID).addSelection(TABLE_ARTISTS, COLUMN_ARTISTS_NAME)
                .addSelection(TABLE_ALBUMS, COLUMN_ALBUMS_ID).addSelection(TABLE_ALBUMS, COLUMN_ALBUMS_NAME)
                .queryFrom(TABLE_ARTISTS)
                .innerJoinOn(TABLE_ALBUMS, COLUMN_ALBUMS_ARTIST, TABLE_ARTISTS, COLUMN_ARTISTS_ID);
        return qb;
    }

    /**
     * The sequence of resultSet: artists._id , artists.name, albums._id , albums.name
     */
    private List<Album> resultSetToAlbum(ResultSet rs) throws SQLException {
        List<Album> albumsToReturn = new ArrayList<>();
        while (rs.next()) {
            Album tempAlbum = new Album();
            Artist tempArtist = new Artist();

            tempArtist.setId(rs.getInt(1));
            tempArtist.setName(rs.getString(2));
            tempAlbum.setArtist(tempArtist);

            tempAlbum.setId(rs.getInt(3));
            tempAlbum.setName(rs.getString(4));
            albumsToReturn.add(tempAlbum);
        }
        return albumsToReturn;
    }

    @Override
    public boolean delete(int albumID, int artistID) throws SQLException {
        String query = buildDeleteByAlbumNameAndArtistID();
        System.out.println(query);
        deleteAlbumByID = sessionManagerSQLite.getPreparedStatement(query);
        deleteAlbumByID.executeUpdate();
        return true;
    }

    /**
     * DELETE FROM albums WHERE albums._id = ? AND albums.artist = ?
     */
    private String buildDeleteByAlbumNameAndArtistID() {
        QueryBuilder qb = new QueryBuilder();
        qb.deleteFrom(TABLE_ALBUMS).specifyFirstCondition(TABLE_ALBUMS, COLUMN_ALBUMS_ID)
                .addANDCondition(TABLE_ALBUMS, COLUMN_ALBUMS_ARTIST);
        return qb.toString();
    }

    @Override
    public boolean deleteByArtistID(int artistID) throws QueryException {
        try {
            String query = buildDeleteByArtistID();
            deleteByArtistID = sessionManagerSQLite.getPreparedStatement(query);
            deleteAlbumByID.setInt(1, artistID);
            deleteAlbumByID.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new QueryException("Unable to delete albums by artist ID", e);
        }
    }

    private String buildDeleteByArtistID() {
        QueryBuilder qb = new QueryBuilder();
        qb.deleteFrom(TABLE_ALBUMS).specifyFirstCondition(TABLE_ALBUMS, COLUMN_ALBUMS_ARTIST);
        return qb.toString();
    }

    //    private static final String QUERY_ALBUMS = "SELECT " + COLUMN_ALBUMS_ID + " FROM " + TABLE_ALBUMS
//            + " WHERE " + COLUMN_ALBUMS_NAME + "= ?";
//
//
//    private static final String QUERY_ALBUMS_BY_ARTIST_NAME = "SELECT " + COLUMN_ALBUMS_ID + ", " + COLUMN_ALBUMS_NAME
//            + ", " + COLUMN_ALBUMS_ARTIST +
//            " FROM " + TABLE_ALBUMS
//            + " WHERE " + COLUMN_ALBUMS_ARTIST + "= ?";
//
//    /**
//     * SELECT albums._id , albums.name, albums.artist , artists._id , artists.name
//     * FROM albums INNER JOIN artists ON albums.artist = artists._id WHERE artists.name = "?"
//     **/
//    private static final String QUERY_BY_ALBUM_NAME = "SELECT " + TABLE_ALBUMS + "." + COLUMN_ALBUMS_ID + ", " +
//            TABLE_ALBUMS + "." + COLUMN_ALBUMS_NAME + ", " + TABLE_ALBUMS + "." + COLUMN_ALBUMS_ARTIST + ", "
//            + TABLE_ARTISTS + "." + COLUMN_ARTISTS_ID + ", " + TABLE_ARTISTS + "." + COLUMN_ARTISTS_NAME + " FROM " +
//            TABLE_ALBUMS + " INNER JOIN " + TABLE_ARTISTS + " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUMS_ARTIST + " = " +
//            TABLE_ARTISTS + "." + COLUMN_ARTISTS_ID + " WHERE " + TABLE_ALBUMS + "." + COLUMN_ALBUMS_NAME + " = ?";
//    int insertAlbum(String name, int artistId) throws SQLException {
//        insertAlbum = SessionManagerSQLite.getPreparedStatement(INSERT_ALBUM);
//        queryAlbums = SessionManagerSQLite.getPreparedStatement(QUERY_ALBUMS);
//        queryAlbums.setString(1, name);
//        ResultSet rs = queryAlbums.executeQuery();
//        if (rs.next()) {
//            System.out.println("Album already exists in db: id is " + rs.getInt(1));
//            return rs.getInt(1);
//        } else {
//
//            insertAlbum.setString(1, name);
//            insertAlbum.setInt(2, artistId);
//            int affectedRows = insertAlbum.executeUpdate();
//            if (affectedRows != 1) {
//                throw new SQLException("Problem with inserting Album");
//            }
//            ResultSet generatedKeys = insertAlbum.getGeneratedKeys();
//            if (generatedKeys.next()) {
//                return generatedKeys.getInt(1);
//            } else {
//                throw new SQLException("Problem retrieving an _id (album)");
//            }
//        }
//    }


//    public List<Album> queryByArtistAndAlbumName(String artistName, String albumName) throws SQLException {
//        List<Album> albumToReturn;
//        String query = buildQueryByArtistAndAlbumNames();
//        queryByArtistAndAlbumName = SessionManagerSQLite.getPreparedStatement(query);
//        ResultSet rs = queryByArtistAndAlbumName.executeQuery();
//        albumToReturn = resultSetToAlbum(rs);
//        return albumToReturn;
//    }
//
//    /**
//     * SELECT artists._id , artists.name, albums._id , albums.name
//     * FROM artists INNER JOIN albums ON albums.artist = artists._id
//     * WHERE artists.name = "?"
//     */
//    private String buildQueryByArtistAndAlbumNames() {
//        QueryBuilder qb = buildGeneralQueryNoConditions();
//        qb.specifyFirstCondition(TABLE_ALBUMS, COLUMN_ALBUMS_NAME).addANDCondition(TABLE_ARTISTS, COLUMN_ARTISTS_NAME);
//        return qb.toString();
//    }
}
