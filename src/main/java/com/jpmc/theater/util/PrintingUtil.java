package com.jpmc.theater.util;

import com.jpmc.theater.model.Showing;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.jpmc.theater.constants.Constants.*;

public class PrintingUtil {

    private static final DecimalFormat decimalFormatter = new DecimalFormat("0.00");

    public static void printScheduleJson(List<Showing> schedule){
        StringBuilder jsonString = new StringBuilder();
        schedule.forEach(s -> {
            String runTime = getReadableTimeDuration(s.getMovie().getRunningTime());
            jsonString.append("{\n");
            jsonString.append(String.format("\ttime: \"%s\",\n", s.getShowStartTime()));
            jsonString.append(String.format("\tmovie: \"%s\",\n", s.getMovie().getTitle()));
            jsonString.append(String.format("\tduration: \"%s\",\n", runTime.substring(1, runTime.length() - 1)));
            jsonString.append(String.format("\tprice: \"$%s\"\n", decimalFormatter.format(s.getShowingPrice())));
            jsonString.append("},\n");
        });
        //Cut off tailing ",\n" characters
        jsonString.deleteCharAt(jsonString.length() - 2);
        System.out.println(DELIMITER);
        System.out.print(jsonString);
        System.out.println(DELIMITER);
    }

    public static void printSchedule(List<Showing> schedule) {
        StringBuilder textString = new StringBuilder();
        schedule.forEach(s -> {
            textString.append(String.format("%s: ", s.getSequenceOfTheDay()));
            textString.append(String.format("%s ", s.getShowStartTime()));
            textString.append(String.format("%s ", s.getMovie().getTitle()));
            textString.append(String.format("%s ", getReadableTimeDuration(s.getMovie().getRunningTime()) + " "));
            textString.append(String.format("$%s", decimalFormatter.format(s.getShowingPrice())));
            textString.append("\n");
        });
        //Cut off tailing "\n" characters
        textString.deleteCharAt(textString.length() - 1);
        LocalDate today = schedule.get(0).getShowStartTime().toLocalDate();
        System.out.println(today);
        System.out.println(DELIMITER);
        System.out.println(textString);
        System.out.println(DELIMITER);
    }

    private static String getReadableTimeDuration(Duration duration) {
        long hour = duration.toHours();
        long remainingMin = duration.toMinutes() - TimeUnit.HOURS.toMinutes(duration.toHours());

        return String.format("(%s hour%s %s minute%s)", hour, handlePlural(hour), remainingMin, handlePlural(remainingMin));
    }

    // (s) postfix should be added to handle plural correctly
    private static String handlePlural(long value) {
        return value == 1 ? EMPTY_STRING : S;
    }

}
