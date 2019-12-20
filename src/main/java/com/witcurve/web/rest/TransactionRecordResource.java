package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.enumeration.AttachmentType;
import com.witcurve.service.TransactionRecordService;
import com.witcurve.service.dto.TransactionRecordDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionRecordResource {

    private final Logger log = LoggerFactory.getLogger(TransactionRecordResource.class);

    @Autowired
    TransactionRecordService transactionRecordService;

    /**
     * save a transaction record
     *
     * @param transactionRecordDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/transaction-record")
    @Timed
    public ResponseEntity<TransactionRecordDTO> saveTransactionRecord(@RequestBody @Valid TransactionRecordDTO transactionRecordDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request to save TransactionRecord  : {}  ", transactionRecordDTO);
        if (transactionRecordDTO.getId() != null) {
            throw new WitcurveException("New record can not have an id already");
        }
        TransactionRecordDTO result = transactionRecordService.saveOrUpdate(transactionRecordDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * update a transaction record
     *
     * @param transactionRecordDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PutMapping("/transaction-record")
    @Timed
    public ResponseEntity<TransactionRecordDTO> updateTransactionRecord(@RequestBody @Valid TransactionRecordDTO transactionRecordDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request to update TransactionRecord  : {}  ", transactionRecordDTO);
        if (transactionRecordDTO.getId() == null) {
            throw new WitcurveException("Id is require to update record");
        }
        TransactionRecordDTO result = transactionRecordService.saveOrUpdate(transactionRecordDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * add attachment to transaction record
     *
     * @param transactionRecordId
     * @param file
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */

    @PatchMapping("/transaction-record/{transactionRecordId}")
    @Timed
    public ResponseEntity<TransactionRecordDTO> addAttachment(@PathVariable Long transactionRecordId, @RequestParam List<MultipartFile> file) throws WitcurveException, URISyntaxException {
        log.debug("Request to add attachment to transaction record with id   : {}  ", transactionRecordId);
        TransactionRecordDTO result = transactionRecordService.addAttachment(transactionRecordId, file);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get transaction record
     *
     * @param transactionRecordId
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */

    @GetMapping("/transaction-record/{transactionRecordId}")
    @Timed
    public ResponseEntity<TransactionRecordDTO> getTransactionRecordById(@PathVariable Long transactionRecordId) throws WitcurveException, URISyntaxException {
        log.debug("Request to get transaction record with id   : {}  ", transactionRecordId);
        TransactionRecordDTO result = transactionRecordService.getById(transactionRecordId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get transaction records
     * @param schoolInfoId, fromDate, endDate
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */

    @GetMapping("/transaction-record/school-info/{schoolInfoId}")
    @Timed
    public ResponseEntity<Page<TransactionRecordDTO>> getTransactionRecordBySchoolInfoAndDates(@ApiParam Pageable pageable, @PathVariable Long schoolInfoId, @RequestParam LocalDate fromDate, @RequestParam LocalDate endDate) throws WitcurveException, URISyntaxException {
        log.debug("Request to get transaction records with schoolInfoId and dateRange of : {} ", schoolInfoId, fromDate, endDate);
        Page<TransactionRecordDTO> result = transactionRecordService.getBySchoolInfoIdAndDateRange(pageable, schoolInfoId, fromDate, endDate);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
