package com.jpmc.theater.model;

import java.util.Objects;

public class Reservation {
    private final int id = 1; //Not used at the moment, but can be incorporated to support multiple reservations being made

    private final int customerId;

    private final Showing showing; //This field should not be here, rather the relation between Reservation and Showing should be in some DB relation table

    private final int ticketsBought;

    private final double reservationPrice; // == ticketsBought * showingPrice

    public Reservation(int customerId, Showing showing, int ticketsBought, double reservationPrice) {
        this.customerId = customerId;
        this.showing = showing;
        this.ticketsBought = ticketsBought;
        this.reservationPrice = reservationPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation reservation = (Reservation) o;
        return id == reservation.id
                && customerId == reservation.customerId
                && ticketsBought == reservation.ticketsBought
                && Double.compare(reservationPrice, reservation.reservationPrice) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerId, showing, ticketsBought, reservationPrice);
    }

    @Override
    public String toString() {
        return "id: " + id + " customerId: " + customerId + " showing sequence #: " + showing.getSequenceOfTheDay() + " ticketsBought: " + ticketsBought + " price: " + reservationPrice;
    }

}