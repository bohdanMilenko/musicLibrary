package com.musicLib.repository.SQLightRepository;

import com.musicLib.SQLUtil.SessionManagerSQLite;
import com.musicLib.entities.Album;
import com.musicLib.entities.Artist;
import com.musicLib.repository.ArtistRepository;
import com.musicLib.repositoryExceptions.ArtistNotFoundException;
import com.musicLib.repositoryExceptions.DuplicatedRecordException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.musicLib.repository.SQLightRepository.MetaData.*;

public class AlbumRepository implements com.musicLib.repository.AlbumRepository {

    private PreparedStatement queryAlbums;
    private PreparedStatement insertAlbum;
    private PreparedStatement queryByArtistName;
    private SessionManagerSQLite SessionManagerSQLite = new SessionManagerSQLite();
    private ArtistRepository artistRepository = new ArtistsRepository();
    private SongsRepository songsRepository = new SongsRepository();

    private static final String QUERY_ALBUMS = "SELECT " + COLUMN_ALBUMS_ID + " FROM " + TABLE_ALBUMS
            + " WHERE " + COLUMN_ALBUMS_NAME + "= ?";
    private static final String INSERT_ALBUM = " INSERT INTO " + TABLE_ALBUMS +
            " (" + COLUMN_ALBUMS_NAME + ", " + COLUMN_ALBUMS_ARTIST + ") VALUES(?,?)";
    private static final String QUERY_ALBUMS_BY_ARTIST_NAME_PREP = "SELECT " + COLUMN_ALBUMS_ID + ", " + COLUMN_ALBUMS_NAME
            + ", " + COLUMN_ALBUMS_ARTIST +
            " FROM " + TABLE_ALBUMS
            + " WHERE " + COLUMN_ALBUMS_ARTIST + "= ?";


    @Override
    public boolean insert(Album album, String artistName) throws ArtistNotFoundException, DuplicatedRecordException, SQLException {
        List<Artist> foundArtist = artistRepository.queryArtist(artistName);
        if (foundArtist.size() == 1) {
            insertAlbum = SessionManagerSQLite.getPreparedStatement(INSERT_ALBUM);
            queryAlbums = SessionManagerSQLite.getPreparedStatement(QUERY_ALBUMS);
            queryAlbums.setString(1, album.getName());
            ResultSet rs = queryAlbums.executeQuery();
            if (rs.next()) {
                throw new DuplicatedRecordException("Such album already exists");
            }
            Artist artist = album.getArtist();
            insertAlbum.setString(1, album.getName());
            insertAlbum.setInt(2, artist.getId());
            int affectedRows = insertAlbum.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException("Problem with inserting Album");
            }
            ResultSet generatedKeys = insertAlbum.getGeneratedKeys();
            if (generatedKeys.next()) {
                int albumKey = generatedKeys.getInt(1);
                album.setId(albumKey);
                return true;
            } else {
                throw new SQLException("Problem retrieving an _id (album)");
            }

        } else if (foundArtist.size() > 1) {
            throw new DuplicatedRecordException("More than one artist with identical name");
        } else {
            throw new ArtistNotFoundException("No Artist found. Cannot insert album without artist");
        }

    }

    @Override
    public List<Album> queryByArtistName(String artistName) throws SQLException {
        List<Album> albumsToReturn = new ArrayList<>();
        List<Artist> artists = artistRepository.queryArtist(artistName);
        if (artists.size() > 0) {
            Artist foundArtist = artists.get(0);
            int artistId = foundArtist.getId();
            queryByArtistName = SessionManagerSQLite.getPreparedStatement(QUERY_ALBUMS_BY_ARTIST_NAME_PREP);
            queryByArtistName.setInt(1, artistId);
            ResultSet rs = queryByArtistName.executeQuery();
            albumsToReturn = resultSetToAlbum(rs);
            return albumsToReturn;
        } else {
            return null;
        }
    }

    @Override
    public boolean delete(String albumName, String artistName) throws SQLException, ArtistNotFoundException {
       List<Album> foundAlbums = queryByArtistName(artistName);
       if(foundAlbums.size()==1) {
           Album album = foundAlbums.get(0);
           int artistId = album.getArtist().getId();
           int albumId = album.getId();
           songsRepository.deleteSongsFromAlbum(albumId);
       }else {
           throw new ArtistNotFoundException("0 or more than 1 artist found");
       }


        return false;
    }

    @Override
    public List<Album> queryByAlbumName(String albumName) throws SQLException {
        List<Album> returnList = new ArrayList<>();
        queryAlbums = SessionManagerSQLite.getPreparedStatement(QUERY_ALBUMS);
        queryAlbums.setString(1,albumName);
        ResultSet rs = queryAlbums.executeQuery();


    }

    public boolean deleteByArtistName(String artist){
        //TODO WRITE IMPLEMENTATION
        return true;

    }

    private List<Album> resultSetToAlbum(ResultSet rs) throws SQLException{

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

    }


    int insertAlbum(String name, int artistId) throws SQLException {
        insertAlbum = SessionManagerSQLite.getPreparedStatement(INSERT_ALBUM);
        queryAlbums = SessionManagerSQLite.getPreparedStatement(QUERY_ALBUMS);
        queryAlbums.setString(1, name);
        ResultSet rs = queryAlbums.executeQuery();
        if (rs.next()) {
            System.out.println("Album already exists in db: id is " + rs.getInt(1));
            return rs.getInt(1);
        } else {

            insertAlbum.setString(1, name);
            insertAlbum.setInt(2, artistId);
            int affectedRows = insertAlbum.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException("Problem with inserting Album");
            }
            ResultSet generatedKeys = insertAlbum.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Problem retrieving an _id (album)");
            }
        }
    }
}
