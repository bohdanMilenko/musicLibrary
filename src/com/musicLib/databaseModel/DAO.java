package com.musicLib.databaseModel;

import java.util.List;

public interface DAO<T> {

    void addEntity(T t);
    T getEntity(int id);
    void editEntity(T t, String newValue);
    void deleteEntity(T t);
    List<T> getAllEntities();
}
