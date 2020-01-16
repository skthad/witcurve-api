package com.witcurve.service.impl;

import com.witcurve.config.ApplicationProperties;
import com.witcurve.domain.*;
import com.witcurve.domain.enumeration.*;
import com.witcurve.repository.*;
import com.witcurve.service.FeePaymentRecordService;
import com.witcurve.service.PaytmCallBackService;
import com.witcurve.service.dto.FeePaymentDetailDTO;
import com.witcurve.service.dto.FeePaymentRecordDTO;
import com.witcurve.service.mapper.FeePaymentDetailMapper;
import com.witcurve.service.mapper.FeePaymentRecordMapper;
import com.witcurve.service.util.WitCurveConstants;
import com.witcurve.service.util.WitcurveUtil;
import com.witcurve.web.rest.vm.PaytmStatusCheckVM;
import com.witcurve.web.rest.vm.PaytmVM;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaytmCallBackServiceImpl implements PaytmCallBackService {

    private final Logger log = LoggerFactory.getLogger(PaytmCallBackServiceImpl.class);

    @Autowired
    private ApplicationProperties applicationProperties;

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

    @Autowired
    FeePaymentRecordMapper feePaymentRecordMapper;


    @Override
    public PaytmVM getStudentFee(String instituteName, String admissionId, String type, HttpServletRequest request) {

        PaytmVM paytmVM = new PaytmVM();
        Student student = null;
        List<StudentStandard> studentStandards = null;
        AcademicSession session = null;
        String[] paytmIps = null;
        StudentFeeStructure studentFeeStructure = null;

        if (!applicationProperties.paytm.getCommunicationIps().equals("")) {
            paytmIps = applicationProperties.paytm.getCommunicationIps().split(",");
        }
        Institute institute = instituteRepository.findInstituteByName(instituteName);
        if (institute != null) {
            List<Student> students = studentRepository.getByInstituteIdAndAdmissionId(institute.getId(), admissionId);
            if (students.size() != 0) {
                student = students.get(0);
            }
        }
        if (student != null) {
            studentStandards = studentStandardRepository.getByStudentId(student.getId());
            session = academicSessionRepository.nearestActiveSessionToDate(student.getSchoolInfo().getId(), LocalDate.now());
            studentFeeStructure = studentFeeStructureRepository.getByStudentIdAndSessionId(student.getId(), session.getId());
        }


        if (paytmIps != null) {
            if (!Arrays.stream(paytmIps).anyMatch(i -> i.equals(getClientIp(request)))) {
                paytmVM.setErrorCode(PaytmErrorCodes.INVALID_IP_ADDRESS_FOR_COMMUNICATION.getValue());
            }
        } else if (instituteName == null || admissionId == null || type == null) {
            paytmVM.setErrorCode(PaytmErrorCodes.MISSING_FIELDS.getValue());
        } else if (institute == null) {
            paytmVM.setErrorCode(PaytmErrorCodes.INVALID_INSTITUTE_NAME.getValue());
        } else if (!type.equals(FeePaymentType.FULL_YEAR_PAYMENT.toString()) && !type.equals(FeePaymentType.OUTSTANDING_FEE.toString())) {
            paytmVM.setErrorCode(PaytmErrorCodes.INVALID_TYPE.getValue());
        } else if (student == null) {
            paytmVM.setErrorCode(PaytmErrorCodes.INVALID_ENROLLMENT_NUMBERS.getValue());
        } else if (studentFeeStructure == null) {
            paytmVM.setErrorCode(PaytmErrorCodes.NO_DUE.getValue());
        } else {
            FeePaymentType feePaymentType;
            if (type.equals(FeePaymentType.FULL_YEAR_PAYMENT.toString()))
                feePaymentType = FeePaymentType.FULL_YEAR_PAYMENT;
            else {
                feePaymentType = FeePaymentType.OUTSTANDING_FEE;
            }

            PaytmVM.StudentDetail studentDetail = new PaytmVM.StudentDetail();
            studentDetail.setFatherName(student.getFatherName());
            studentDetail.setDateOfBirth(student.getDateOfBirth().toString());
            studentDetail.setNote("");
            studentDetail.setMotherName(student.getMotherName());
            studentDetail.setStudentName(WitcurveUtil.getStudentName(student.getFirstName(), student.getMiddleName(), student.getLastName()));
            studentDetail.setRollNo(studentStandards.get(0).getRollNo());
            studentDetail.setAdmissionId(student.getAdmissionId());
            studentDetail.setStandard(studentStandards.get(0).getStandard().getGrade() + " " + studentStandards.get(0).getStandard().getSection());

            switch (feePaymentType) {
                case FULL_YEAR_PAYMENT:
                    //change it to check if any on er cord exists
                    List<FeePaymentRecord> feePaymentRecords = feePaymentRecordRepository.getByStudentAndSessionId(student.getId(), session.getId());

                    Double totalAmount = 0.0;
                    for (StudentFeeType studentFeeType : studentFeeStructure.getStudentFeeTypes()) {
                        for (StudentFeeDescription feeDescription : studentFeeType.getStudentFeeDescriptions()) {
                            totalAmount = totalAmount + feeDescription.getAmount() + feeDescription.getAdjustment() - feeDescription.getOneTimeDiscount();

                        }
                    }
                    if (feePaymentRecords.size() == 1) {
                        paytmVM.setErrorCode(PaytmErrorCodes.NO_DUE.getValue());
                    } else {
                        paytmVM.setErrorCode(PaytmErrorCodes.SUCCESS.getValue());
                        paytmVM.setFullYearAmount(totalAmount);
                        paytmVM.setStudentDetails(studentDetail);
                    }
                    break;
                case OUTSTANDING_FEE:

                    feePaymentRecords = feePaymentRecordRepository.getByStudentAndSessionId(student.getId(), session.getId());

                    List<String> paidFee = new ArrayList<>();
                    for (FeePaymentRecord feePaymentRecord : feePaymentRecords) {
                        for (FeePaymentDetail feePaymentDetail : feePaymentRecord.getFeePaymentDetails()) {
                            String feeName;
                            if (feePaymentDetail.getPenalty()) {
                                feeName = "Penalty" + "(" + feePaymentDetail.getFeeType().getName() + ")";
                            } else {
                                feeName = feePaymentDetail.getFeeDescription().getName() + "(" + feePaymentDetail.getFeeType().getName() + ")";
                            }
                            paidFee.add(feeName);
                        }
                    }

                    List<PaytmVM.FeeTypeDetail> feeTypeDetails = new ArrayList<>();
                    for (StudentFeeType studentFeeType : studentFeeStructure.getStudentFeeTypes()) {
                        List<PaytmVM.FeeTypeDetail> feeTypeDetailsForSingleFeeType = new ArrayList<>();
                        for (StudentFeeDescription studentFeeDescription : studentFeeType.getStudentFeeDescriptions()) {
                            String name = studentFeeDescription.getFeeDescription().getName() + "(" + studentFeeType.getFeeType().getName() + ")";

                            if (!paidFee.contains(name)) {
                                PaytmVM.FeeTypeDetail feeTypeDetail = new PaytmVM.FeeTypeDetail();
                                feeTypeDetail.setAmount(studentFeeDescription.getAmount() + (studentFeeDescription.getAdjustment()));
                                feeTypeDetail.setName(name);
                                feeTypeDetail.setEditable(false);
                                if (studentFeeType.getDueDate().compareTo(LocalDate.now()) < 0) {
                                    feeTypeDetail.setRequired(true);
                                } else {
                                    feeTypeDetail.setRequired(studentFeeDescription.getRequired());
                                }
                                feeTypeDetailsForSingleFeeType.add(feeTypeDetail);
                            }
                        }
                        if (studentFeeType.getDueDate().compareTo(LocalDate.now()) < 0) {
                            if (feeTypeDetailsForSingleFeeType.size() > 0) {
                                if (!paidFee.contains("Penalty(" + studentFeeType.getFeeType().getName() + ")")) {
                                    PaytmVM.FeeTypeDetail feeTypeDetail = new PaytmVM.FeeTypeDetail();
                                    feeTypeDetail.setRequired(true);
                                    feeTypeDetail.setAmount(studentFeeType.getPenalty());
                                    feeTypeDetail.setName("Penalty(" + studentFeeType.getFeeType().getName() + ")");
                                    feeTypeDetail.setEditable(false);
                                    feeTypeDetailsForSingleFeeType.add(feeTypeDetail);
                                }
                                feeTypeDetails.addAll(feeTypeDetailsForSingleFeeType);
                            }
                        } else {
                            if (feeTypeDetailsForSingleFeeType.size() > 0) {
                                feeTypeDetails.addAll(feeTypeDetailsForSingleFeeType);
                                break;
                            }
                        }
                    }
                    if (feeTypeDetails.size() == 0) {
                        paytmVM.setErrorCode(PaytmErrorCodes.NO_DUE.getValue());
                    } else {
                        paytmVM.setStudentDetails(studentDetail);
                        paytmVM.setFeeTypeDetails(feeTypeDetails);
                        paytmVM.setErrorCode(PaytmErrorCodes.SUCCESS.getValue());
                    }
            }
        }
        return paytmVM;
    }


    @Override
    public Map<String, String> save(PaytmStatusCheckVM paytmStatusCheckVM, String orderId, String
        admissionId, String instituteName, HttpServletRequest request) {
        log.debug("Request to check status with given  order Id, enrollment no, instituteName : ", orderId, admissionId, instituteName);

        Map<String, String> responseMap = new HashMap<>();
        String[] paytmIps = null;
        Student student = null;
        AcademicSession session = null;

        if (!applicationProperties.paytm.getCommunicationIps().equals("")) {
            paytmIps = applicationProperties.paytm.getCommunicationIps().split(",");
        }

        Institute institute = instituteRepository.findInstituteByName(instituteName);
        if (institute != null) {
            List<Student> students = studentRepository.getByInstituteIdAndAdmissionId(institute.getId(), admissionId);
            if (students.size() != 0) {
                student = students.get(0);
            }
        }
        if (student != null) {
            session = academicSessionRepository.nearestActiveSessionToDate(student.getSchoolInfo().getId(), LocalDate.now());
        }

        Double amount = null;
        if (paytmStatusCheckVM.getAmount() != null) {
            try {
                amount = Double.parseDouble(paytmStatusCheckVM.getAmount());
            } catch (NumberFormatException e) {
                amount = null;
            }
        }

        LocalDate transactionDate = null;
        if (paytmStatusCheckVM.getTransactionDate() != null) {
            try {
                transactionDate = WitcurveUtil.getLocalDate(paytmStatusCheckVM.getTransactionDate(), WitCurveConstants.DEFAULT_DATE_FORMAT);
            } catch (DateTimeParseException e) {
                transactionDate = null;
            }
        }
        if (orderId == null || admissionId == null || instituteName == null) {
            responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.MISSING_FIELDS.getValue()));
        } else if (institute == null) {
            responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_INSTITUTE_NAME.getValue()));
        } else if (student == null) {
            responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_ENROLLMENT_NUMBERS.getValue()));
        } else if (paytmIps != null) {
            if (!Arrays.stream(paytmIps).anyMatch(i -> i.equals(getClientIp(request)))) {
                responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_IP_ADDRESS_FOR_COMMUNICATION.getValue()));
            }
        } else {
            if (paytmStatusCheckVM.getName() == null || paytmStatusCheckVM.getTransactionDate() == null || paytmStatusCheckVM.getAmount() == null
                || paytmStatusCheckVM.getType() == null) {
                responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.MISSING_FIELDS.getValue()));
            } else if (paytmStatusCheckVM.getType().equals(FeePaymentType.OUTSTANDING_FEE.toString()) && paytmStatusCheckVM.getItemId() == null) {
                responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.MISSING_FIELDS.getValue()));
            } else if (transactionDate == null) {
                responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_TRANSACTION_DATE.getValue()));
            } else if (!paytmStatusCheckVM.getType().equals(FeePaymentType.FULL_YEAR_PAYMENT.toString()) && !paytmStatusCheckVM.getType().equals(FeePaymentType.OUTSTANDING_FEE.toString())) {
                responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_TYPE.getValue()));
            } else if (amount == null) {
                responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_AMOUNT.getValue()));
            } else {


                FeePaymentType feePaymentType;
                if (paytmStatusCheckVM.getType().equals(FeePaymentType.FULL_YEAR_PAYMENT.toString()))
                    feePaymentType = FeePaymentType.FULL_YEAR_PAYMENT;
                else {
                    feePaymentType = FeePaymentType.OUTSTANDING_FEE;
                }

                StudentFeeStructure studentFeeStructure = studentFeeStructureRepository.getByStudentIdAndSessionId(student.getId(), session.getId());

                FeePaymentRecord existingFeePaymentRecord;
                if (feePaymentType.equals(FeePaymentType.FULL_YEAR_PAYMENT)) {
                    List<FeePaymentRecord> feePaymentRecords = feePaymentRecordRepository.getByStudentIdIdAndType(student.getId(), feePaymentType);
                    if (feePaymentRecords.size() == 0) {
                        existingFeePaymentRecord = null;
                    } else {
                        existingFeePaymentRecord = feePaymentRecords.get(0);
                    }
                } else {
                    existingFeePaymentRecord = feePaymentRecordRepository.getByOrderIdAndItemIdAndType(orderId, paytmStatusCheckVM.getItemId(), feePaymentType);
                }
                List<FeePaymentRecord> feePaymentRecords = feePaymentRecordRepository.getByStudentAndSessionIdAndType(student.getId(), session.getId(), feePaymentType);

                Map<String, String> mapOfFeeIncludingPenalty = new HashMap<>();
                Map<String, String> mapOfPaidFeeExcludingPenalties = new HashMap<>();
                Map<String, String> mapOfAllFeeExcludingPenalties = new HashMap<>();
                Map<String, String> mapOfPaidPenalties = new HashMap<>();
                Map<String, String> mapOfAllPaidFee = new HashMap<>();
                Double fullYearPaymentAmount = 0.0;

                if (feePaymentRecords != null) {
                    for (FeePaymentRecord feePaymentRecord : feePaymentRecords) {
                        for (FeePaymentDetail feePaymentDetail : feePaymentRecord.getFeePaymentDetails()) {
                            if (!feePaymentDetail.getPenalty()) {
                                String fee = feePaymentDetail.getFeeDescription().getName() + "(" + feePaymentDetail.getFeeType().getName() + ")";
                                Double feeMoney = feePaymentDetail.getAmount();
                                mapOfPaidFeeExcludingPenalties.put(fee, feeMoney.toString());
                            } else {
                                String fee = "Penalty(" + feePaymentDetail.getFeeType().getName() + ")";
                                mapOfPaidPenalties.put(fee, feePaymentDetail.getAmount().toString());
                            }
                        }
                    }
                    mapOfAllPaidFee.putAll(mapOfPaidFeeExcludingPenalties);
                    mapOfAllPaidFee.putAll(mapOfPaidPenalties);
                }
                Map<String, String> mapOfPenalties = new HashMap<>();
                for (StudentFeeType studentFeeType : studentFeeStructure.getStudentFeeTypes()) {
                    for (StudentFeeDescription feeDescriptions : studentFeeType.getStudentFeeDescriptions()) {
                        String name = feeDescriptions.getFeeDescription().getName() + "(" + studentFeeType.getFeeType().getName() + ")";
                        Double finalAmount = feeDescriptions.getAmount() + (feeDescriptions.getAdjustment());

                        fullYearPaymentAmount = fullYearPaymentAmount + feeDescriptions.getAmount() + (feeDescriptions.getAdjustment()) - (feeDescriptions.getOneTimeDiscount());
                        mapOfAllFeeExcludingPenalties.put(name.trim(), finalAmount.toString());
                        if (studentFeeType.getPenalty() != null) {
                            String penalty = "Penalty" + "(" + studentFeeType.getFeeType().getName() + ")";
                            mapOfPenalties.put(penalty, studentFeeType.getPenalty().toString());
                        }

                    }
                }
                mapOfFeeIncludingPenalty.putAll(mapOfAllFeeExcludingPenalties);
                mapOfFeeIncludingPenalty.putAll(mapOfPenalties);

                List<String> feeNames = mapOfFeeIncludingPenalty.keySet().stream().collect(Collectors.toList());
                Set<String> paidFees = mapOfAllPaidFee.keySet();
                if (studentFeeStructure == null) {
                    responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.NO_DUE.getValue()));
                } else if (feePaymentType.equals(FeePaymentType.OUTSTANDING_FEE) && mapOfPaidFeeExcludingPenalties.size() == mapOfAllFeeExcludingPenalties.size()) {
                    responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.NO_DUE.getValue()));
                } else if (feePaymentType.equals(FeePaymentType.OUTSTANDING_FEE) && !feeNames.contains(paytmStatusCheckVM.getName())) {
                    responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_FEE_NAME.getValue()));
                } else if (feePaymentType.equals(FeePaymentType.OUTSTANDING_FEE) && paidFees.contains(paytmStatusCheckVM.getName())) {
                    responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.RECORD_ALREADY_EXIST.getValue()));
                } else if (feePaymentType.equals(FeePaymentType.FULL_YEAR_PAYMENT) && existingFeePaymentRecord != null && existingFeePaymentRecord.getTransactionId().equals(orderId)) {
                    responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.RECORD_ALREADY_EXIST.getValue()));
                } else if (feePaymentType.equals(FeePaymentType.FULL_YEAR_PAYMENT) && existingFeePaymentRecord != null && !existingFeePaymentRecord.getTransactionId().equals(orderId)) {
                    responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.NO_DUE.getValue()));
                } else if (feePaymentType.equals(FeePaymentType.OUTSTANDING_FEE) && !Double.valueOf(paytmStatusCheckVM.getAmount()).equals(Double.valueOf(mapOfFeeIncludingPenalty.get(paytmStatusCheckVM.getName())))) {
                    responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_AMOUNT.getValue()));
                } else if (paytmStatusCheckVM.getType().equals(FeePaymentType.FULL_YEAR_PAYMENT.toString()) && !Double.valueOf(paytmStatusCheckVM.getAmount()).equals(fullYearPaymentAmount)) {
                    responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INVALID_AMOUNT.getValue()));
                } else if (feePaymentType.equals(FeePaymentType.OUTSTANDING_FEE) && existingFeePaymentRecord != null) {
                    responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.RECORD_ALREADY_EXIST.getValue()));
                } else {
                    FeePaymentRecordDTO feePaymentRecordDTO = prepareFeePaymentObjAndSave(orderId, studentFeeStructure, paytmStatusCheckVM, feePaymentType);
                    responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.SUCCESS.getValue()));
                    responseMap.put("transactionStatus", "success");
                    responseMap.put("receiptId", feePaymentRecordDTO.getOrderId());
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

    private FeePaymentRecordDTO prepareFeePaymentObjAndSave(String orderId, StudentFeeStructure
        studentFeeStructure, PaytmStatusCheckVM paytmStatusCheckVM, FeePaymentType type) {

        FeePaymentRecordDTO feePaymentRecordDTO = new FeePaymentRecordDTO();

        if (type.equals(FeePaymentType.OUTSTANDING_FEE)) {

            String[] splitStrings = paytmStatusCheckVM.getName().split("\\(");
            String feeDescriptionName = splitStrings[0];
            String feeTypeName = splitStrings[1].substring(0, splitStrings[1].length() - 1);

            FeeDetails feeType = feeDetailsRepository.findByNameAndSchoolInfoAndType(feeTypeName, studentFeeStructure.getStudent().getSchoolInfo().getId(), FeeDetailsType.FEE_TYPE);

            FeeDetails feeDescription = null;
            if (!feeDescriptionName.trim().equals("Penalty")) {
                feeDescription = feeDetailsRepository.findByNameAndSchoolInfoAndType(feeDescriptionName, studentFeeStructure.getStudent().getSchoolInfo().getId(), FeeDetailsType.FEE_DESCRIPTION);
            }

            FeePaymentRecord feePaymentRecord = feePaymentRecordRepository.getByOrderIdAndType(orderId, type);

            if (feePaymentRecord != null) {

                List<FeePaymentDetail> alreadyExistRecords = feePaymentRecord.getFeePaymentDetails();

                FeePaymentDetailDTO feePaymentDetailDTO = new FeePaymentDetailDTO();
                feePaymentDetailDTO.setAmount(Double.valueOf(paytmStatusCheckVM.getAmount()));
                feePaymentDetailDTO.setItemId(paytmStatusCheckVM.getItemId());
                feePaymentDetailDTO.setTransactionDate(LocalDate.parse(paytmStatusCheckVM.getTransactionDate()));
                feePaymentDetailDTO.setFeeTypeId(feeType.getId());

                if (!feeDescriptionName.trim().equals("Penalty")) {
                    feePaymentDetailDTO.setPenalty(false);
                    feePaymentDetailDTO.setFeeDescriptionId(feeDescription.getId());
                } else {
                    feePaymentDetailDTO.setPenalty(true);

                }
                alreadyExistRecords.add(feePaymentDetailMapper.toEntity(feePaymentDetailDTO));
                feePaymentRecordDTO = feePaymentRecordMapper.toDto(feePaymentRecord);
            } else {
                feePaymentRecordDTO.setTransactionId(orderId);
                feePaymentRecordDTO.setType(PaymentRecordType.PAYTM);
                feePaymentRecordDTO.setOrderId(RandomStringUtils.randomAlphanumeric(8));
                feePaymentRecordDTO.setStudentFeeStructureId(studentFeeStructure.getId());
                feePaymentRecordDTO.setFeePaymentType(type);


                FeePaymentDetailDTO feePaymentDetailDTO = new FeePaymentDetailDTO();
                feePaymentDetailDTO.setAmount(Double.valueOf(paytmStatusCheckVM.getAmount()));
                feePaymentDetailDTO.setItemId(paytmStatusCheckVM.getItemId());
                feePaymentDetailDTO.setTransactionDate(LocalDate.parse(paytmStatusCheckVM.getTransactionDate()));
                feePaymentDetailDTO.setFeeTypeId(feeType.getId());
                feePaymentRecordDTO.setTotalAmount(Double.valueOf(paytmStatusCheckVM.getAmount()));

                if (feeDescriptionName.trim().equals("Penalty")) {
                    feePaymentDetailDTO.setPenalty(true);
                } else {
                    feePaymentDetailDTO.setPenalty(false);
                    feePaymentDetailDTO.setFeeDescriptionId(feeDescription.getId());
                }
                feePaymentRecordDTO.setFeePaymentDetails(Arrays.asList(feePaymentDetailDTO));
                feePaymentRecordDTO = feePaymentRecordService.saveOrUpdate(feePaymentRecordDTO, ModeOfTransaction.SYSTEM);
            }
        } else if (type.equals(FeePaymentType.FULL_YEAR_PAYMENT)) {
            feePaymentRecordDTO.setTransactionId(orderId);
            feePaymentRecordDTO.setType(PaymentRecordType.PAYTM);
            feePaymentRecordDTO.setOrderId(RandomStringUtils.randomAlphanumeric(8));
            feePaymentRecordDTO.setStudentFeeStructureId(studentFeeStructure.getId());
            feePaymentRecordDTO.setFeePaymentType(type);

            List<FeePaymentDetailDTO> feePaymentDetailDTOS = new ArrayList<>();
            for (StudentFeeType studentFeeType : studentFeeStructure.getStudentFeeTypes()) {
                for (StudentFeeDescription studentFeeDescription : studentFeeType.getStudentFeeDescriptions()) {
                    FeePaymentDetailDTO feePaymentDetailDTO = new FeePaymentDetailDTO();
                    feePaymentDetailDTO.setAmount(studentFeeDescription.getAmount() + studentFeeDescription.getAdjustment() - studentFeeDescription.getOneTimeDiscount());
                    feePaymentDetailDTO.setItemId(paytmStatusCheckVM.getItemId());
                    feePaymentDetailDTO.setTransactionDate(LocalDate.parse(paytmStatusCheckVM.getTransactionDate()));
                    feePaymentDetailDTO.setPenalty(false);
                    feePaymentDetailDTO.setFeeDescriptionId(studentFeeDescription.getFeeDescription().getId());
                    feePaymentDetailDTO.setFeeTypeId(studentFeeType.getFeeType().getId());
                    feePaymentDetailDTOS.add(feePaymentDetailDTO);
                }
            }
            feePaymentRecordDTO.setTotalAmount(Double.valueOf(paytmStatusCheckVM.getAmount()));
            feePaymentRecordDTO.setFeePaymentDetails(feePaymentDetailDTOS);
            feePaymentRecordDTO = feePaymentRecordService.saveOrUpdate(feePaymentRecordDTO, ModeOfTransaction.SYSTEM);
        }
        return feePaymentRecordDTO;
    }
}
