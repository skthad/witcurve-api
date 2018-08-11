package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.HolidayService;
import com.witcurve.service.dto.HolidayDTO;
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

@RestController
@RequestMapping("/api")
public class HolidayResource {

    private final Logger log = LoggerFactory.getLogger(HolidayResource.class);

    @Autowired
    HolidayService holidayService;

    /**
     * creates a holiday
     * @param holidayDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/holiday")
    @Timed
    public ResponseEntity<HolidayDTO> createHoliday(@RequestBody @Valid HolidayDTO holidayDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request Save Holiday");
        if (holidayDTO.getId() != null) {
            throw new WitcurveException("New Holiday can't already have an id");
        }
        HolidayDTO result = holidayService.saveOrUpdate(holidayDTO);
        return ResponseEntity.created(new URI("/api/holiday/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("holiday", result.getId().toString()))
            .body(result);
    }

    /**
     * get holiday by id
     * @param holidayId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/holiday/{holidayId}")
    @Timed
    public ResponseEntity<HolidayDTO> getHolidayById(@PathVariable("holidayId") Long holidayId) throws WitcurveException {
        log.debug("Request to get Holiday with id {}", holidayId);
        HolidayDTO result = holidayService.getHolidayById(holidayId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * update the given holiday
     * @param holidayDTO
     * @return
     * @throws WitcurveException
     */

    @PutMapping("/holiday")
    @Timed
    public ResponseEntity<HolidayDTO> updateHoliday(@RequestBody @Valid HolidayDTO holidayDTO) throws WitcurveException {
        log.debug("Request to update holiday");
        if (holidayDTO.getId() == null) {
            throw new WitcurveException("Id is required for update request");
        }
        HolidayDTO result = holidayService.saveOrUpdate(holidayDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("holiday", holidayDTO.getId().toString()))
            .body(result);
    }

    /**
     * delete the holiday
     * @param holidayId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/holiday/{holidayId}")
    @Timed
    public ResponseEntity<Void> deleteHoliday(@PathVariable Long holidayId) throws WitcurveException {
        log.debug("REST request to delete Holiday: {}", holidayId);
        holidayService.deleteHoliday(holidayId);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A holiday is deleted with identifier " + holidayId,
            holidayId.toString())).build();
    }
}
