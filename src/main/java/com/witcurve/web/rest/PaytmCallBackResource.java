package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.StudentFeeDescription;
import com.witcurve.domain.StudentFeeStructure;
import com.witcurve.domain.StudentFeeType;
import com.witcurve.domain.enumeration.PaytmErrorCodes;
import com.witcurve.service.PaytmCallBackService;
import com.witcurve.web.rest.vm.PaytmStatusCheckVM;
import com.witcurve.web.rest.vm.PaytmVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api")
public class PaytmCallBackResource {
    private final Logger log = LoggerFactory.getLogger(PaytmCallBackResource.class);

    @Autowired
    PaytmCallBackService paytmCallBackService;

    @GetMapping("/paytm-fee/validation")
    @Timed
    public ResponseEntity<PaytmVM> getStudentFee(@RequestParam(required = false) String instituteName, @RequestParam(required = false) String admissionId, @RequestParam(required = false) String type) {
        log.debug("Request to get fee detail of given institute with admissionId and type : ", instituteName, admissionId, type);
        PaytmVM paytmVM = paytmCallBackService.getStudentFee(instituteName, admissionId, type);
        return ResponseEntity.ok(paytmVM);






       /* int index = ADMISSION_IDS.indexOf(admissionId);
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
        return ResponseEntity.ok(paytmVM);*/
    }


