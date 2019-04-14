package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.SchoolInfo;
import com.witcurve.repository.SchoolInfoRepository;
import com.witcurve.service.BulkImportService;
import com.witcurve.service.dto.csv.ImportResponse;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class BulkImportResource {

    private final Logger log = LoggerFactory.getLogger(BulkImportResource.class);

    @Autowired
    BulkImportService bulkImportService;

    @Autowired
    SchoolInfoRepository schoolInfoRepository;


    /**
     * bulk import staff or student data
     * @param schoolInfoId
     * @param file
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/bulk-import/schoolInfo/{schoolInfoId}")
    @Timed
    public ResponseEntity<ImportResponse> bulkImportWithCsv(@RequestParam MultipartFile file, @PathVariable Long schoolInfoId, @RequestParam String type) throws WitcurveException, URISyntaxException {
        log.debug("Request to bulk import date to schoolInfo with id : {} of type : {} with file : {}", schoolInfoId, type, file.getName());
        ImportResponse result = null;
        if(!file.getOriginalFilename().contains(".csv")) {
            throw new WitcurveException("Incorrect file format, only csv files are allowed for bulk import");
        }
        Optional<SchoolInfo> schoolInfo = schoolInfoRepository.findById(schoolInfoId);
        if(!schoolInfo.isPresent()) {
            throw new WitcurveException("School Board with given Id doesn't exists");
        }
        if(type.equalsIgnoreCase("staff")) {
            result = bulkImportService.bulkStaffImport(file, schoolInfoId);
        } else if(type.equalsIgnoreCase("student")) {
            result = bulkImportService.bulkStudentImport(file, schoolInfoId);
        } else {
            throw new WitcurveException("Invalid type value, please enter student or staff");
        }
        return ResponseEntity.ok(result);

    }


}
