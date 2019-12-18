package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.witcurve.config.ApplicationProperties;
import com.witcurve.domain.enumeration.FeePaymentType;
import com.witcurve.domain.enumeration.PaytmErrorCodes;
import com.witcurve.service.util.WitCurveConstants;
import com.witcurve.service.util.WitcurveUtil;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.vm.PaytmStatusCheckVM;
import com.witcurve.web.rest.vm.PaytmVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
    private static final Map<String, Double> FEE_DESCRIPTION_AMOUNT= Collections.unmodifiableMap(
        new HashMap<String, Double>() {{
            put("Tution Fee", 30000d);
            put("Hostel Fee", 30000d);
            put("Transport Fee", 5000d);
            put("Books and Stationary Fee", 10000d);
            put("Penalty", 500d);
        }});


    @GetMapping("/paytm-fee/validation")
    @Timed
    public ResponseEntity<PaytmVM> getStudentFee(@RequestParam(required = false) String instituteName, @RequestParam(required = false) String admissionId, @RequestParam(required = false) String type) {
        log.debug("Request to get fee detail of given institute with admissionId and type : ", instituteName, admissionId, type);
        PaytmVM paytmVM = new PaytmVM();
        String[] paytmIps = applicationProperties.paytm.getCommunicationIps().split(",");
        int index = ADMISSION_IDS.indexOf(admissionId);
        if (instituteName == null || admissionId == null || type == null) {
            paytmVM.setErrorCode(PaytmErrorCodes.MISSING_FIELDS.getValue());
        } else if (!instituteName.equals(NAME)) {
            paytmVM.setErrorCode(PaytmErrorCodes.INVALID_INSTITUTE_NAME.getValue());
        } else if (!type.equals(FeePaymentType.OUTSTANDING_FEE.toString()) && !type.equals(FeePaymentType.FULL_YEAR_PAYMENT.toString())) {
            paytmVM.setErrorCode(PaytmErrorCodes.INVALID_TYPE.getValue());
        } else if (index == -1) {
            paytmVM.setErrorCode(PaytmErrorCodes.INVALID_ENROLLMENT_NUMBERS.getValue());
        } else if (paytmIps.length > 1) {
            if (!Arrays.stream(paytmIps).anyMatch(i -> i.equals(getClientIp(request)))) {
                paytmVM.setErrorCode(PaytmErrorCodes.INVALID_IP_ADDRESS_FOR_COMMUNICATION.getValue());
            }
        } else if (index == 0) {
            paytmVM.setErrorCode(PaytmErrorCodes.NO_DUE.getValue());
        } else if (index == 1) {
            paytmVM.setErrorCode(PaytmErrorCodes.INTERNAL_SERVER_ERROR.getValue());
        } else if (index == 2 || index == 3 || index ==4 || index ==5) {
            if(type.equals(FeePaymentType.OUTSTANDING_FEE.toString())) {
                paytmVM = getSamplePaytmRequest("srujan_sample.json");
            } else {
                paytmVM = getSamplePaytmRequest("ravi_sample.json");
            }
            PaytmVM.StudentDetail studentDetail = paytmVM.getStudentDetails();
            studentDetail.setAdmissionId(admissionId);
            paytmVM.setStudentDetails(studentDetail);
        } else {
            if(type.equals(FeePaymentType.OUTSTANDING_FEE.toString())) {
                paytmVM = getSamplePaytmRequest("vamsi_sample.json");
            } else {
                paytmVM = getSamplePaytmRequest("ravi_sample.json");
            }
            PaytmVM.StudentDetail studentDetail = paytmVM.getStudentDetails();
            studentDetail.setAdmissionId(admissionId);
            paytmVM.setStudentDetails(studentDetail);
        }
        return ResponseEntity.ok(paytmVM);
    }

    @PostMapping("/paytm-fee/post-payment")
    @Timed
    public ResponseEntity<Map<String, String>> statusCheck(@RequestBody @Valid PaytmStatusCheckVM paytmStatusCheckVM, @RequestParam(required = false) String orderId, @RequestParam(required = false) String admissionId, @RequestParam(required = false) String instituteName) {
        log.debug("Request to check status with given  order Id, enrollment no, instituteName : ", orderId, admissionId, instituteName);
        //todo - while integrating this api to fee modules - use a check method which returns error code.

        //todo - missing cases, some cases are not covered, these should be checked when integrated with fee modules
        //todo check if the fee type and fee description matches properly not just description alone
        //todo since each item will have separate call, make the fee record properly to
        // make sure apis with same order id are attached with same receipt number
        //todo handle the case when one record is missing in full year payment
        //todo handle the case when required fee has not be record because of some reason.

        Map<String, String> responseMap = new HashMap<>();
        String[] paytmIps = applicationProperties.paytm.getCommunicationIps().split(",");

        String[] splitStrings = paytmStatusCheckVM.getName().split("\\(");
        String feeDescription = splitStrings[0];
        String feeType = splitStrings[1].substring(0, splitStrings[1].length()-1);

        Double amount;
        try {
            amount = Double.parseDouble(paytmStatusCheckVM.getAmount());
        } catch (NumberFormatException e) {
            amount = null;
        }

        LocalDate transactionDate;
        try {
            transactionDate = WitcurveUtil.getLocalDate(paytmStatusCheckVM.getTransactionDate(), WitCurveConstants.DEFAULT_DATE_FORMAT);
        } catch (DateTimeParseException e) {
            transactionDate = null;
        }

        int index = ADMISSION_IDS.indexOf(admissionId);
        if (orderId == null || admissionId == null || instituteName == null) {
            responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.MISSING_FIELDS.getValue()));
        } else if (!instituteName.equals(NAME)) {
            responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_INSTITUTE_NAME.getValue()));
        } else if (index == -1) {
            responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_ENROLLMENT_NUMBERS.getValue()));
        } else if (paytmIps.length > 1) {
            if (!Arrays.stream(paytmIps).anyMatch(i -> i.equals(getClientIp(request)))) {
                responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_IP_ADDRESS_FOR_COMMUNICATION.getValue()));
            }
        } else if (index == 0) {
            responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.NO_DUE.getValue()));
        } else if (index == 1 || index == 2 || index == 6) {
            responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INTERNAL_SERVER_ERROR.getValue()));
        } else if (index == 3 || index ==7 ) {
            responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.RECORD_ALREADY_EXIST.getValue()));
        } else {
            if(paytmStatusCheckVM.getName() == null || paytmStatusCheckVM.getItemId() == null
                || paytmStatusCheckVM.getTransactionDate() == null || paytmStatusCheckVM.getAmount() == null
                || paytmStatusCheckVM.getType() == null) {
                responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.MISSING_FIELDS.getValue()));
            } else if(transactionDate == null) {
                responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_TRANSACTION_DATE.getValue()));
            } else if (!paytmStatusCheckVM.getType().equals(FeePaymentType.OUTSTANDING_FEE.toString())
                && !paytmStatusCheckVM.getType().equals(FeePaymentType.FULL_YEAR_PAYMENT.toString())) {
                responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_TYPE.getValue()));
            } else if(amount == null){
                responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_AMOUNT.getValue()));
            } else if(!feeType.equals("Installment-1") && !feeType.equals("Installment-2")) {
                responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_FEE_NAME.getValue()));
            } else if(paytmStatusCheckVM.getType().equals(FeePaymentType.FULL_YEAR_PAYMENT.toString())) {
                if(feeDescription.equals("Penalty")) {
                    responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_FEE_NAME.getValue()));
                } else if(!FEE_DESCRIPTION_AMOUNT.keySet().contains(feeDescription)) {
                    responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_FEE_NAME.getValue()));
                } else if(!FEE_DESCRIPTION_AMOUNT.get(feeDescription).equals(amount)) {
                    responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_AMOUNT.getValue()));
                } else {
                    responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.SUCCESS.getValue()));
                    responseMap.put("transactionStatus", "success");
                    Random rnd = new Random();
                    int n = 100000 + rnd.nextInt(900000);
                    responseMap.put("receiptId", String.valueOf(n));
                }
            } else if(paytmStatusCheckVM.getType().equals(FeePaymentType.OUTSTANDING_FEE.toString())) {
                if((index == 4 || index ==5)) {
                    if(feeType.equals("Installment-2")) {
                        responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_FEE_NAME.getValue()));
                    } else if(feeDescription.equals("Penalty")) {
                        responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_FEE_NAME.getValue()));
                    } else if(!FEE_DESCRIPTION_AMOUNT.keySet().contains(feeDescription)) {
                        responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_FEE_NAME.getValue()));
                    } else if(!FEE_DESCRIPTION_AMOUNT.get(feeDescription).equals(amount)) {
                        responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_AMOUNT.getValue()));
                    } else {
                        responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.SUCCESS.getValue()));
                        responseMap.put("transactionStatus", "success");
                        Random rnd = new Random();
                        int n = 100000 + rnd.nextInt(900000);
                        responseMap.put("receiptId", String.valueOf(n));
                    }
                } else if((index == 8 || index ==9)) {
                    if(feeType.equals("Installment-1") && !feeDescription.equals("Penalty") && !feeDescription.equals("Transport Fee")) {
                        responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_FEE_NAME.getValue()));
                    } else if(feeType.equals("Installment-2") && (feeDescription.equals("Penalty") || feeDescription.equals("Books and Stationary Fee"))) {
                        responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_FEE_NAME.getValue()));
                    } else if(!FEE_DESCRIPTION_AMOUNT.keySet().contains(feeDescription)) {
                        responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_FEE_NAME.getValue()));
                    } else if(!FEE_DESCRIPTION_AMOUNT.get(feeDescription).equals(amount)) {
                        responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_AMOUNT.getValue()));
                    } else {
                        responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.SUCCESS.getValue()));
                        responseMap.put("transactionStatus", "success");
                        Random rnd = new Random();
                        int n = 100000 + rnd.nextInt(900000);
                        responseMap.put("receiptId", String.valueOf(n));
                    }
                }
            }
        }

        return ResponseEntity.ok(responseMap);
    }

    private String getClientIp(HttpServletRequest request) {
        String remoteAddr = "";
        Map<String, String> map = new HashMap<String, String>();
        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }
        log.info("Client Ip address : {}", remoteAddr);
        return remoteAddr;
    }

    private PaytmVM getSamplePaytmRequest(String fileName) {
        try {
            File jsonFile = new File("src/main/resources/"+fileName);
            String jsonString = new String(Files.readAllBytes(Paths.get(jsonFile.getAbsolutePath())));
            ObjectMapper mapper = new ObjectMapper();
            PaytmVM paytmVM = mapper.readValue(jsonString, PaytmVM.class);
            return paytmVM;
        } catch (IOException e) {
            throw new WitcurveException("Error while reading resource files for sample");
        }
    }
}
