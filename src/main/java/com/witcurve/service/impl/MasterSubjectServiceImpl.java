package com.witcurve.service.impl;

import com.witcurve.domain.MasterSubject;
import com.witcurve.repository.MasterSubjectRepository;
import com.witcurve.service.MasterSubjectService;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MasterSubjectServiceImpl implements MasterSubjectService {

    private final Logger log  = LoggerFactory.getLogger(MasterSubjectServiceImpl.class);

    @Autowired
    MasterSubjectRepository masterSubjectRepository;

    @Override
    public MasterSubject saveOrUpdate(MasterSubject masterSubject) {
        log.debug("Request to save or update master subject : {}", masterSubject);
        return masterSubjectRepository.save(masterSubject);
    }

    @Override
    public MasterSubject getMasterSubjectId(Long masterSubjectId) throws WitcurveException {
        log.debug("Request to get master subject with id : {}", masterSubjectId);

        MasterSubject masterSubject = masterSubjectRepository.findById(masterSubjectId).get();
        if (masterSubject == null) {
            throw new WitcurveException("No master subject with given id");
        }
        return masterSubject;
    }
}
