package com.musicLib.repository.SQLUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesLoader {

     static Map<String, String> getProperties() {
         Map<String, String> stringStringHashMap = new HashMap<>();
        try (FileInputStream readingProperties = new FileInputStream("connectionSQL.property")){
            Properties p = new Properties();
            if(readingProperties!=null) {
                p.load(readingProperties);
                p.forEach(( key,value)-> stringStringHashMap.put((String) key, (String) value));
            }else {
                System.out.println("Cannot load .properties file!");
            }
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
