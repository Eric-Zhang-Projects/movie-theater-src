package com.jpmc.theater;

import com.jpmc.theater.model.Movie;
import com.jpmc.theater.model.Showing;
import com.jpmc.theater.provider.LocalDateProvider;
import com.jpmc.theater.util.DateUtil;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

/**
 * Common test data
 */
public class TestDataBase {

    private static final LocalDate todaysDate = LocalDateProvider.singleton().currentDate();
    public static final Movie movie1 = new Movie(1, "Test Movie 1", "Description", Duration.ofMinutes(105), 10);
    public static final Movie movie2 = new Movie(2, "Test Movie 2", "Description", Duration.ofMinutes(105), 20);
    public static final Movie movie3= new Movie(3, "Test Movie 3", "Description", Duration.ofMinutes(105), 30);
    public static final Movie movieNegativePrice= new Movie(3, "Test Movie 3", "Description", Duration.ofMinutes(105), -30);

    public static final Showing showing1 = new Showing(movie1,
            DateUtil.createDateTime(todaysDate, 9, 0), 1, false, 7);
    public static final Showing showing2 = new Showing(movie2,
            DateUtil.createDateTime(todaysDate, 11, 0), 2, true, 15);
    public static final Showing showing3 = new Showing(movie3,
            DateUtil.createDateTime(todaysDate, 13, 0), 3, false, 22.5);
    public static final Showing showing4 = new Showing(movie1,
            DateUtil.createDateTime(todaysDate, 15, 0), 4, false, 7.50);
    public static final Showing showing5 = new Showing(movie2,
            DateUtil.createDateTime(todaysDate, 17, 0), 5, true, 16);
    public static final Showing showing6 = new Showing(movie3,
            DateUtil.createDateTime(todaysDate, 19, 0), 6, false, 30);
    public static final Showing showing7 = new Showing(movie1,
            DateUtil.createDateTime(todaysDate, 21, 0), 7, false, 9);

    public List<Showing> schedule = List.of(showing1, showing2, showing3, showing4, showing5, showing6, showing7);
    public List<Showing> smallSchedule = List.of(
        new Showing(movie1, DateUtil.createDateTime(todaysDate, 15, 0), 1, false, 7),
        new Showing(movie2, DateUtil.createDateTime(todaysDate, 17, 0), 2, true, 16),
        new Showing(movie3, DateUtil.createDateTime(todaysDate, 19, 0), 3, false, 30)
    );
    public List<Movie> movies = List.of(movie1, movie2, movie3);

}
