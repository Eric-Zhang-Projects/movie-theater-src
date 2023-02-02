package com.jpmc.theater.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Showing {

    //private final int id = 1; //Not being used at the moment, but would be if a datastore of some kind were in use

    private final Movie movie;

    private final LocalDateTime showStartTime;

    private final int sequenceOfTheDay;

    private final boolean isSpecialMovie;

    private final double showingPrice; //Price of the showing that customers will pay. This price includes discounts

    public Showing(Movie movie, LocalDateTime showStartTime, int sequenceOfTheDay, boolean isSpecialMovie, double showingPrice) {
        this.movie = movie;
        this.showStartTime = showStartTime;
        this.sequenceOfTheDay = sequenceOfTheDay;
        this.isSpecialMovie = isSpecialMovie;
        this.showingPrice = showingPrice;
    }

    public Movie getMovie() {
        return movie;
    }

    public LocalDateTime getShowStartTime() {
        return showStartTime;
    }

    public int getSequenceOfTheDay() {
        return sequenceOfTheDay;
    }

    public boolean isSpecialMovie() {
        return isSpecialMovie;
    }

    public double getShowingPrice() {
        return showingPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Showing showing = (Showing) o;
        return movie.getId() == showing.getMovie().getId()
                && showStartTime.equals(showing.getShowStartTime())
                && sequenceOfTheDay == showing.sequenceOfTheDay
                && isSpecialMovie == showing.isSpecialMovie
                && Double.compare(showingPrice, showing.showingPrice) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(movie, showStartTime, sequenceOfTheDay, isSpecialMovie, showingPrice);
    }

    @Override
    public String toString(){
        return "sequence: " + sequenceOfTheDay + " showStartTime:" + showStartTime + " movie: " + movie.getTitle() + " isSpecial: " + isSpecialMovie
                + " actualPrice: " + showingPrice;
    }
}
