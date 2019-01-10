package com.witcurve.service.impl;

import com.witcurve.domain.Standard;
import com.witcurve.repository.CourseTeacherRepository;
import com.witcurve.repository.StandardRepository;
import com.witcurve.service.StandardService;
import com.witcurve.service.dto.StandardDTO;
import com.witcurve.service.mapper.StandardMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Override
    public StandardDTO saveOrUpdateStandard(StandardDTO standardDTO) {
        log.debug("Request to save or update Standard: {}", standardDTO);
        Standard standard = standardMapper.toEntity(standardDTO);
        standard = standardRepository.save(standard);
        return standardMapper.toDto(standard);
    }

    @Override
    public StandardDTO getStandardById(Long standardId) throws WitcurveException {
        log.debug("Request to get standard with id : {}", standardId);
        Standard standard = standardRepository.findById(standardId).get();
        if (standard == null) {
            throw new WitcurveException("No standard exits with given id");
        }
        return standardMapper.toDto(standard);
    }

    @Override
    public List<StandardDTO> getStandardsBySchoolInfoId(Long schoolInfoId) {
        log.debug("Request to get standards with school info id : {}", schoolInfoId);
        List<Standard> standards = standardRepository.findBySchoolInfoId(schoolInfoId);
        return standardMapper.toDto(standards);
    }

    @Override
    public List<StandardDTO> getStandardsByTeacherIdAndTermId(Long teacherId, Long termId) throws WitcurveException {
        log.debug("Request to get all standards by by teacher id : {}", teacherId);
        List<Standard> result = courseTeacherRepository.findStandardsByTeacherIdAndTermId(teacherId, termId);
        return standardMapper.toDto(result);
    }

    @Override
    public void deleteStandard(Long standardId) throws WitcurveException {
        log.debug("Request to delete standard with id {}", standardId);
        Standard standard = standardRepository.findById(standardId).get();
        if (standard == null){
            throw new WitcurveException("No standard with given Id");
        }
        standardRepository.delete(standard);
    }
}
