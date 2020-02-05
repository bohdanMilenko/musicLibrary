package com.musicLib.repository.SQLightRepository;

import com.musicLib.SQLUtil.SessionManagerSQLite;
import com.musicLib.entities.Album;
import com.musicLib.entities.Artist;
import com.musicLib.entities.Song;
import com.musicLib.repository.SongRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.musicLib.repository.SQLightRepository.MetaData.*;

public class SongRepositorySQL implements SongRepository {


    private SessionManagerSQLite SessionManagerSQLite = new SessionManagerSQLite();
    private PreparedStatement queryByAlbumId;
    private PreparedStatement queryBySongName;
    private PreparedStatement insertSong;
    private PreparedStatement deleteQueryByAlbumID;
    private PreparedStatement deleteQueryBySongName;

    @Override
    public boolean add(Song song) throws SQLException {
        String query = buildInsertQuery();
        insertSong = SessionManagerSQLite.getPreparedStatement(query);
        insertSong.setInt(1, song.getTrackNumber());
        insertSong.setString(2, song.getName());
        insertSong.setInt(3, song.getAlbum().getId());
        insertSong.executeUpdate();
        return true;
    }

    /**
     * INSERT INTO songs (track, title, album) VALUES (?,?,?)
     */
    private String buildInsertQuery() {
        QueryBuilder qb = new QueryBuilder();
        qb.insertTo(TABLE_SONGS).insertSpecifyColumns(COLUMN_SONGS_TRACK, COLUMN_SONGS_TITLE, COLUMN_SONGS_ALBUM);
        return qb.toString();
    }

    @Override
    public List<Song> getByName(String songName) throws SQLException {
        List<Song> returnList = new ArrayList<>();
        String query = buildQueryByName();
        queryBySongName = SessionManagerSQLite.getPreparedStatement(query);
        queryBySongName.setString(1, songName);
        ResultSet rs = queryBySongName.executeQuery();
        returnList = resultSetToSong(rs);
        return returnList;
    }

    /**
     * SELECT artists._id , artists.name, albums._id , albums.name, songs._id, songs.title, songs.track
     * FROM artists INNER JOIN albums ON albums.artist = artists._id
     * INNER JOIN songs ON songs.album = albums._id
     * WHERE albums._id = "?"
     */

    private String buildQueryByName() {
        QueryBuilder qb = buildGeneralQuery();
        qb.specifyFirstCondition(TABLE_SONGS, COLUMN_SONGS_TITLE);
        return qb.toString();
    }


    public List<Song> getByAlbumId(int albumId) throws SQLException {
        List<Song> listToReturn;
        //StringBuilder query = QueryBuilder.buildQueryWithCondition(QUERY_BODY, TABLE_ALBUMS, COLUMN_ALBUMS_ID);
        String query = buildQueryByAlbumID();
        System.out.println(query);
        queryByAlbumId = SessionManagerSQLite.getPreparedStatement(query);
        queryByAlbumId.setInt(1, albumId);
        ResultSet rs = queryByAlbumId.executeQuery();
        listToReturn = resultSetToSong(rs);
        return listToReturn;
    }

    /**
     * SELECT artists._id , artists.name, albums._id , albums.name, songs._id, songs.title, songs.track
     * FROM artists INNER JOIN albums ON albums.artist = artists._id
     * INNER JOIN songs ON songs.album = albums._id
     * WHERE albums._id = "?"
     */
    private String buildQueryByAlbumID() {
        QueryBuilder qb = buildGeneralQuery();
        qb.specifyFirstCondition(TABLE_ALBUMS, COLUMN_ALBUMS_ID);
        return qb.toString();
    }

