# Explanation of Changes:
#### Assumptions:
1. This application only serves a single movie theater
2. Movies have their own unique price (normally the movie theater decides 1 price for all movies, but for sake of this application movie prices are determined by the movie)
3. Theater determines the special movie of the day, and opening and closing times (new fields I added). 
   * Normally with a database involved, Theater data should be in a table by itself, which should have some Administrative client that can change the special movie of the day,
   and the opening + closing times. (Not implemented)
4. Updated pricing model as follows:
   * Movie price - default price of a movie without discount or ticket quantity considered
   * Showing price - calculates the price of 1 ticket for a movie with discount included
   * Reservation price - price of showing * number of tickets bought

#### General Changes
* Created 2 entry points that could serve as realistic API endpoints as if this was a real production application:
  * ``TheaterScheduleService.getTodaysSchedule`` + ``ReservationService.createNewReservation`` explained in detail in the ``API`` section below.
* Dynamically creating theater Schedule based on Theater opening + closing times (new fields), with the same repeating Movie pattern (Movie 1 -> Movie 2 -> Movie 3 -> Movie 1, etc)
instead of hard coding each show
* Moved business logic out of Data objects (Reservation, Showing, etc.)
* Created custom exception + more thorough exception handling
* Fleshed out unit testing coverage
* Added comments

### API Breakdown:
1. ``TheaterScheduleService.getTodaysSchedule`` - this application's main API which returns today's schedule based on today's theater data. Imagine that this is the API that is called
when users want to view the entire Schedule for today to see available movie showings.
   * As the schedule does not change  per day, I have implemented a mock local cache ``MockCache`` to store schedules per day. If the cache has today's schedule,
   this API will return the schedule in the cache. Otherwise, we call ``createScheduleForTheaterToday`` method to
   create a new schedule.
     * ``createScheduleForTheaterToday`` takes a list of movies from the database and based on a theater's opening and closing times,
     dynamically fills a theater's schedule with Showings. 
     * Each showing has a price that is calculated using the movie's default price, and the discounts the Showing/Movie are eligible for
     * Each showing is added to the schedule, which is returned
2. ``ReservationService.createNewReservation`` - Imagine this as the API that is called once a user selects the movie they want to watch from the schedule (sequence number), and 
enters in the number of tickets they want to buy.
   * Calls the above ``TheaterScheduleService.getTodaysSchedule`` to get (or create based on the cache)
   today's schedule. Once the schedule is retrieved, user attempts to select a sequence number from the schedule, and buys any number of tickets
   specified. The new reservation object is then created and returned. 
   * In a production setting, we would have a database table set up similar to Reservation, Customer, Customer_Reservation, Reservation_Showing, etc. to properly create and persist the new reservation
3. ``PrintSchedule`` - calls the above ``TheaterScheduleService.getTodaysSchedule`` to create (cache will always be empty when running this as a main method) today's schedule. 
Once schedule is retrieved, print normal text + JSON versions to the console.

# (Original description below)
# Introduction

This is a poorly written application, and we're expecting the candidate to greatly improve this code base.

## Instructions
* **Consider this to be your project! Feel free to make any changes**
* There are several deliberate design, code quality and test issues in the current code, they should be identified and resolved
* Implement the "New Requirements" below
* Keep it mind that code quality is very important
* Focus on testing, and feel free to bring in any testing strategies/frameworks you'd like to implement
* You're welcome to spend as much time as you like, however, we're expecting that this should take no more than 2 hours

## `movie-theater`

### Current Features
* Customer can make a reservation for the movie
  * And, system can calculate the ticket fee for customer's reservation
* Theater have a following discount rules
  * 20% discount for the special movie
  * $3 discount for the movie showing 1st of the day
  * $2 discount for the movie showing 2nd of the day
* System can display movie schedule with simple text format

## New Requirements
* New discount rules; In addition to current rules
  * Any movies showing starting between 11AM ~ 4pm, you'll get 25% discount
  * Any movies showing on 7th, you'll get 1$ discount
  * The discount amount applied only one if met multiple rules; biggest amount one
* We want to print the movie schedule with simple text & json format