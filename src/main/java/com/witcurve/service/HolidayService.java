package com.witcurve.service;

import com.witcurve.service.dto.HolidayDTO;
import com.witcurve.web.rest.errors.WitcurveException;

public interface HolidayService {

    HolidayDTO saveOrUpdate(HolidayDTO holidayDTO);

    HolidayDTO getHolidayById(Long holidayId) throws WitcurveException;

    void deleteHoliday(Long holidayId) throws WitcurveException;
}
