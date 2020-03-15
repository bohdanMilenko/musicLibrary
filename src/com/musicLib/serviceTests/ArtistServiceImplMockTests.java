package com.musicLib.serviceTests;

import com.musicLib.entities.Artist;
import com.musicLib.exceptions.QueryException;
import com.musicLib.exceptions.ServiceException;
import com.musicLib.repository.AlbumRepository;
import com.musicLib.repository.ArtistRepository;
import com.musicLib.repository.MongoDBRepisotory.AlbumRepositoryMongo;
import com.musicLib.repository.MongoDBRepisotory.ArtistRepositoryMongo;
import com.musicLib.repository.MongoDBRepisotory.SongRepositoryMongo;
import com.musicLib.repository.SongRepository;
import com.musicLib.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ArtistServiceImplMockTests {


    //ArtistServiceImp - SUT System under Test
    //ArtistRepo - is a dependency
    SongService songServiceMongo;
    AlbumService albumService;
    ArtistRepository artistRepositoryStub = new ArtistRepositoryMongoTestStub();
    ArtistService artistServiceMongoStubBased = new ArtistServiceImpl(artistRepositoryStub);


    private ArtistRepository artistRepositoryMock;
    private RecordValidator recordValidator;
    private ArtistService artistServiceMock;
    private Artist artist;

    @BeforeEach
    public void setUp() {
        artistRepositoryMock = mock(ArtistRepository.class);
        recordValidator = mock(RecordValidator.class);
        artistServiceMock = new ArtistServiceImpl(artistRepositoryMock);
        artistServiceMock.setRecordValidator(recordValidator);
        albumService = mock(AlbumService.class);
        artistServiceMock.setAlbumService(albumService);

        artist = new Artist();
        artist.setName("Bob Dylan");
    }

    @Test
    public void testAddValidationPassedObjectAddedToDB() throws ServiceException {
        when(recordValidator.validateArtistAddMethod(artist)).thenReturn(true);
        when(artistServiceMock.add(artist)).thenReturn(true);
        assertTrue(artistServiceMock.add(artist));
    }

    @Test
    public void testAddValidationNotPassed() throws ServiceException {
        when(recordValidator.validateArtistAddMethod(artist)).thenThrow(ServiceException.class);
        assertThrows(ServiceException.class, () -> artistServiceMock.add(artist));
    }

    @Test
    public void testAddValidationPassedObjectNotAddedToDB() throws ServiceException {
        when(recordValidator.validateArtistAddMethod(artist)).thenReturn(true);
        when(artistServiceMock.add(artist)).thenThrow(QueryException.class);
        assertThrows(ServiceException.class, () -> artistServiceMock.add(artist));
    }

    @Test
    public void testAddValidationPassedObjectNotAddedToDBSQL() throws ServiceException {
        when(recordValidator.validateArtistAddMethod(artist)).thenReturn(true);
        when(artistServiceMock.add(artist)).thenThrow(SQLException.class);
        assertThrows(ServiceException.class, () -> artistServiceMock.add(artist));
    }

    @Test
    public void testGetAllMethodUsingStub() throws ServiceException {
        List<Artist> returnedArtists = artistServiceMongoStubBased.getAll();
        assertEquals(2, returnedArtists.size());
    }

    @Test
    public void testGetAllUsingMock() throws ServiceException {
        when(artistServiceMock.getAll()).thenReturn(Arrays.asList(new Artist(), new Artist()));
        List<Artist> returnedArtists = artistServiceMock.getAll();
        assertEquals(2, returnedArtists.size());
        assertEquals(returnedArtists.get(0).getClass(), Artist.class);
    }

    @Test
    public void testGetAllValidationPassedRepoFailedSQL() throws ServiceException {
        when(recordValidator.validateIfNotNull(any())).thenReturn(true);
        when(artistServiceMock.getAll()).thenThrow(SQLException.class);
        assertThrows(ServiceException.class, () -> artistServiceMock.getAll());
    }

    @Test
    public void testGetByNameValidationPassedRepoWorkedAsExpected() throws ServiceException {
        when(recordValidator.validateIfNotNull(any())).thenReturn(true);
        when(artistServiceMock.getByName(artist)).thenReturn(Arrays.asList(new Artist(), new Artist()));
        assertEquals(2, artistServiceMock.getByName(artist).size());
    }

    @Test
    public void testGetByNameValidationNotPassed() throws ServiceException {
        when(recordValidator.validateIfNotNull(any())).thenThrow(ServiceException.class);
        assertThrows(ServiceException.class, () -> artistServiceMock.getByName(artist));
    }

    @Test
    public void testGetByNameValidationPassedRepoFailed() throws ServiceException {
        when(recordValidator.validateIfNotNull(any())).thenReturn(true);
        when(artistServiceMock.getByName(artist)).thenThrow(SQLException.class);
        assertThrows(ServiceException.class, () -> artistServiceMock.getByName(artist));
    }

    @Test
    public void testDeleteValidationPassedRepoWorkedAsExpected() throws ServiceException, SQLException {
        when(recordValidator.validateArtistDeleteMethod(artist)).thenReturn(true);
        when(recordValidator.hasDependantAlbums(artist)).thenReturn(true);
        //Ask how to test if it returns void, should I change the return type here?
        //when(albumService.deleteAlbumsForArtist(artist)).thenThrow(ServiceException.class);
        assertTrue(artistServiceMock.delete(artist));
    }

    @Test
    public void testDeleteValidationPassedRepoWorkedAsExpected2() throws ServiceException {
        when(recordValidator.validateArtistDeleteMethod(any())).thenThrow(ServiceException.class);
        assertThrows(ServiceException.class, () -> artistServiceMock.delete(artist));
    }


    private ArtistService initializeServices() {
        ArtistRepository artistRepositoryMongo = new ArtistRepositoryMongo();
        AlbumRepository albumRepositoryMongo = new AlbumRepositoryMongo();
        SongRepository songRepositoryMongo = new SongRepositoryMongo();

        songServiceMongo = new SongServiceImpl(songRepositoryMongo);
        albumService = new AlbumServiceImpl(albumRepositoryMongo);
        artistServiceMongoStubBased = new ArtistServiceImpl(artistRepositoryMongo);

        RecordValidator recordValidator = new RecordValidator(artistServiceMongoStubBased, albumService, songServiceMongo);

        songServiceMongo.setAlbumService(albumService);
        songServiceMongo.setRecordValidator(recordValidator);

        artistServiceMongoStubBased.setAlbumService(albumService);
        artistServiceMongoStubBased.setRecordValidator(recordValidator);

        albumService.setSongService(songServiceMongo);
        albumService.setArtistService(artistServiceMongoStubBased);
        albumService.setRecordValidator(recordValidator);

        return artistServiceMongoStubBased;
    }


}
