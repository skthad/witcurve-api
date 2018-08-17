package com.witcurve.service;

import com.witcurve.domain.MasterSubject;
import com.witcurve.web.rest.errors.WitcurveException;

public interface MasterSubjectService {

    MasterSubject saveOrUpdate(MasterSubject masterSubject);

    MasterSubject getMasterSubjectId(Long subjectId) throws WitcurveException;

    void deleteMasterSubject(Long masterSubjectId) throws WitcurveException;
}
