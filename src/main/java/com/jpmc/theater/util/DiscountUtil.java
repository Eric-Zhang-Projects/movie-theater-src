package com.jpmc.theater.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DiscountUtil {

    /**
     * Calculates discounted price for the showing based on these conditions:
     * 20% discount for the special movie
     * $3 discount for the movie showing 1st of the day
     * $2 discount for the movie showing 2nd of the day
     * Any movies showing starting between 11AM ~ 4pm, you'll get 25% discount
     * Any movies showing on 7th, you'll get 1$ discount
     * The discount amount applied only one if met multiple rules; biggest amount one
     * @param defaultMoviePrice - default price of movie. Determined by Movie object in database
     * @param sequenceOfTheDay - sequence of the current show
     * @param isSpecialMovie - true if the current movie is today's special movie. Determined by specialMovieId field in TheaterProvider
     * @param showStartTime - showing start time
     * @return Lowest showing price based on discount qualifications
     */
    public static double calculateShowingPrice(double defaultMoviePrice, int sequenceOfTheDay,
                                               boolean isSpecialMovie, LocalDateTime showStartTime){
        if (defaultMoviePrice < 0){
            throw new IllegalArgumentException("Default movie price is < 0");
        }

        double specialMovieDiscountPrice = getSpecialMovieDiscountPrice(defaultMoviePrice, isSpecialMovie);
        double sequenceDiscountPrice = getSequenceDiscountPrice(defaultMoviePrice, sequenceOfTheDay);
        double showTimeDiscountPrice = getShowTimeDiscountPrice(defaultMoviePrice, showStartTime);

        //Return lowest price / price with the greatest discount
        return Math.min(Math.min(specialMovieDiscountPrice, sequenceDiscountPrice), showTimeDiscountPrice);
    }

    /**
     * Calculates discounted price based on Sequence Number
     * $3 discount for the movie showing 1st of the day
     * $2 discount for the movie showing 2nd of the day
     * Any movies showing on 7th, you'll get 1$ discount
     * @param defaultMoviePrice Default movie price without discount
     * @param showingSequenceOfTheDay Sequence # of the day
     * @return Discounted price if qualifying, otherwise returns default price
     */
    private static double getSequenceDiscountPrice(double defaultMoviePrice, int showingSequenceOfTheDay){
        if (showingSequenceOfTheDay == 1){
            return defaultMoviePrice - 3;
        } else if (showingSequenceOfTheDay == 2){
            return defaultMoviePrice- 2;
        }
        else if (showingSequenceOfTheDay == 7){
            return defaultMoviePrice - 1;
        }
        return defaultMoviePrice;
    }

    /**
     * Calculates discounted price based on Special Movie promotion
     * 20% discount for the special movie
     * @param defaultMoviePrice Default movie price without discount
     * @param isSpecialMovie isSpecialMovie boolean determined by theater
     * @return Discounted price if qualifying, otherwise returns default price
     */
    private static double getSpecialMovieDiscountPrice(double defaultMoviePrice, boolean isSpecialMovie){
        if (isSpecialMovie){
            return defaultMoviePrice * 0.8;
        }
        return defaultMoviePrice;
    }

    /**
     * Calculates discounted price based on Show Time
     * Any movies showing starting between 11AM ~ 4pm, you'll get 25% discount (Assuming inclusive)
     * @param defaultMoviePrice Default movie price without discount
     * @param showTime Starting show time
     * @return Discounted price if qualifying, otherwise retusn default price
     */
    private static double getShowTimeDiscountPrice(double defaultMoviePrice, LocalDateTime showTime){
        LocalDate today = showTime.toLocalDate(); //Show Time already has today's date from TheaterProvider
        LocalDateTime lowerBounds = DateUtil.createDateTime(today, 11, 0);
        LocalDateTime upperBounds = DateUtil.createDateTime(today, 16, 0);
        if ((showTime.isEqual(lowerBounds) || showTime.isAfter(lowerBounds)) &&
                (showTime.isEqual(upperBounds) || showTime.isBefore(upperBounds))){
            return defaultMoviePrice * 0.75;
        }
        return defaultMoviePrice;
    }

}
