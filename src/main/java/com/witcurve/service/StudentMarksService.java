package com.witcurve.service;

import com.witcurve.domain.StudentMarks;
import com.witcurve.domain.enumeration.EventType;
import com.witcurve.service.dto.StudentMarksDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;
import java.util.Map;

public interface StudentMarksService {
    List<StudentMarksDTO> saveOrUpdateStudentMarks(List<StudentMarksDTO> studentMarksDTO) throws WitcurveException;

    StudentMarksDTO getStudentMarksById(Long studentMarksId) throws WitcurveException;

    void deleteStudentMarks(Long studentMarksId) throws WitcurveException;

    List<StudentMarksDTO> getListStudentMarksInACourseTeacherByEventId(Long eventId) throws WitcurveException;

    List<StudentMarksDTO> getListStudentMarksForExamInACourseTeacher(Long examCourseDetailsId) throws WitcurveException;

    Map<EventType,List<StudentMarksDTO>> getAllMarksForStudent(Long courseTeacherId,Long studentId) throws WitcurveException;

}
