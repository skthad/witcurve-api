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

import java.util.List;
import java.util.Optional;

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

        if (schoolDTO.getPrimaryBranch()) {
            schoolRepository.deactivatePrimaryBranchByInstituteId(schoolDTO.getInstitute().getId());
        }

        School school = schoolMapper.toEntity(schoolDTO);
        school = schoolRepository.save(school);
        return schoolMapper.toDto(school);
    }

    @Override
    public SchoolDTO getSchoolById(Long schoolId) throws WitcurveException {
        log.debug("Request to get school with id: {}", schoolId);
        Optional<School> school = schoolRepository.findById(schoolId);
        if (!school.isPresent()) {
            throw new WitcurveException("No school with given Id " + school);
        }
        return schoolMapper.toDto(school.get());
    }

    @Override
    public List<SchoolDTO> getSchoolByInstituteId(Long instituteId) {
        log.debug("Request to get schools with institute id: {}", instituteId);
        List<School> schools = schoolRepository.findByInstituteId(instituteId);
        return schoolMapper.toDto(schools);
    }

    @Override
    public void deleteSchool(Long schoolId) throws WitcurveException {
        log.debug("Request to delete school with id {}", schoolId);
        Optional<School> school = schoolRepository.findById(schoolId);
        if (!school.isPresent()) {
            throw new WitcurveException("No school with given Id " + school);
        }
        schoolRepository.delete(school.get());
    }

    @Override
    public SchoolDTO changeToPrimaryBranch(Long schoolId) {
        log.debug("Request to make School as primary branch with id {}", schoolId);
        Optional<School> school = schoolRepository.findById(schoolId);

        if (!school.isPresent()) {
            throw new WitcurveException("No School present with given id {} : " + schoolId);
        }
        schoolRepository.deactivatePrimaryBranchByInstituteId(school.get().getInstitute().getId());
        school.get().setPrimaryBranch(true);
        return schoolMapper.toDto(school.get());
    }
}
