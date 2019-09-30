package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.AttributeValueService;
import com.witcurve.service.dto.AttributeValueDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AttributeValueResource {

    private final Logger log = LoggerFactory.getLogger(AttributeValueResource.class);

    @Autowired
    AttributeValueService attributeValueService;

    /**
     * creates attributeValue
     *
     * @param attributeValueDTOs
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */

    @PostMapping("/attribute-value")
    @Timed
    public ResponseEntity<List<AttributeValueDTO>> saveOrUpdateAttributeValue(@RequestBody @Valid List<AttributeValueDTO> attributeValueDTOs) throws WitcurveException, URISyntaxException {
        log.debug("Request Save or Update AttributeValues : {}",attributeValueDTOs);
        try {
            List<AttributeValueDTO> result =attributeValueService.saveOrUpdate(attributeValueDTOs);
            return ResponseEntity.ok(result);
        }catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("attribute_student_value_UK")) {
                log.debug("Unique constraint (attribute_id, value, student_id) violated");
                throw new WitcurveException("Duplicated records cannot be created");
            } else if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }

        }

    }


    /**
     * get AttributeValue
     *
     * @param rcdId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/attribute-value/report-card-design/{rcdId}")
    @Timed
    public ResponseEntity<List<AttributeValueDTO>> getAttributeValuesByReportCardDesignId(@PathVariable("rcdId") Long rcdId,
                                                                                           @RequestParam(value = "studentId", required = false) Long studentId) throws WitcurveException {
        log.debug("Request to get AttributeValue for report card design with Id : {} and student with id : {}", rcdId, studentId);
        List<AttributeValueDTO> result =attributeValueService.getAttributeValueByReportCardDesign(rcdId,studentId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * delete AttributeValues by ids
     * @param ids
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/attribute-value")
    @Timed
    public ResponseEntity<Void> deleteStudentRemarks(@RequestParam List<Long> ids) throws WitcurveException {
        log.debug("Request to delete AttributeValues with ids : {} ", ids);
        attributeValueService.deleteAttributeValues(ids);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
