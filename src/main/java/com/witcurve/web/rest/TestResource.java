package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.TestService;
import com.witcurve.service.dto.TestDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;

import static com.witcurve.service.util.WitcurveUtil.getLocalDate;


@RestController
@RequestMapping("/api")
public class TestResource {

    private final Logger log = LoggerFactory.getLogger(AcademicSessionResource.class);

    @Autowired
    TestService testService;

    /**
     * creates a test
     *
     * @param testDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/test")
    @Timed
    public ResponseEntity<TestDTO> createTest(@RequestBody @Valid TestDTO testDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request Save Test");
        if (testDTO.getId() != null) {
            throw new WitcurveException("New Test can't already have an id");
        }
        TestDTO result = testService.saveOrUpdate(testDTO);
        return ResponseEntity.created(new URI("/api/test/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("testSession", result.getId().toString()))
            .body(result);
    }

    /**
     * get test by id
     *
     * @param testId
     * @return
     * @throws WitcurveException
     */
    @GetMapping("/test/{testId}")
    @Timed
    public ResponseEntity<TestDTO> getTestById(@PathVariable("testId") Long testId) throws WitcurveException {
        log.debug("Request to get Test with id {}", testId);
        TestDTO result = testService.getTestById(testId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * update the given test
     *
     * @param testDTO
     * @return
     * @throws WitcurveException
     */
    @PutMapping("/test")
    @Timed
    public ResponseEntity<TestDTO> updateTest(@RequestBody @Valid TestDTO testDTO) throws WitcurveException {
        log.debug("Request to update test");
        if (testDTO.getId() == null) {
            throw new WitcurveException("Id is required for update request");
        }
        TestDTO result = testService.saveOrUpdate(testDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("test", testDTO.getId().toString()))
            .body(result);
    }

    /**
     * delete the test
     * @param testId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/test/{testId}")
    @Timed
    public ResponseEntity<Void> deleteTest(@PathVariable Long testId) throws WitcurveException {
        log.debug("REST request to delete Test: {}", testId);
        testService.deleteTest(testId);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A test is deleted with identifier " + testId,
            testId.toString())).build();
    }

    /**
     *
     * @param postedDate
     * @param timeTableUnitId
     * @return
     */
    @GetMapping("/test/posted-on/{timeTableUnitId}")
    public ResponseEntity<List<TestDTO>> getTestsByPostedDateAndTimeTableUnitId(
        @RequestParam("postedDate")String postedDate, @PathVariable("timeTableUnitId") Long timeTableUnitId) {
        log.debug("REST Request to get test posted on given date : {} and for time table unit id : {}", postedDate, timeTableUnitId);
        LocalDate date = getLocalDate(postedDate);
        List<TestDTO> assignments = testService.findTestsPostedOnGivenDateAndTimetableUnitId(date, timeTableUnitId);
        return new ResponseEntity<>(assignments, HttpStatus.OK);
    }

    /**
     *
     * @param testDate
     * @param timeTableUnitId
     * @return
     */

    @GetMapping("/test/test-on/{timeTableUnitId}")
    public ResponseEntity<List<TestDTO>> getTestsBySubmissionDateAndTimeTableUnitId(
        @RequestParam("testDate")String testDate, @PathVariable("timeTableUnitId") Long timeTableUnitId) {
        log.debug("REST Request to get Tests with submission on given date : {} and for time table unit id : {}", testDate, timeTableUnitId);
        LocalDate date = getLocalDate(testDate);
        List<TestDTO> tests = testService.findTestsByTestDateAndTimetableUnitId(date, timeTableUnitId);
        return new ResponseEntity<>(tests, HttpStatus.OK);
    }

}
