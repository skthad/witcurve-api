package com.witcurve.service;

import com.witcurve.service.dto.StudentStandardDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface StudentStandardService {

    List<StudentStandardDTO> save(List<StudentStandardDTO> studentStandardDTOs, Long standardId);

    StudentStandardDTO getByStudentId(Long studentId) throws WitcurveException;

    List<StudentStandardDTO> getByStandardId(Long standardId);

    List<StudentStandardDTO> getBySchoolInfoId(Long schoolInfoId);

    List<StudentStandardDTO> getByStaffId(Long staffId) throws WitcurveException;

    StudentStandardDTO update(StudentStandardDTO studentStandardDTO);
}
