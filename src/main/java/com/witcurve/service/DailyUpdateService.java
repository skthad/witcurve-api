package com.witcurve.service;

import com.witcurve.service.dto.DailyUpdateDTO;
import com.witcurve.web.rest.errors.WitcurveException;

public interface DailyUpdateService {

    DailyUpdateDTO saveOrUpdate(DailyUpdateDTO dailyUpdateDTO);

    DailyUpdateDTO getDailyUpdateById(Long id) throws WitcurveException;

    void deleteDailyUpdate(Long dailyUpdateId) throws WitcurveException;
}
