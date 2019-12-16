package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.witcurve.config.ApplicationProperties;
import com.witcurve.domain.enumeration.FeePaymentType;
import com.witcurve.domain.enumeration.PaytmErrorCodes;
import com.witcurve.web.rest.vm.PaytmStatusCheckVM;
import com.witcurve.web.rest.vm.PaytmVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api")
public class PaytmCallBackResource {
    private final Logger log = LoggerFactory.getLogger(PaytmCallBackResource.class);
    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private HttpServletRequest request;

    private static final String NAME = "Radiant International School Of Excellence";
    private static final List<String> ADMISSION_IDS = new ArrayList<>(
        Arrays.asList("1001", "1002", "1003", "1004", "1005", "1006", "1007", "1008", "1009", "1010"));


    @GetMapping("/paytm-fee/validation")
    @Timed
    public ResponseEntity<PaytmVM> getStudentFee(@RequestParam(required = false) String instituteName, @RequestParam(required = false) String admissionId, @RequestParam(required = false) String type) {
        log.debug("Request to get fee detail of given institute with admissionId and type : ", instituteName, admissionId, type);
        PaytmVM paytmVM = new PaytmVM();
        String[] paytmIps = applicationProperties.paytm.getCommunicationIps().split(",");
        if (instituteName == null || admissionId == null || type == null) {
            paytmVM.setErrorCode(PaytmErrorCodes.All_FIELDS_ARE_NOT_PRESENT.getValue());
        } else if (!instituteName.equals(NAME)) {
            paytmVM.setErrorCode(PaytmErrorCodes.INVALID_INSTITUTE_NAME.getValue());
        } else if (!type.equals("Outstanding Fee") && !type.equals("Full Year Payment")) {
            paytmVM.setErrorCode(PaytmErrorCodes.INVALID_TYPE.getValue());
        } else if (!ADMISSION_IDS.contains(admissionId)) {
            paytmVM.setErrorCode(PaytmErrorCodes.INVALID_ENROLLMENT_NUMBERS.getValue());
        } else if (paytmIps.length > 1) {
            if (!Arrays.stream(paytmIps).anyMatch(i -> i.equals(getClientIp(request)))) {
                paytmVM.setErrorCode(PaytmErrorCodes.INVALID_IP_ADDRESS_FOR_COMMUNICATION.getValue());
            }
        } else if (admissionId.equals(ADMISSION_IDS.get(0)) && type.equals(FeePaymentType.OUTSTANDING_FEE.toString()) || admissionId.equals(ADMISSION_IDS.get(4)) && type.equals(FeePaymentType.OUTSTANDING_FEE.toString())) {
            paytmVM.setErrorCode(PaytmErrorCodes.NO_DUE.getValue());
        } else if (admissionId.equals(ADMISSION_IDS.get(1)) && type.equals(FeePaymentType.OUTSTANDING_FEE.toString()) || admissionId.equals(ADMISSION_IDS.get(5)) && type.equals(FeePaymentType.OUTSTANDING_FEE.toString()) || admissionId.equals(ADMISSION_IDS.get(9)) && type.equals(FeePaymentType.OUTSTANDING_FEE.toString())) {
            paytmVM.setErrorCode(PaytmErrorCodes.SUCCESS.getValue());
            PaytmVM.StudentDetail studentDetail = new PaytmVM().new StudentDetail();
            studentDetail.setStudentName("Srujan Kumar");
            studentDetail.setAdmissionId(ADMISSION_IDS.get(1));
            studentDetail.setStandard("X A");
            studentDetail.setRollNo("1");
            studentDetail.setFatherName("Mr. Rakesh Kumar");
            studentDetail.setMotherName("Mrs Rekha");
            studentDetail.setNote("No dues");
            studentDetail.setDateOfBirth("01/01/1990");
            paytmVM.setStudentDetails(studentDetail);
            List<PaytmVM.FeeTypeDetail> feeTypeDetails = new ArrayList<>();
            PaytmVM.FeeTypeDetail feeTypeDetail1 = new PaytmVM().new FeeTypeDetail();
            feeTypeDetail1.setName("Tution Fee(Installment-1)");
            feeTypeDetail1.setAmount(30000.0);
            feeTypeDetail1.setEditable(false);
            feeTypeDetail1.setRequired(true);
            feeTypeDetails.add(feeTypeDetail1);
            PaytmVM.FeeTypeDetail feeTypeDetail2 = new PaytmVM().new FeeTypeDetail();
            feeTypeDetail2.setName("Hostel Fee(Installment-1)");
            feeTypeDetail2.setAmount(30000.0);
            feeTypeDetail2.setEditable(false);
            feeTypeDetail2.setRequired(true);
            feeTypeDetails.add(feeTypeDetail2);
            PaytmVM.FeeTypeDetail feeTypeDetail3 = new PaytmVM().new FeeTypeDetail();
            feeTypeDetail3.setName("Transport Fee(Installment-1)");
            feeTypeDetail3.setAmount(5000.0);
            feeTypeDetail3.setEditable(false);
            feeTypeDetail3.setRequired(false);
            feeTypeDetails.add(feeTypeDetail3);
            PaytmVM.FeeTypeDetail feeTypeDetail4 = new PaytmVM().new FeeTypeDetail();
            feeTypeDetail4.setName("Books and Stationary Fee(Installment-1)");
            feeTypeDetail4.setAmount(10000.0);
            feeTypeDetail4.setEditable(false);
            feeTypeDetail4.setRequired(false);
            feeTypeDetails.add(feeTypeDetail4);
            paytmVM.setFeeTypeDetails(feeTypeDetails);
        } else if (admissionId.equals(ADMISSION_IDS.get(2)) && type.equals(FeePaymentType.OUTSTANDING_FEE.toString()) || admissionId.equals(ADMISSION_IDS.get(6)) && type.equals(FeePaymentType.OUTSTANDING_FEE.toString()) || admissionId.equals(ADMISSION_IDS.get(8)) && type.equals(FeePaymentType.OUTSTANDING_FEE.toString())) {
            paytmVM.setErrorCode(PaytmErrorCodes.SUCCESS.getValue());
            PaytmVM.StudentDetail studentDetail = new PaytmVM().new StudentDetail();
            studentDetail.setStudentName("Vamshi");
            studentDetail.setAdmissionId(ADMISSION_IDS.get(2));
            studentDetail.setStandard("X A");
            studentDetail.setRollNo("10");
            studentDetail.setMotherName("Mrs Priya");
            studentDetail.setFatherName("Mr Ramesh");
            studentDetail.setDateOfBirth("02/04/1993");
            studentDetail.setNote("Due payment from first installment");
            paytmVM.setStudentDetails(studentDetail);
            List<PaytmVM.FeeTypeDetail> feeTypeDetails = new ArrayList<>();
            PaytmVM.FeeTypeDetail feeTypeDetail1 = new PaytmVM().new FeeTypeDetail();
            feeTypeDetail1.setName("Transport Fee(Installment-1)");
            feeTypeDetail1.setAmount(5000.0);
            feeTypeDetail1.setEditable(false);
            feeTypeDetail1.setRequired(true);
            feeTypeDetails.add(feeTypeDetail1);
            PaytmVM.FeeTypeDetail feeTypeDetail2 = new PaytmVM().new FeeTypeDetail();
            feeTypeDetail2.setName("Tution Fee(Installment-2)");
            feeTypeDetail2.setAmount(30000.0);
            feeTypeDetail2.setEditable(false);
            feeTypeDetail2.setRequired(true);
            feeTypeDetails.add(feeTypeDetail2);
            PaytmVM.FeeTypeDetail feeTypeDetail3 = new PaytmVM().new FeeTypeDetail();
            feeTypeDetail3.setName("Hostel Fee(Installment-2)");
            feeTypeDetail3.setAmount(30000.0);
            feeTypeDetail3.setEditable(false);
            feeTypeDetail3.setRequired(true);
            feeTypeDetails.add(feeTypeDetail3);
            PaytmVM.FeeTypeDetail feeTypeDetail4 = new PaytmVM().new FeeTypeDetail();
            feeTypeDetail4.setName("Transport Fee(Installment-2)");
            feeTypeDetail4.setAmount(5000.0);
            feeTypeDetail4.setEditable(false);
            feeTypeDetail4.setRequired(false);
            feeTypeDetails.add(feeTypeDetail4);
            PaytmVM.FeeTypeDetail feeTypeDetail5 = new PaytmVM().new FeeTypeDetail();
            feeTypeDetail5.setName("Penalty(Installment-1)");
            feeTypeDetail5.setAmount(500.0);
            feeTypeDetail5.setEditable(false);
            feeTypeDetail5.setRequired(true);
            feeTypeDetails.add(feeTypeDetail5);
            paytmVM.setFeeTypeDetails(feeTypeDetails);
        } else if (admissionId.equals(ADMISSION_IDS.get(3)) && type.equals(FeePaymentType.OUTSTANDING_FEE.toString()) || admissionId.equals(ADMISSION_IDS.get(7)) && type.equals(FeePaymentType.OUTSTANDING_FEE.toString())) {
            paytmVM.setErrorCode(PaytmErrorCodes.INTERNAL_SERVER_ERROR.getValue());
        } else if (type.equals(FeePaymentType.FULL_YEAR_PAYMENT.toString()) && ADMISSION_IDS.contains(admissionId)) {
            paytmVM.setErrorCode(PaytmErrorCodes.SUCCESS.getValue());
            PaytmVM.StudentDetail studentDetail = new PaytmVM().new StudentDetail();
            studentDetail.setStudentName("Ravi Kumar");
            studentDetail.setAdmissionId(admissionId);
            studentDetail.setStandard("X A");
            studentDetail.setRollNo("1");
            studentDetail.setDateOfBirth("19/06/1995");
            studentDetail.setFatherName("Mr Prakash");
            studentDetail.setMotherName("Mrs Nisha");
            studentDetail.setNote("No dues");
            paytmVM.setStudentDetails(studentDetail);
            List<PaytmVM.FeeTypeDetail> feeTypeDetails = new ArrayList<>();
            PaytmVM.FeeTypeDetail feeTypeDetail1 = new PaytmVM().new FeeTypeDetail();
            feeTypeDetail1.setName("Tution Fee(Installment-1)");
            feeTypeDetail1.setAmount(30000.0);
            feeTypeDetail1.setEditable(false);
            feeTypeDetail1.setRequired(true);
            feeTypeDetails.add(feeTypeDetail1);
            PaytmVM.FeeTypeDetail feeTypeDetail2 = new PaytmVM().new FeeTypeDetail();
            feeTypeDetail2.setName("Hostel Fee(Installment-1)");
            feeTypeDetail2.setAmount(30000.0);
            feeTypeDetail2.setEditable(false);
            feeTypeDetail2.setRequired(true);
            feeTypeDetails.add(feeTypeDetail2);
            PaytmVM.FeeTypeDetail feeTypeDetail3 = new PaytmVM().new FeeTypeDetail();
            feeTypeDetail3.setName("Transport Fee(Installment-1)");
            feeTypeDetail3.setAmount(5000.0);
            feeTypeDetail3.setEditable(false);
            feeTypeDetail3.setRequired(true);
            feeTypeDetails.add(feeTypeDetail3);
            PaytmVM.FeeTypeDetail feeTypeDetail4 = new PaytmVM().new FeeTypeDetail();
            feeTypeDetail4.setName("Books and Stationary Fee(Installment-1)");
            feeTypeDetail4.setAmount(10000.0);
            feeTypeDetail4.setEditable(false);
            feeTypeDetail4.setRequired(true);
            feeTypeDetails.add(feeTypeDetail4);
            PaytmVM.FeeTypeDetail feeTypeDetail5 = new PaytmVM().new FeeTypeDetail();
            feeTypeDetail5.setName("Tution Fee(Installment-2)");
            feeTypeDetail5.setAmount(30000.0);
            feeTypeDetail5.setEditable(false);
            feeTypeDetail5.setRequired(true);
            feeTypeDetails.add(feeTypeDetail5);
            PaytmVM.FeeTypeDetail feeTypeDetail6 = new PaytmVM().new FeeTypeDetail();
            feeTypeDetail6.setName("Hostel Fee(Installment-2)");
            feeTypeDetail6.setAmount(30000.0);
            feeTypeDetail6.setEditable(false);
            feeTypeDetail6.setRequired(true);
            feeTypeDetails.add(feeTypeDetail6);
            PaytmVM.FeeTypeDetail feeTypeDetail7 = new PaytmVM().new FeeTypeDetail();
            feeTypeDetail7.setName("Transport Fee(Installment-2)");
            feeTypeDetail7.setAmount(5000.0);
            feeTypeDetail7.setEditable(false);
            feeTypeDetail7.setRequired(true);
            feeTypeDetails.add(feeTypeDetail7);
            paytmVM.setFeeTypeDetails(feeTypeDetails);
        }
        return ResponseEntity.ok(paytmVM);
    }

