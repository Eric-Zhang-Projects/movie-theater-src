package com.jpmc.theater.service;

import com.jpmc.theater.TestDataBase;
import com.jpmc.theater.mockDataAccess.MockCache;
import com.jpmc.theater.mockDataAccess.MockDatabase;
import com.jpmc.theater.model.Movie;
import com.jpmc.theater.model.Showing;
import com.jpmc.theater.provider.TheaterProvider;
import com.jpmc.theater.util.DateUtil;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TheaterScheduleTestsData extends TestDataBase {

    /**
     * Tests
     * Schedule is already in cache, return schedule
     * Create schedule, movies list is empty, throws caught exception, return empty list
     * Create schedule, theater opening + closing times are invalid, throws caught exception, return empty list
     * Create schedule, test theater opening + closing times that allow 0 movies to fit, throws caught exception, return empty list
     * Create schedule, default movie price is negative, throws caught exception, return empty list
     * Create schedule, only 1 movie in schedule
     * Create schedule successful
     * Create schedule successful - testing different operational hours
     */

    private final TheaterProvider theaterProvider = TheaterProvider.singleton();

    //Schedule is already in cache, return schedule
    @Test
    public void scheduleAlreadyInCache_test(){
        try (MockedStatic<MockCache> mockCache = Mockito.mockStatic(MockCache.class)) {
            //Cache hit, returns non-null schedule. Return schedule
            mockCache.when(() -> MockCache.get(any())).thenReturn(schedule);

            List<Showing> actual = MockCache.get(theaterProvider.getTodaysDate());
            List<Showing> expected = TheaterScheduleService.getTodaysSchedule();
            assertEquals(expected, actual);

            verifySchedule(theaterProvider, schedule, movies);
        }
    }

    //Create schedule, movies list is empty, throws caught exception, return empty list
    @Test
    public void createSchedule_moviesEmpty_test(){
        try (MockedStatic<MockCache> mockCache = Mockito.mockStatic(MockCache.class)) {
            //Cache miss, need to calculate schedule
            mockCache.when(() -> MockCache.get(any())).thenReturn(null);
            try (MockedStatic<MockDatabase> mockDatabase = Mockito.mockStatic(MockDatabase.class)){
                //MockDatabase returns empty movies list, throw caught exception and return empty list
                mockDatabase.when(MockDatabase::getMoviesList).thenReturn(new ArrayList<>());

                List<Showing> actual = new ArrayList<>();
                List<Showing> expected = TheaterScheduleService.getTodaysSchedule();
                assertEquals(expected, actual);

                verifySchedule(theaterProvider, schedule, movies);
            }
        }
    }

    //Create schedule, theater opening + closing times are invalid, throws caught exception, return empty list
    @Test
    public void invalidOperatingHours_Test(){
        try (MockedStatic<MockCache> mockCache = Mockito.mockStatic(MockCache.class)) {
            //Cache miss, need to calculate schedule
            mockCache.when(() -> MockCache.get(any())).thenReturn(null);
            try (MockedStatic<MockDatabase> mockDatabase = Mockito.mockStatic(MockDatabase.class)){
                mockDatabase.when(MockDatabase::getMoviesList).thenReturn(movies);
                try (MockedStatic<TheaterProvider> theaterProviderMockedStatic = Mockito.mockStatic(TheaterProvider.class)) {
                    TheaterProvider theaterProviderMock = mock(TheaterProvider.class);
                    theaterProviderMockedStatic.when(TheaterProvider::singleton).thenReturn(theaterProviderMock);

                    List<Showing> expected = new ArrayList<>();
                    List<Showing> actual;

                    //Closing time is before opening time, throw caught exception to return empty list

                    when(theaterProviderMock.getOpeningTime()).thenReturn(DateUtil.createDateTime(theaterProvider.getTodaysDate(), 9, 0));
                    when(theaterProviderMock.getClosingTime()).thenReturn(DateUtil.createDateTime(theaterProvider.getTodaysDate(), 3, 0));

                    actual = TheaterScheduleService.getTodaysSchedule();
                    assertEquals(expected, actual);

                    //Closing time == opening time, throw caught exception to return empty list
                    when(theaterProviderMock.getOpeningTime()).thenReturn(DateUtil.createDateTime(theaterProvider.getTodaysDate(), 9, 0));
                    when(theaterProviderMock.getClosingTime()).thenReturn(DateUtil.createDateTime(theaterProvider.getTodaysDate(), 9, 0));

                    actual = TheaterScheduleService.getTodaysSchedule();
                    assertEquals(expected, actual);
                }
            }
        }
    }

    //Create schedule, test theater opening + closing times that allow 0 movies to fit, throws caught exception, return empty list
    @Test
    public void emptyScheduleCreated_Test(){
        try (MockedStatic<MockCache> mockCache = Mockito.mockStatic(MockCache.class)) {
            //Cache miss, need to calculate schedule
            mockCache.when(() -> MockCache.get(any())).thenReturn(null);
            try (MockedStatic<MockDatabase> mockDatabase = Mockito.mockStatic(MockDatabase.class)){
                mockDatabase.when(MockDatabase::getMoviesList).thenReturn(movies);
                try (MockedStatic<TheaterProvider> theaterProviderMockedStatic = Mockito.mockStatic(TheaterProvider.class)) {
                    TheaterProvider theaterProviderMock = mock(TheaterProvider.class);
                    theaterProviderMockedStatic.when(TheaterProvider::singleton).thenReturn(theaterProviderMock);
                    List<Showing> expected = new ArrayList<>();
                    List<Showing> actual;

                    //Closing time is before first movie end time, cannot fit movie in. Throw caught exception to return empty list
                    when(theaterProviderMock.getOpeningTime()).thenReturn(DateUtil.createDateTime(theaterProvider.getTodaysDate(), 9, 0));
                    when(theaterProviderMock.getClosingTime()).thenReturn(DateUtil.createDateTime(theaterProvider.getTodaysDate(), 9, 45));

                    actual = TheaterScheduleService.getTodaysSchedule();
                    assertEquals(expected, actual);

                    //Closing time is at first movie end time, cannot fit movie in. Throw caught exception to return empty list
                    when(theaterProviderMock.getOpeningTime()).thenReturn(DateUtil.createDateTime(theaterProvider.getTodaysDate(), 9, 0));
                    when(theaterProviderMock.getClosingTime()).thenReturn(DateUtil.createDateTime(theaterProvider.getTodaysDate(), 10, 45));

                    actual = TheaterScheduleService.getTodaysSchedule();
                    assertEquals(expected, actual);
                }
            }
        }
    }

    //Create schedule, default movie price is negative, throws caught exception, return empty lis
    @Test
    public void scheduleCreated_negativePrice_Test(){
        try (MockedStatic<MockCache> mockCache = Mockito.mockStatic(MockCache.class)) {
            //Cache miss, need to calculate schedule
            mockCache.when(() -> MockCache.get(any())).thenReturn(null);
            try (MockedStatic<MockDatabase> mockDatabase = Mockito.mockStatic(MockDatabase.class)){
                mockDatabase.when(MockDatabase::getMoviesList).thenReturn(List.of(movie1, movieNegativePrice));

                List<Showing> expected = new ArrayList<>();
                List<Showing> actual = TheaterScheduleService.getTodaysSchedule();
                assertEquals(expected, actual);
            }
        }
    }

    //Create schedule, only 1 movie in schedule
    @Test
    public void scheduleCreated_OneShowing_Test(){
        try (MockedStatic<MockCache> mockCache = Mockito.mockStatic(MockCache.class)) {
            //Cache miss, need to calculate schedule
            mockCache.when(() -> MockCache.get(any())).thenReturn(null);
            try (MockedStatic<MockDatabase> mockDatabase = Mockito.mockStatic(MockDatabase.class)){
                mockDatabase.when(MockDatabase::getMoviesList).thenReturn(movies);
                try (MockedStatic<TheaterProvider> theaterProviderMockedStatic = Mockito.mockStatic(TheaterProvider.class)) {
                    TheaterProvider theaterProviderMock = mock(TheaterProvider.class);
                    theaterProviderMockedStatic.when(TheaterProvider::singleton).thenReturn(theaterProviderMock);

                    List<Showing> actual;
                    List<Showing> expected;

                    //Closing time is after first movie end time, create schedule with only first movie in it
                    when(theaterProviderMock.getOpeningTime()).thenReturn(DateUtil.createDateTime(theaterProvider.getTodaysDate(), 9, 0));
                    when(theaterProviderMock.getClosingTime()).thenReturn(DateUtil.createDateTime(theaterProvider.getTodaysDate(), 10, 46));
                    when(theaterProviderMock.getTodaySpecialMovieId()).thenReturn(2);

                    expected = List.of(showing1);
                    actual = TheaterScheduleService.getTodaysSchedule();
                    assertEquals(expected, actual);

                    verifySchedule(theaterProviderMock, expected, movies);
                }
            }
        }
    }

    //Create schedule successful
    @Test
    public void scheduleCreated_FullDay_Test(){
        try (MockedStatic<MockCache> mockCache = Mockito.mockStatic(MockCache.class)) {
            //Cache miss, need to calculate schedule
            mockCache.when(() -> MockCache.get(any())).thenReturn(null);
            try (MockedStatic<MockDatabase> mockDatabase = Mockito.mockStatic(MockDatabase.class)){
                mockDatabase.when(MockDatabase::getMoviesList).thenReturn(movies);
                List<Showing> expected = schedule;
                List<Showing> actual = TheaterScheduleService.getTodaysSchedule();
                assertEquals(expected, actual);
                verifySchedule(theaterProvider, expected, movies);
            }
        }
    }

    //Create schedule successful - testing different operational hours
    @Test
    public void scheduleCreated_FullDay_DifferentOperationalTimes_Test(){
        try (MockedStatic<MockCache> mockCache = Mockito.mockStatic(MockCache.class)) {
            //Cache miss, need to calculate schedule
            mockCache.when(() -> MockCache.get(any())).thenReturn(null);
            try (MockedStatic<MockDatabase> mockDatabase = Mockito.mockStatic(MockDatabase.class)){
                mockDatabase.when(MockDatabase::getMoviesList).thenReturn(movies);
                try (MockedStatic<TheaterProvider> theaterProviderMockedStatic = Mockito.mockStatic(TheaterProvider.class)) {
                    TheaterProvider theaterProviderMock = mock(TheaterProvider.class);
                    theaterProviderMockedStatic.when(TheaterProvider::singleton).thenReturn(theaterProviderMock);

                    when(theaterProviderMock.getOpeningTime()).thenReturn(DateUtil.createDateTime(theaterProvider.getTodaysDate(), 15, 0));
                    when(theaterProviderMock.getClosingTime()).thenReturn(DateUtil.createDateTime(theaterProvider.getTodaysDate(), 21, 30));
                    when(theaterProviderMock.getTodaySpecialMovieId()).thenReturn(2);

                    List<Showing> expected = smallSchedule;
                    List<Showing> actual = TheaterScheduleService.getTodaysSchedule();
                    assertEquals(expected, actual);
                    verifySchedule(theaterProviderMock, expected, movies);
                }
            }
        }
    }

    /**
     * Verifies the following schedule attributes:
     * Movie ID corresponds to correct showing
     * First show starts at opening time
     * Last show ends before closing time
     */
    private void verifySchedule(TheaterProvider theaterProvider, List<Showing> schedule, List<Movie> movies){
        for (int i = 0; i < schedule.size(); i++){
            Showing showing = schedule.get(i);
            Movie movie = showing.getMovie();
            //Check IDs are in order, ex. for 3 movies, schedule is created as Movie 1 -> Movie 2 -> Movie 3 -> Movie 1 etc...
            assertEquals(movie.getId(), (i % movies.size()) + 1);
        }

        //First show starts at opening time
        Showing firstShowing = schedule.get(0);
        assertEquals(firstShowing.getShowStartTime(), theaterProvider.getOpeningTime());

        //Last show ends before closing time
        Showing lastShowing = schedule.get(schedule.size() - 1);
        assertTrue(lastShowing.getShowStartTime().plus(lastShowing.getMovie().getRunningTime())
                .isBefore(theaterProvider.getClosingTime()));
    }
}
