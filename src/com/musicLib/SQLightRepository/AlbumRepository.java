package com.musicLib.Repository;

import com.musicLib.util.SessionManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.musicLib.Repository.MetaData.*;

public class AlbumRepository {

    private PreparedStatement queryAlbums;
    private  PreparedStatement insertAlbum;
    private SessionManager sessionManager = new SessionManager();

    private static final String QUERY_ALBUMS = "SELECT " + COLUMN_ALBUMS_ID + " FROM " + TABLE_ALBUMS
            + " WHERE " + COLUMN_ALBUMS_NAME + "= ?";
    private static final String INSERT_ALBUM = " INSERT INTO " + TABLE_ALBUMS +
            " (" + COLUMN_ALBUMS_NAME + ", " + COLUMN_ALBUMS_ARTIST + ") VALUES(?,?)";


    int insertAlbum(String name, int artistId) throws SQLException {
        insertAlbum = sessionManager.getPreparedStatement(INSERT_ALBUM);
        queryAlbums = sessionManager.getPreparedStatement(QUERY_ALBUMS);
        queryAlbums.setString(1,name);
        ResultSet rs = queryAlbums.executeQuery();
        if(rs.next()){
            System.out.println("Album already exists in db: id is " + rs.getInt(1));
            return rs.getInt(1);
        }else {

            insertAlbum.setString(1,name);
            insertAlbum.setInt(2,artistId);
            int affectedRows =  insertAlbum.executeUpdate();
            if(affectedRows != 1){
                throw  new SQLException("Problem with inserting Album");
            }
            ResultSet generatedKeys = insertAlbum.getGeneratedKeys();
            if (generatedKeys.next()){
                return  generatedKeys.getInt(1);
            }
            else {
                throw new SQLException("Problem retrieving an _id (album)");
            }
        }
    }
}
