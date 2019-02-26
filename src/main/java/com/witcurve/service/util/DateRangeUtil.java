package com.witcurve.service.util;

import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class DateRangeUtil {
    private static final Logger logger = LoggerFactory.getLogger("DateRangeUtil");

    public static void correctDateFormat(LocalDate startDate, LocalDate endDate) throws WitcurveException {
        if((startDate == null) ^ (endDate == null)) {
            throw new WitcurveException("Dude send the both dates brah! >:(");
        }
        if (startDate.isAfter(endDate)) {
            throw new WitcurveException("Come on bro! you know start date cannot be before end date");
        }
    }
}
