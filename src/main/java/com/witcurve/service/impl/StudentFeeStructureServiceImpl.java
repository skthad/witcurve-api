package com.witcurve.service.impl;

import com.witcurve.domain.*;
import com.witcurve.domain.enumeration.FeePaymentType;
import com.witcurve.repository.FeeDetailsRepository;
import com.witcurve.repository.SessionFeeStructureRepository;
import com.witcurve.repository.StudentFeeStructureRepository;
import com.witcurve.repository.StudentStandardRepository;
import com.witcurve.service.StudentFeeStructureService;
import com.witcurve.service.dto.StudentFeeDescriptionDTO;
import com.witcurve.service.dto.StudentFeeStructureDTO;
import com.witcurve.service.dto.StudentFeeTypeDTO;
import com.witcurve.service.mapper.StudentFeeStructureMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentFeeStructureServiceImpl implements StudentFeeStructureService {

    private final Logger log = LoggerFactory.getLogger(StudentFeeStructureServiceImpl.class);

    @Autowired
    StudentFeeStructureRepository studentfeeStructureRepository;

    @Autowired
    StudentFeeStructureMapper studentFeeStructureMapper;

    @Autowired
    FeeDetailsRepository feeDetailsRepository;

    @Autowired
    SessionFeeStructureRepository sessionFeeStructureRepository;

    @Autowired
    StudentStandardRepository studentStandardRepository;

    @Override
    public StudentFeeStructureDTO saveOrUpdate(StudentFeeStructureDTO studentFeeStructureDTO) {
        log.debug("Request to save or update StudentFeeStructure : {} ", studentFeeStructureDTO);
        isValid(studentFeeStructureDTO);
        StudentFeeStructure studentFeeStructure = studentfeeStructureRepository.save(studentFeeStructureMapper.toEntity(studentFeeStructureDTO));
        return studentFeeStructureMapper.toDto(studentFeeStructure);
    }

    @Override
    public StudentFeeStructureDTO getByStudentIdAndSessionId(Long studentId, Long sessionId) {
        log.debug("Request to get studentFeeStructure by studentId and sessionId");
        StudentFeeStructure studentFeeStructure = studentfeeStructureRepository.getByStudentIdAndSessionId(studentId, sessionId);
        List<StudentFeeStructureDTO> studentFeeStructureDTOS = formatStudentFeeStructureDTOs(Arrays.asList(studentFeeStructure));
        return studentFeeStructureDTOS.get(0);
    }

    @Override
    public List<StudentFeeStructureDTO> getByStandardIdAndSessionId(Long standardId, Long sessionId) {
        log.debug("Request to get studentFeeStructure by standardId and sessionId");
        List<StudentFeeStructure> studentFeeStructures = studentfeeStructureRepository.getByStandardIdAndSessionId(standardId, sessionId);
        return formatStudentFeeStructureDTOs(studentFeeStructures);
    }

    @Override
    public StudentFeeStructureDTO getByStudentFeeStructureId(Long studentFeeStructureId) {
        log.debug("Request to get studentFeeStructure by id");
        Optional<StudentFeeStructure> studentFeeStructure = studentfeeStructureRepository.findById(studentFeeStructureId);
        if (!studentFeeStructure.isPresent()) {
            throw new WitcurveException("No record is present with id " + studentFeeStructureId);
        }
        return studentFeeStructureMapper.toDto(studentFeeStructure.get());
    }

    private void isValid(StudentFeeStructureDTO studentFeeStructureDTO) {

        if (studentFeeStructureDTO.getStudentFeeTypes().size() == 0) {
            throw new WitcurveException("Minimum one record of student fee type is require to save ");
        }
        List<SessionFeeStructure> sessionFeeStructures = sessionFeeStructureRepository.findByStudentIdAndSessionId(studentFeeStructureDTO.getStudentId(), studentFeeStructureDTO.getSelectedSessionId());

        if (sessionFeeStructures.size() < 1) {
            throw new WitcurveException("No session fee structure is found with respect to given session and student");
        }

        Map<Long, List<Long>> mapOfFeeTypeAndRequiredFeeDescriptionIds = new HashMap<>();
        Map<Long, List<Long>> mapOfFeeTypeAndAllFeeDescriptionIds = new HashMap<>();
        for (SessionFeeStructure sessionFeeStructure : sessionFeeStructures) {
            Long feeTypeId = sessionFeeStructure.getFeeType().getId();
            List<Long> requireFeeDescriptionIds = new ArrayList<>();
            List<Long> allFeeDescriptionIds = new ArrayList<>();

            for (SessionFeeDescription sessionFeeDescription : sessionFeeStructure.getSessionFeeDescriptions()) {
                allFeeDescriptionIds.add(sessionFeeDescription.getFeeDescription().getId());
                if (sessionFeeDescription.getRequired() == true) {
                    requireFeeDescriptionIds.add(sessionFeeDescription.getFeeDescription().getId());
                }
            }
            mapOfFeeTypeAndRequiredFeeDescriptionIds.put(feeTypeId, requireFeeDescriptionIds);
            mapOfFeeTypeAndAllFeeDescriptionIds.put(feeTypeId, allFeeDescriptionIds);
        }
        List<Long> studentFeeTypeIds = studentFeeStructureDTO.getStudentFeeTypes().stream().map(StudentFeeTypeDTO::getFeeTypeId).collect(Collectors.toList());
        List<Long> sessionFeeTypeIds = mapOfFeeTypeAndRequiredFeeDescriptionIds.keySet().stream().collect(Collectors.toList());

        if (!sessionFeeTypeIds.containsAll(studentFeeTypeIds)) {
            throw new WitcurveException("Given fee type id is not present in session fee type");
        }
        for (StudentFeeTypeDTO studentFeeType : studentFeeStructureDTO.getStudentFeeTypes()) {

            if (studentFeeType.getStudentFeeDescriptions().size() == 0) {
                throw new WitcurveException("Minimum one record of student fee description is require to save ");
            }
            List<Long> studentFeeDescriptionIds = studentFeeType.getStudentFeeDescriptions().stream().map(StudentFeeDescriptionDTO::getFeeDescriptionId).collect(Collectors.toList());
            List<Long> allRequiredFeeDescriptionIds = mapOfFeeTypeAndRequiredFeeDescriptionIds.get(studentFeeType.getFeeTypeId());
            List<Long> allSessionFeeDescriptionIds = mapOfFeeTypeAndAllFeeDescriptionIds.get(studentFeeType.getFeeTypeId());

            if (!studentFeeDescriptionIds.containsAll(allRequiredFeeDescriptionIds)) {
                throw new WitcurveException("All required fee description are not present in student fee structure");
            }
            if (!allSessionFeeDescriptionIds.containsAll(studentFeeDescriptionIds)) {
                throw new WitcurveException("Given fee description id is not present in session fee type");
            }
            for (StudentFeeDescriptionDTO studentFeeDescription : studentFeeType.getStudentFeeDescriptions()) {

                if (studentFeeDescription.getAmount() < 0 || studentFeeDescription.getOneTimeDiscount() < 0) {
                    throw new WitcurveException("Amount and discount must be positive");
                }
                if (studentFeeDescription.getAmount() + studentFeeDescription.getAdjustment() - (studentFeeDescription.getOneTimeDiscount()) < 0) {
                    throw new WitcurveException("Total amount for one or more fee descriptions is negative. Please make sure the total amount for each description is not negative");
                }
            }
        }
    }

    private List<StudentFeeStructureDTO> formatStudentFeeStructureDTOs(List<StudentFeeStructure> studentFeeStructures) {
        List<StudentFeeStructureDTO> studentFeeStructureDTOs = new ArrayList<>();
        for (StudentFeeStructure studentFeeStructure : studentFeeStructures) {
            StudentFeeStructureDTO studentFeeStructureDTO = studentFeeStructureMapper.toDto(studentFeeStructure);

            Double totalAmount = 0.0;
            Double totalOneTimeDiscount = 0.0;
            for (StudentFeeType feeType : studentFeeStructure.getStudentFeeTypes()) {
                Double totalAmountInFeeType = 0.0;
                Double totalOneTimeDiscountInFeeType = 0.0;
                for (StudentFeeDescription feeDescription : feeType.getStudentFeeDescriptions()) {
                    Double feeDescriptionAmt = feeDescription.getAmount() + feeDescription.getAdjustment();
                    Double oneTimeDiscount = feeDescription.getOneTimeDiscount();
                    totalAmountInFeeType = totalAmountInFeeType + feeDescriptionAmt;
                    totalOneTimeDiscountInFeeType = totalOneTimeDiscountInFeeType + oneTimeDiscount;
                }
                totalAmount = totalAmount + totalAmountInFeeType;
                totalOneTimeDiscount = totalOneTimeDiscount + totalOneTimeDiscountInFeeType;
            }
            studentFeeStructureDTO.setTotalAmount(totalAmount);
            studentFeeStructureDTO.setTotalOneTimeDiscount(totalOneTimeDiscount);

            Double totalPaidAmount = 0.0;
            Double totalPaidPenalty = 0.0;
            List<FeePaymentType> feePaymentTypes = new ArrayList<>();

            for (FeePaymentRecord feePaymentRecord : studentFeeStructure.getFeePaymentRecords()) {
                feePaymentTypes.add(feePaymentRecord.getFeePaymentType());

                Double amountPaidInOneRecord = 0.0;
                Double penaltyPaidInOneRecord = 0.0;
                for (FeePaymentDetail feePaymentDetail : feePaymentRecord.getFeePaymentDetails()) {
                    Double paidAmount = feePaymentDetail.getAmount();
                    Double paidPenalty = 0.0;
                    if (feePaymentDetail.getPenalty()) {
                        paidPenalty = feePaymentDetail.getAmount();
                    }
                    amountPaidInOneRecord = amountPaidInOneRecord + paidAmount;
                    penaltyPaidInOneRecord = penaltyPaidInOneRecord + paidPenalty;
                }
                totalPaidAmount = totalPaidAmount + amountPaidInOneRecord;
                totalPaidPenalty = totalPaidPenalty + penaltyPaidInOneRecord;
            }

            studentFeeStructureDTO.setPaidAmount(totalPaidAmount);
            studentFeeStructureDTO.setPaidPenalty(totalPaidPenalty);

            Double dueAmount;
            if (feePaymentTypes.contains(FeePaymentType.FULL_YEAR_PAYMENT)) {
                dueAmount = studentFeeStructureDTO.getTotalAmount() - studentFeeStructureDTO.getTotalOneTimeDiscount()
                    - studentFeeStructureDTO.getPaidAmount() - studentFeeStructureDTO.getPaidPenalty();
            } else {
                dueAmount = studentFeeStructureDTO.getTotalAmount() - studentFeeStructureDTO.getPaidAmount() - studentFeeStructureDTO.getPaidPenalty();
            }
            studentFeeStructureDTO.setDueAmount(dueAmount);
            studentFeeStructureDTOs.add(studentFeeStructureDTO);
        }
        return studentFeeStructureDTOs;
    }
}

