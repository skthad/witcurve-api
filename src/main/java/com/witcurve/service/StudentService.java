package com.witcurve.service;

import com.witcurve.service.dto.StudentDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface StudentService {

    StudentDTO create(StudentDTO studentDTO);

    StudentDTO update(StudentDTO studentDTO);

    StudentDTO getStudentById(Long studentId) throws WitcurveException;

    StudentDTO getStudentByUserId(Long userId) throws WitcurveException;

    List<StudentDTO> getStudentsByStandardId(Long standardId) throws WitcurveException;

    StudentDTO getStudentByUsername(String username) throws WitcurveException;

    void deactivate(Long studentId);
}
