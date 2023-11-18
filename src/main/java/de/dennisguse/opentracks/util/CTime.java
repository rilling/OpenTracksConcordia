package de.dennisguse.opentracks.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CTime {
    public String getCurrentTime(){
        LocalTime currentTime = LocalTime.now();

        // Create a formatter for the desired time format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");

        // Format the current time using the formatter
        String formattedTime = currentTime.format(formatter);
        return formattedTime;
    }


}
