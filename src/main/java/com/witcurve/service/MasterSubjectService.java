package com.witcurve.service;

import com.witcurve.domain.MasterSubject;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface MasterSubjectService {

    MasterSubject saveOrUpdate(MasterSubject masterSubject);

    void deleteMasterSubject(String name) throws WitcurveException;

    List<MasterSubject> search(String like);
}
