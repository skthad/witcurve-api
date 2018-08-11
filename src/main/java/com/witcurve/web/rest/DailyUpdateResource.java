package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.DailyUpdateService;
import com.witcurve.service.dto.DailyUpdateDTO;
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
public class DailyUpdateResource {

    private final Logger log = LoggerFactory.getLogger(DailyUpdateResource.class);

    @Autowired
    DailyUpdateService dailyUpdateService;

    /**
     * creates a daily update
     * @param dailyUpdateDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/daily-update")
    @Timed
    public ResponseEntity<DailyUpdateDTO> createDailyUpdate(@RequestBody @Valid DailyUpdateDTO dailyUpdateDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request Save DailyUpdate");
        if (dailyUpdateDTO.getId() != null) {
            throw new WitcurveException("New DailyUpdate can't already have an id");
        }
        DailyUpdateDTO result = dailyUpdateService.saveOrUpdate(dailyUpdateDTO);
        return ResponseEntity.created(new URI("/api/daily-update/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("dailyUpdate", result.getId().toString()))
            .body(result);
    }

    /**
     * get daily update by id
     * @param dailyUpdateId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/daily-update/{dailyUpdateId}")
    @Timed
    public ResponseEntity<DailyUpdateDTO> getDailyUpdateById(@PathVariable("dailyUpdateId") Long dailyUpdateId) throws WitcurveException {
        log.debug("Request to get DailyUpdate with id {}", dailyUpdateId);
        DailyUpdateDTO result = dailyUpdateService.getDailyUpdateById(dailyUpdateId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * update the given daily update
     * @param dailyUpdateDTO
     * @return
     * @throws WitcurveException
     */

    @PutMapping("/daily-update")
    @Timed
    public ResponseEntity<DailyUpdateDTO> updateDailyUpdate(@RequestBody @Valid DailyUpdateDTO dailyUpdateDTO) throws WitcurveException {
        log.debug("Request to update daily update");
        if (dailyUpdateDTO.getId() == null) {
            throw new WitcurveException("Id is required for update request");
        }
        DailyUpdateDTO result = dailyUpdateService.saveOrUpdate(dailyUpdateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("dailyUpdate", dailyUpdateDTO.getId().toString()))
            .body(result);
    }

    /**
     * delete the daily update
     * @param dailyUpdateId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/daily-update/{dailyUpdateId}")
    @Timed
    public ResponseEntity<Void> deleteDailyUpdate(@PathVariable Long dailyUpdateId) throws WitcurveException {
        log.debug("REST request to delete Daily Update: {}", dailyUpdateId);
        dailyUpdateService.deleteDailyUpdate(dailyUpdateId);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A Daily Update is deleted with identifier " + dailyUpdateId,
            dailyUpdateId.toString())).build();
    }
}
