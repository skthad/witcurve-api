package com.witcurve.service.impl;

import com.witcurve.domain.*;
import com.witcurve.domain.enumeration.EventType;
import com.witcurve.domain.enumeration.ExamStatus;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.domain.enumeration.ReportFieldType;
import com.witcurve.repository.*;
import com.witcurve.service.StudentMarksService;
import com.witcurve.service.dto.EventDTO;
import com.witcurve.service.dto.ExamCourseDetailsDTO;
import com.witcurve.service.dto.ReportCardDesignDTO;
import com.witcurve.service.dto.StudentMarksDTO;
import com.witcurve.service.mapper.StudentMarksMapper;
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
public class StudentMarksServiceImpl implements StudentMarksService {

    private final Logger log  = LoggerFactory.getLogger(StandardServiceImpl.class);

    private final List<Boolean> ALL = Arrays.asList(Boolean.TRUE, Boolean.FALSE);
    private final List<Boolean> PUBLISHED_ONLY = Arrays.asList(Boolean.TRUE);
    private final List<EventType> ALLOWED_EVENT_TYPES = Arrays.asList(EventType.TEST, EventType.ASSIGNMENT, EventType.PERIODIC_TEST);
    private final List<ReportFieldType> ALLOWED_FIELD_TYPES = Arrays.asList(ReportFieldType.MAIN, ReportFieldType.NON_SCHOLASTIC, ReportFieldType.MANUAL_ENTRY);

    @Autowired
    StudentMarksMapper studentMarksMapper;

    @Autowired
    StudentMarksRepository studentMarksRepository;

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

    @Autowired
    ReportCardDesignRepository reportCardDesignRepository;


    @Override
    public List<StudentMarksDTO> saveOrUpdateStudentMarks(List<StudentMarksDTO> studentMarksDTOs, Long ecdId, Long eventId, Long rcdId) throws WitcurveException {
        studentMarksDTOs = validateAndFormatStudentMarks(studentMarksDTOs, ecdId, eventId, rcdId);
        List<StudentMarks> studentMarks = studentMarksMapper.toEntity(studentMarksDTOs);
        studentMarks = studentMarksRepository.saveAll(studentMarks);
        return studentMarksMapper.toDto(studentMarks);
    }

    @Override
    public List<StudentMarksDTO> getStudentMarksByEventId(Long eventId, Long rcdId) throws WitcurveException {
        log.debug("Request to get student Marks by event id : {}", eventId);
        List<StudentMarks> studentMarks;
        Optional<Event> event = eventRepository.findById(eventId);
        if (!event.isPresent()) {
            throw new WitcurveException("Event does not exist with id: " + eventId);
        }
        if(ALLOWED_EVENT_TYPES.contains(event.get().getType())){
            if(rcdId == null) {
                studentMarks=studentMarksRepository.getStudentMarksByEventId(eventId);
            } else {
                studentMarks = studentMarksRepository.getStudentMarksByEventIdAndRcdId(eventId, rcdId);
            }
        } else  {
            throw new WitcurveException("The event must be test or assignment or periodic test");
        }
        return studentMarksMapper.toDto(studentMarks);
    }

    @Override
    public List<StudentMarksDTO> getStudentMarksByExamId(Long examId, Long ecdId, Long rcdId, Long standardId, Boolean publishedOnly) throws WitcurveException {
        log.debug("Request to get student Marks by examId id : {}", examId);
        Optional<Exam> exam = examRepository.findById(examId);
        if (!exam.isPresent()) {
            throw new WitcurveException("Exam does not exist with id: " + examId);
        }
        List<Boolean> flags = ALL;
        if(publishedOnly) {
            flags = PUBLISHED_ONLY;
        }
        if (ecdId == null && standardId == null) {
            return studentMarksMapper.toDto(studentMarksRepository.getStudentMarksByExamId(examId, flags));
        } else if (ecdId != null) {
            Optional<ExamCourseDetails> ecd = examCourseDetailsRepository.findById(ecdId);
            if (!ecd.isPresent()) {
                throw new WitcurveException("ExamCourseDetails does not exist with id: " + examId);
            }
            if (!ecd.get().getGsd().getExam().getId().equals(examId)) {
                throw new WitcurveException("ExamCourseDetails provided does not belong to the examId: " + examId);
            }
            if (standardId == null) {
                return studentMarksMapper.toDto(studentMarksRepository.getStudentMarksByEcdId(ecdId, flags));
            } else {
                if(rcdId != null) {
                    return studentMarksMapper.toDto(studentMarksRepository.getStudentMarksByEcdIdAndRcdIdAndStandardId(ecdId, rcdId, standardId, flags));
                } else {
                    return studentMarksMapper.toDto(studentMarksRepository.getStudentMarksByEcdIdAndStandardId(ecdId, standardId, flags));
                }
            }
        } else {
            return studentMarksMapper.toDto(studentMarksRepository.getStudentMarksByExamIdAndStandardId(examId, standardId, flags));
        }
    }

