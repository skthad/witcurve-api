package com.witcurve.service.impl;

import com.witcurve.config.ApplicationProperties;
import com.witcurve.domain.*;
import com.witcurve.domain.enumeration.FeePaymentType;
import com.witcurve.domain.enumeration.ModeOfTransaction;
import com.witcurve.domain.enumeration.PaymentRecordType;
import com.witcurve.domain.enumeration.PaytmErrorCodes;
import com.witcurve.repository.*;
import com.witcurve.service.FeePaymentRecordService;
import com.witcurve.service.PaytmCallBackService;
import com.witcurve.service.dto.FeePaymentDetailDTO;
import com.witcurve.service.dto.FeePaymentRecordDTO;
import com.witcurve.service.mapper.FeePaymentDetailMapper;
import com.witcurve.service.util.WitCurveConstants;
import com.witcurve.service.util.WitcurveUtil;
import com.witcurve.web.rest.vm.PaytmStatusCheckVM;
import com.witcurve.web.rest.vm.PaytmVM;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaytmCallBackServiceImpl implements PaytmCallBackService {

    private final Logger log = LoggerFactory.getLogger(PaytmCallBackServiceImpl.class);

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    InstituteRepository instituteRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    StudentFeeStructureRepository studentFeeStructureRepository;

    @Autowired
    FeePaymentRecordRepository feePaymentRecordRepository;

    @Autowired
    AcademicSessionRepository academicSessionRepository;

    @Autowired
    StudentStandardRepository studentStandardRepository;

    @Autowired
    FeeDetailsRepository feeDetailsRepository;

    @Autowired
    FeePaymentRecordService feePaymentRecordService;

    @Autowired
    FeePaymentDetailMapper feePaymentDetailMapper;


    @Override
    public PaytmVM getStudentFee(String instituteName, String admissionId, String type) {

        PaytmVM paytmVM = new PaytmVM();

        String[] paytmIps = applicationProperties.paytm.getCommunicationIps().split(",");
        Institute institute = instituteRepository.findInstituteByName(instituteName);
        //check what if institute is null
        Student student = studentRepository.getByInstituteIdAndAdmissionId(institute.getId(), admissionId);
        List<StudentStandard> studentStandards = studentStandardRepository.getByStudentId(student.getId());
        AcademicSession session = academicSessionRepository.nearestActiveSessionToDate(student.getSchoolInfo().getId(), LocalDate.now());

        if (!Arrays.stream(paytmIps).anyMatch(i -> i.equals(getClientIp(request)))) {
            paytmVM.setErrorCode(PaytmErrorCodes.INVALID_IP_ADDRESS_FOR_COMMUNICATION.getValue());
        } else if (instituteName == null || admissionId == null || type == null) {
            paytmVM.setErrorCode(PaytmErrorCodes.MISSING_FIELDS.getValue());
        } else if (institute == null) {
            paytmVM.setErrorCode(PaytmErrorCodes.INVALID_INSTITUTE_NAME.getValue());
        } else if (!EnumUtils.isValidEnum(FeePaymentType.class, type.toUpperCase())) {
            paytmVM.setErrorCode(PaytmErrorCodes.INVALID_TYPE.getValue());
        } else if (student == null) {
            paytmVM.setErrorCode(PaytmErrorCodes.INVALID_ENROLLMENT_NUMBERS.getValue());
        } else {
            StudentFeeStructure studentFeeStructure = studentFeeStructureRepository.getByStudentIdAndSessionId(student.getId(), session.getId());

            FeePaymentType feePaymentType = FeePaymentType.valueOf(type);
            switch (feePaymentType) {
                case FULL_YEAR_PAYMENT:
                    List<FeePaymentRecord> feePaymentRecords = feePaymentRecordRepository.getByStudentAndSessionIdAndType(student.getId(), session.getId(), feePaymentType);

                    Double totalAmount = 0.0;
                    for (StudentFeeType studentFeeType : studentFeeStructure.getStudentFeeTypes()) {
                        for (StudentFeeDescription feeDescription : studentFeeType.getStudentFeeDescriptions()) {
                            totalAmount = totalAmount + feeDescription.getAmount() + (feeDescription.getAdjustment() - (feeDescription.getOneTimeDiscount()));

                        }
                    }
                    if (feePaymentRecords.size() == 1) {
                        paytmVM.setErrorCode(PaytmErrorCodes.NO_DUE.getValue());
                    } else {
                        paytmVM.setErrorCode(PaytmErrorCodes.SUCCESS.getValue());
                        paytmVM.setTotalAmount(totalAmount);
                        PaytmVM.StudentDetail studentDetail = new PaytmVM.StudentDetail();
                        studentDetail.setFatherName(student.getFatherName());
                        studentDetail.setDateOfBirth(student.getDateOfBirth().toString());
                        studentDetail.setNote("");
                        studentDetail.setMotherName(student.getMotherName());
                        if (student.getMiddleName() == null) {
                            studentDetail.setStudentName(student.getFirstName() + " " + student.getLastName());
                        } else {
                            studentDetail.setStudentName(student.getFirstName() + " " + student.getMiddleName() + " " + student.getLastName());
                        }
                        studentDetail.setRollNo(studentStandards.get(0).getRollNo());
                        studentDetail.setAdmissionId(student.getAdmissionId());
                        studentDetail.setStandard(studentStandards.get(0).getStandard().getGrade() + " " + studentStandards.get(0).getStandard().getSection());
                        paytmVM.setStudentDetails(studentDetail);
                    }
            }

        }
        return null;

    }

    @Override
    public Map<String, String> save(PaytmStatusCheckVM paytmStatusCheckVM, String orderId, String admissionId, String instituteName) {
        log.debug("Request to check status with given  order Id, enrollment no, instituteName : ", orderId, admissionId, instituteName);
        //todo - while integrating this api to fee modules - use a check method which returns error code.

        //todo - missing cases, some cases are not covered, these should be checked when integrated with fee modules
        //todo check if the fee type and fee description matches properly not just description alone
        //todo since each item will have separate call, make the fee record properly to
        // make sure apis with same order id are attached with same receipt number
        //todo handle the case when one record is missing in full year payment
        //todo handle the case when required fee has not be record because of some reason.
        //todo Internal server errorcode use left

        Map<String, String> responseMap = new HashMap<>();
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
                    FeePaymentRecordDTO feePaymentRecord1 = feePaymentRecordService.saveOrUpdate(feePaymentRecordObj, ModeOfTransaction.SYSTEM);

                    responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.SUCCESS.getValue()));
                    responseMap.put("transactionStatus", "success");
                    responseMap.put("receiptId", feePaymentRecord1.getOrderId());
                }

            }
        }
        return responseMap;
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

    private FeePaymentRecordDTO prepareFeePaymentObj(String orderId, Long studentFeeStructureId, PaytmStatusCheckVM
        paytmStatusCheckVM, String feeType, String feeDescription) {
        FeePaymentRecordDTO feePaymentRecordDTO = new FeePaymentRecordDTO();

        FeePaymentRecord feePaymentRecord = feePaymentRecordRepository.getByOrderId(orderId);

        if (feePaymentRecord != null) {
            if (feeDescription.equals("Penalty")) {
                Double penaltyAmount = feePaymentRecord.getPenaltyAmount() + (Double.valueOf(paytmStatusCheckVM.getAmount()));
                feePaymentRecord.setPenaltyAmount(penaltyAmount);
            } else {
                List<FeePaymentDetail> alreadyExistRecords = feePaymentRecord.getFeePaymentDetails();

                FeePaymentDetailDTO feePaymentDetailDTO = new FeePaymentDetailDTO();
                feePaymentDetailDTO.setAmount(Double.valueOf(paytmStatusCheckVM.getAmount()));
                feePaymentDetailDTO.setItemId(paytmStatusCheckVM.getItemId());
                feePaymentDetailDTO.setTransactionDate(LocalDate.parse(paytmStatusCheckVM.getTransactionDate()));

                FeeDetails feeDetailOfDescriptionType = feeDetailsRepository.findFeeDetailsByName(feeDescription);
                feePaymentDetailDTO.setFeeDescriptionId(feeDetailOfDescriptionType.getId());

                FeeDetails feeDetailOfFeeType = feeDetailsRepository.findFeeDetailsByName(feeType);
                feePaymentDetailDTO.setFeeTypeId(feeDetailOfFeeType.getId());
                feePaymentRecordDTO.setTotalAmount(Double.valueOf(paytmStatusCheckVM.getAmount()));
                feePaymentRecordDTO.setFeePaymentDetails(Arrays.asList(feePaymentDetailDTO));

                alreadyExistRecords.add(feePaymentDetailMapper.toEntity(feePaymentDetailDTO));

                if (feePaymentRecord.getTotalAmount() != null) {
                    Double amount = feePaymentRecord.getTotalAmount();
                    feePaymentRecord.setTotalAmount(amount + (Double.valueOf(paytmStatusCheckVM.getAmount())));
                } else {
                    feePaymentRecord.setTotalAmount(Double.valueOf(paytmStatusCheckVM.getAmount()));
                }
            }
        }
        feePaymentRecordDTO.setTransactionId(orderId);
        feePaymentRecordDTO.setType(PaymentRecordType.PAYTM);
        feePaymentRecordDTO.setOrderId(RandomStringUtils.randomAlphanumeric(8));
        feePaymentRecordDTO.setStudentFeeStructureId(studentFeeStructureId);
        feePaymentRecordDTO.setFeePaymentType(FeePaymentType.valueOf(paytmStatusCheckVM.getType()));

        if (feeDescription.equals("Penalty")) {
            feePaymentRecordDTO.setPenaltyAmount(Double.valueOf(paytmStatusCheckVM.getAmount()));
        } else {
            FeePaymentDetailDTO feePaymentDetailDTO = new FeePaymentDetailDTO();
            feePaymentDetailDTO.setAmount(Double.valueOf(paytmStatusCheckVM.getAmount()));
            feePaymentDetailDTO.setItemId(paytmStatusCheckVM.getItemId());
            feePaymentDetailDTO.setTransactionDate(LocalDate.parse(paytmStatusCheckVM.getTransactionDate()));

            FeeDetails feeDetailOfDescriptionType = feeDetailsRepository.findFeeDetailsByName(feeDescription);
            feePaymentDetailDTO.setFeeDescriptionId(feeDetailOfDescriptionType.getId());

            FeeDetails feeDetailOfFeeType = feeDetailsRepository.findFeeDetailsByName(feeType);
            feePaymentDetailDTO.setFeeTypeId(feeDetailOfFeeType.getId());
            feePaymentRecordDTO.setTotalAmount(Double.valueOf(paytmStatusCheckVM.getAmount()));
            feePaymentRecordDTO.setFeePaymentDetails(Arrays.asList(feePaymentDetailDTO));
        }
        return feePaymentRecordDTO;
    }


}
