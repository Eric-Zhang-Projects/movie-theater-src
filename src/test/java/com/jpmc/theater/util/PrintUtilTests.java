package com.jpmc.theater.util;

import com.jpmc.theater.TestDataBase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrintUtilTests extends TestDataBase {

    private final ByteArrayOutputStream printContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(printContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void printSchedule_test() {
        String expected = "2023-02-01" + System.lineSeparator() +
                "===================================================" + System.lineSeparator() +
                "1: 2023-02-01T15:00 Test Movie 1 (1 hour 45 minutes)  $7.00" + System.lineSeparator() +
                "2: 2023-02-01T17:00 Test Movie 2 (1 hour 45 minutes)  $16.00" + System.lineSeparator() +
                "3: 2023-02-01T19:00 Test Movie 3 (1 hour 45 minutes)  $30.00" + System.lineSeparator() +
                "===================================================" + System.lineSeparator();
        PrintingUtil.printSchedule(smallSchedule);
        assertEquals(expected, printContent.toString());
    }

    @Test
    public void printScheduleJson_test() {
        String expected = "===================================================" + System.lineSeparator() +
                "{" + System.lineSeparator() +
                "\ttime: \"2023-02-01T15:00\"," + System.lineSeparator() +
                "\tmovie: \"Test Movie 1\"," + System.lineSeparator() +
                "\tduration: \"1 hour 45 minutes\"," + System.lineSeparator() +
                "\tprice: \"$7.00\"" + System.lineSeparator() +
                "}," + System.lineSeparator() +
                "{" + System.lineSeparator() +
                "\ttime: \"2023-02-01T17:00\"," + System.lineSeparator() +
                "\tmovie: \"Test Movie 2\"," + System.lineSeparator() +
                "\tduration: \"1 hour 45 minutes\"," + System.lineSeparator() +
                "\tprice: \"$16.00\"" + System.lineSeparator() +
                "}," + System.lineSeparator() +
                "{" + System.lineSeparator() +
                "\ttime: \"2023-02-01T19:00\"," + System.lineSeparator() +
                "\tmovie: \"Test Movie 3\"," + System.lineSeparator() +
                "\tduration: \"1 hour 45 minutes\"," + System.lineSeparator() +
                "\tprice: \"$30.00\"" + System.lineSeparator() +
                "}" + System.lineSeparator() +
                "===================================================" + System.lineSeparator();
        PrintingUtil.printScheduleJson(smallSchedule);
        assertEquals(expected, printContent.toString());
    }

}
