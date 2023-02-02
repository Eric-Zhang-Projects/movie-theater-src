package com.jpmc.theater;

import com.jpmc.theater.model.Showing;
import com.jpmc.theater.service.TheaterScheduleService;
import com.jpmc.theater.util.PrintingUtil;

import java.util.List;

public class PrintSchedule {

    /**
     * Main method used to print normal + JSON text
     * To test different cases, change fields in the following locations:
     * src/provider/TheaterProvider - Today's Special Movie ID, Opening Time, Closing Time
     * src/mockDataAccess/MockDatabase/getMoviesList() - Movie Title, Description, Duration, Default Price
     */
    public static void main(String[] args){
        //Get today's schedule
        List<Showing> schedule = TheaterScheduleService.getTodaysSchedule();

        //Print Schedule Normal Text
        PrintingUtil.printSchedule(schedule);
        //Print Schedule in JSON Format
        PrintingUtil.printScheduleJson(schedule);
    }

}
