package com.witcurve.service;

import com.witcurve.domain.enumeration.Grade;
import com.witcurve.service.dto.StudentStandardDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface StudentStandardService {

    List<StudentStandardDTO> saveMultiple(List<StudentStandardDTO> studentStandardDTOs, Long standardId) throws WitcurveException;

    StudentStandardDTO save(StudentStandardDTO studentStandardDTO) throws WitcurveException;

    StudentStandardDTO getByStudentId(Long studentId) throws WitcurveException;

    List<StudentStandardDTO> getByStandardId(Long standardId);

    List<StudentStandardDTO> getBySchoolInfoId(Long schoolInfoId, Grade grade, Long standardId);

    List<StudentStandardDTO> getByStaffId(Long staffId, Grade grade, Long standardId) throws WitcurveException;
}
