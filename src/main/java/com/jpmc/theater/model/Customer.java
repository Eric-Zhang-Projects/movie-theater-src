package com.jpmc.theater.model;

import java.util.Objects;

/**
 * Largely unused for the purposes of this mock application
 */
public class Customer {

    private final String name;

    private final int id;

    //private final int reservationId; //Not used, but normally customer/reservation relation would be in a relation table in some DB

    /**
     * @param name customer name
     * @param id customer id
     */
    public Customer(String name, int id) {
        this.id = id; // NOTE - id is not used anywhere at the moment
        this.name = name;
    }

    public int getId(){
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return Objects.equals(name, customer.name) && id == customer.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id);
    }

    @Override
    public String toString() {
        return "name: " + name;
    }
}