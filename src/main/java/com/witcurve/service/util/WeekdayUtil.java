package com.witcurve.service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public class WeekdayUtil {

    private static final Logger logger = LoggerFactory.getLogger("WeekdayUtil");

    public static long getNoOfWeekDayBetweenDates(LocalDate fromDate, LocalDate toDate, DayOfWeek dayOfWeek) {
        long result = 0;
        long noOfDays = DAYS.between(fromDate, toDate) + 1;
        if(noOfDays < 7) {
            for(LocalDate date=fromDate;
                date.isBefore(toDate) || date.isEqual(toDate);
                date= date.plusDays(1)) {
                if(dayOfWeek.equals(date)) {
                    result ++ ;
                    break;
                }
            }

        } else {
            result = noOfDays/7;
            if(dayOfWeek.equals(fromDate.getDayOfWeek())) {
                result ++;
            }
        }

        return result;
    }

}
