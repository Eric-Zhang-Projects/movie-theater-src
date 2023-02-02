package com.jpmc.theater.mockDataAccess;

import com.jpmc.theater.model.Movie;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;

/**
 * This class represents a mock database with hard coded data instead
 */
public class MockDatabase {

    /**
     * Returns a list of movies as if we just queried some Movie table in a database and assigned each row to a Movie object
     * Mental model for how this data is retrieved:
     * We have a movies table which stores all movies with columns:
     * id, movie_id, title, description, duration
     * From which we perform the following query:
     * SELECT * FROM movies
     */
    public static Collection<Movie> getMoviesList(){
        return Arrays.asList(
                new Movie(1, "Spider-Man: No Way Home", "s", Duration.ofMinutes(90), 12.5),
                new Movie(2, "Turning Red", "red", Duration.ofMinutes(85), 11.0),
                new Movie(3, "The Batman", "bat", Duration.ofMinutes(95), 9.0)
        );
    }
}
