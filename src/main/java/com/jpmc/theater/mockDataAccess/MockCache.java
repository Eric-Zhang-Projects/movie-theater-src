package com.jpmc.theater.mockDataAccess;

import com.jpmc.theater.model.Showing;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a mock cache that holds schedule data per theater
 */
public class MockCache {

    /**
     * Mock Cache where Key = Date, value = Schedule. For the limited use of this application, there will only be 1 entry for today's schedule
     */
    final static Map<LocalDate, List<Showing>> scheduleCache = new HashMap<>();

    /**
     * Check cache for the current date
     * @param date Date
     * @return Schedule for the date given, or null if no schedule found
     */
    public static List<Showing> get(LocalDate date){
        return scheduleCache.getOrDefault(date, null);
    }

    /**
     * Put new schedule into a cache for the given date
     * @param date Given date
     * @param schedule Schedule for that day
     */
    public static void put(LocalDate date, List<Showing> schedule){
        scheduleCache.put(date, schedule);
    }

    /**
     * Clears cache
     */
    public static void clear() {
        scheduleCache.clear();
    }
}
