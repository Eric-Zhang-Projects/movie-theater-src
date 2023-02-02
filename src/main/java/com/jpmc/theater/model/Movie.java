package com.jpmc.theater.model;

import java.time.Duration;
import java.util.Objects;

public class Movie {

    private final int id;

    private final String title;

    private final String description;

    private final Duration runningTime;

    //Default price of movie without any discounts included. In real life all movies per theater would be the same price, in which case this field would be in the TheaterProvider
    private final double defaultPrice;

    public Movie(int id, String title, String description, Duration runningTime, double defaultPrice) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.runningTime = runningTime;
        this.defaultPrice = defaultPrice;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Duration getRunningTime() {
        return runningTime;
    }

    public double getDefaultPrice() {
        return defaultPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return id == movie.id &&
                Objects.equals(title, movie.title)
                && Objects.equals(description, movie.description)
                && Objects.equals(runningTime, movie.runningTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, runningTime);
    }

    @Override
    public String toString(){
        return "id: " + id + " title: " + title + " description: " + description + " runningTime: " + runningTime;
    }
}