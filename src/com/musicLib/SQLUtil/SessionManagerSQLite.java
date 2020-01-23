package com.musicLib.SQLUtil;

import java.sql.*;
import java.util.Map;

public class SessionManagerSQLite {

    private Map<String,String> connectionProperties;
    private String DB_NAME;
    private String CONNECTION_STRING;
    private static Connection conn;


    public Connection getConnection(){
        return initSession();
    }

    private Connection initSession(){
        if(assignDBProperties()) {
            return initConnection();
            //initPrepStatements();
        }else {
            throw new RuntimeException("Cannot get properties");
        }
    }


    private boolean assignDBProperties(){
        connectionProperties = PropertiesLoader.getProperties();
        if( connectionProperties != null && !connectionProperties.isEmpty()){
            DB_NAME = connectionProperties.get("DB_NAME");
            CONNECTION_STRING = connectionProperties.get("CONNECTION_STRING") + DB_NAME;
            return true;
        }else {
            System.out.println("Cannot retrieve Properties");
            return false;
        }
    }

    private Connection initConnection() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            System.out.println("Connection established");
            return conn;
        } catch (SQLException e) {
            System.out.println("Cannot open a DB " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

//    private void initPrepStatements() {
//        try {
//            insertArtist = conn.prepareStatement(sqlStatements.getINSERT_ARTIST(), Statement.RETURN_GENERATED_KEYS);
//            insertAlbum = conn.prepareStatement(sqlStatements.getINSERT_ALBUM(), Statement.RETURN_GENERATED_KEYS);
//            insertSong = conn.prepareStatement(sqlStatements.getINSERT_SONG());
//            queryArtistBySong = conn.prepareStatement(sqlStatements.getQUERY_VIEW_ARTISTS_LIST_PREP());
//            queryArtists = conn.prepareStatement(sqlStatements.getQUERY_ARTISTS());
//            queryAlbums = conn.prepareStatement(sqlStatements.getQUERY_ALBUMS());
//        } catch (SQLException e) {
//            System.out.println("Issue with Preparing Statements :" + e.getMessage());
//            e.printStackTrace();
//        }
//    }

    public static void closeSession(ResultSet resultSet, Statement statement) {
        //SongsRepository.closingAllPrepStatements();
        closeResultSet(resultSet);
        closeStatement(statement);
        closeConnection();
        System.out.println("CLOSING CONNECTOPN");
    }

    private static void closeConnection(){
        try {
            if (conn != null) {
                conn.close();
                System.out.println("Session ended successfully");
            }
        } catch (SQLException e) {
            System.out.println("Issue with closing a DB");
            e.printStackTrace();
        }
    }


    // Closing ResultSet and Statement
    private static void closeResultSet(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            System.out.println("ResultSet is not closed" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void closeStatement(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            System.out.println("Statement is not closed " + e.getMessage());
            e.printStackTrace();
        }
    }

    public PreparedStatement getPreparedStatement(String query){
        PreparedStatement preparedStatement;
        try {
            getConnection();
            preparedStatement = conn.prepareStatement(query);
            if(preparedStatement!=null) {
                return preparedStatement;
            }else {
                throw new RuntimeException("Prepared statement is NULL");
            }
        }catch (SQLException e){
            System.out.println("Issue with preparing statement (ArtistBySong): " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
