package com.witcurve.service;

import com.witcurve.service.dto.StudentDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface StudentService {

    StudentDTO create(StudentDTO studentDTO);

    StudentDTO update(StudentDTO studentDTO) throws WitcurveException;

    StudentDTO getStudentById(Long studentId) throws WitcurveException;

    StudentDTO getStudentByUserId(Long userId) throws WitcurveException;

    List<StudentDTO> getStudentsByStandardId(Long standardId) throws WitcurveException;

    List<StudentDTO> getStudentsBySchoolInfoId(Long schoolInfoId);

    StudentDTO getStudentByUsername(String username) throws WitcurveException;

    void deactivate(Long studentId);

    void mapUnmapStudentAndCourse(Long studentStandardId, Long courseId, Boolean map);

    void mapOneTime(Long schoolInfoId);
}
