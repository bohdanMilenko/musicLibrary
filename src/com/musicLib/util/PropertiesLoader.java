package com.musicLib.util;

import org.sqlite.core.DB;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class PropertiesLoader {

     static Map<String, String> getProperties() {
         Map<String, String> stringStringHashMap = new HashMap<>();
        try {
            FileInputStream readingProperties = new FileInputStream("connection.property");
            Properties p = new Properties();
            p.load(readingProperties);
            String DB_NAME =  p.getProperty("DB_NAME");
            String CONNECTION_STRING = p.getProperty("CONNECTION_STRING");

            stringStringHashMap.put("DB_NAME", DB_NAME);
            stringStringHashMap.put("CONNECTION_STRING", CONNECTION_STRING);

            return stringStringHashMap;
        }catch (IOException e){
            System.out.println("Problem retrieving properties for Connection: " + e.getMessage());
            e.printStackTrace();
            stringStringHashMap.put("DB_NAME", "music.db");
            stringStringHashMap.put("CONNECTION_STRING", "jdbc:sqlite:C:\\Drive D\\Java Root\\Java Directory\\JDBCmusic\\");
            return stringStringHashMap;
        }

    }
}
