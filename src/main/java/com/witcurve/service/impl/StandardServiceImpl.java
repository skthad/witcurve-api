package com.witcurve.service.impl;

import com.witcurve.domain.SchoolInfo;
import com.witcurve.domain.Standard;
import com.witcurve.repository.CourseTeacherRepository;
import com.witcurve.repository.SchoolInfoRepository;
import com.witcurve.repository.StandardRepository;
import com.witcurve.service.StandardService;
import com.witcurve.service.dto.StandardDTO;
import com.witcurve.service.mapper.StandardMapper;
import com.witcurve.service.mapper.StandardMapperLite;
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
public class StandardServiceImpl implements StandardService {

    private final Logger log  = LoggerFactory.getLogger(StandardServiceImpl.class);

    @Autowired
    StandardRepository standardRepository;

    @Autowired
    CourseTeacherRepository courseTeacherRepository;

    @Autowired
    StandardMapper standardMapper;

    @Autowired
    StandardMapperLite standardMapperLite;

    @Autowired
    SchoolInfoRepository schoolInfoRepository;

    @Override
    public StandardDTO saveOrUpdateStandard(StandardDTO standardDTO) throws WitcurveException {
        log.debug("Request to save or update Standard: {}", standardDTO);
        Optional<SchoolInfo> schoolInfo = schoolInfoRepository.findById(standardDTO.getSchoolInfo().getId());
        if (!schoolInfo.isPresent()) {
            throw new WitcurveException("SchoolInfo does not exist with given id");
        }
        Standard standard = standardMapper.toEntity(standardDTO);
        standard.setSchoolInfo(schoolInfo.get());
        standard = standardRepository.save(standard);
        return standardMapper.toDto(standard);
    }

    @Override
    public StandardDTO getStandardById(Long standardId) throws WitcurveException {
        log.debug("Request to get standard with id : {}", standardId);
        Optional<Standard> standard = standardRepository.findById(standardId);
        if (!standard.isPresent()) {
            throw new WitcurveException("No standard exits with given id " + standardId);
        }
        return standardMapperLite.toDto(standard.get());
    }

    @Override
    public List<StandardDTO> getStandardsBySchoolInfoId(Long schoolInfoId) {
        log.debug("Request to get standards with school info id : {}", schoolInfoId);
        List<Standard> standards = standardRepository.findBySchoolInfoId(schoolInfoId);
        return standardMapperLite.toDto(standards);
    }

    @Override
    public List<StandardDTO> getStandardsByTeacherId(Long teacherId) {
        log.debug("Request to get all standards by by teacher id : {}", teacherId);
        List<Standard> result = courseTeacherRepository.findStandardsByTeacherId(teacherId);
        return standardMapperLite.toDto(result);
    }

    @Override
    public void deleteStandard(Long standardId) throws WitcurveException {
        log.debug("Request to delete standard with id {}", standardId);
        Optional<Standard> standard = standardRepository.findById(standardId);
        if (!standard.isPresent()) {
            throw new WitcurveException("No standard exits with given id " + standardId);
        }
        standardRepository.delete(standard.get());
    }
}
