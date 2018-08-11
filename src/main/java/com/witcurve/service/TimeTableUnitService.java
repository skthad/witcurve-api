package com.witcurve.service;

import com.witcurve.service.dto.TimeTableUnitDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface TimeTableUnitService {

    TimeTableUnitDTO saveOrUpdate(TimeTableUnitDTO timetableUnitDTO);

    TimeTableUnitDTO getTimetableUnitById(Long timetableUnitId) throws WitcurveException;

    void deleteTimetableUnitById(Long timetableUnitId) throws WitcurveException;

    List<TimeTableUnitDTO> getAllTimeTableUnitsByStudentId(Long studentId) throws WitcurveException;
}
