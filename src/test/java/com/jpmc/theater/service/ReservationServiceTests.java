package com.jpmc.theater.service;

import com.jpmc.theater.TestDataBase;
import com.jpmc.theater.exceptions.TheaterException;
import com.jpmc.theater.model.Reservation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTests extends TestDataBase {

    private final int customerId = 1;
    private int sequenceOfTheDay = 2;
    private int ticketsBought = 1;

    /**
     * Tests
     * Testing invalid sequence number throws exception
     * Testing if TheaterScheduleService.getTodaysSchedule() returns an empty schedule. Should throw exception
     * Testing if sequence of day is greater than schedule size. Indicates that user is trying to reserve a show later than the last showing. Should throw an exception
     * Testing reservation made successfully for 1 ticket
     * Testing reservation made successfully for 2 tickets
     */

    //Testing invalid sequence number throws exception
    @Test
    public void invalidSequenceOfTheDay_test(){
        sequenceOfTheDay = -1;
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            ReservationService.createNewReservation(customerId, sequenceOfTheDay, ticketsBought));

        String expected = "Invalid sequence number";
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    //Testing if TheaterScheduleService.getTodaysSchedule() returns an empty schedule. Should throw exception
    @Test
    public void emptySchedule_test(){
        try (MockedStatic<TheaterScheduleService> theaterScheduleService = Mockito.mockStatic(TheaterScheduleService.class)) {
            schedule = new ArrayList<>();
            theaterScheduleService.when(TheaterScheduleService::getTodaysSchedule).thenReturn(schedule);

            Exception exception = assertThrows(TheaterException.class, () ->
                    ReservationService.createNewReservation(customerId, sequenceOfTheDay, ticketsBought));

            String expected = "No showings found for today";
            String actual = exception.getMessage();

            assertEquals(expected, actual);
        }
    }

    //Testing if sequence of day is greater than schedule size. Indicates that user is trying to
    //reserve a show later than the last showing. Should throw an exception
    @Test
    public void sequenceOfTheDayOutOfBounds_test(){
        try (MockedStatic<TheaterScheduleService> theaterScheduleService = Mockito.mockStatic(TheaterScheduleService.class)) {
            theaterScheduleService.when(TheaterScheduleService::getTodaysSchedule).thenReturn(schedule);

            sequenceOfTheDay = 200;
            Exception exception = assertThrows(TheaterException.class, () -> {
                ReservationService.createNewReservation(customerId, sequenceOfTheDay, ticketsBought);
            });

            String expected = "No shows found for this sequence number";
            String actual = exception.getMessage();

            assertEquals(expected, actual);
        }
    }

    //Testing reservation made successfully for 1 ticket
    @Test
    public void createNewReservationSuccessful_1Ticket_test(){
        try (MockedStatic<TheaterScheduleService> theaterScheduleService = Mockito.mockStatic(TheaterScheduleService.class)) {
            theaterScheduleService.when(TheaterScheduleService::getTodaysSchedule).thenReturn(schedule);

            Reservation actual = ReservationService.createNewReservation(customerId, sequenceOfTheDay, ticketsBought);
            Reservation expected = new Reservation(customerId, showing2, ticketsBought, showing2.getShowingPrice() * ticketsBought);
            assertEquals(expected, actual);
        }
    }

    //Testing reservation made successfully for 2 tickets
    @Test
    public void createNewReservationSuccessful_2Tickets_test(){
        try (MockedStatic<TheaterScheduleService> theaterScheduleService = Mockito.mockStatic(TheaterScheduleService.class)) {
            theaterScheduleService.when(TheaterScheduleService::getTodaysSchedule).thenReturn(schedule);

            ticketsBought = 2;
            Reservation actual = ReservationService.createNewReservation(customerId, sequenceOfTheDay, ticketsBought);
            Reservation expected = new Reservation(customerId, showing2, ticketsBought, showing2.getShowingPrice() * ticketsBought);
            assertEquals(expected, actual);
        }
    }
}
