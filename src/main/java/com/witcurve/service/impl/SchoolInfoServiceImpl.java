package com.witcurve.service.impl;

import com.witcurve.domain.SchoolInfo;
import com.witcurve.repository.SchoolInfoRepository;
import com.witcurve.service.SchoolInfoService;
import com.witcurve.service.dto.SchoolInfoDTO;
import com.witcurve.service.mapper.SchoolInfoMapper;
import com.witcurve.service.mapper.SchoolInfoMapperLite;
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
public class SchoolInfoServiceImpl implements SchoolInfoService {

    private final Logger log = LoggerFactory.getLogger(SchoolInfoServiceImpl.class);

    @Autowired
    SchoolInfoRepository schoolInfoRepository;

    @Autowired
    SchoolInfoMapper schoolInfoMapper;

    @Autowired
    SchoolInfoMapperLite schoolInfoMapperLite;

    @Override
    public SchoolInfoDTO saveOrUpdate(SchoolInfoDTO schoolInfoDTO) {
        log.debug("Request to create schoolInfo");
        SchoolInfo schoolInfo = schoolInfoMapper.toEntity(schoolInfoDTO);
        schoolInfo = schoolInfoRepository.save(schoolInfo);
        return schoolInfoMapper.toDto(schoolInfo);
    }

    @Override
    public SchoolInfoDTO getSchoolInfoById(Long schoolInfoId) throws WitcurveException {
        log.debug("Request to get schoolInfo with id: {}", schoolInfoId);
        Optional<SchoolInfo> schoolInfo = schoolInfoRepository.findById(schoolInfoId);
        if (!schoolInfo.isPresent()) {
            throw new WitcurveException("No SchoolInfo with given id " + schoolInfoId);
        }
        return schoolInfoMapper.toDto(schoolInfo.get());
    }

    @Override
    public List<SchoolInfoDTO> getSchoolInfosBySchoolId(Long schoolId) {
        log.debug("Request to get school info list with school id: {}", schoolId);
        List<SchoolInfo> schoolInfos = schoolInfoRepository.findBySchoolId(schoolId);
        return schoolInfoMapperLite.toDto(schoolInfos);
    }

    @Override
    public void deleteSchoolInfo(Long schoolInfoId) throws WitcurveException {
        log.debug("Request to delete schoolInfo with id {}", schoolInfoId);
        Optional<SchoolInfo> schoolInfo = schoolInfoRepository.findById(schoolInfoId);
        if (!schoolInfo.isPresent()) {
            throw new WitcurveException("No SchoolInfo with given id: " + schoolInfoId);
        }
        schoolInfoRepository.delete(schoolInfo.get());
    }

}
