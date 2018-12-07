package com.witcurve.service.util;

import com.witcurve.domain.AcademicSession;
import com.witcurve.repository.AcademicSessionRepository;
import com.witcurve.repository.EventRepository;
import com.witcurve.service.AcademicSessionService;
import com.witcurve.service.mapper.AcademicSessionMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import com.witcurve.service.util.LocalDateConverter;

public class WorkingDaysUtil {
    private static final Logger logger = LoggerFactory.getLogger("WorkingDaysUtil");
    @Autowired
    EventRepository eventRepository;

    @Autowired
    AcademicSession academicSessionRepository;

    public Long workingDays(LocalDate fromDate, LocalDate toDate, Long sessionId, boolean isSaturdayWorking)
        throws WitcurveException {
        LocalDate startDate = academicSessionRepository.getStartDate();
        LocalDate endDate = startDate.plusYears(1);
        Long workingDays = 0L;
        if (fromDate.isAfter(toDate)) {
            throw new WitcurveException("from date cannot be after to date.");
        }
        if (fromDate.isAfter(startDate) && toDate.isBefore(endDate)) {
            LocalDateConverter lcon = new LocalDateConverter();
            Date startVal= lcon.convertToDatabaseColumn(fromDate);
            Date endVal= lcon.convertToDatabaseColumn(toDate);
            Calendar startCal = Calendar.getInstance();
            startCal.setTime(startVal);
            Calendar endCal= Calendar.getInstance();
            endCal.setTime(endVal);
            if (isSaturdayWorking == false) {
                do {
                    startCal.add(Calendar.DAY_OF_MONTH, 1);
                    if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                        workingDays++;
                    }

                } while (startCal.getTimeInMillis() <= endCal.getTimeInMillis());
            }
            else
            {
                do {
                    startCal.add(Calendar.DAY_OF_MONTH, 1);
                    if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                        workingDays++;
                    }

                } while (startCal.getTimeInMillis() <= endCal.getTimeInMillis());
            }
            // to remove the holidays
            Long holidays = eventRepository.findHolidayInSession(sessionId);
            workingDays = workingDays - holidays;

        }
        else
        {
            throw new WitcurveException("start date and end date are out of academic session");
        }
        return workingDays;
    }

}
