package com.witcurve.service.impl;

import com.witcurve.domain.FeeDetails;
import com.witcurve.domain.StudentFeeType;
import com.witcurve.domain.enumeration.FeeDetailsType;
import com.witcurve.repository.FeeDetailsRepository;
import com.witcurve.repository.StudentFeeTypeRepository;
import com.witcurve.service.StudentFeeTypeService;
import com.witcurve.service.dto.StudentFeeDescriptionDTO;
import com.witcurve.service.dto.StudentFeeTypeDTO;
import com.witcurve.service.mapper.StudentFeeTypeMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentFeeTypeServiceImpl implements StudentFeeTypeService {

    @Autowired
    FeeDetailsRepository feeDetailsRepository;

    @Autowired
    StudentFeeTypeRepository studentFeeTypeRepository;

    @Autowired
    StudentFeeTypeMapper studentFeeTypeMapper;

    @Override
    public StudentFeeTypeDTO saveOrUpdate(StudentFeeTypeDTO studentFeeTypeDTO) {
        Optional<FeeDetails> feeDetails = feeDetailsRepository.findById(studentFeeTypeDTO.getFeeTypeId());
        if (!feeDetails.isPresent()) {
            throw new WitcurveException("No fee detail is present with given id : {} " + studentFeeTypeDTO.getFeeTypeId());
        }
        if (!feeDetails.get().getType().equals(FeeDetailsType.FEE_TYPE)) {
            throw new WitcurveException("Given fee detail id is not of fee type");
        }
        if (studentFeeTypeDTO.getPenalty() != null) {
            if (studentFeeTypeDTO.getDueDate() == null) {
                throw new WitcurveException("Due date require when penalty is given");
            }
        }
        if (studentFeeTypeDTO.getStudentFeeDescriptions().size() == 0) {
            throw new WitcurveException("Minimum one record of student fee description is require");
        }
        List<Long> feeDescriptionIds = new ArrayList<>();
        for (StudentFeeDescriptionDTO studentFeeDescription : studentFeeTypeDTO.getStudentFeeDescriptions()) {
            feeDescriptionIds.add(studentFeeDescription.getFeeDescriptionId());
        }
        List<FeeDetails> FeeDescriptions = feeDetailsRepository.findBySchoolInfoIdAndType(feeDetails.get().getSchoolInfo().getId(), FeeDetailsType.FEE_DESCRIPTION);
        List<Long> allFeeDescriptionIds = new ArrayList<>();
        for (FeeDetails feeDescription : FeeDescriptions) {
            allFeeDescriptionIds.add(feeDescription.getId());
        }
        for (Long feeDescriptionId : feeDescriptionIds) {
            if (!allFeeDescriptionIds.contains(feeDescriptionId)) {
                throw new WitcurveException("Given fee description id is not of fee description type");
            }
        }
        StudentFeeType feeType = studentFeeTypeMapper.toEntity(studentFeeTypeDTO);
        StudentFeeType studentFeeType = studentFeeTypeRepository.save(feeType);
        return studentFeeTypeMapper.toDto(studentFeeType);
    }
}


