package com.witcurve.service;

import com.witcurve.service.dto.StudentDTO;
import com.witcurve.web.rest.errors.WitcurveException;

public interface StudentService {

    StudentDTO saveOrUpdate(StudentDTO studentDTO);

    StudentDTO getStudentById(Long studentId) throws WitcurveException;

    void deleteStudent(Long studentId) throws WitcurveException;
}