    @Override
    public List<StudentMarksDTO> getAllMarksForAStudentInACourse(Long studentId, Long courseId, EventType type, LocalDate startDate, LocalDate endDate, Boolean publishedOnly) throws WitcurveException{
        log.debug("Request to get all {} marks for student {} in course {}", type, studentId, courseId);
        WitcurveUtil.correctDateFormat(startDate, endDate);
        Optional<Course> course = courseRepository.findById(courseId);
        if (!course.isPresent()) {
            throw new WitcurveException("No course found with ID: " + courseId);
        }
        List<Boolean> flags = ALL;
        if(publishedOnly) {
            flags = PUBLISHED_ONLY;
        }
        switch (type) {
            case TEST:
            case ASSIGNMENT:
            case PERIODIC_TEST:
                return studentMarksMapper.toDto(studentMarksRepository.getByCourseIdAndStudentIdForEvent(courseId, studentId, type, startDate, endDate));
            case EXAM:
                return studentMarksMapper.toDto(studentMarksRepository.getByCourseIdAndStudentIdForExam(courseId,studentId, startDate, endDate, flags));

        }
        throw new WitcurveException("Not a valid type");
    }

    @Override
    public List<StudentMarksDTO> getAllMarksForAStudentInAnExam(Long studentId, Long examId, Boolean publishedOnly) throws WitcurveException {
        log.debug("Request to get all marks for student with id : {} and exam with id : {}", studentId, examId);
        List<Boolean> flags = ALL;
        if(publishedOnly) {
            flags = PUBLISHED_ONLY;
        }
        List<StudentMarks> result = studentMarksRepository.getByStudentIdForExam(examId, studentId, flags);
        return studentMarksMapper.toDto(result);

    }

    @Override
    public List<StudentMarksDTO> getMarksForAllStudentsInAGradeAndCourse(Grade grade, Long courseId, EventType type, LocalDate startDate, LocalDate endDate,  Boolean publishedOnly) throws WitcurveException{
        log.debug("Request to get marks for all students {} in grade {} in course {}", type, grade, courseId);
        WitcurveUtil.correctDateFormat(startDate, endDate);
        Optional<Course> course = courseRepository.findById(courseId);
        if (!course.isPresent()) {
            throw new WitcurveException("No course found with ID: " + courseId);
        }
        if (!course.get().getGrade().equals(grade)) {
            throw new WitcurveException("Provided grade does not have the course with ID: " + courseId);
        }
        List<Boolean> flags = ALL;
        if(publishedOnly) {
            flags = PUBLISHED_ONLY;
        }
        switch (type) {
            case TEST:
            case ASSIGNMENT:
            case PERIODIC_TEST:
                return studentMarksMapper.toDto(studentMarksRepository.getByCourseIdAndGradeForEvent(courseId, grade, type, startDate, endDate));
            case EXAM:
                return studentMarksMapper.toDto(studentMarksRepository.getByCourseIdAndGradeForExam(courseId, grade, startDate, endDate, flags));

        }
        throw new WitcurveException("Not a valid type");
    }

