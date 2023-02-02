package com.jpmc.theater.provider;

import com.jpmc.theater.util.DateUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class TheaterProvider {

    private static TheaterProvider instance = null;
    public static TheaterProvider singleton(){
        if (instance == null){
            instance = new TheaterProvider();
        }
        return instance;
    }
    private final LocalDateProvider localDateProvider;
    private final int todaySpecialMovieId;
    private final LocalDateTime openingTime;
    private final LocalDateTime closingTime;

    public TheaterProvider() {
        this.localDateProvider = LocalDateProvider.singleton();
        this.todaySpecialMovieId = 2;
        this.openingTime = DateUtil.createDateTime(this.localDateProvider.currentDate(), 9, 0);
        this.closingTime = DateUtil.createDateTime(this.localDateProvider.currentDate(), 23, 0);
    }

    public int getTodaySpecialMovieId() {
        return todaySpecialMovieId;
    }

    public LocalDateTime getOpeningTime() {
        return openingTime;
    }

    public LocalDateTime getClosingTime() {
        return closingTime;
    }

    public LocalDate getTodaysDate() {
        return this.localDateProvider.currentDate();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TheaterProvider theaterProvider = (TheaterProvider) o;
        return openingTime.isEqual(theaterProvider.getOpeningTime())
                && closingTime.isEqual(theaterProvider.getClosingTime())
                && todaySpecialMovieId == theaterProvider.getTodaySpecialMovieId()
                && Objects.equals(localDateProvider, theaterProvider.localDateProvider);
    }

    @Override
    public int hashCode() {
        return Objects.hash(openingTime, closingTime, todaySpecialMovieId, localDateProvider);
    }

}
