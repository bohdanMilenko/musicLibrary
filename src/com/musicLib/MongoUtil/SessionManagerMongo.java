package com.musicLib.MongoUtil;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class SessionManagerMongo {

    private static final MongoClient mongoClient = new MongoClient();
    private static final String databaseName = loadMongoProperties();


    public static MongoDatabase getDbFromPropertyFile(){
        System.out.println(databaseName);
        return mongoClient.getDatabase(databaseName);
    }

    private static String loadMongoProperties() {
        String returnString = "";
        try (FileInputStream inputStream = new FileInputStream("mongoProperties.txt")) {
            Properties p = new Properties();
            p.load(inputStream);
            returnString = p.getProperty("dbName");
        } catch (IOException e) {
            System.out.println(e.getMessage()+ ": Cannot load Mongo Properties");
            e.printStackTrace();
        }
        return returnString;
    }
}