    public List<StudentMarksDTO> getMarksForAllStudentsInAStandardAndCourse(Long standardId, Long courseId, EventType type, LocalDate startDate, LocalDate endDate) throws WitcurveException{
        log.debug("Request to get marks for all students {} in standard {} in course {}", type, standardId, courseId);
        WitcurveUtil.correctDateFormat(startDate, endDate);
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
            case PERIODIC_TEST:
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
    private List<StudentMarksDTO> validateAndFormatStudentMarks(List<StudentMarksDTO> studentMarksDTOs, Long ecdId, Long eventId, Long rcdId) {
        List<StudentMarks> existingStudentMarksList;
        List<Long> requestStudentIds = new ArrayList<>();
        EventDTO eventDTO = null;
        ExamCourseDetailsDTO ecd = null;
        if(eventId == null && ecdId == null) {
            throw new WitcurveException("EcdId or EventId need to be entered");
        } else if(eventId != null && ecdId != null) {
            throw new WitcurveException("You cannot send both ecdId and eventId");
        } else {
            ReportCardDesignDTO reportCardDesignDTO = null;
            if(rcdId != null) {
                Optional<ReportCardDesign> reportCardDesign = reportCardDesignRepository.findById(rcdId);
                if(!reportCardDesign.isPresent()) {
                    throw new WitcurveException("There is no report card design with given id");
                }
                if(!ALLOWED_FIELD_TYPES.contains(reportCardDesign.get().getFieldType()) ) {
                    throw new WitcurveException("Invalid report card design, make sure it is of manual entry or main or non_scholastic field");
                }
                reportCardDesignDTO = new ReportCardDesignDTO();
                reportCardDesignDTO.setId(rcdId);
            }
            if(eventId != null) {
                log.debug("Request to save or update Student Marks: {} with event id : {} ", studentMarksDTOs, eventId);
                Optional<Event> event = eventRepository.findById(eventId);
                if(!event.isPresent()) {
                    throw new WitcurveException("Event doesn't exist with id "+ eventId);
                }
                if(rcdId == null) {
                    existingStudentMarksList = studentMarksRepository.getStudentMarksByEventId(eventId);
                } else {
                    existingStudentMarksList = studentMarksRepository.getStudentMarksByEventIdAndRcdId(eventId, rcdId);
                }
                eventDTO = new EventDTO();
                eventDTO.setId(eventId);
            } else {
                log.debug("Request to save or update Student Marks: {} with ecd id : {} ", studentMarksDTOs, ecdId);
                Optional<ExamCourseDetails> examCourseDetails = examCourseDetailsRepository.findById(ecdId);
                if(!examCourseDetails.isPresent()) {
                    throw new WitcurveException("ExamCourseDetails doesn't exist with id "+ ecdId);
                }
                if(examCourseDetails.get().getGsd().getExam().getStatus().equals(ExamStatus.DRAFT)) {
                    throw new WitcurveException("Marks cannot be posted for draft exams");
                }
                if(rcdId == null) {
                    existingStudentMarksList = studentMarksRepository.getStudentMarksByEcdId(ecdId, ALL);
                } else {
                    existingStudentMarksList = studentMarksRepository.getStudentMarksByEcdIdAndRcdId(ecdId, rcdId, ALL);
                }
                ecd = new ExamCourseDetailsDTO();
                ecd.setId(ecdId);
            }
            Map<Long, StudentMarks> existingRecordMap = new HashMap<>();
            for(StudentMarks studentMarks : existingStudentMarksList) {
                existingRecordMap.put(studentMarks.getStudent().getId(), studentMarks);
            }
            for(StudentMarksDTO studentMarksDTO : studentMarksDTOs) {
                if(requestStudentIds.contains(studentMarksDTO.getStudentId())) {
                    throw new WitcurveException("There should be only one record for a student in the request");
                }
                StudentMarks existingStudentMarks = existingRecordMap.get(studentMarksDTO.getStudentId());
                if(studentMarksDTO.getId() == null) {
                    if(existingStudentMarks != null) {
                        throw new WitcurveException("There already exists a student marks for this student with id "+studentMarksDTO.getStudentId()+", so new record cannot be created");
                    }
                } else {
                    if(existingStudentMarks == null) {
                        throw new WitcurveException("There is no existing student marks record with this student id "+studentMarksDTO.getStudentId()+"to update");
                    } else {
                        if(!existingStudentMarks.getId().equals(studentMarksDTO.getId())) {
                            throw new WitcurveException("Student marks id cannot be changed while updating for student id "+studentMarksDTO.getStudentId());
                        }
                    }
                }
                studentMarksDTO.setEventDTO(eventDTO);
                studentMarksDTO.setExamCourseDetailsDTO(ecd);
                studentMarksDTO.setReportCardDesignDTO(reportCardDesignDTO);
                requestStudentIds.add(studentMarksDTO.getStudentId());
            }
        }
        return studentMarksDTOs;
    }
}
