package com.musicLib.databaseModel;

import org.sqlite.core.DB;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class PropertiesLoader {

    public  Map<String, String> getProperties() {
        try {
            FileInputStream readingProperties = new FileInputStream("connection.property");
            Properties p = new Properties();
            p.load(readingProperties);
            String DB_NAME =  p.getProperty("DB_NAME");
            String CONNECTION_STRING = p.getProperty("CONNECTION_STRING");

            Map<String, String> propertiesList = new HashMap<>();
            propertiesList.put("DB_NAME", DB_NAME);
            propertiesList.put("CONNECTION_STRING", CONNECTION_STRING);

            System.out.println(propertiesList);
            return propertiesList;
        }catch (IOException e){
            System.out.println("Problem retrieving properties for Connection: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

    }
}
