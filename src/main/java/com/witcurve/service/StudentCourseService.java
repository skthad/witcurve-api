package com.witcurve.service;

import com.witcurve.service.dto.StudentCourseDTO;

import java.util.List;

public interface StudentCourseService {

    List<StudentCourseDTO> saveOrUpdate(List<StudentCourseDTO> studentCourseDTOs);

    List<StudentCourseDTO> getSelectiveCoursesByStudent(Long studentId);

    List<StudentCourseDTO> getSelectiveCoursesByStandard(Long standardId);

    void deactivateStudentCourseByIds(List<Long> ids);


}
