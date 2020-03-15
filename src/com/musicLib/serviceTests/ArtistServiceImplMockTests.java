package com.musicLib.serviceTests;

import com.musicLib.entities.Artist;
import com.musicLib.exceptions.ServiceException;
import com.musicLib.repository.AlbumRepository;
import com.musicLib.repository.ArtistRepository;
import com.musicLib.repository.MongoDBRepisotory.AlbumRepositoryMongo;
import com.musicLib.repository.MongoDBRepisotory.ArtistRepositoryMongo;
import com.musicLib.repository.MongoDBRepisotory.SongRepositoryMongo;
import com.musicLib.repository.SongRepository;
import com.musicLib.services.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ArtistServiceImplMockTests {


    //ArtistServiceImp - SUT System under Test
    //ArtistRepo - is a dependency
    SongService songServiceMongo;
    AlbumService albumServiceMongo;
    ArtistRepository artistRepositoryStub = new ArtistRepositoryMongoTestStub();
    ArtistService artistServiceMongoStubBased = new ArtistServiceImpl(artistRepositoryStub);


    private ArtistRepository artistRepositoryMock;

    @Test
    public void testGetAllMethodUsingStub() throws ServiceException {
        List<Artist> returnedArtists = artistServiceMongoStubBased.getAll();
        assertEquals(2, returnedArtists.size());
    }

    @Test
    public void testGetAllUsingMock() throws ServiceException {
        artistRepositoryMock = mock(ArtistRepository.class);
        ArtistService artistServiceMongoMock = new ArtistServiceImpl(artistRepositoryMock);
        when(artistServiceMongoMock.getAll()).thenReturn(Arrays.asList(new Artist(), new Artist()));
        List<Artist> returnedArtists = artistServiceMongoMock.getAll();
        assertEquals(2, returnedArtists.size());
    }

    @Test
    public void testGetByNameUsingMocks() throws ServiceException {
        artistRepositoryMock = mock(ArtistRepository.class);
        RecordValidator recordValidator = mock(RecordValidator.class);
        ArtistService artistServiceMongoMock = new ArtistServiceImpl(artistRepositoryMock);
        artistServiceMongoMock.setRecordValidator(recordValidator);

        Artist artist = new Artist();
        artist.setName("Bob");

        when(recordValidator.validateIfNotNull(any())).thenReturn(true);
        when(artistServiceMongoMock.getByName(artist)).thenReturn(Arrays.asList(new Artist(), new Artist()));

        List<Artist> returnedArtists = artistServiceMongoMock.getByName(artist);
        assertEquals(2, returnedArtists.size());
    }

    @Test
    public void testGetByNameUsingMocks2() throws ServiceException {
        artistRepositoryMock = mock(ArtistRepository.class);
        RecordValidator recordValidator = mock(RecordValidator.class);
        ArtistService artistServiceMongoMock = new ArtistServiceImpl(artistRepositoryMock);
        artistServiceMongoMock.setRecordValidator(recordValidator);

        Artist artist = new Artist();
        artist.setName("Bob");

        when(recordValidator.validateIfNotNull(any())).thenThrow(new ServiceException());
        when(artistServiceMongoMock.getByName(artist)).thenReturn(Arrays.asList(new Artist(), new Artist()));

        assertThrows(ServiceException.class,  () -> artistServiceMongoMock.getByName(artist));
    }


    private ArtistService initializeServices() {
        ArtistRepository artistRepositoryMongo = new ArtistRepositoryMongo();
        AlbumRepository albumRepositoryMongo = new AlbumRepositoryMongo();
        SongRepository songRepositoryMongo = new SongRepositoryMongo();

        songServiceMongo = new SongServiceImpl(songRepositoryMongo);
        albumServiceMongo = new AlbumServiceImpl(albumRepositoryMongo);
        artistServiceMongoStubBased = new ArtistServiceImpl(artistRepositoryMongo);

        RecordValidator recordValidator = new RecordValidator(artistServiceMongoStubBased, albumServiceMongo, songServiceMongo);

        songServiceMongo.setAlbumService(albumServiceMongo);
        songServiceMongo.setRecordValidator(recordValidator);

        artistServiceMongoStubBased.setAlbumService(albumServiceMongo);
        artistServiceMongoStubBased.setRecordValidator(recordValidator);

        albumServiceMongo.setSongService(songServiceMongo);
        albumServiceMongo.setArtistService(artistServiceMongoStubBased);
        albumServiceMongo.setRecordValidator(recordValidator);

        return artistServiceMongoStubBased;
    }


}
