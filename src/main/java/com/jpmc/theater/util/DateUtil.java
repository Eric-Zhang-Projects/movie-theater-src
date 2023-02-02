package com.jpmc.theater.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateUtil {

    /**
     * Helper to create date time for specified date and time (hours + minutes)
     * @param date Date
     * @param hour Hour
     * @param minutes Minutes
     * @return Date Time
     */
    public static LocalDateTime createDateTime(LocalDate date, int hour, int minutes){
        return LocalDateTime.of(date, LocalTime.of(hour, minutes));
    }
}
