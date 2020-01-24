package com.musicLib.repository.SQLightRepository;

import com.musicLib.SQLUtil.SessionManagerSQLite;
import com.musicLib.entities.Album;
import com.musicLib.entities.Artist;
import com.musicLib.entities.Song;
import com.musicLib.repositoryExceptions.ArtistNotFoundException;
import com.musicLib.repositoryExceptions.DuplicatedRecordException;

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
    private static ArtistRepositorySQL artistRepository = new ArtistRepositorySQL();
    private static SongRepositorySQL songRepositorySQL = new SongRepositorySQL();

    private static final String QUERY_ALBUMS = "SELECT " + COLUMN_ALBUMS_ID + " FROM " + TABLE_ALBUMS
            + " WHERE " + COLUMN_ALBUMS_NAME + "= ?";

    private static final String INSERT_ALBUM = " INSERT INTO " + TABLE_ALBUMS +
            " (" + COLUMN_ALBUMS_NAME + ", " + COLUMN_ALBUMS_ARTIST + ") VALUES(?,?)";

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
    public boolean insert(Album album, String artistName) throws ArtistNotFoundException, DuplicatedRecordException, SQLException {
        List<Artist> foundArtistList = artistRepository.queryArtist(artistName);
        foundArtistList.forEach(v -> System.out.println(v.getName()));
        if (foundArtistList.size() == 1) {
            insertAlbum = SessionManagerSQLite.getPreparedStatement(INSERT_ALBUM);
            queryAlbums = SessionManagerSQLite.getPreparedStatement(QUERY_ALBUMS);
            queryAlbums.setString(1, album.getName());
            ResultSet rs = queryAlbums.executeQuery();
            if (rs.next()) {
                throw new DuplicatedRecordException("Such album already exists");
            }
            Artist artist = foundArtistList.get(0);
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

        } else if (foundArtistList.size() > 1) {
            throw new DuplicatedRecordException("More than one artist with identical name");
        } else {
            throw new ArtistNotFoundException("No Artist found. Cannot insert album without artist");
        }

    }

    @Override
    public List<Album> queryByArtistName(String artistName) throws SQLException {
        List<Album> albumsToReturn;
        List<Artist> artists = artistRepository.queryArtist(artistName);
        if (artists.size() == 1) {
            Artist foundArtist = artists.get(0);
            int artistId = foundArtist.getId();
            queryByArtistName = SessionManagerSQLite.getPreparedStatement(QUERY_ALBUMS_BY_ARTIST_NAME);
            queryByArtistName.setInt(1, artistId);
            ResultSet rs = queryByArtistName.executeQuery();
            albumsToReturn = resultSetToAlbum(rs, artistName);
            for (Album album : albumsToReturn) {
                List<Song> songsList = songRepositorySQL.queryByAlbumId(album.getId());
                album.setSongs(songsList);
            }
            return albumsToReturn;
        } else {
            return null;
        }
    }

    @Override
    public boolean delete(String albumName, String artistName) throws SQLException, ArtistNotFoundException {
        List<Album> foundAlbums = queryByArtistName(artistName);
        if (foundAlbums.size() == 1) {
            Album album = foundAlbums.get(0);
            int artistId = album.getArtist().getId();
            int albumId = album.getId();
            //songsRepository.deleteSongsFromAlbum(albumId);
        } else {
            throw new ArtistNotFoundException("0 or more than 1 artist found");
        }


        return false;
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
        List<Album> albumsToDelete = queryByArtistName(artist);
        deleteRelatedSongs(albumsToDelete);
        //TODO DELETE ALBUMS BY ARTIST ID
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
            songRepositorySQL.deleteSongsByAlbumId(albumId);
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