package com.witcurve.service.impl;

import com.witcurve.domain.Test;
import com.witcurve.repository.TestRepository;
import com.witcurve.service.TestService;
import com.witcurve.service.dto.TestDTO;
import com.witcurve.service.mapper.TestMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TestServiceImpl implements TestService {

    private final Logger log  = LoggerFactory.getLogger(TestServiceImpl.class);

    @Autowired
    TestRepository testRepository;

    @Autowired
    TestMapper testMapper;

    @Override
    public TestDTO saveOrUpdate(TestDTO testDTO) {
        log.debug("Request to save or update test : {}", testDTO);
        Test test = testMapper.testDTOToTest(testDTO);
        test = testRepository.save(test);
        return testMapper.testToTestDTO(test);
    }

    @Override
    public TestDTO getTestById(Long testId) throws WitcurveException {
        log.debug("Request to get test with id : {}", testId);
        Test test = testRepository.findById(testId).get();

       if( test == null) {
           throw new WitcurveException("No test with given id");
       }
       return testMapper.testToTestDTO(test);
    }

    @Override
    public void deleteTest(long testId) throws WitcurveException {
        log.debug("Request to delete test with id : {}", testId);
        Test test = testRepository.findById(testId).get();

        if (test == null)
            throw new WitcurveException("No test with given id");

        testRepository.delete(test);
    }

    @Override
    public List<TestDTO> findTestsPostedOnGivenDateAndTimetableUnitId(LocalDate postedDate, Long timeTableUnitId) {
        log.debug("Request to get tests with posted Date : {}", postedDate);
        List<Test> testsOnGivenDate = testRepository.findTestsByPostedDateAndTimetableUnitId(postedDate, timeTableUnitId);
        return testMapper.testsToTestDTOs(testsOnGivenDate);
    }

    @Override
    public List<TestDTO> findTestsByTestDateAndTimetableUnitId(LocalDate testDate, Long timeTableUnitId) {
        log.debug("Request to get tests with test Date : {}", testDate);
        List<Test> testsForGivenTestDate = testRepository.findAllTestsByTestDateAndTimetableUnitId(testDate, timeTableUnitId);
        return testMapper.testsToTestDTOs(testsForGivenTestDate);
    }
}
