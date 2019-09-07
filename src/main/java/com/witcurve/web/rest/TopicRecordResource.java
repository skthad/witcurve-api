package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.enumeration.TopicType;
import com.witcurve.security.PermissionsConstants;
import com.witcurve.service.TopicRecordService;
import com.witcurve.service.dto.TopicRecordDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TopicRecordResource {

    private final Logger log = LoggerFactory.getLogger(TopicRecordResource.class);

    @Autowired
    TopicRecordService topicRecordService;

    /**
     * creates a topic record
     * @param type
     * @param schoolInfoId
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/topic-records")
    @PreAuthorize("hasAuthority('" + PermissionsConstants.SUPER_ACCESS + "')")
    @Timed
    public ResponseEntity<TopicRecordDTO> create(@RequestParam TopicType type, @RequestParam(required = false) Long schoolInfoId,@RequestParam(required = false) Long standardId) throws WitcurveException, URISyntaxException {
        log.debug("Request to create Topic Record of type : {} for schoolInfoId: {}", type, schoolInfoId);
        try {
            TopicRecordDTO result = topicRecordService.addTopic(type, schoolInfoId,standardId);
            return ResponseEntity.created(new URI("/api/topic-records"))
                .headers(HeaderUtil.createEntityCreationAlert("topicRecords", null))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("term_start_date_session_id_UK")) {
                throw new WitcurveException("Unique constraint (start_date, session_id) violated");
            } else if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }

    }

    /**
     * get topicRecord by schoolInfo Id
     * @param schoolInfoId
     * @return
     * @throws WitcurveException
     */
    @PreAuthorize("hasAuthority('" + PermissionsConstants.SUPER_ACCESS + "')")
    @GetMapping("/topic-records/school-info/{schoolInfoId}")
    @Timed
    public ResponseEntity<List<TopicRecordDTO>> getTopicRecordBySchoolInfoId(@PathVariable("schoolInfoId") Long schoolInfoId) throws WitcurveException {
        log.debug("Request to get Topic Record with school info with id {}", schoolInfoId);
        List<TopicRecordDTO> result = topicRecordService.findTopicRecordBySchoolInfoId(schoolInfoId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get all topicRecords
     * @return
     * @throws WitcurveException
     */
    @PreAuthorize("hasAuthority('" + PermissionsConstants.SUPER_ACCESS + "')")
    @GetMapping("/topic-records")
    @Timed
    public ResponseEntity<List<TopicRecordDTO>> getAllTopicRecords() throws WitcurveException {
        log.debug("Request to get all TopicRecords with id");
        List<TopicRecordDTO> result = topicRecordService.findAllTopicRecords();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
