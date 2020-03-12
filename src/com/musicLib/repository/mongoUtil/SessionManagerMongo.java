package com.musicLib.repository.mongoUtil;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.musicLib.exceptions.DbConnectionException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SessionManagerMongo {

    private static final String databaseNameProperty = "dbName";
    private static final String dockerIpProperty = "ip";
    private static final String mongoPortExternalProperty = "externalPort";
    private static final String mongoUserNameProperty = "MONGO_INITDB_ROOT_USERNAME";
    private static final String mongoPasswordProperty = "MONGO_INITDB_ROOT_PASSWORD";

    private static Map<String, String> properties;

    static {
        try {
            properties = loadMongoProperties();
        } catch (DbConnectionException e) {
            e.printStackTrace();
        }
    }
    private static Map<String, String> loadMongoProperties() throws DbConnectionException {
        Map<String, String> returnMap = new HashMap<>();
        try (FileInputStream inputStream = new FileInputStream("connectionMongo.property")) {
            Properties p = new Properties();
            p.load(inputStream);
            returnMap.put(databaseNameProperty, p.getProperty(databaseNameProperty));
            returnMap.put(dockerIpProperty, p.getProperty(dockerIpProperty));
            returnMap.put(mongoPortExternalProperty, p.getProperty(mongoPortExternalProperty));
            returnMap.put(mongoUserNameProperty, p.getProperty(mongoUserNameProperty));
            returnMap.put(mongoPasswordProperty, p.getProperty(mongoPasswordProperty));
            return returnMap;
        } catch (IOException e) {
            System.out.println(e.getMessage() + ": Cannot load Mongo Properties");
            e.printStackTrace();
            throw new DbConnectionException("Unable to connect to MongoDB", e);
        }
    }

    private static final String databaseName = properties.get(databaseNameProperty);
    private static final String ip = properties.get(dockerIpProperty);
    private static final String externalPort = properties.get(mongoPortExternalProperty);
    private static final String mongoUser = properties.get(mongoUserNameProperty);
    private static final String mongoPassword = properties.get(mongoPasswordProperty);


    private static final MongoClient mongoClient = new MongoClient();

    public static MongoClient getMongoClient() {
        return mongoClient;
    }

    public static MongoDatabase getDbFromPropertyFile() {
        return mongoClient.getDatabase(databaseName);
    }

//    private static String loadMongoProperties() {
//        String returnString = "";
//        try (FileInputStream inputStream = new FileInputStream("connectionMongo.property")) {
//            Properties p = new Properties();
//            p.load(inputStream);
//            returnString = p.getProperty("dbName");
//
//        } catch (IOException e) {
//            System.out.println(e.getMessage()+ ": Cannot load Mongo Properties");
//            e.printStackTrace();
//        }
//        return returnString;
//    }
}
