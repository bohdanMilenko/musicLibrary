package com.musicLib.services;

import com.musicLib.entities.Album;
import com.musicLib.repository.AlbumRepository;
import com.musicLib.repositoryExceptions.ArtistNotFoundException;
import com.musicLib.repositoryExceptions.DuplicatedRecordException;

import java.sql.SQLException;
import java.util.List;

public class AlbumService {

    public AlbumService() {
    }

    public boolean add(AlbumRepository albumRepo, String artistName, Album album) {
        try{
            return albumRepo.insert(album, artistName);
        }catch (ArtistNotFoundException e ){
            System.out.println("There is no such artist to add an album: " + e.getMessage());
            e.printStackTrace();
        }catch (DuplicatedRecordException e2){
            System.out.println("Such album already exists: " + e2.getMessage());
            e2.printStackTrace();
        }catch (SQLException e3){
            System.out.println("Cannot perform query (Add Album): " + e3.getMessage());
            e3.printStackTrace();
        }
        return false;
    }

    public List<Album> queryByArtist(AlbumRepository albumRepo, String artistName) {
        try{
            return albumRepo.queryByArtistName(artistName);
        }catch (SQLException e) {
            System.out.println("Cannot perform query (Query Albums By Artist): " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<Album> queryByAlbumName(AlbumRepository albumRepo, String albumName) {
        try{
            return albumRepo.queryByAlbumName(albumName);
        }catch (SQLException e) {
            System.out.println("Cannot perform query (Query Albums By Artist): " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean delete(AlbumRepository albumRepo, String artistName, String albumName) {
        try {
            return albumRepo.delete(albumName, artistName);
        }catch (ArtistNotFoundException e ){
            System.out.println("There is no such artist to add an album: " + e.getMessage());
            e.printStackTrace();
        }catch (SQLException e3){
            System.out.println("Cannot perform query (Add Album): " + e3.getMessage());
            e3.printStackTrace();
        }
        return false;
    }
}
