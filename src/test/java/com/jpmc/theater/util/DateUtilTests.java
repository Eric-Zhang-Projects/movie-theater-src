package com.jpmc.theater.util;

import com.jpmc.theater.provider.LocalDateProvider;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateUtilTests {

    //Test createDateTime helper function that creates a date time for a specified day, hours, and minutes
    @Test
    public void createDateTime_test(){
        LocalDateProvider localDateProvider = LocalDateProvider.singleton();
        LocalDateTime actual = DateUtil.createDateTime(localDateProvider.currentDate(), 9, 30);
        LocalDateTime expected = LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 30));

        assert(actual.isEqual(expected));
    }
}
