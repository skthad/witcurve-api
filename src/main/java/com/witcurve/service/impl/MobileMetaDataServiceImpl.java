package com.witcurve.service.impl;

import com.witcurve.domain.Institute;
import com.witcurve.domain.MobileMetaData;
import com.witcurve.repository.InstituteRepository;
import com.witcurve.repository.MobileMetaDataRepository;
import com.witcurve.service.MobileMetaDataService;
import com.witcurve.service.dto.MobileMetaDataDTO;
import com.witcurve.service.mapper.MobileMetaDataMapper;
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
public class MobileMetaDataServiceImpl implements MobileMetaDataService {

    private final Logger log = LoggerFactory.getLogger(MobileMetaDataServiceImpl.class);

    @Autowired
    private MobileMetaDataMapper mobileMetaDataMapper;

    @Autowired
    private MobileMetaDataRepository mobileMetaDataRepository;

    @Autowired
    private InstituteRepository instituteRepository;


    @Override
    public MobileMetaDataDTO saveOrUpdate(MobileMetaDataDTO mobileMetaDataDTO) {
        log.debug("Request to save MobileMetaData : {}", mobileMetaDataDTO);
        MobileMetaData mobileMetaData = mobileMetaDataMapper.toEntity(mobileMetaDataDTO);
        mobileMetaData = mobileMetaDataRepository.save(mobileMetaData);
        return mobileMetaDataMapper.toDto(mobileMetaData);
    }

    @Override
    public List<MobileMetaDataDTO> getMobileMetaDataByInstitute(Long instituteId) throws WitcurveException {
        List<MobileMetaData> mobileMetaDataList = null;
        if(instituteId == null) {
            log.debug("Request to get all MobileMetaData");
            mobileMetaDataList = mobileMetaDataRepository.findAll();
        } else {
            log.debug("Request to get all MobileMetaData for Institute with id: {}", instituteId);
            Optional<Institute> institute = instituteRepository.findById(instituteId);
            if (!institute.isPresent()) {
                throw new WitcurveException("No Institute with given id " + instituteId);
            }
            mobileMetaDataList = mobileMetaDataRepository.findByInstituteId(instituteId);
        }
        return mobileMetaDataMapper.toDto(mobileMetaDataList);
    }


    @Override
    public MobileMetaDataDTO getMobileMetaDataById(Long mobileMetaDataId) throws WitcurveException {
        log.debug("Request to get MobileMetaData with id : {}", mobileMetaDataId);
        Optional<MobileMetaData> mobileMetaData = mobileMetaDataRepository.findById(mobileMetaDataId);
        if(!mobileMetaData.isPresent()) {
            throw new WitcurveException("No MobileMetaData with given Id : "+mobileMetaDataId);
        }
        return mobileMetaDataMapper.toDto(mobileMetaData.get());
    }

    @Override
    public void deleteMobileMetaDataByInstituteId(Long instituteId) throws WitcurveException {
        log.debug("Request to delete MobileMetaData for institute with Id : {}", instituteId);
        mobileMetaDataRepository.deleteByInstituteId(instituteId);
    }
}