    @PostMapping("/paytm-fee/post-payment")
    @Timed
    public ResponseEntity<Map<String, String>> statusCheck(@RequestBody @Valid List<PaytmStatusCheckVM> paytmStatusCheckVMs, @RequestParam(required = false) String orderId, @RequestParam(required = false) String admissionId,
                                                           @RequestParam(required = false) Double amount, @RequestParam(required = false) String instituteName) {
        Map<String, String> responseMap = new HashMap<>();
        String[] paytmIps = applicationProperties.paytm.getCommunicationIps().split(",");
        log.debug("Request to check status with given  order Id, amount, enrollment no, instituteName : ", orderId, admissionId, amount, instituteName);
        if (orderId == null || admissionId == null || amount == null || instituteName == null) {
            responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.All_FIELDS_ARE_NOT_PRESENT.getValue()));
        } else if (!instituteName.equals(NAME)) {
            responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_INSTITUTE_NAME.getValue()));
        } else if (!ADMISSION_IDS.contains(admissionId)) {
            responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_ENROLLMENT_NUMBERS.getValue()));
        } else if (paytmIps.length > 1) {
            if (!Arrays.stream(paytmIps).anyMatch(i -> i.equals(getClientIp(request)))) {
                responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_IP_ADDRESS_FOR_COMMUNICATION.getValue()));
            }
        } else if (admissionId.equals(ADMISSION_IDS.get(1)) || admissionId.equals(ADMISSION_IDS.get(2))) {
            responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.SUCCESS.getValue()));
            responseMap.put("transactionStatus", "success");
            Random rnd = new Random();
            int n = 100000 + rnd.nextInt(900000);
            responseMap.put("receiptId", String.valueOf(n));
        } else if (admissionId.equals(ADMISSION_IDS.get(0)) || admissionId.equals(ADMISSION_IDS.get(4))) {
            responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.RECORD_ALREADY_EXIST.getValue()));
        } else if (admissionId.equals(ADMISSION_IDS.get(7)) || admissionId.equals(ADMISSION_IDS.get(5)) || admissionId.equals(ADMISSION_IDS.get(9))) {
            responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INTERNAL_SERVER_ERROR.getValue()));
        } else if (admissionId.equals(ADMISSION_IDS.get(3)) || admissionId.equals(ADMISSION_IDS.get(6)) || admissionId.equals(ADMISSION_IDS.get(8))) {
            responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.GIVEN_FEETYPE_OR_FEEDESCRIPTION_NOT_PRESENT.getValue()));
        }
        return ResponseEntity.ok(responseMap);
    }

    private String getClientIp(HttpServletRequest request) {

        String remoteAddr = "";

        Map<String, String> map = new HashMap<String, String>();

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            String objectJson = mapper.writeValueAsString(map);
            log.info("Headers : {}", objectJson);
        } catch (Exception e) {
            //
        }

        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }
        log.info("Client Ip address : {}", remoteAddr);
        return remoteAddr;
    }
}