    private QueryBuilder buildGeneralQuery() {
        QueryBuilder qb = new QueryBuilder();
        qb.startQuery(TABLE_ARTISTS, COLUMN_ARTISTS_ID).addSelection(TABLE_ARTISTS, COLUMN_ARTISTS_NAME)
                .addSelection(TABLE_ALBUMS, COLUMN_ALBUMS_ID).addSelection(TABLE_ALBUMS, COLUMN_ALBUMS_NAME)
                .addSelection(TABLE_SONGS, COLUMN_SONGS_ID).addSelection(TABLE_SONGS, COLUMN_SONGS_TITLE)
                .addSelection(TABLE_SONGS, COLUMN_SONGS_TRACK).queryFrom(TABLE_ARTISTS)
                .innerJoinOn(TABLE_ALBUMS, COLUMN_ALBUMS_ARTIST, TABLE_ARTISTS, COLUMN_ARTISTS_ID)
                .innerJoinOn(TABLE_SONGS, COLUMN_SONGS_ALBUM, TABLE_ALBUMS, COLUMN_ALBUMS_ID);
        return qb;
    }

    @Override
    public boolean delete(Song song) throws SQLException {
        String query = buildDeleteBySongName();
        deleteQueryBySongName = SessionManagerSQLite.getPreparedStatement(query);
        deleteQueryBySongName.setString(1, song.getName());
        deleteQueryBySongName.executeUpdate();
        return true;
    }

    /**
     * DELETE FROM songs WHERE songs.title = ?
     */
    private String buildDeleteBySongName() {
        QueryBuilder qb = new QueryBuilder();
        qb.deleteFrom(TABLE_SONGS).specifyFirstCondition(TABLE_SONGS, COLUMN_SONGS_TITLE);

        return qb.toString();
    }

    public boolean deleteByAlbumId(int albumId) throws SQLException {
        String query = buildDeleteByAlbumID();
        deleteQueryByAlbumID = SessionManagerSQLite.getPreparedStatement(query);
        deleteQueryByAlbumID.setInt(1, albumId);
        deleteQueryByAlbumID.executeUpdate();
        return true;
    }

    /**
     * DELETE FROM songs WHERE songs.album = "?"
     */
    private String buildDeleteByAlbumID() {
        QueryBuilder qb = new QueryBuilder();
        qb.deleteFrom(TABLE_SONGS).specifyFirstCondition(TABLE_SONGS, COLUMN_SONGS_ALBUM);
        return qb.toString();
    }

