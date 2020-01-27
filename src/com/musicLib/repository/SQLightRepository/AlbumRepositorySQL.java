package com.musicLib.repository.SQLightRepository;

import com.musicLib.SQLUtil.SessionManagerSQLite;
import com.musicLib.entities.Album;
import com.musicLib.entities.Artist;
import com.musicLib.exceptions.QueryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.musicLib.repository.SQLightRepository.MetaData.*;

public class AlbumRepositorySQL implements com.musicLib.repository.AlbumRepository {

    private PreparedStatement queryAlbums;
    private PreparedStatement insertAlbum;
    private PreparedStatement queryByArtistName;
    private PreparedStatement deleteAlbumById;
    private SessionManagerSQLite SessionManagerSQLite = new SessionManagerSQLite();
    private static SongRepositorySQL songRepositorySQL = new SongRepositorySQL();

    private static final String QUERY_ALBUMS = "SELECT " + COLUMN_ALBUMS_ID + " FROM " + TABLE_ALBUMS
            + " WHERE " + COLUMN_ALBUMS_NAME + "= ?";


    private static final String QUERY_ALBUMS_BY_ARTIST_NAME = "SELECT " + COLUMN_ALBUMS_ID + ", " + COLUMN_ALBUMS_NAME
            + ", " + COLUMN_ALBUMS_ARTIST +
            " FROM " + TABLE_ALBUMS
            + " WHERE " + COLUMN_ALBUMS_ARTIST + "= ?";

    /**
     * SELECT albums._id , albums.name, albums.artist , artists._id , artists.name
     * FROM albums INNER JOIN artists ON albums.artist = artists._id WHERE artists.name = "?"
     **/
    private static final String QUERY_BY_ALBUM_NAME = "SELECT " + TABLE_ALBUMS + "." + COLUMN_ALBUMS_ID + ", " +
            TABLE_ALBUMS + "." + COLUMN_ALBUMS_NAME + ", " + TABLE_ALBUMS + "." + COLUMN_ALBUMS_ARTIST + ", "
            + TABLE_ARTISTS + "." + COLUMN_ARTISTS_ID + ", " + TABLE_ARTISTS + "." + COLUMN_ARTISTS_NAME + " FROM " +
            TABLE_ALBUMS + " INNER JOIN " + TABLE_ARTISTS + " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUMS_ARTIST + " = " +
            TABLE_ARTISTS + "." + COLUMN_ARTISTS_ID + " WHERE " + TABLE_ALBUMS + "." + COLUMN_ALBUMS_NAME + " = ?";

    public static final String DELETE_ALBUM_BY_ID = "DELETE FROM " + TABLE_ALBUMS +
            " WHERE " + TABLE_ALBUMS + "." + COLUMN_ALBUMS_ID + " =?";

