package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.enumeration.MobileOsType;
import com.witcurve.service.MobileMetaDataService;
import com.witcurve.service.dto.MobileMetaDataDTO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MobileMetaDataResource {

    private final Logger log = LoggerFactory.getLogger(MobileMetaDataResource.class);

    @Autowired
    MobileMetaDataService mobileMetaDataService;

    /**
     * creates a MobileMetaData
     * @param mobileMetaDataDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/mobile-meta-data")
    @Timed
    public ResponseEntity<MobileMetaDataDTO> createMobileMetaData(@RequestBody @Valid MobileMetaDataDTO mobileMetaDataDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request Save MobileMetaData : {}"+mobileMetaDataDTO);
        if (mobileMetaDataDTO.getId() != null) {
            throw new WitcurveException("New MobileMetaData can't already have an id");
        }
        try {
            MobileMetaDataDTO result = mobileMetaDataService.saveOrUpdate(mobileMetaDataDTO);
            return ResponseEntity.ok(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("type_institute_UK")) {
                throw new WitcurveException("There already exists a record with given type for this institute");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }

    }

    /**
     * updates a MobileMetaData
     * @param mobileMetaDataDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PutMapping("/mobile-meta-data")
    @Timed
    public ResponseEntity<MobileMetaDataDTO> updateMobileMetaData(@RequestBody @Valid MobileMetaDataDTO mobileMetaDataDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request Save MobileMetaData : {}"+mobileMetaDataDTO);
        if (mobileMetaDataDTO.getId() == null) {
            throw new WitcurveException("Updated MetaData needs id");
        }
        try {
            MobileMetaDataDTO result = mobileMetaDataService.saveOrUpdate(mobileMetaDataDTO);
            return ResponseEntity.ok(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("type_institute_UK")) {
                throw new WitcurveException("There already exists a record with given type for this institute");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }

    /**
     * get MobileMetaData by id
     * @param mobileMetaDataId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/mobile-meta-data/{mobileMetaDataId}")
    @Timed
    public ResponseEntity<MobileMetaDataDTO> getMobileMetaDataById(@RequestParam("mobileMetaDataId") Long mobileMetaDataId) throws WitcurveException {
        log.debug("Request to get mobileMetaData with id {}", mobileMetaDataId);
        MobileMetaDataDTO result = mobileMetaDataService.getMobileMetaDataById(mobileMetaDataId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get all MobileMetaData
     * @param instituteId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/mobile-meta-data")
    @Timed
    public ResponseEntity<List<MobileMetaDataDTO>> getMobileMetaDatasByStudentId(@RequestParam(required = false) Long instituteId) {
        log.debug("Request to get all MobileMetaData with instituteId : {}", instituteId);
        List<MobileMetaDataDTO> result = mobileMetaDataService.getMobileMetaDataByInstitute(instituteId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get format MobileMetaData by instituteId
     * @param instituteId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/mobile-meta-data/institute/{instituteId}")
    @Timed
    public ResponseEntity<Map<String, Object>> getFromattedMobileMetaDatasByStudentId(@PathVariable("instituteId") Long instituteId) {
        log.debug("Request to get all MobileMetaData with instituteId : {}", instituteId);
        List<MobileMetaDataDTO> mobileMetaDataDTOS = mobileMetaDataService.getMobileMetaDataByInstitute(instituteId);
        Map<String, Object> result = new HashMap<>();
        for(MobileMetaDataDTO mobileMetaDataDTO : mobileMetaDataDTOS) {
            Map<String, String> rootMetaData = new HashMap<>();
            rootMetaData.put("latest",mobileMetaDataDTO.getLatest());
            rootMetaData.put("minimum", mobileMetaDataDTO.getMinimum());
            rootMetaData.put("url",mobileMetaDataDTO.getUrl());
            rootMetaData.put("enabled", mobileMetaDataDTO.getEnabled().toString());
            if(mobileMetaDataDTO.getType().equals(MobileOsType.ANDROID)) {
                result.put("android", rootMetaData);
            } else {
                result.put("ios", rootMetaData);
            }
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    /**
     * delete MobileMetaData by instituteId
     * @param instituteId
     * @return
     * @throws WitcurveException
     */

    @DeleteMapping("/mobile-meta-data/institute/{instituteId}")
    @Timed
    public ResponseEntity<Void> deletedMobileMetaDataByInstituteId(@PathVariable("instituteId") Long instituteId) {
        log.debug("Request to delete MobileMetaData with instituteId : {}", instituteId);
        mobileMetaDataService.deleteMobileMetaDataByInstituteId(instituteId);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }


}