    @PostMapping("/paytm-fee/post-payment")
    @Timed
    public ResponseEntity<Map<String, String>> statusCheck(@RequestBody @Valid PaytmStatusCheckVM
                                                               paytmStatusCheckVM, @RequestParam(required = false) String orderId, @RequestParam(required = false) String
                                                               admissionId, @RequestParam(required = false) String instituteName) {
        log.debug("Request to check status with given  order Id, enrollment no, instituteName : ", orderId, admissionId, instituteName);
        Map<String, String> responseMap = null;
        try {
            responseMap = paytmCallBackService.save(paytmStatusCheckVM, orderId, admissionId, instituteName);
        } catch (Exception e) {
            responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INTERNAL_SERVER_ERROR.getValue()));
        }
        return ResponseEntity.ok(responseMap);
    }

        /*    Map<String, String> responseMap = new HashMap<>();
            String[] paytmIps = applicationProperties.paytm.getCommunicationIps().split(",");
            Institute institute = instituteRepository.findInstituteByName(instituteName);
            Student student = studentRepository.getByInstituteIdAndAdmissionId(institute.getId(), admissionId);
            AcademicSession session = academicSessionRepository.nearestActiveSessionToDate(student.getSchoolInfo().getId(), LocalDate.now());

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
            if (orderId == null || admissionId == null || instituteName == null) {
                responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.MISSING_FIELDS.getValue()));
            } else if (institute == null) {
                responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_INSTITUTE_NAME.getValue()));
            } else if (student == null) {
                responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_ENROLLMENT_NUMBERS.getValue()));
                //remove this length check while pushing it to test
            } else if (paytmIps.length > 1) {
                if (!Arrays.stream(paytmIps).anyMatch(i -> i.equals(getClientIp(request)))) {
                    responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_IP_ADDRESS_FOR_COMMUNICATION.getValue()));
                }
            } else {
                if (paytmStatusCheckVM.getName() == null || paytmStatusCheckVM.getItemId() == null
                    || paytmStatusCheckVM.getTransactionDate() == null || paytmStatusCheckVM.getAmount() == null
                    || paytmStatusCheckVM.getType() == null) {
                    responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.MISSING_FIELDS.getValue()));
                } else if (transactionDate == null) {
                    responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_TRANSACTION_DATE.getValue()));
                } else if (!EnumUtils.isValidEnum(FeePaymentType.class, paytmStatusCheckVM.getType().toUpperCase())) {
                    responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_TYPE.getValue()));
                } else if (amount == null) {
                    responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_AMOUNT.getValue()));
                } else {

                    String[] splitStrings = paytmStatusCheckVM.getName().split("\\(");
                    String feeDescription = splitStrings[0];
                    String feeType = splitStrings[1].substring(0, splitStrings[1].length() - 1);


                    StudentFeeStructure studentFeeStructure = studentFeeStructureRepository.getByStudentIdAndSessionId(student.getId(), session.getId());
                    FeePaymentRecord feePaymentRecord = feePaymentRecordRepository.getByStudentIdOrderIdAndFeeName(student.getId(), orderId, feeType, feeDescription);

                    Map<String, String> mapOfPaidFeeExcludingPenalties = new HashMap<>();
                    for (FeePaymentDetail feePaymentDetail : feePaymentRecord.getFeePaymentDetails()) {
                        if (!feePaymentDetail.getFeeDescription().getName().equals("Penalty")) {
                            String fee = feePaymentDetail.getFeeDescription().getName() + "(" + feePaymentDetail.getFeeType().getName() + ")";
                            Double feeMoney = feePaymentDetail.getAmount();
                            mapOfPaidFeeExcludingPenalties.put(fee, feeMoney.toString());
                        }
                    }

                    Map<String, String> mapOfAllFeeExcludingPenalties = new HashMap<>();
                    Map<String, String> mapOfPenalties = new HashMap<>();
                    for (StudentFeeType studentFeeType : studentFeeStructure.getStudentFeeTypes()) {
                        String feeTypeName = studentFeeType.getFeeType().getName();
                        for (StudentFeeDescription feeDescriptions : studentFeeType.getStudentFeeDescriptions()) {
                            String feeDescriptionName = feeDescriptions.getFeeDescription().getName();
                            String name = feeDescriptionName + "(" + feeTypeName + ")";
                            Double finalAmount = feeDescriptions.getAmount() + (feeDescriptions.getAdjustment()) - (feeDescriptions.getOneTimeDiscount());
                            mapOfAllFeeExcludingPenalties.put(name, finalAmount.toString());
                            if (studentFeeType.getPenalty() != null) {
                                String penalty = "Penalty" + "(" + feeTypeName + ")";
                                mapOfPenalties.put(penalty, studentFeeType.getPenalty().toString());
                            }

                        }
                    }
                    Map<String, String> mapOfFeeIncludingPenalty = new HashMap<>();
                    mapOfFeeIncludingPenalty.putAll(mapOfAllFeeExcludingPenalties);
                    mapOfFeeIncludingPenalty.putAll(mapOfPenalties);

                    List<String> feeNames = mapOfFeeIncludingPenalty.keySet().stream().collect(Collectors.toList());
                    if (mapOfPaidFeeExcludingPenalties.size() == mapOfAllFeeExcludingPenalties.size()) {
                        responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.NO_DUE.getValue()));
                    } else if (!feeNames.contains(paytmStatusCheckVM.getName())) {
                        responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_FEE_NAME.getValue()));
                    } else if (paytmStatusCheckVM.getAmount().equals(mapOfAllFeeExcludingPenalties.get(paytmStatusCheckVM.getName()))) {
                        responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_AMOUNT.getValue()));
                    } else if (paytmStatusCheckVM.getType().equals(FeePaymentType.FULL_YEAR_PAYMENT.toString()) && feeDescription.equals("Penalty")) {
                        responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_FEE_NAME.getValue()));
                    } else if (feePaymentRecord != null) {
                        responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.RECORD_ALREADY_EXIST.getValue()));
                    } else {
                        //heremake call to save feePaymentRecord
                        FeePaymentRecordDTO feePaymentRecordObj = prepareFeePaymentObj(orderId, studentFeeStructure.getId(), paytmStatusCheckVM, feeType, feeDescription);
                        feePaymentRecordService.saveOrUpdate(feePaymentRecordObj, ModeOfTransaction.SYSTEM);

                        responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.SUCCESS.getValue()));
                        responseMap.put("transactionStatus", "success");
                        Random rnd = new Random();
                        int n = 100000 + rnd.nextInt(900000);
                        //check record with particular orderId is present or not if present than fetch that receitId and set here
                        List<FeePaymentRecord> feePaymentRecords = feePaymentRecordRepository.getByOrderId(orderId);
                        if (feePaymentRecords.size() > 0) {
                            String receiptId = feePaymentRecords.get(0).getTransactionId();
                        }
                        responseMap.put("receiptId", String.valueOf(n));
                    }

                }
            }
            return ResponseEntity.ok(responseMap);
        }

        private FeePaymentRecordDTO prepareFeePaymentObj (String orderId, Long studentFeeStructureId, PaytmStatusCheckVM
        paytmStatusCheckVM, String feeType, String FeeDescription){
            FeePaymentRecordDTO feePaymentRecordDTO = new FeePaymentRecordDTO();
            feePaymentRecordDTO.setTransactionId(orderId);
            feePaymentRecordDTO.setType(PaymentRecordType.PAYTM);
            feePaymentRecordDTO.setOrderId(RandomStringUtils.randomAlphanumeric(8));
            feePaymentRecordDTO.setStudentFeeStructureId(studentFeeStructureId);
            feePaymentRecordDTO.setTransactionDate(LocalDate.now());


            FeePaymentDetailDTO feePaymentDetailDTO = new FeePaymentDetailDTO();
            feePaymentDetailDTO.setAmount(Double.valueOf(paytmStatusCheckVM.getAmount()));
            feePaymentDetailDTO.setItemId(paytmStatusCheckVM.getItemId());

            FeeDetails feeDescription = feeDetailsRepository.findFeeDetailsByName(FeeDescription);
            feePaymentDetailDTO.setFeeDescriptionId(feeDescription.getId());

            FeeDetails feeTypes = feeDetailsRepository.findFeeDetailsByName(feeType);
            feePaymentDetailDTO.setFeeTypeId(feeTypes.getId());

            feePaymentRecordDTO.setFeePaymentDetails(Arrays.asList(feePaymentDetailDTO));

            if (feeDescription.equals("Penalty")) {
                feePaymentRecordDTO.setPenaltyAmount(Double.valueOf(paytmStatusCheckVM.getAmount()));
            } else {
                feePaymentRecordDTO.setTotalAmount(Double.valueOf(paytmStatusCheckVM.getAmount()));
            }

            return feePaymentRecordDTO;
        }*/
































             /*else if (!feeType.equals("Installment-1") && !feeType.equals("Installment-2")) {
                responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_FEE_NAME.getValue()));
            } else if (paytmStatusCheckVM.getType().equals(FeePaymentType.FULL_YEAR_PAYMENT.toString())) {
                if (feeDescription.equals("Penalty")) {
                    responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_FEE_NAME.getValue()));
                } else if (!FEE_DESCRIPTION_AMOUNT.keySet().contains(feeDescription)) {
                    responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_FEE_NAME.getValue()));
                } else if (!FEE_DESCRIPTION_AMOUNT.get(feeDescription).equals(amount)) {
                    responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_AMOUNT.getValue()));
                } else {
                    responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.SUCCESS.getValue()));
                    responseMap.put("transactionStatus", "success");
                    Random rnd = new Random();
                    int n = 100000 + rnd.nextInt(900000);
                    responseMap.put("receiptId", String.valueOf(n));
                }
            } else if (paytmStatusCheckVM.getType().equals(FeePaymentType.OUTSTANDING_FEE.toString())) {
                if ((index == 4 || index == 5)) {
                    if (feeType.equals("Installment-2")) {
                        responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_FEE_NAME.getValue()));
                    } else if (feeDescription.equals("Penalty")) {
                        responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_FEE_NAME.getValue()));
                    } else if (!FEE_DESCRIPTION_AMOUNT.keySet().contains(feeDescription)) {
                        responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_FEE_NAME.getValue()));
                    } else if (!FEE_DESCRIPTION_AMOUNT.get(feeDescription).equals(amount)) {
                        responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_AMOUNT.getValue()));
                    } else {
                        responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.SUCCESS.getValue()));
                        responseMap.put("transactionStatus", "success");
                        Random rnd = new Random();
                        int n = 100000 + rnd.nextInt(900000);
                        responseMap.put("receiptId", String.valueOf(n));
                    }
                } else if ((index == 8 || index == 9)) {
                    if (feeType.equals("Installment-1") && !feeDescription.equals("Penalty") && !feeDescription.equals("Transport Fee")) {
                        responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_FEE_NAME.getValue()));
                    } else if (feeType.equals("Installment-2") && (feeDescription.equals("Penalty") || feeDescription.equals("Books and Stationary Fee"))) {
                        responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_FEE_NAME.getValue()));
                    } else if (!FEE_DESCRIPTION_AMOUNT.keySet().contains(feeDescription)) {
                        responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_FEE_NAME.getValue()));
                    } else if (!FEE_DESCRIPTION_AMOUNT.get(feeDescription).equals(amount)) {
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

        return ResponseEntity.ok(responseMap);*/




   /* private PaytmVM getSamplePaytmRequest(String fileName) {
        try {
            File jsonFile = new File("src/main/resources/" + fileName);
            String jsonString = new String(Files.readAllBytes(Paths.get(jsonFile.getAbsolutePath())));
            ObjectMapper mapper = new ObjectMapper();
            PaytmVM paytmVM = mapper.readValue(jsonString, PaytmVM.class);
            return paytmVM;
        } catch (IOException e) {
            throw new WitcurveException("Error while reading resource files for sample");
        }
    }*/

    private Map<Long, List<Long>> prepareMap(StudentFeeStructure studentFeeStructure, LocalDate todayDate) {
        Map<Long, List<Long>> map = new HashMap<>();
        List<Long> feeTypeIdAfterDueDate = new ArrayList<>();
        for (StudentFeeType studentFeeType : studentFeeStructure.getStudentFeeTypes()) {
            Long feeTypeId = studentFeeType.getFeeType().getId();
            if (todayDate != null) {
                if (todayDate.compareTo(studentFeeType.getDueDate()) > 0) {
                    feeTypeIdAfterDueDate.add(feeTypeId);
                }
            }
            for (StudentFeeDescription studentFeeDescription : studentFeeType.getStudentFeeDescriptions()) {
                if (map.size() > 0) {
                    if (map.containsKey(feeTypeId)) {
                        List<Long> values = map.get(feeTypeId);
                        values.add(studentFeeDescription.getFeeDescription().getId());
                        map.put(feeTypeId, values);
                    }
                }
                map.put(feeTypeId, Arrays.asList(studentFeeDescription.getFeeDescription().getId()));
            }
        }
        if (feeTypeIdAfterDueDate.size() > 0) {
            for (Long feeTypeId : feeTypeIdAfterDueDate) {
                map.remove(feeTypeId);
            }
        }
        return map;
    }





    /*private Map<Long, List<Long>> compareWithFeePaymentRecord(List<FeePaymentRecord> feePaymentRecords, Map<Long, List<Long>> map) {
        if (feePaymentRecords.size() == 0) {
            return map;
        }

        Map<Long, List<Long>> fMap = new HashMap<>();
        List<Long> feeTypeOfFeePaymentRecord = new ArrayList<>();
        for (FeePaymentRecord feePaymentRecord : feePaymentRecords) {
            for (FeePaymentDetail feePaymentDetail : feePaymentRecord.getFeePaymentDetails()) {
                feeTypeOfFeePaymentRecord.add(feePaymentDetail.getFeeType().getId());
            }
        }

        List<Long> feeTypesOfStudentFeeStructure = map.keySet().stream().collect(Collectors.toList());
        // List<Long> feeTypeOfFeePaymentRecord = fMap.keySet().stream().collect(Collectors.toList());

        Map<Long, List<Long>> dueFee = new HashMap<>();
        if (feeTypeOfFeePaymentRecord.containsAll(feeTypesOfStudentFeeStructure)) {
            for (FeePaymentRecord feePaymentRecord1 : feePaymentRecords) {
                for (FeePaymentDetail feePaymentDetail : feePaymentRecord1.getFeePaymentDetails()) {
                    List<Long> feeDescriptionIds = map.get(feePaymentDetail.getFeeType().getId());
                    if (!feeDescriptionIds.contains(feePaymentDetail.getFeeDescription().getId())) {
                        if (dueFee.size() > 0) {
                            if (dueFee.containsKey(feePaymentDetail.getFeeType().getId())) {
                                List<Long> values = dueFee.get(feePaymentDetail.getFeeType().getId());
                                values.add(feePaymentDetail.getFeeDescription().getId());
                                dueFee.put(feePaymentDetail.getFeeType().getId(), values);
                            }
                        }
                        dueFee.put(feePaymentDetail.getFeeType().getId(), Arrays.asList(feePaymentDetail.getFeeDescription().getId()));
                    }
                }
            }
        }
        return dueFee;
    }

    private PaytmVM prepareObject(Map<Long, List<Long>> map, Map<Long, List<Long>> studentFeeStructureMap, Student student) {

        PaytmVM paytmVM = new PaytmVM();
        if (map.size() < 1) {
            paytmVM.setErrorCode(PaytmErrorCodes.NO_DUE.getValue());
            return paytmVM;
        } else if (map.size() < studentFeeStructureMap.size()) {
            paytmVM.setErrorCode(PaytmErrorCodes.INVALID_TYPE.getValue());
            return paytmVM;
        } else {
            List<StudentStandard> studentStandard = studentStandardRepository.getByStudentId(student.getId());

            paytmVM.setErrorCode(PaytmErrorCodes.SUCCESS.getValue());
            PaytmVM.StudentDetail studentDetail = new PaytmVM.StudentDetail();
            studentDetail.setAdmissionId(student.getAdmissionId());
            studentDetail.setDateOfBirth(student.getDateOfBirth().toString());
            studentDetail.setFatherName(student.getFatherName());
            studentDetail.setMotherName(student.getMotherName());
            studentDetail.setRollNo(studentStandard.get(0).getRollNo());
            studentDetail.setNote("");
            paytmVM.setStudentDetails(studentDetail);

            List<PaytmVM.FeeTypeDetail> feeTypeDetails = new ArrayList<>();
            for (int i = 0; i < map.size(); i++) {

                PaytmVM.FeeTypeDetail feeTypeDetail = new PaytmVM.FeeTypeDetail();
                feeTypeDetail.setEditable(false);
                feeTypeDetail.setRequired(true);
                feeTypeDetail.setAmount();
                feeTypeDetail.setName();


            }


            return null;
        }
    }*/
}
