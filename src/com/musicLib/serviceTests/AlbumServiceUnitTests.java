package com.musicLib.serviceTests;

import com.musicLib.entities.Album;
import com.musicLib.entities.Artist;
import com.musicLib.exceptions.ServiceException;
import com.musicLib.repository.AlbumRepository;
import com.musicLib.repository.ArtistRepository;
import com.musicLib.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AlbumServiceUnitTests {

    private AlbumRepository albumRepoMock;
    private RecordValidator recordValidatorMock;
    private SongService songServiceMock;
    private ArtistService artistServiceMock;
    private AlbumService classToTest;
    private Artist artist;
    private Album album;


    @BeforeEach
    public void setUp() {
        albumRepoMock = mock(AlbumRepository.class);
        classToTest = new AlbumServiceImpl(albumRepoMock);
        recordValidatorMock = mock(RecordValidator.class);
        classToTest.setRecordValidator(recordValidatorMock);
        songServiceMock = mock(SongService.class);
        classToTest.setSongService(songServiceMock);
        artistServiceMock = mock(ArtistService.class);
        classToTest.setArtistService(artistServiceMock);
        album = mock(Album.class);
//        artist = new Artist();
//        artist.setName("Bob Dylan");
    }


    //Index out of bounds
    @Test
    public void testAddMethodWorkedAsExpected() throws ServiceException, SQLException {
        when(recordValidatorMock.validateAlbumAddMethod(album)).thenReturn(true);
        when(albumRepoMock.add(album)).thenReturn(true);
        assertTrue(classToTest.add(album));
    }
}
