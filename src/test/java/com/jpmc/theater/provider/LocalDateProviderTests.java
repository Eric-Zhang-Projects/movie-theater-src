package com.jpmc.theater.provider;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocalDateProviderTests {
    @Test
    void makeSureCurrentTime() {
        LocalDate todaysDate = LocalDate.now();
        assertEquals(todaysDate, LocalDateProvider.singleton().currentDate());
    }
}
