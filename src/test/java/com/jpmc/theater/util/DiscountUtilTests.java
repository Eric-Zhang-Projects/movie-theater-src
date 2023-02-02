package com.jpmc.theater.util;

import com.jpmc.theater.provider.LocalDateProvider;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DiscountUtilTests {

    /**
     * Tests
     * Testing if price of movie is invalid (negative), in which case exception should be thrown
     * Testing sequence discounts only, Start Time + Special Movie discounts are not valid
     * Testing special movie discount only, Start Time + Sequence discounts are not valid
     * Testing Show Start Time discount only, Special Movie + Sequence discounts are not valid
     * Test all discounts together to get the best discount
     */

    private double defaultMoviePrice = 10;
    private int sequenceOfTheDay;
    private boolean isSpecialMovie;
    private LocalDateTime showStartTime;
    private final LocalDateProvider localDateProvider = LocalDateProvider.singleton();

    //Testing if price of movie is invalid (negative), in which case exception should be thrown
    @Test
    public void calculateShowingPrice_InvalidInput_Tests(){
        sequenceOfTheDay = 2;
        isSpecialMovie = false;
        showStartTime = DateUtil.createDateTime(localDateProvider.currentDate(), 12, 0);
        defaultMoviePrice = -10;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            DiscountUtil.calculateShowingPrice(defaultMoviePrice, sequenceOfTheDay, isSpecialMovie, showStartTime);
        });

        String expected = "Default movie price is < 0";
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    //Testing sequence discounts only, Start Time + Special Movie discounts are not valid
    @Test
    public void calculateShowingPrice_SequenceDiscountOnly_Tests(){
        isSpecialMovie = false;
        showStartTime = DateUtil.createDateTime(localDateProvider.currentDate(), 9, 0);

        double actual;
        //Apply $3 discount
        sequenceOfTheDay = 1;
        actual = DiscountUtil.calculateShowingPrice(defaultMoviePrice, sequenceOfTheDay, isSpecialMovie, showStartTime);
        assertEquals(actual, defaultMoviePrice - 3);

        //Apply $2 discount
        sequenceOfTheDay = 2;
        actual = DiscountUtil.calculateShowingPrice(defaultMoviePrice, sequenceOfTheDay, isSpecialMovie, showStartTime);
        assertEquals(actual, defaultMoviePrice - 2);

        //Apply $1 discount
        sequenceOfTheDay = 7;
        actual = DiscountUtil.calculateShowingPrice(defaultMoviePrice, sequenceOfTheDay, isSpecialMovie, showStartTime);
        assertEquals(actual, defaultMoviePrice - 1);

        //Apply no discount
        sequenceOfTheDay = 5;
        actual = DiscountUtil.calculateShowingPrice(defaultMoviePrice, sequenceOfTheDay, isSpecialMovie, showStartTime);
        assertEquals(actual, defaultMoviePrice);
    }

    //Testing special movie discount only, Start Time + Sequence discounts are not valid
    @Test
    public void calculateShowingPrice_SpecialMovieDiscountOnly_Tests(){
        sequenceOfTheDay = 5;
        showStartTime = DateUtil.createDateTime(localDateProvider.currentDate(), 9, 0);

        double actual;
        //No discount applied
        isSpecialMovie = false;
        actual = DiscountUtil.calculateShowingPrice(defaultMoviePrice, sequenceOfTheDay, isSpecialMovie, showStartTime);
        assertEquals(actual, defaultMoviePrice);

        //Apply discount
        isSpecialMovie = true;
        actual = DiscountUtil.calculateShowingPrice(defaultMoviePrice, sequenceOfTheDay, isSpecialMovie, showStartTime);
        assertEquals(actual, defaultMoviePrice - (defaultMoviePrice * 0.2));
    }

    //Testing Show Start Time discount only, Special Movie + Sequence discounts are not valid
    @Test
    public void calculateShowingPrice_ShowTimeDiscountOnly_Tests(){
        sequenceOfTheDay = 5;
        isSpecialMovie = false;

        double actual;
        //Not in discount window, do not apply discount
        showStartTime = DateUtil.createDateTime(localDateProvider.currentDate(), 9, 0);
        actual = DiscountUtil.calculateShowingPrice(defaultMoviePrice, sequenceOfTheDay, isSpecialMovie, showStartTime);
        assertEquals(actual, defaultMoviePrice);

        //In discount window, apply discount
        showStartTime = DateUtil.createDateTime(localDateProvider.currentDate(), 12, 0);
        actual = DiscountUtil.calculateShowingPrice(defaultMoviePrice, sequenceOfTheDay, isSpecialMovie, showStartTime);
        assertEquals(actual, defaultMoviePrice - (defaultMoviePrice * 0.25));

        //Right at end of discount window, apply discount
        showStartTime = DateUtil.createDateTime(localDateProvider.currentDate(), 16, 0);
        actual = DiscountUtil.calculateShowingPrice(defaultMoviePrice, sequenceOfTheDay, isSpecialMovie, showStartTime);
        assertEquals(actual, defaultMoviePrice - (defaultMoviePrice * 0.25));
    }

    //Test all discounts together to get the best discount
    @Test
    public void calculateShowingPrice_FindBestDiscount_Tests(){
        double actual;

        //Show time discount is greatest (25%)
        sequenceOfTheDay = 2; //$2 dollar discount
        isSpecialMovie = true; //%20 = $2 dollar discount
        showStartTime = DateUtil.createDateTime(localDateProvider.currentDate(), 14, 0); //%25 = $2.50 dollar discount
        actual = DiscountUtil.calculateShowingPrice(defaultMoviePrice, sequenceOfTheDay, isSpecialMovie, showStartTime);
        assertEquals(actual, defaultMoviePrice - (defaultMoviePrice * 0.25));

        //Sequence discount is greatest ($3)
        sequenceOfTheDay = 1; //$3 dollar discount
        isSpecialMovie = true; //$2 dollar discount
        showStartTime = DateUtil.createDateTime(localDateProvider.currentDate(), 14, 0); //%25 = $2.50 dollar discount
        actual = DiscountUtil.calculateShowingPrice(defaultMoviePrice, sequenceOfTheDay, isSpecialMovie, showStartTime);
        assertEquals(actual, defaultMoviePrice - 3);

        //Special movie discount is greatest (%20) (Showtime discount must not be active)
        sequenceOfTheDay = 7; //$1 discount
        isSpecialMovie = true; //%20 = $2 discount
        showStartTime = DateUtil.createDateTime(localDateProvider.currentDate(), 18, 0); //Out of window, 0% discount
        actual = DiscountUtil.calculateShowingPrice(defaultMoviePrice, sequenceOfTheDay, isSpecialMovie, showStartTime);
        assertEquals(actual, defaultMoviePrice - (defaultMoviePrice * 0.2));
    }
}
