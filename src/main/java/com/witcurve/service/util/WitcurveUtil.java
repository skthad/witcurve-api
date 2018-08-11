package com.witcurve.service.util;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.witcurve.service.util.WitCurveConstants.DEFAULT_DATE_FORMAT;

public class WitcurveUtil {

    public static LocalDate getLocalDate(String localDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
        return LocalDate.parse(localDate, formatter);
    }
}
