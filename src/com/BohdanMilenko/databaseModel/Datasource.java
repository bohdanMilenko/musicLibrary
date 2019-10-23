package com.BohdanMilenko.databaseModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Datasource {

    private static final String DB_NAME = "music.db";
    private static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Drive D\\Java Root\\Java Directory\\JDBCmusic\\" + DB_NAME;

    private static final String TABLE_ALBUMS = "albums";
    private static final String COLUMN_ALBUMS_ID = "_id";
    private static final String COLUMN_ALBUMS_NAME = "name";
    private static final String COLUMN_ALBUMS_ARTIST = "artist";
    private static final int INDEX_ALBUMS_ID = 1;
    private static final int INDEX_ALBUMS_NAME = 2;
    private static final int INDEX_ALBUMS_ARTIST = 3;

    private static final String TABLE_SONGS = "songs";
    private static final String COLUMN_SONGS_ID = "_id";
    private static final String COLUMN_SONGS_TITLE = "title";
    private static final String COLUMN_SONGS_TRACK = "track";
    private static final String COLUMN_SONGS_ALBUM = "album";
    private static final int INDEX_SONGS_ID = 1;
    private static final int INDEX_SONGS_TRACK = 2;
    private static final int INDEX_SONGS_TITLE = 3;
    private static final int INDEX_SONGS_ALBUM = 4;

    private static final String TABLE_ARTISTS = "artists";
    private static final String COLUMN_ARTISTS_ID = "_id";
    private static final String COLUMN_ARTISTS_NAME = "name";
    private static final int INDEX_ARTISTS_ID = 1;
    private static final int INDEX_ARTISTS_NAME = 2;

    public static final int ORDER_NONE = 1;
    public static final int ORDER_ASC = 2;
    public static final int ORDER_DESC = 3;


    public static final String QUERY_BY_SONG_NAME = "SELECT " + TABLE_SONGS + "." + COLUMN_SONGS_TITLE + ", "
            + TABLE_ARTISTS + "." + COLUMN_ARTISTS_NAME + ", "
            + TABLE_ALBUMS + "." + COLUMN_ALBUMS_NAME +
            " FROM " + TABLE_SONGS + " INNER JOIN " + TABLE_ALBUMS + " ON " + TABLE_SONGS + "." + COLUMN_SONGS_ALBUM + " = " + TABLE_ALBUMS + "." + COLUMN_ARTISTS_ID
            + " INNER JOIN " + TABLE_ARTISTS + " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUMS_ARTIST + " = " + TABLE_ARTISTS + "." + COLUMN_ALBUMS_ID
            + " WHERE " + TABLE_SONGS + "." + COLUMN_SONGS_TITLE + " = \"";

    public static final String TABLE_ARTIST_SONG_VIEW = "artist_list3";

    public static final String CREATE_ARTIST_FOR_SONG_VIEW = "CREATE VIEW IF NOT EXISTS " +
            TABLE_ARTIST_SONG_VIEW + " AS SELECT " + TABLE_ARTISTS + "." + COLUMN_ARTISTS_NAME + " AS Artist, " +
            TABLE_ALBUMS + "." + COLUMN_ALBUMS_NAME + " AS " + COLUMN_SONGS_ALBUM + ", " +
            TABLE_SONGS + "." + COLUMN_SONGS_TRACK + ", " + TABLE_SONGS + "." + COLUMN_SONGS_TITLE +
            " FROM " + TABLE_SONGS +
            " INNER JOIN " + TABLE_ALBUMS + " ON " + TABLE_SONGS +
            "." + COLUMN_SONGS_ALBUM + " = " + TABLE_ALBUMS + "." + COLUMN_ALBUMS_ID +
            " INNER JOIN " + TABLE_ARTISTS + " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUMS_ARTIST +
            " = " + TABLE_ARTISTS + "." + COLUMN_ARTISTS_ID +
            " ORDER BY " +
            TABLE_ARTISTS + "." + COLUMN_ARTISTS_NAME + ", " +
            TABLE_ALBUMS + "." + COLUMN_ALBUMS_NAME + ", " +
            TABLE_SONGS + "." + COLUMN_SONGS_TRACK;

    public static final String QUERY_VIEW_ARTISTS_LIST = "SELECT " + COLUMN_ARTISTS_NAME + ", " + COLUMN_SONGS_ALBUM + ", " + COLUMN_SONGS_TRACK
            + " FROM " + TABLE_ARTIST_SONG_VIEW + " WHERE " + COLUMN_SONGS_TITLE + "= \"";

    public static final String QUERY_VIEW_ARTISTS_LIST_PREP = "SELECT " + COLUMN_ARTISTS_NAME + ", " + COLUMN_SONGS_ALBUM + ", " + COLUMN_SONGS_TRACK
            + " FROM " + TABLE_ARTIST_SONG_VIEW + " WHERE " + COLUMN_SONGS_TITLE + "= ?";

    public static final String INSERT_ARTIST = "INSERT INTO " +  TABLE_ARTISTS +
                    " (" + COLUMN_ARTISTS_NAME + ") VALUES(?)";
    public static final String INSERT_ALBUM = " INSERT INTO " + TABLE_ALBUMS +
                    " (" +COLUMN_ALBUMS_NAME + ", " + COLUMN_ALBUMS_ARTIST + ") VALUES(?,?)";
    public static final String INSERT_SONG = " INSERT INTO " + TABLE_SONGS +
                    " (" + COLUMN_SONGS_TRACK + ", " +  COLUMN_SONGS_TITLE + ", " + COLUMN_SONGS_ALBUM + ") VALUES(?,?,?)";

    private static final String QUERY_ARTISTS = "SELECT " + COLUMN_ARTISTS_ID + " FROM " + TABLE_ARTISTS
            + " WHERE " + COLUMN_ARTISTS_NAME + "= ?";
    private static final String QUERY_ALBUMS = "SELECT " + COLUMN_ALBUMS_ID + " FROM " + TABLE_ALBUMS
            + " WHERE " + COLUMN_ALBUMS_NAME + "= ?";


    private Connection conn;
    private PreparedStatement queryArtistBySong;
    private PreparedStatement insertArtist;
    private PreparedStatement insertAlbum;
    private PreparedStatement insertSong;
    private PreparedStatement queryArtists;
    private PreparedStatement queryAlbums;
    // Open-close connection
    public boolean openDB() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            insertArtist = conn.prepareStatement(INSERT_ARTIST, Statement.RETURN_GENERATED_KEYS);
            insertAlbum = conn.prepareStatement(INSERT_ALBUM,Statement.RETURN_GENERATED_KEYS);
            insertSong = conn.prepareStatement(INSERT_SONG);
            queryArtistBySong = conn.prepareStatement(QUERY_VIEW_ARTISTS_LIST_PREP);
            queryArtists = conn.prepareStatement(QUERY_ARTISTS);
            queryAlbums = conn.prepareStatement(QUERY_ALBUMS);
            System.out.println("Connection established");
            return true;
        } catch (SQLException e) {
            System.out.println("Cannot open a DB " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }


    public boolean closeDB() {
        try {
            if(queryArtistBySong != null){
                queryArtistBySong.close();
            }
            if (conn != null) {
                conn.close();
                System.out.println("Session ended successfully");
            }
            closeStatement(insertAlbum);
            closeStatement(insertArtist);
            closeStatement(insertSong);
            closeStatement(queryArtists);
            closeStatement(queryAlbums);
            return true;
        } catch (SQLException e) {
            System.out.println("Issue with closing a DB");
            e.printStackTrace();
            return false;
        }
    }

    // Closing ResultSet and Statement
    private boolean closeResultSet(ResultSet resultSet) {
        try {
            if (resultSet != null) {

                resultSet.close();
            }
            return true;
        } catch (SQLException e) {
            System.out.println("ResultSet is not closed" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean closeStatement(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Statement is not closed " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    //Queries
    public List<Artist> queryArtists(int sortingOrder) {

        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(TABLE_ARTISTS);

        orderingQuery(sb, sortingOrder, TABLE_ARTISTS, COLUMN_ALBUMS_NAME);

        List<Artist> artists = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = conn.createStatement();
            resultSet = statement.executeQuery(sb.toString());
            while (resultSet.next()) {
                Artist artist = new Artist();
                artist.setId(resultSet.getInt(INDEX_ARTISTS_ID));
                artist.setName(resultSet.getString(INDEX_ARTISTS_NAME));
                artists.add(artist);
            }
            return artists;

        } catch (SQLException e) {
            System.out.println("Query was not performed " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
           closeResultSet(resultSet);
           closeStatement(statement);
        }
    }

    public List<Artist> queryArtistsTry() {

        List<Artist> artists2 = new ArrayList<>();
        //No need to close as it is Try with resources;
        try (Statement statement = conn.createStatement();
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

    public List<String> queryAlbumsForArtists(String artist, int sortOrder) {

        StringBuilder sb = new StringBuilder("SELECT ");
        sb.append(TABLE_ALBUMS).append(".").append(COLUMN_ALBUMS_NAME).append(" FROM ").append(TABLE_ALBUMS)
                .append(" INNER JOIN ").append(TABLE_ARTISTS).append(" ON ")
                .append(TABLE_ALBUMS).append(".").append(COLUMN_ALBUMS_ARTIST).append(" = ")
                .append(TABLE_ARTISTS).append(".").append(COLUMN_ARTISTS_ID)
                .append(" WHERE ").append(TABLE_ARTISTS).append(".").append(COLUMN_ARTISTS_NAME).append(" = ")
                .append("\"").append(artist).append("\"");

        orderingQuery(sb, sortOrder, TABLE_ALBUMS, COLUMN_ALBUMS_NAME);
        System.out.println("SQL statement: " + sb.toString());
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = conn.createStatement();
            resultSet = statement.executeQuery(sb.toString());
            List<String> albums = new ArrayList<>();
            while ((resultSet.next())) {
                albums.add(resultSet.getString(1));
                //  We can use index for getting the column from the ResultSet, as far as we query only one column,
                // we are good to hardcode it with 1.
            }
            System.out.println(artist + "s' albums:");
            return albums;

        } catch (SQLException e) {
            System.out.println("Query is not performed " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            closeResultSet(resultSet);
            closeStatement(statement);
        }
    }

    public List<MusicLibrary> queryArtistBySong(String songName, int sortingOrder) {

        StringBuilder sb = new StringBuilder(QUERY_BY_SONG_NAME);
        sb.append(songName).append("\" ");
        orderingQuery(sb, sortingOrder, TABLE_ARTISTS, COLUMN_ARTISTS_NAME);
        List<MusicLibrary> songsByArtistReturnList = new ArrayList<>();

        System.out.println(" SQL Statement: " + sb.toString());

        try (Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sb.toString())) {
            while (resultSet.next()) {
                SongArtist internalInstance = new SongArtist();
                internalInstance.setTrackName(resultSet.getString(1));
                internalInstance.setArtistName(resultSet.getString(2));
                internalInstance.setAlbumName(resultSet.getString(3));

                songsByArtistReturnList.add(internalInstance);
            }
            return songsByArtistReturnList;

        } catch (SQLException e) {
            System.out.println("Issue with Query Artist by Song" + e.getMessage());
            e.printStackTrace();
        }

        return songsByArtistReturnList;
    }


    public int getCountMinMax(String tableName) {
        String query = "SELECT COUNT(*), MIN(" + COLUMN_SONGS_ID + "), MAX (" + COLUMN_SONGS_ID + ") FROM " + tableName;

        try (Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            int count = rs.getInt(1);
            int min = rs.getInt(2);
            int max = rs.getInt(3);

            System.out.format("The number of songs: %d , min - %d, max %d\n", count, min, max);
            return 1;
        } catch (SQLException e) {
            System.out.println("Count and Min/Max query problem: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }

    }

    public boolean createArtistsListView() {
        try (Statement statement = conn.createStatement()) {

            statement.execute(CREATE_ARTIST_FOR_SONG_VIEW);
            return true;
        } catch (SQLException e) {
            System.out.println("Count and Min/Max query problem: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<MusicLibrary> queryBySongTitleView(String songName) {

        List<MusicLibrary> listForReturn = new ArrayList<>();
        try {
            queryArtistBySong.setString(1,songName);
            ResultSet rs = queryArtistBySong.executeQuery();
            while (rs.next()){
                SongArtist internalObject = new SongArtist();
                internalObject.setArtistName(rs.getString(1));
                internalObject.setAlbumName(rs.getString(2));
                listForReturn.add(internalObject);
            }
            return listForReturn;
        } catch (SQLException e) {
            System.out.println("Query artist by song issue (view): " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private int insertArtist(String name) throws SQLException{
        queryArtists.setString(1,name);
        ResultSet rs = queryArtists.executeQuery();
        if(rs.next()){
            System.out.println("Artist already exists in db: id is " + rs.getInt(1));
            return rs.getInt(1);
        }else {

            insertArtist.setString(1,name);
            int affectedRows =  insertArtist.executeUpdate();
            if(affectedRows != 1){
                throw  new SQLException("Problem with inserting Artist");
            }
            ResultSet generatedKeys = insertArtist.getGeneratedKeys();
            if (generatedKeys.next()){
                 return  generatedKeys.getInt(1);
            }
            else {
                throw new SQLException("Problem retrieving an _id (artist)");
            }
        }
    }

    private int insertAlbum(String name, int artistId) throws SQLException{
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

    public void insertSong(String title, String artist, String album, int trackNumber){
        try{
            conn.setAutoCommit(false);
            int artistId =  insertArtist(artist);
            int albumId =  insertAlbum(album, artistId );
            insertSong.setInt(1, trackNumber);
            insertSong.setString(2,title);
            insertSong.setInt(3,albumId);
            int rowsAffected =  insertSong.executeUpdate();
            if(rowsAffected == 1){
                conn.commit();
                System.out.println("Successfully inserted a song!");
            } else {
                throw new SQLException(("Issue creating songs' entity"));
            }
        }catch (SQLException e){
            System.out.println("Performing rollback " + e.getMessage());
            e.printStackTrace();
            try {
                conn.rollback();
            }catch (SQLException e2){
                System.out.println("Unable to rollback " + e2.getMessage());
            }
        }finally {
            try{
                conn.setAutoCommit(true);
            }catch (SQLException e){
                System.out.println("Issue with setting conn to true " + e.getMessage());
                e.printStackTrace();
            }
        }

    }

    //Supporting function
    private void orderingQuery(StringBuilder sb, int sortingOrder, String table, String column) {
        if (sortingOrder != ORDER_NONE) {
            sb.append(" ORDER BY ");
            sb.append(table).append(".").append(column);
            sb.append(" COLLATE NOCASE ");
            if (sortingOrder == ORDER_DESC) {
                sb.append(" DESC");
            } else {
                sb.append(" ASC");
            }
        }
    }
}