    private List<Song> resultSetToSong(ResultSet rs) throws SQLException {
        List<Song> listToReturn = new ArrayList<>();
        while (rs.next()) {
            Artist tempArtist = new Artist();
            Album tempAlbum = new Album();
            Song tempSong = new Song();
            //artists._id , artists.name, albums._id , albums.name, songs._id, songs.title, songs.track
            tempArtist.setId(rs.getInt(1));
            tempArtist.setName(rs.getString(2));
            tempAlbum.setId(rs.getInt(3));
            tempAlbum.setName(rs.getString(4));
            tempAlbum.setArtist(tempArtist);

            tempSong.setId(rs.getInt(5));
            tempSong.setName(rs.getString(6));
            tempSong.setTrackNumber(rs.getInt(7));

            tempSong.setAlbum(tempAlbum);
            tempSong.setArtist(tempArtist);

            System.out.println("Artist id: " + rs.getInt(1));
            System.out.println("Artist Name: " + rs.getString(2));
            System.out.println("Album ID: " + rs.getInt(3));
            System.out.println("Album Name: " + rs.getString(4));
            System.out.println("Song ID: " + rs.getInt(5));
            System.out.println("Song Name: " + rs.getString(6));
            System.out.println("Track Number: " + rs.getInt(7));
            System.out.println(rs.getString(2));
            listToReturn.add(tempSong);
        }
        return listToReturn;
    }


//    /**
//     * SELECT artists._id , artists.name, albums._id , albums.name, songs._id, songs.title, songs.track
//     * FROM artists INNER JOIN albums ON albums.artist = artists._id
//     * INNER JOIN songs ON songs.album = albums._id
//     */
//
//    private static final String QUERY_BODY = "SELECT " + TABLE_ARTISTS + "." + COLUMN_ARTISTS_ID + ", "
//            + TABLE_ARTISTS + "." + COLUMN_ARTISTS_NAME + ", " + TABLE_ALBUMS + "." + COLUMN_ALBUMS_ID + ", " +
//            TABLE_ALBUMS + "." + COLUMN_ALBUMS_NAME + ", " +
//            TABLE_SONGS + "." + COLUMN_SONGS_ID + ", " + TABLE_SONGS + "." + COLUMN_SONGS_TITLE + ", " +
//            TABLE_SONGS + "." + COLUMN_SONGS_TRACK + " FROM " + TABLE_ARTISTS +
//            " INNER JOIN " + TABLE_ALBUMS + " ON " + TABLE_ARTISTS + "." + COLUMN_ARTISTS_ID + " = " +
//            TABLE_ALBUMS + "." + COLUMN_ALBUMS_ARTIST +
//            " INNER JOIN " + TABLE_SONGS + " ON " + TABLE_SONGS + "." + COLUMN_SONGS_ALBUM + " = " + TABLE_ALBUMS + "." + COLUMN_ARTISTS_ID;
//
//    private static final String INSERT_SONG = " INSERT INTO " + TABLE_SONGS +
//            " (" + COLUMN_SONGS_TRACK + ", " + COLUMN_SONGS_TITLE + ", " + COLUMN_SONGS_ALBUM + ") VALUES(?,?,?)";
//
//    private static final String QUERY_BY_SONG_NAME = "SELECT " + TABLE_SONGS + "." + COLUMN_SONGS_TITLE + ", "
//            + MetaData.TABLE_ARTISTS + "." + COLUMN_ARTISTS_NAME + ", "
//            + TABLE_ALBUMS + "." + COLUMN_ALBUMS_NAME +
//            " FROM " + TABLE_SONGS + " INNER JOIN " + TABLE_ALBUMS + " ON " + TABLE_SONGS + "." + COLUMN_SONGS_ALBUM + " = " + TABLE_ALBUMS + "." + COLUMN_ARTISTS_ID
//            + " INNER JOIN " + TABLE_ARTISTS + " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUMS_ARTIST + " = " + TABLE_ARTISTS + "." + COLUMN_ALBUMS_ID
//            + " WHERE " + TABLE_ARTISTS + "." + COLUMN_ARTISTS_NAME + " = \"";
//
//    private static final String TABLE_ARTIST_SONG_VIEW = "artist_list3";
//
//    private static final String CREATE_ARTIST_FOR_SONG_VIEW = "CREATE VIEW IF NOT EXISTS " +
//            TABLE_ARTIST_SONG_VIEW + " AS SELECT " + TABLE_ARTISTS + "." + COLUMN_ARTISTS_NAME + " AS Artist, " +
//            TABLE_ALBUMS + "." + COLUMN_ALBUMS_NAME + " AS " + COLUMN_SONGS_ALBUM + ", " +
//            TABLE_SONGS + "." + COLUMN_SONGS_TRACK + ", " + TABLE_SONGS + "." + COLUMN_SONGS_TITLE +
//            " FROM " + TABLE_SONGS +
//            " INNER JOIN " + TABLE_ALBUMS + " ON " + TABLE_SONGS +
//            "." + COLUMN_SONGS_ALBUM + " = " + TABLE_ALBUMS + "." + COLUMN_ALBUMS_ID +
//            " INNER JOIN " + TABLE_ARTISTS + " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUMS_ARTIST +
//            " = " + TABLE_ARTISTS + "." + COLUMN_ARTISTS_ID +
//            " ORDER BY " +
//            TABLE_ARTISTS + "." + COLUMN_ARTISTS_NAME + ", " +
//            TABLE_ALBUMS + "." + COLUMN_ALBUMS_NAME + ", " +
//            TABLE_SONGS + "." + COLUMN_SONGS_TRACK;
//
//    private static final String QUERY_VIEW_ARTISTS_LIST = "SELECT " + COLUMN_ARTISTS_NAME + ", " + COLUMN_SONGS_ALBUM + ", " + COLUMN_SONGS_TRACK
//            + " FROM " + TABLE_ARTIST_SONG_VIEW + " WHERE " + COLUMN_SONGS_TITLE + "= \"";
//
//    private static final String QUERY_VIEW_ARTISTS_LIST_PREP = "SELECT " + COLUMN_ARTISTS_NAME + ", " + COLUMN_SONGS_ALBUM + ", " + COLUMN_SONGS_TRACK
//            + " FROM " + TABLE_ARTIST_SONG_VIEW + " WHERE " + COLUMN_SONGS_TITLE + "= ?";
//
//    private static final String QUERY_SONG = " SELECT " + COLUMN_SONGS_ID + " FROM " + TABLE_SONGS
//            + " WHERE " + COLUMN_SONGS_ALBUM + " = ?  AND " + COLUMN_SONGS_TITLE + " = ?";
//

//    static String getQueryArtistsTable(int sorting) {
//        StringBuilder sb = new StringBuilder("SELECT * FROM ");
//        sb.append(TABLE_ARTISTS);
//        orderingQuery(sb, sorting, TABLE_ARTISTS, COLUMN_ALBUMS_NAME);
//        return sb.toString();
//    }
//
//    static String getQueryArtistsBySong(String songName, int sortingOrder) {
//        StringBuilder sb = new StringBuilder(QUERY_BY_SONG_NAME);
//        sb.append(songName).append("\" ");
//        orderingQuery(sb, sortingOrder, TABLE_ARTISTS, COLUMN_ARTISTS_NAME);
//        return sb.toString();
//    }
//
//    //Queries
//
//    public List<String> queryAlbumsForArtists(String artist) {
//        StringBuilder sb = new StringBuilder("SELECT ");
//        sb.append(TABLE_ALBUMS).append(".").append(COLUMN_ALBUMS_NAME).append(" FROM ").append(TABLE_ALBUMS)
//                .append(" INNER JOIN ").append(TABLE_ARTISTS).append(" ON ")
//                .append(TABLE_ALBUMS).append(".").append(COLUMN_ALBUMS_ARTIST).append(" = ")
//                .append(TABLE_ARTISTS).append(".").append(COLUMN_ARTISTS_ID)
//                .append(" WHERE ").append(TABLE_ARTISTS).append(".").append(COLUMN_ARTISTS_NAME).append(" = ")
//                .append("\"").append(artist).append("\"");
//        int sortOrder = orderResultSet();
//        orderingQuery(sb, sortOrder, TABLE_ALBUMS, COLUMN_ALBUMS_ARTIST);
//
//        System.out.println("SQL statement: " + sb.toString());
//        Statement statement = null;
//        ResultSet resultSet = null;
//        try {
//            Connection conn = SessionManagerSQLite.getConnection();
//            statement = conn.createStatement();
//            resultSet = statement.executeQuery(sb.toString());
//            List<String> albums = new ArrayList<>();
//            while ((resultSet.next())) {
//                albums.add(resultSet.getString(1));
//                //  We can use index for getting the column from the ResultSet, as far as we query only one column,
//                // we are good to hardcode it with 1.
//            }
//            System.out.println(artist + "s' albums:");
//            return albums;
//
//        } catch (SQLException e) {
//            System.out.println("Query is not performed " + e.getMessage());
//            e.printStackTrace();
//            return null;
//        } finally {
//            SessionManagerSQLite.closeSession(resultSet, statement);
//        }
//    }
//
//    public List<SongArtist> queryArtistBySong(String songName) {
//        List<SongArtist> songsByArtistReturnList = new ArrayList<>();
//        StringBuilder sb = new StringBuilder(QUERY_BY_SONG_NAME);
//        sb.append(songName).append("\"");
//        int sortingOrder = orderResultSet();
//        orderingQuery(sb, sortingOrder, TABLE_ARTISTS, COLUMN_ARTISTS_NAME);
//
//        System.out.println(" SQL Statement: " + sb.toString());
//        try (Connection conn = SessionManagerSQLite.getConnection();
//             Statement statement = conn.createStatement();
//             ResultSet resultSet = statement.executeQuery(sb.toString())) {
//            while (resultSet.next()) {
//                SongArtist internalInstance = new SongArtist();
//                internalInstance.setTrackName(resultSet.getString(1));
//                internalInstance.setArtistName(resultSet.getString(2));
//                internalInstance.setAlbumName(resultSet.getString(3));
//                songsByArtistReturnList.add(internalInstance);
//            }
//
//            return songsByArtistReturnList;
//
//        } catch (SQLException e) {
//            System.out.println("Issue with Query Artist by Song" + e.getMessage());
//            e.printStackTrace();
//        }
//
//        return songsByArtistReturnList;
//    }
//
//
//    public int getCountMinMaxInSongsTable(String tableName) {
//        String query = "SELECT COUNT(*), MIN(" + COLUMN_SONGS_ID + "), MAX (" + COLUMN_SONGS_ID + ") FROM " + tableName;
//
//        try (Connection conn = SessionManagerSQLite.getConnection();
//             Statement statement = conn.createStatement();
//             ResultSet rs = statement.executeQuery(query)) {
//
//            int count = rs.getInt(1);
//            int min = rs.getInt(2);
//            int max = rs.getInt(3);
//
//            System.out.format("The number of songs: %d , min - %d, max %d\n", count, min, max);
//            return 1;
//        } catch (SQLException e) {
//            System.out.println("Count and Min/Max query problem: " + e.getMessage());
//            e.printStackTrace();
//            return -1;
//        }
//
//    }
//
//    public boolean createArtistsListView() {
//        try (Connection conn = SessionManagerSQLite.getConnection();
//             Statement statement = conn.createStatement()) {
//            statement.execute(CREATE_ARTIST_FOR_SONG_VIEW);
//            return true;
//        } catch (SQLException e) {
//            System.out.println("Count and Min/Max query problem: " + e.getMessage());
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public List<SongArtist> queryBySongTitleView(String songName) {
//        List<SongArtist> listForReturn = new ArrayList<>();
//        try (Connection conn = SessionManagerSQLite.getConnection();
//        ) {
//            queryArtistBySong = SessionManagerSQLite.getPreparedStatement(QUERY_VIEW_ARTISTS_LIST_PREP);
//            queryArtistBySong.setString(1, songName);
//            ResultSet rs = queryArtistBySong.executeQuery();
//            while (rs.next()) {
//                SongArtist internalObject = new SongArtist();
//                internalObject.setArtistName(rs.getString(1));
//                internalObject.setAlbumName(rs.getString(2));
//                listForReturn.add(internalObject);
//            }
//            return listForReturn;
//        } catch (SQLException e) {
//            System.out.println("Query artist by song issue (view): " + e.getMessage());
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//
//    public void insertSong(String title, String artist, String album, int trackNumber){
//        Connection conn = null;
//        int rowsAffected;
//        try{conn = SessionManagerSQLite.getConnection();
//            conn.setAutoCommit(false);
//            insertSong = SessionManagerSQLite.getPreparedStatement(INSERT_SONG);
//            int artistId =  artistsRepository.insertArtist(artist);
//            int albumId =  albumRepository.insertAlbum(album, artistId );
//            querySongIfExists = SessionManagerSQLite.getPreparedStatement(QUERY_SONG);
//            querySongIfExists.setInt(1,albumId);
//            querySongIfExists.setString(2, title);
//            ResultSet rs = querySongIfExists.executeQuery();
//            if(rs.next()) {
//                throw new SQLException("Such song already exists!");
//            } else {
//                insertSong.setInt(1, trackNumber);
//                insertSong.setString(2, title);
//                insertSong.setInt(3, albumId);
//                rowsAffected = insertSong.executeUpdate();
//            }
//            if(rowsAffected == 1){
//                conn.commit();
//                System.out.println("Successfully inserted a song!");
//            } else {
//                throw new SQLException(("Issue creating songs' entity"));
//            }
//        }catch (Exception e){
//            System.out.println("Performing rollback " + e.getMessage());
//            e.printStackTrace();
//            try {
//                if(conn != null){
//                    conn.rollback();
//                    System.out.println("Rolling back");
//                }
//            }catch (SQLException e2){
//                System.out.println("Unable to rollback " + e2.getMessage());
//            }
//        }finally {
//            try{
//                if(conn != null) {
//                    conn.setAutoCommit(true);
//                }
//            }catch (SQLException e){
//                System.out.println("Issue with setting conn to true " + e.getMessage());
//                e.printStackTrace();
//
//            }
//        }
//
//    }
}
