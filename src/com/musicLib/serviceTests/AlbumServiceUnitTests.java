package com.musicLib.serviceTests;

import com.musicLib.entities.Album;
import com.musicLib.entities.Artist;
import com.musicLib.exceptions.ServiceException;
import com.musicLib.repository.AlbumRepository;
import com.musicLib.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        artist = mock(Artist.class);
        album.setArtist(artist);
    }


    //TESTING add(Album album)
    @Test
    public void testAddMethodWorkedAsExpected() throws ServiceException, SQLException {
        //Defining expected results for AlbumService: updateAlbumWithArtistID(album)
        when(artistServiceMock.getByName(any())).thenReturn(Arrays.asList(artist, artist));
        when(artist.getId()).thenReturn(anyInt());
        when(album.getArtist()).thenReturn(artist);
        //Service method results defining
        when(recordValidatorMock.validateAlbumAddMethod(album)).thenReturn(true);
        when(albumRepoMock.add(album)).thenReturn(true);
        //Tests
        assertTrue(classToTest.add(album));
    }


    //Question: Should recordValidator throw ValidationException and then Service handle it?
    @Test
    public void testAddValidationFailed() throws ServiceException, SQLException {
        //Defining expected results for AlbumService: updateAlbumWithArtistID(album)
        when(artistServiceMock.getByName(any())).thenReturn(Arrays.asList(artist, artist));
        when(artist.getId()).thenReturn(anyInt());
        when(album.getArtist()).thenReturn(artist);
        //Service method results defining
        when(recordValidatorMock.validateAlbumAddMethod(album)).thenThrow(ServiceException.class);
        when(albumRepoMock.add(album)).thenReturn(true);
        //Tests
        ServiceException e = assertThrows(ServiceException.class, () -> classToTest.add(album));
    }

    @Test
    public void testAddValidationPassedRepoFailed() throws ServiceException, SQLException {
        //Defining expected results for AlbumService: updateAlbumWithArtistID(album)
        when(artistServiceMock.getByName(any())).thenReturn(Arrays.asList(artist, artist));
        when(artist.getId()).thenReturn(anyInt());
        when(album.getArtist()).thenReturn(artist);
        //Service method results defining
        when(recordValidatorMock.validateAlbumAddMethod(album)).thenReturn(true);
        when(albumRepoMock.add(album)).thenThrow(SQLException.class);
        //Tests
        ServiceException e = assertThrows(ServiceException.class, () -> classToTest.add(album));
        assertEquals("Issue with adding album to db", e.getMessage());
    }

    @Test
    public void testAddUpdateAlbumFailed() throws ServiceException, SQLException {
        //Defining expected results for AlbumService: updateAlbumWithArtistID(album)
        when(artistServiceMock.getByName(any())).thenReturn(new ArrayList<>());
        //Service method results defining
        when(recordValidatorMock.validateAlbumAddMethod(album)).thenReturn(true);
        when(albumRepoMock.add(album)).thenThrow(SQLException.class);
        //Tests
        ServiceException e = assertThrows(ServiceException.class, () -> classToTest.add(album));
        assertEquals("Unable to update Album with Artist id", e.getMessage());
    }


    //TESTING get(Album album)
    @Test
    public void testGetWorkedAsExpected() throws ServiceException, SQLException {
        when(recordValidatorMock.validateRecordForNulls(album)).thenReturn(true);
        when(albumRepoMock.getByName(any())).thenReturn(Arrays.asList(new Album(), new Album()));
        assertEquals(2, classToTest.get(album).size());
        assertEquals(Album.class, classToTest.get(album).get(0).getClass());
    }

    //Create ValidationException and then catch it and wrap it as ServiceException?
    @Test
    public void testGetValidationFailed() throws ServiceException {
        when(recordValidatorMock.validateRecordForNulls(any(Album.class))).thenThrow(ServiceException.class);
        ServiceException e = assertThrows(ServiceException.class, () -> classToTest.get(album));
        //assertEquals("Validation for nulls failed", e.getMessage());
        assertEquals(null, e.getMessage());
    }

    @Test
    public void testGetValidationPassedRepoFailed() throws ServiceException, SQLException {
        when(recordValidatorMock.validateRecordForNulls(any(Album.class))).thenReturn(true);
        when(albumRepoMock.getByName(any())).thenThrow(SQLException.class);
        ServiceException e = assertThrows(ServiceException.class, () -> classToTest.get(album));
        assertEquals("Issue with db connectivity", e.getMessage());
    }


    //TESTING getByArtist(Artists artist)
    @Test
    void testGetByArtistWorkedAsExpected() throws ServiceException, SQLException {
        //Setting up the behavior
        when(artist.getId()).thenReturn(anyInt());
        when(recordValidatorMock.validateGetAlbumByArtist(artist)).thenReturn(true);
        when(artistServiceMock.updateArtistID(artist)).thenReturn(artist);
        when(albumRepoMock.getAlbumsByArtistID(anyInt())).thenReturn(Arrays.asList(new Album(), new Album()));
        //Actual Test
        List<Album> returnedAlbums = classToTest.getByArtist(artist);
        assertEquals(2, returnedAlbums.size());
        assertEquals(Album.class, returnedAlbums.get(0).getClass());
    }

    @Test
    void testGetByArtistValidationFailed() throws ServiceException, SQLException {
        //Setting up the behavior
        when(recordValidatorMock.validateGetAlbumByArtist(any(Artist.class))).thenThrow(new ServiceException());
        //Actual Test
        ServiceException e = assertThrows(ServiceException.class, () -> classToTest.getByArtist(artist));
        assertNull(e.getMessage());
    }

    @Test
    void testGetByArtistValidationPassedUpdateFailed() throws ServiceException, SQLException {
        //Setting up the behavior
        when(recordValidatorMock.validateGetAlbumByArtist(artist)).thenReturn(true);
        when(artistServiceMock.updateArtistID(any(Artist.class))).thenThrow(ServiceException.class);
        //Actual Test
        ServiceException e = assertThrows(ServiceException.class, () -> classToTest.getByArtist(artist));
        assertNull(e.getMessage());
    }

    @Test
    void testGetByArtistRepoFailed() throws ServiceException, SQLException {
        //Setting up the behavior
        when(artist.getId()).thenReturn(anyInt());
        when(recordValidatorMock.validateGetAlbumByArtist(artist)).thenReturn(true);
        when(artistServiceMock.updateArtistID(artist)).thenReturn(artist);
        when(albumRepoMock.getAlbumsByArtistID(anyInt())).thenThrow(SQLException.class);
        //Actual Test
        ServiceException e = assertThrows(ServiceException.class, () -> classToTest.getByArtist(artist));
        assertEquals("Unable to get albums by artist:\n\t" + artist.toString() ,e.getMessage());
    }

    //TESTING Delete Method
    @Test
    void testDelete() throws ServiceException, SQLException {

    }
}
