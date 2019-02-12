package com.witcurve.service.impl;

import com.witcurve.domain.*;
import com.witcurve.domain.enumeration.EventType;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.repository.*;
import com.witcurve.service.EventService;
import com.witcurve.service.StudentMarksService;
import com.witcurve.service.dto.StudentMarksDTO;
import com.witcurve.service.mapper.StudentMarksMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class StudentMarksServiceImpl implements StudentMarksService {
    private final Logger log  = LoggerFactory.getLogger(StandardServiceImpl.class);
    @Autowired
    StudentMarksMapper studentMarksMapper;

    @Autowired
    StudentMarksRepository studentMarksRepository;

    @Autowired
    EventService eventService;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    ExamRepository examRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    StandardRepository standardRepository;

    @Autowired
    ExamCourseDetailsRepository examCourseDetailsRepository;


    @Override
    public List<StudentMarksDTO> saveOrUpdateStudentMarks(List<StudentMarksDTO> studentMarksDTO) throws WitcurveException {
        log.debug("Request to save or update Student Marks: {}", studentMarksDTO);
        List<StudentMarks> studentMarks = studentMarksMapper.toEntity(studentMarksDTO);
        if(studentMarksDTO.size() !=0) {
            studentMarks = studentMarksRepository.saveAll(studentMarks);
            return studentMarksMapper.toDto(studentMarks);
        } else{
            throw new WitcurveException("No records to update");
        }
    }

    @Override
    public void publishMarksForEvent(Long eventId) {
        studentMarksRepository.publishMarksForEvent(eventId);
    }

    @Override
    public void publishMarksForECD(Long ecdId) {
        studentMarksRepository.publishMarksForECD(ecdId);
    }

    @Override
    public List<StudentMarksDTO> getStudentMarksByEventId(Long eventId) throws WitcurveException {
        log.debug("Request to get student Marks by event id : {}", eventId);
        List<StudentMarks> studentMarks;
        Optional<Event> event = eventRepository.findById(eventId);
        if (!event.isPresent()) {
            throw new WitcurveException("Event does not exist with id: " + eventId);
        }
        if(EventType.TEST.equals(event.get().getType()) || EventType.ASSIGNMENT.equals(event.get().getType())){
            studentMarks=studentMarksRepository.getStudentMarksByEventId(eventId);
        } else  {
            throw new WitcurveException("The eventId must be TEST or ASSIGNMENT");
        }
        return studentMarksMapper.toDto(studentMarks);
    }

    @Override
    public List<StudentMarksDTO> getStudentMarksByExamId(Long examId, Long ecdId) throws WitcurveException {
        log.debug("Request to get student Marks by examId id : {}", examId);
        Optional<Exam> exam = examRepository.findById(examId);
        if (!exam.isPresent()) {
            throw new WitcurveException("Exam does not exist with id: " + examId);
        }
        if (ecdId == null) {
            return studentMarksMapper.toDto(studentMarksRepository.getStudentMarksByExamId(examId));
        } else {
            Optional<ExamCourseDetails> ecd = examCourseDetailsRepository.findById(ecdId);
            if (!ecd.isPresent()) {
                throw new WitcurveException("ExamCourseDetails does not exist with id: " + examId);
            }
            if (!ecd.get().getGsd().getExam().getId().equals(examId)) {
                throw new WitcurveException("ExamCourseDetails provided does not belong to the examId: " + examId);
            }
            return studentMarksMapper.toDto(studentMarksRepository.getStudentMarksByExamId(examId, ecdId));
        }
    }

    @Override
    public List<StudentMarksDTO> getAllMarksForAStudentInACourse(Long studentId, Long courseId, EventType type, LocalDate startDate, LocalDate endDate) throws WitcurveException{
        log.debug("Request to get all {} marks for student {} in course {}", type, studentId, courseId);
        if (startDate.isAfter(endDate)) {
            throw new WitcurveException("StartDate cannot be after EndDate");
        }
        Optional<Course> course = courseRepository.findById(courseId);
        if (!course.isPresent()) {
            throw new WitcurveException("No course found with ID: " + courseId);
        }
        if (startDate.isAfter(endDate)) {
            throw new WitcurveException("StartDate cannot be after EndDate");
        }
        switch (type) {
            case TEST:
            case ASSIGNMENT:
                return studentMarksMapper.toDto(studentMarksRepository.getByCourseIdAndStudentIdForEvent(courseId, studentId, type, startDate, endDate));
            case EXAM:
                return studentMarksMapper.toDto(studentMarksRepository.getByCourseIdAndStudentIdForExam(courseId,studentId, startDate, endDate));
        }
        throw new WitcurveException("Not a valid type");
    }

    public List<StudentMarksDTO> getMarksForAllStudentsInAGradeAndCourse(Grade grade, Long courseId, EventType type, LocalDate startDate, LocalDate endDate) throws WitcurveException{
        log.debug("Request to get marks for all students {} in grade {} in course {}", type, grade, courseId);
        if (startDate.isAfter(endDate)) {
            throw new WitcurveException("StartDate cannot be after EndDate");
        }
        Optional<Course> course = courseRepository.findById(courseId);
        if (!course.isPresent()) {
            throw new WitcurveException("No course found with ID: " + courseId);
        }
        if (!course.get().getGrade().equals(grade)) {
            throw new WitcurveException("Provided grade does not have the course with ID: " + courseId);
        }
        switch (type) {
            case TEST:
            case ASSIGNMENT:
                return studentMarksMapper.toDto(studentMarksRepository.getByCourseIdAndGradeForEvent(courseId, grade, type, startDate, endDate));
            case EXAM:
                return studentMarksMapper.toDto(studentMarksRepository.getByCourseIdAndGradeForExam(courseId, grade, startDate, endDate));
        }
        throw new WitcurveException("Not a valid type");
    }

    public List<StudentMarksDTO> getMarksForAllStudentsInAStandardAndCourse(Long standardId, Long courseId, EventType type, LocalDate startDate, LocalDate endDate) throws WitcurveException{
        log.debug("Request to get marks for all students {} in standard {} in course {}", type, standardId, courseId);
        if (startDate.isAfter(endDate)) {
            throw new WitcurveException("StartDate cannot be after EndDate");
        }
        Optional<Course> course = courseRepository.findById(courseId);
        if (!course.isPresent()) {
            throw new WitcurveException("No course found with ID: " + courseId);
        }
        Optional<Standard> standard = standardRepository.findById(standardId);
        if (!standard.isPresent()) {
            throw new WitcurveException("No standard found with ID: " + standardId);
        }
        if (!course.get().getSchoolInfo().getId().equals(standard.get().getSchoolInfo().getId())) {
            throw new WitcurveException("School Info ID in course and standard don't match");
        }
        switch (type) {
            case TEST:
            case ASSIGNMENT:
                return studentMarksMapper.toDto(studentMarksRepository.getByCourseIdAndStandardForEvent(courseId, standardId, type, startDate, endDate));
        }
        throw new WitcurveException("Not a valid type");
    }

    @Override
    public void deleteStudentMarks(Long studentMarksId) throws WitcurveException {
        log.debug("Request to delete student Marks with id {}", studentMarksId);
        Optional<StudentMarks> studentMarks = studentMarksRepository.findById(studentMarksId);
        if (!studentMarks.isPresent()){
            throw new WitcurveException("No student Marks relation with given Id: " + studentMarksId);
        }
        studentMarksRepository.delete(studentMarks.get());
    }
}
