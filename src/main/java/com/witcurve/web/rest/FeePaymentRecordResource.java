package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.enumeration.ModeOfTransaction;
import com.witcurve.service.FeePaymentRecordService;
import com.witcurve.service.dto.FeePaymentRecordDTO;
import com.witcurve.service.util.WitcurveUtil;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.util.HeaderUtil;
import com.witcurve.web.rest.vm.ReportCardVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;

import javax.validation.Valid;
import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class FeePaymentRecordResource {

    private final Logger log = LoggerFactory.getLogger(FeePaymentRecordResource.class);

    @Autowired
    FeePaymentRecordService feePaymentRecordService;

    /**
     * creates feePaymentRecord
     *
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/fee-payment-record")
    @Timed
    public ResponseEntity<FeePaymentRecordDTO> createFeePaymentRecord(@RequestBody @Valid FeePaymentRecordDTO feePaymentRecordDTO, @RequestParam ModeOfTransaction mode) throws WitcurveException, URISyntaxException {
        log.debug("Request to save FeePaymentRecord : {}", feePaymentRecordDTO);
        if (feePaymentRecordDTO.getId() != null) {
            throw new WitcurveException("New record can not have an id");
        }
        try {
            FeePaymentRecordDTO result = feePaymentRecordService.saveOrUpdate(feePaymentRecordDTO,mode);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("orderId_UK")) {
                throw new WitcurveException("Unique constraint (orderId) violated");
            } else if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }

        }
    }

    /**
     * updates feePaymentRecord
     *
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PutMapping("/fee-payment-record")
    @Timed
    public ResponseEntity<FeePaymentRecordDTO> updateFeePaymentRecord(@RequestBody @Valid FeePaymentRecordDTO
                                                                          feePaymentRecordDTO, @RequestParam ModeOfTransaction mode) throws WitcurveException, URISyntaxException {
        log.debug("Request to update FeePaymentRecord : {}", feePaymentRecordDTO);
        if (feePaymentRecordDTO.getId() == null) {
            throw new WitcurveException("Id is require to update record");
        }
        try {
            FeePaymentRecordDTO result = feePaymentRecordService.saveOrUpdate(feePaymentRecordDTO, mode);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("orderId_UK")) {
                throw new WitcurveException("Unique constraint (orderId) violated");
            } else if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }

        }
    }

    /**
     * get feePaymentRecords
     *
     * @param studentId
     * @param sessionId
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @GetMapping("/fee-payment-record/students/{studentId}/academic-session/{sessionId}")
    @Timed
    public ResponseEntity<List<FeePaymentRecordDTO>> getByStudentAndSessionId(@PathVariable Long
                                                                                  studentId, @PathVariable Long sessionId) throws WitcurveException, URISyntaxException {
        log.debug("Request to get FeePaymentRecords by studentId and sessionId : {}", studentId, sessionId);
        List<FeePaymentRecordDTO> result = feePaymentRecordService.getByStudentAndSessionId(studentId, sessionId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get feePaymentRecords
     *
     * @param standardId
     * @param sessionId
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @GetMapping("/fee-payment-record/standards/{standardId}/academic-session/{sessionId}")
    @Timed
    public ResponseEntity<List<FeePaymentRecordDTO>> getByStandardAndSessionId(@PathVariable Long
                                                                                   standardId, @PathVariable Long sessionId) throws WitcurveException, URISyntaxException {
        log.debug("Request to get FeePaymentRecords by standardId and sessionId : {}", standardId, sessionId);
        List<FeePaymentRecordDTO> result = feePaymentRecordService.getByStandardAndSessionId(standardId, sessionId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * delete the FeePaymentRecord
     *
     * @param feePaymentRecordId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/fee-payment-record/{feePaymentRecordId}")
    @Timed
    public ResponseEntity<Void> deleteExam(@PathVariable Long feePaymentRecordId) throws WitcurveException, URISyntaxException {
        log.debug("REST request to delete FeePaymentRecord: {}", feePaymentRecordId);
        feePaymentRecordService.deleteOne(feePaymentRecordId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("A feePaymentRecord is deleted with identifier " + feePaymentRecordId,
            feePaymentRecordId.toString())).build();


    }

    @PostMapping("/generate-invoice/fee-payment-record/{feePaymentRecordId}")
    public ResponseEntity<Resource> getInvoice(@PathVariable Long feePaymentRecordId) {
        log.debug("Request to get invoice pdf of admissionId : {}", feePaymentRecordId);
        File result = feePaymentRecordService.generateInvoice(feePaymentRecordId);
        Resource resource = WitcurveUtil.getResourceFromFile(result);
        return ResponseEntity.ok(resource);


    }


}
