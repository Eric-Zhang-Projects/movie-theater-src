package com.jpmc.theater.service;

import com.jpmc.theater.exceptions.TheaterException;
import com.jpmc.theater.mockDataAccess.MockCache;
import com.jpmc.theater.mockDataAccess.MockDatabase;
import com.jpmc.theater.model.Movie;
import com.jpmc.theater.model.Showing;
import com.jpmc.theater.provider.TheaterProvider;
import com.jpmc.theater.util.DiscountUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TheaterScheduleService {

    /**
     * Check cache for Today's Schedule.
     * If today's schedule is not in cache, calculate the schedule for today, put in cache, then return schedule.
     * Otherwise, if today's schedule is already in cache, return schedule
     * @return Today's schedule, represented by a list of showings
     */
    public static List<Showing> getTodaysSchedule() {
        //Get theater details
        TheaterProvider theaterProvider = TheaterProvider.singleton();
        //Get today's schedule for the theater
        List<Showing> schedule = MockCache.get(theaterProvider.getTodaysDate());
        //If schedule is in cache and not null, return schedule
        if (schedule != null){
            return schedule;
        }
        //Otherwise, we need to calculate the schedule for today
        try {
            //Create schedule from Theater object + list of movies
            schedule = createScheduleForTheaterToday(theaterProvider);
        } catch (Exception ex){
            System.out.println("Error creating Theater schedule: " + ex.getMessage());
            ex.printStackTrace();
            schedule = new ArrayList<>();
        }
        //Put today's schedule in cache
        MockCache.put(theaterProvider.getTodaysDate(), schedule);
        return schedule;
    }

    /**
     * Creates today's schedule for the given movie theater
     *
     * @param theaterProvider Theater object that represents the given theater. Determines default movie price,
     *                and schedule start (theater opening) + end (theater closing) times
     * @return Newly created schedule for today, represented by a list of showings
     */
    private static List<Showing> createScheduleForTheaterToday(TheaterProvider theaterProvider){
        //Mocking a database call to get all movies
        Collection<Movie> movies = MockDatabase.getMoviesList();
        if (movies.isEmpty()){
            throw new TheaterException("No movies being shown today");
        }
        LocalDateTime showTime = theaterProvider.getOpeningTime();
        LocalDateTime closingTime = theaterProvider.getClosingTime();
        if (showTime.isAfter(closingTime) || showTime.isEqual(closingTime)){
            throw new TheaterException("Theater operational hours are incorrect");
        }
        //Initialize empty schedule
        List<Showing> schedule = new ArrayList<>();
        //Creates schedule from theater opening to closing time, with movie sequence repeating in order of movies
        while (showTime.isBefore(closingTime)){
            //Add showing for each movie in order
            for (Movie movie : movies){
                //Used to break outer loop condition if needed during inner loop
                if (!showTime.plus(movie.getRunningTime()).isBefore(closingTime)){
                    showTime = showTime.plus(movie.getRunningTime());
                    break;
                }
                //Create new showing
                try {
                    Showing showing = createNewShowing(schedule, movie, theaterProvider, showTime);
                    schedule.add(showing);
                    showTime = showTime.plus(movie.getRunningTime());
                    //Add 15 minute buffer to clean up theater before next show
                    showTime = showTime.plusMinutes(15);
                } catch (RuntimeException ex){
                    throw new TheaterException("Could not create showing for " + movie.getTitle() + " at " + showTime
                    + " Error: " + ex.getMessage());
                }
            }
        }
        if (schedule.isEmpty()){
            throw new TheaterException("No showings scheduled for today");
        }
        return schedule;
    }

    /**
     * Creates new showing object based on current movie details, with discounted price calculated
     * @param schedule Current schedule of showings
     * @param movie Current movie
     * @param theaterProvider Current Theater
     * @param showTime Current Show start time
     * @return new Showing object
     */
    private static Showing createNewShowing(List<Showing> schedule, Movie movie, TheaterProvider theaterProvider,
                                            LocalDateTime showTime){
        //Current sequence of day for current movie/showing = # of movies already on schedule + 1
        int sequenceOfTheDay = schedule.size() + 1;
        //Theater determines which movie is special per day (as it should in real life per different theaters)
        boolean isSpecialMovie = theaterProvider.getTodaySpecialMovieId() == movie.getId();
        //Default movie price is determined by theater (as it should in real life per different theaters)
        double defaultMoviePrice = movie.getDefaultPrice();

        //Calculate showing price including discounts and update showing object
        double showingPrice = DiscountUtil.calculateShowingPrice(defaultMoviePrice, sequenceOfTheDay, isSpecialMovie, showTime);
        //Return new Showing
        return new Showing(movie, showTime, sequenceOfTheDay, isSpecialMovie, showingPrice);
    }

}
