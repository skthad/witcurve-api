package com.witcurve.service;

import com.witcurve.service.dto.TestDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.time.LocalDate;
import java.util.List;

public interface TestService {

    TestDTO saveOrUpdate(TestDTO testDTO);

    TestDTO getTestById(Long testId) throws WitcurveException;

    void deleteTest(long testId) throws WitcurveException;

    List<TestDTO> findTestsPostedOnGivenDateAndTimetableUnitId(LocalDate postedDate, Long timetableUnitId);

    List<TestDTO> findTestsByTestDateAndTimetableUnitId(LocalDate testDate, Long timeTableUnitId);
}