    @Override
    public boolean add(Album album) throws SQLException {
        String query = buildInsertQuery();
        insertAlbum = SessionManagerSQLite.getPreparedStatement(query);
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
    public List<Album> queryAlbumsByArtistName(int artistID) throws SQLException {
        String query = buildQueryByArtistName();
        System.out.println(query);
        queryByArtistName = SessionManagerSQLite.getPreparedStatement(query);
        // queryByArtistName = SessionManagerSQLite.getPreparedStatement(QUERY_ALBUMS_BY_ARTIST_NAME);
        queryByArtistName.setInt(1, artistID);
        ResultSet rs = queryByArtistName.executeQuery();
        List<Album> albums = resultSetToAlbum(rs);
        return albums;
    }

    /**
     * SELECT artists._id , artists.name, albums._id , albums.name
     * FROM artists INNER JOIN albums ON albums.artist = artists._id
     * WHERE artists.name = "?"
     */

    private String buildQueryByArtistName() {
        QueryBuilder generalQuery = buildGeneralQueryNoConditions();
        generalQuery.specifyFirstCondition(TABLE_ARTISTS, COLUMN_ARTISTS_NAME);
        return generalQuery.toString();
    }

    private QueryBuilder buildGeneralQueryNoConditions() {
        QueryBuilder qb = new QueryBuilder();
        qb.startQuery(TABLE_ARTISTS, COLUMN_ARTISTS_ID).addSelection(TABLE_ARTISTS, COLUMN_ARTISTS_NAME)
                .addSelection(TABLE_ALBUMS, COLUMN_ALBUMS_ID).addSelection(TABLE_ALBUMS, COLUMN_ALBUMS_NAME)
                .queryFrom(TABLE_ARTISTS)
                .innerJoinOn(TABLE_ALBUMS, COLUMN_ALBUMS_ARTIST, TABLE_ARTISTS, COLUMN_ARTISTS_ID);
        return qb;
    }


    @Override
    public boolean delete(int albumID, int artistID) throws SQLException {
        Connection conn = SessionManagerSQLite.getConnection();
        conn.setAutoCommit(false);
        songRepositorySQL.deleteByAlbumId(albumID);
        String query = buildDeleteByAlbumNameAndArtistID();
        System.out.println(query);
        deleteAlbumById = SessionManagerSQLite.getPreparedStatement(query);
        deleteAlbumById.executeUpdate();
        conn.commit();
        conn.setAutoCommit(true);
        return true;
    }

    private String buildDeleteByAlbumNameAndArtistID() {
    QueryBuilder qb = new QueryBuilder();
    qb.deleteFrom(TABLE_ALBUMS).specifyFirstCondition(TABLE_ALBUMS,COLUMN_ALBUMS_ID)
            .addANDCondition(TABLE_ALBUMS,COLUMN_ALBUMS_ARTIST);
    return qb.toString();
    }


    //TODO FINISH IMPLEMENTATION
//    public Album queryByArtistAndAlbumName(String artistName, String albumName){
//        Album albumToReturn = new Album();
//
//    }

    @Override
    public List<Album> queryByAlbumName(String albumName) throws SQLException {
        List<Album> returnList = new ArrayList<>();
        queryAlbums = SessionManagerSQLite.getPreparedStatement(QUERY_BY_ALBUM_NAME);
        queryAlbums.setString(1, albumName);
        ResultSet rs = queryAlbums.executeQuery();
        returnList = resultSetToAlbum(rs);
        return returnList;
    }


    private List<Album> resultSetToAlbum(ResultSet rs) throws SQLException {
        List<Album> albumsToReturn = new ArrayList<>();
        while (rs.next()) {
            Album tempAlbum = new Album();
            Artist tempArtist = new Artist();

            tempAlbum.setId(rs.getInt(1));
            tempAlbum.setName(rs.getString(2));

            tempArtist.setId(rs.getInt(4));
            tempArtist.setName(rs.getString(5));
            tempAlbum.setArtist(tempArtist);

            albumsToReturn.add(tempAlbum);
        }
        return albumsToReturn;
    }

    public boolean deleteAlbumsByArtistName(String artist) throws SQLException {
        List<Album> albumsToDelete = queryAlbumsByArtistName(artist);
        deleteRelatedSongs(albumsToDelete);
        for (Album tempAlbum : albumsToDelete) {
            deleteAlbumById = SessionManagerSQLite.getPreparedStatement(DELETE_ALBUM_BY_ID);
            deleteAlbumById.setInt(1, tempAlbum.getId());
            deleteAlbumById.executeUpdate();
        }
        return true;
    }

    private void deleteRelatedSongs(List<Album> albumsToDelete) throws SQLException {
        for (Album tempAlbum : albumsToDelete) {
            int albumId = tempAlbum.getId();
            songRepositorySQL.deleteByAlbumId(albumId);
        }
    }

    private List<Album> resultSetToAlbum(ResultSet rs, String artistName) throws SQLException {
        List<Album> albumsToReturn = new ArrayList<>();
        while (rs.next()) {
            Album tempAlbum = new Album();
            Artist tempArtist = new Artist();
            tempAlbum.setId(rs.getInt(1));
            tempAlbum.setName(rs.getString(2));
            tempArtist.setName(artistName);
            tempArtist.setId(rs.getInt(3));
            tempAlbum.setArtist(tempArtist);
            albumsToReturn.add(tempAlbum);
        }
        return albumsToReturn;
    }


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
}
