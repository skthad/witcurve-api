package com.witcurve.service.impl;

import com.witcurve.domain.FeeDetails;
import com.witcurve.domain.StudentFeeType;
import com.witcurve.domain.enumeration.FeeDetailsType;
import com.witcurve.repository.FeeDetailsRepository;
import com.witcurve.repository.StudentFeeTypeRepository;
import com.witcurve.service.StudentFeeTypeService;
import com.witcurve.service.dto.StudentFeeTypeDTO;
import com.witcurve.service.mapper.StudentFeeTypeMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            throw new WitcurveException("No FeeDetail is present with given id : {} " + studentFeeTypeDTO.getFeeTypeId());
        }
        if (!feeDetails.get().getType().equals(FeeDetailsType.FEE_TYPE)) {
            throw new WitcurveException("Given FeeDetail id is not of fee type");
        }
        if (studentFeeTypeDTO.getPenalty() != null) {
            if (studentFeeTypeDTO.getDueDate() == null) {
                throw new WitcurveException("Due date can not be null when penalty is given");
            }
        }
        StudentFeeType studentFeeType = studentFeeTypeRepository.save(studentFeeTypeMapper.toEntity(studentFeeTypeDTO));
        return studentFeeTypeMapper.toDto(studentFeeType);
    }
}


