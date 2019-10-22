package com.witcurve.service.impl;

import com.witcurve.domain.FeeDetails;
import com.witcurve.domain.SchoolInfo;
import com.witcurve.domain.SessionFeeStructure;
import com.witcurve.domain.enumeration.FeeDetailsType;
import com.witcurve.repository.FeeDetailsRepository;
import com.witcurve.repository.SchoolInfoRepository;
import com.witcurve.repository.SessionFeeStructureRepository;
import com.witcurve.service.FeeDetailsService;
import com.witcurve.service.dto.FeeDetailsDTO;
import com.witcurve.service.mapper.FeeDetailsMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FeeDetailsServiceImpl implements FeeDetailsService {

    private final Logger log = LoggerFactory.getLogger(FeeDetailsServiceImpl.class);

    @Autowired
    SchoolInfoRepository schoolInfoRepository;

    @Autowired
    FeeDetailsRepository feeDetailsRepository;

    @Autowired
    FeeDetailsMapper feeDetailsMapper;

    @Autowired
    SessionFeeStructureRepository sessionFeeStructureRepository;

    @Override
    public FeeDetailsDTO saveOrUpdateFeeDetails(FeeDetailsDTO feeDetailsDTO) throws WitcurveException {
        log.debug("Request to save or update FeeDetails: {}", feeDetailsDTO);
        FeeDetails feeDetails = feeDetailsRepository.save(feeDetailsMapper.toEntity(feeDetailsDTO));
        return feeDetailsMapper.toDto(feeDetails);
    }

    @Override
    public List<FeeDetailsDTO> getFeeDetailsBySchoolInfoId(Long schoolInfoId, FeeDetailsType type) throws WitcurveException {
        log.debug("Request to get FeeDetails of schoolInfoId : {} ", schoolInfoId);

        List<FeeDetailsDTO> result;
        Optional<SchoolInfo> schoolInfo = schoolInfoRepository.findById(schoolInfoId);
        if (!schoolInfo.isPresent()) {
            throw new WitcurveException("No SchoolInfo present with given id : {} " + schoolInfoId);
        }
        if (type == null) {
            result = feeDetailsMapper.toDto(feeDetailsRepository.findBySchoolInfoId(schoolInfoId));
        } else {
            result = feeDetailsMapper.toDto(feeDetailsRepository.findBySchoolInfoIdAndType(schoolInfoId, type));
        }
        return result;
    }

    @Override
    public FeeDetailsDTO getFeeDetailsById(Long feeDetailsId) throws WitcurveException {
        log.debug("Request to get FeeDetails of feeDetailsId : {} ", feeDetailsId);

        Optional<FeeDetails> feeDetails = feeDetailsRepository.findById(feeDetailsId);
        if (!feeDetails.isPresent()) {
            throw new WitcurveException("No FeeDetails present with given id : {}" + feeDetailsId);
        }
        return feeDetailsMapper.toDto(feeDetails.get());
    }

    @Override
    public void deleteFeeDetails(Long feeDetailsId) {
        log.debug("Request to delete FeeDetails of feeDetailsId : {} ", feeDetailsId);

        Optional<FeeDetails> feeDetail = feeDetailsRepository.findById(feeDetailsId);
        if (!feeDetail.isPresent()) {
            throw new WitcurveException("No record present with give feeDetailsId : {} " + feeDetailsId);
        }
        List<SessionFeeStructure> sessionFeeStructures = sessionFeeStructureRepository.findByFeeTypeAndFeeDescriptionId(feeDetailsId);
        if (!sessionFeeStructures.isEmpty()) {
            throw new WitcurveException("Can not delete record as StudentFeeStructure record exist with given feeDetailsId : {} " + feeDetailsId);
        }
        feeDetailsRepository.deleteById(feeDetailsId);
    }
}
