package com.musicLib.repository.mongoUtil;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.musicLib.exceptions.DbConnectionException;
import org.bson.Document;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SessionManagerMongo {

    private static final String adminDbNameProperty = "adminDB";
    private static final String databaseNameProperty = "dbName";
    private static final String dockerIpProperty = "ip";
    private static final String mongoPortExternalProperty = "externalPort";
    private static final String mongoUserNameProperty = "MONGO_INITDB_ROOT_USERNAME";
    private static final String mongoPasswordProperty = "MONGO_INITDB_ROOT_PASSWORD";

    private static Map<String, String> properties;
    private static String adminDBName;
    private static String databaseName;
    private static String ip;
    private static int externalPort;
    private static String mongoUser;
    private static String mongoPassword;
    private static String mongoClientURIAdmin;
    private static MongoClient mongoClientAdmin;

    static {
        try {
            properties = loadMongoProperties();
            adminDBName = properties.get(adminDbNameProperty);
            databaseName = properties.get(databaseNameProperty);
            ip = properties.get(dockerIpProperty);
            externalPort = Integer.parseInt(properties.get(mongoPortExternalProperty));
            mongoUser = properties.get(mongoUserNameProperty);
            mongoPassword = properties.get(mongoPasswordProperty);
            mongoClientURIAdmin = "mongodb://" + mongoUser + ":" + mongoPassword + "@" + ip + ":" + externalPort + "/" + adminDBName;
            MongoCredential credential = MongoCredential.createCredential(mongoUser,adminDBName,mongoPassword.toCharArray());
            //mongoClientURIAdmin = "mongodb://" + mongoUser + ":" + mongoPassword + "@" + "host.docker.internal" + ":" + 27018 + "/" + adminDBName;
            System.out.println("CURRENT MONGOURI: " + mongoClientURIAdmin);
            //mongoClientAdmin = new MongoClient( new ServerAddress(ip,externalPort), Arrays.asList(credential));
            mongoClientAdmin = new MongoClient(mongoClientURIAdmin);
            MongoDatabase db = mongoClientAdmin.getDatabase(databaseName);
        } catch (DbConnectionException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, String> loadMongoProperties() throws DbConnectionException {
        Map<String, String> returnMap = new HashMap<>();
        try (FileInputStream inputStream = new FileInputStream("/usr/local/bin/connectionMongo.property")) {
            Properties p = new Properties();
            p.load(inputStream);
            returnMap.put(databaseNameProperty, p.getProperty(databaseNameProperty));
            returnMap.put(dockerIpProperty, p.getProperty(dockerIpProperty));
            returnMap.put(mongoPortExternalProperty, p.getProperty(mongoPortExternalProperty));
            returnMap.put(mongoUserNameProperty, p.getProperty(mongoUserNameProperty));
            returnMap.put(mongoPasswordProperty, p.getProperty(mongoPasswordProperty));
            returnMap.put(adminDbNameProperty, p.getProperty(adminDbNameProperty));
            returnMap.forEach((k, v) -> System.out.println(k + ": " + v));
            return returnMap;
        } catch (IOException e) {
            System.out.println(e.getMessage() + ": Cannot load Mongo Properties");
            e.printStackTrace();
            throw new DbConnectionException("Unable to connect to MongoDB", e);
        }
    }

    public static MongoClient getMongoClient() {
        return mongoClientAdmin;
    }

    public static MongoDatabase getDbFromPropertyFile() {
        return mongoClientAdmin.getDatabase(databaseName);
    }

}
