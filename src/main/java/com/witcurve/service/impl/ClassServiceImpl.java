package com.witcurve.service.impl;

import com.witcurve.domain.Class;
import com.witcurve.repository.ClassRepository;
import com.witcurve.service.ClassService;
import com.witcurve.service.dto.ClassDTO;
import com.witcurve.service.mapper.ClassMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClassServiceImpl implements ClassService {

    private final Logger log  = LoggerFactory.getLogger(ClassServiceImpl.class);

    @Autowired
    ClassRepository classRepository;

    @Autowired
    ClassMapper classMapper;


    @Override
    public ClassDTO saveOrUpdateClass(ClassDTO classDTO) {
        log.debug("Request to save or update Class: {}", classDTO);
        Class aClass = classMapper.classDTOToClass(classDTO);
        aClass = classRepository.save(aClass);
        return classMapper.classToClassDTO(aClass);
    }

    @Override
    public ClassDTO getClassById(Long classId) throws WitcurveException {
        log.debug("Request to get class with id : {}", classId);
        Class standard = classRepository.findById(classId).get();
        if (standard == null) {
            throw new WitcurveException("No class exits with given id");
        }
        return classMapper.classToClassDTO(standard);
    }
}
