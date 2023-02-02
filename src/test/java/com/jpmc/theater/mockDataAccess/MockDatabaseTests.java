package com.jpmc.theater.mockDataAccess;

import com.jpmc.theater.TestDataBase;
import com.jpmc.theater.model.Movie;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MockDatabaseTests extends TestDataBase {

    //Simple test mocking getMoviesList call
    @Test
    public void dbGetAllMovies_test(){
        try (MockedStatic<MockDatabase> mockDatabase = Mockito.mockStatic(MockDatabase.class)) {
            mockDatabase.when(MockDatabase::getMoviesList).thenReturn(movies);
            Collection<Movie> actual = MockDatabase.getMoviesList();
            List<Movie> expected = movies;
            assertEquals(actual, expected);
        }
    }
}
