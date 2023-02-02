package com.jpmc.theater.service;

import com.jpmc.theater.exceptions.TheaterException;
import com.jpmc.theater.model.Reservation;
import com.jpmc.theater.model.Showing;

import java.util.List;

public class ReservationService {

    /**
     * Method that implements a customer placing a reservation.
     * Calls TheaterService.getTodaysSchedule to get today's schedule, then creates a new reservation object for the sequence number specified.
     * @param customerId Customer ID
     * @param sequenceOfTheDay Sequence of the day number for which movie customer wants to watch
     * @param ticketsBought Number of tickets bought
     * @return
     */
    public static Reservation createNewReservation(int customerId, int sequenceOfTheDay, int ticketsBought) {
        //Normally we would first check if the customerId is valid from some DB, and exit if it is not. Not implementing this

        if (sequenceOfTheDay <= 0){
            throw new IllegalArgumentException("Invalid sequence number");
        }

        //Get today's schedule
        List<Showing> schedule = TheaterScheduleService.getTodaysSchedule();

        if (schedule.isEmpty()){
            throw new TheaterException("No showings found for today");
        } else if (sequenceOfTheDay >= schedule.size()){
            throw new TheaterException("No shows found for this sequence number");
        }

        //Get the showing by sequenceOfTheDay number provided by the customer
        Showing showing = schedule.get(sequenceOfTheDay - 1);

        //Normally we would put this new reservation data into some Reservation table in our DB, and then
        //update some relation table between customer and reservation, ie. customer_reservation table with columns id, customer_id, reservation_id
        //But for the limited purpose of this code, we just return the new reservation object
        return new Reservation(customerId, showing, ticketsBought, showing.getShowingPrice() * ticketsBought);
    }

}
