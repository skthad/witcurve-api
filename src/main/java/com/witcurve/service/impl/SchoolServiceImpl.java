package com.witcurve.service.impl;

import com.witcurve.domain.School;
import com.witcurve.repository.SchoolRepository;
import com.witcurve.service.SchoolService;
import com.witcurve.service.dto.SchoolDTO;
import com.witcurve.service.mapper.SchoolMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SchoolServiceImpl implements SchoolService {

    private final Logger log = LoggerFactory.getLogger(SchoolServiceImpl.class);

    @Autowired
    SchoolRepository schoolRepository;

    @Autowired
    SchoolMapper schoolMapper;

    @Override
    public SchoolDTO saveOrUpdate(SchoolDTO schoolDTO) {
        log.debug("Request to save or update school");
        School school = schoolMapper.toEntity(schoolDTO);
        school = schoolRepository.save(school);
        return schoolMapper.toDto(school);
    }

    @Override
    public SchoolDTO getSchoolById(Long schoolId) throws WitcurveException {
        log.debug("Request to get school with id: {}", schoolId);
        School school = schoolRepository.findById(schoolId).get();
        if (school == null) {
            throw new WitcurveException(String.format("No School with given id: , {}", schoolId));
        }
        return schoolMapper.toDto(school);
    }

    @Override
    public void deleteSchool(Long schoolId) throws WitcurveException {
        log.debug("Request to delete school with id {}", schoolId);
        School school = schoolRepository.findById(schoolId).get();
        if (school == null){
            throw new WitcurveException("No school with given Id");
        }
        schoolRepository.delete(school);
    }

}
