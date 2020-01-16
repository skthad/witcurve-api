package com.witcurve.service.impl;

import com.witcurve.domain.*;
import com.witcurve.domain.enumeration.CourseType;
import com.witcurve.domain.enumeration.ReportFieldType;
import com.witcurve.repository.*;
import com.witcurve.service.CourseGradeService;
import com.witcurve.service.SnsService;
import com.witcurve.service.dto.CourseDTO;
import com.witcurve.service.dto.CourseGradeDTO;
import com.witcurve.service.dto.ReportCardDesignDTO;
import com.witcurve.service.mapper.CourseGradeMapper;
import com.witcurve.service.util.WitcurveUtil;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class CourseGradeServiceImpl implements CourseGradeService {

    private final Logger log = LoggerFactory.getLogger(CourseGradeServiceImpl.class);

    @Autowired
    ReportCardDesignRepository reportCardDesignRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    ExamRepository examRepository;

    @Autowired
    CourseGradeMapper courseGradeMapper;

    @Autowired
    CourseGradeRepository courseGradeRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    SnsService snsService;

    @Autowired
    StudentCourseRepository studentCourseRepository;

    @Override
    public List<CourseGradeDTO> saveOrUpdateCourseGrade(List<CourseGradeDTO> courseGradeDTOs, Long rcdId, Long courseId) {
        log.debug("Request to save student grade");
        courseGradeDTOs = validateAndFormatStudentGrade(courseGradeDTOs, rcdId, courseId);
        List<CourseGrade> courseGrades = courseGradeMapper.toEntity(courseGradeDTOs);
        courseGrades = courseGradeRepository.saveAll(courseGrades);
        snsService.sendPushNotificationWhenGradeSaved(courseGradeDTOs);
        return courseGradeMapper.toDto(courseGrades);
    }

    private List<CourseGradeDTO> validateAndFormatStudentGrade(List<CourseGradeDTO> courseGradeDTOs, Long rcdId, Long courseId) {

        Optional<ReportCardDesign> reportCardDesign = reportCardDesignRepository.findById(rcdId);
        if (!reportCardDesign.isPresent()) {
            throw new WitcurveException("There is no report card design with given id");
        }
        if (!reportCardDesign.get().getFieldType().equals(ReportFieldType.NON_SCHOLASTIC)) {
            throw new WitcurveException("Report card design type should be non scholastic type");
        }
        //TODO check course assign to student is required here?
        Optional<Course> course = courseRepository.findById(courseId);
        if (!course.isPresent() || !course.get().getActive() || !course.get().getCourseType().equals(CourseType.NON_SCHOLASTIC)) {
            throw new WitcurveException("This is not a valid course, only active non scholastic courses are allowed");
        }

        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(courseId);

        ReportCardDesignDTO reportCardDesignDTO = new ReportCardDesignDTO();
        reportCardDesignDTO.setId(rcdId);


        List<Long> requestStudentIds = new ArrayList<>();
        Map<Long, CourseGrade> existingRecordMap = new HashMap<>();
        List<CourseGrade> existingRecord = courseGradeRepository.getStudentGradesByRcdIdAndCourseId(rcdId, courseId);


        for (CourseGrade courseGrade : existingRecord) {
            existingRecordMap.put(courseGrade.getStudent().getId(), courseGrade);
        }
        for (CourseGradeDTO courseGradeDTO : courseGradeDTOs) {

            StudentCourse studentCourse = studentCourseRepository.getByCourseIdAndStudentIdWithStudentStandard(courseId, courseGradeDTO.getStudentId());
            if (studentCourse == null) {
                throw new WitcurveException("In One of the record given course does not belong to student");
            }
            if (requestStudentIds.contains(courseGradeDTO.getId())) {
                throw new WitcurveException("There should be only one record for a student in the request");
            }
            CourseGrade existingStudentCourseGrade = existingRecordMap.get(courseGradeDTO.getStudentId());
            if (courseGradeDTO.getId() == null) {
                if (existingStudentCourseGrade != null) {
                    throw new WitcurveException("There already exists a student course grade for this student with id " + courseGradeDTO.getStudentId() + ", so new record cannot be created");
                }
            } else {
                if (existingStudentCourseGrade == null) {
                    throw new WitcurveException("There is no existing student course grade record with this student id " + courseGradeDTO.getStudentId() + "to update");
                } else {
                    if (!existingStudentCourseGrade.getId().equals(courseGradeDTO.getId())) {
                        throw new WitcurveException("Student course grade id cannot be changed while updating for student id " + courseGradeDTO.getStudentId());
                    }
                }
            }
            courseGradeDTO.setCourseDTO(courseDTO);
            courseGradeDTO.setReportCardDesignDTO(reportCardDesignDTO);
            requestStudentIds.add(courseGradeDTO.getStudentId());
        }
        return courseGradeDTOs;
    }

    @Override
    public List<CourseGradeDTO> getCourseGradeByExamId(Long examId, Long courseId, Long rcdId, Long standardId) {
        log.debug("Request to get student grade by examId id : {}", examId);

        Optional<Exam> exam = examRepository.findById(examId);

        if (!exam.isPresent()) {
            throw new WitcurveException("Exam does not exist with id: " + examId);
        }
        if (courseId == null && standardId == null) {
            return courseGradeMapper.toDto(courseGradeRepository.getCourseGradeByExamId(examId));
        } else if (courseId != null) {
            Optional<Course> course = courseRepository.findById(courseId);
            if (!course.isPresent()) {
                throw new WitcurveException("Course does not exist with id: " + courseId);
            }
            if (standardId == null) {
                return courseGradeMapper.toDto(courseGradeRepository.getCourseGradeByCourseId(courseId));
            } else {
                if (rcdId != null) {
                    return courseGradeMapper.toDto(courseGradeRepository.getCourseGradeByCourseIdAndRcdIdAndStandardId(courseId, rcdId, standardId));
                } else {
                    return courseGradeMapper.toDto(courseGradeRepository.getCourseGradeByCourseIdAndExamIdAndStandardId(courseId, examId, standardId));
                }
            }
        } else {
            return courseGradeMapper.toDto(courseGradeRepository.getCourseGradeByExamIdAndStandardId(examId, standardId));
        }
    }

    @Override
    public List<CourseGradeDTO> getAllGradesForAStudentInACourse(Long studentId, Long courseId, LocalDate startDate, LocalDate endDate) {
        log.debug("Request to get all grades for student {} in course {}", studentId, courseId);
        WitcurveUtil.correctDateFormat(startDate, endDate);
        Optional<Student> student = studentRepository.findById(studentId);
        if (!student.isPresent()) {
            throw new WitcurveException("No student found with ID: " + studentId);
        }
        Optional<Course> course = courseRepository.findById(courseId);
        if (!course.isPresent()) {
            throw new WitcurveException("Course does not exist with id: " + courseId);
        }
        return courseGradeMapper.toDto(courseGradeRepository.getByCourseIdAndStudentId(studentId, courseId, startDate, endDate));
    }

    @Override
    public void deleteStudentGrades(List<Long> studentGradeIds) {
        log.debug("Request to delete student grades with id {}", studentGradeIds);
        courseGradeRepository.deleteStudentGradesByIds(studentGradeIds);
    }
}

