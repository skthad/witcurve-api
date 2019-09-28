package com.witcurve.service.impl;

import com.witcurve.domain.*;
import com.witcurve.domain.enumeration.*;
import com.witcurve.repository.*;
import com.witcurve.service.StudentMarksService;
import com.witcurve.service.dto.*;
import com.witcurve.service.mapper.StudentMarksMapper;
import com.witcurve.service.util.WitcurveUtil;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
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

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    StudentStandardRepository studentStandardRepository;


    @Override
    public List<StudentMarksDTO> saveOrUpdateStudentMarks(List<StudentMarksDTO> studentMarksDTOs, Long eventId, Long rcdId, Long courseId) throws WitcurveException {
        studentMarksDTOs = validateAndFormatStudentMarks(studentMarksDTOs, eventId, rcdId, courseId);
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
    public List<StudentMarksDTO> getStudentMarksByExamId(Long examId, Long courseId, Long rcdId, Long standardId) throws WitcurveException {
        log.debug("Request to get student Marks by examId id : {}", examId);
        Optional<Exam> exam = examRepository.findById(examId);
        if (!exam.isPresent()) {
            throw new WitcurveException("Exam does not exist with id: " + examId);
        }
        if (courseId == null && standardId == null) {
            return studentMarksMapper.toDto(studentMarksRepository.getStudentMarksByExamId(examId));
        } else if (courseId != null) {
            Optional<Course> course = courseRepository.findById(courseId);
            if (!course.isPresent()) {
                throw new WitcurveException("Course does not exist with id: " + courseId);
            }
            if (standardId == null) {
                return studentMarksMapper.toDto(studentMarksRepository.getStudentMarksByCourseId(courseId));
            } else {
                if(rcdId != null) {
                    return studentMarksMapper.toDto(studentMarksRepository.getStudentMarksByCourseIdAndRcdIdAndStandardId(courseId, rcdId, standardId));
                } else {
                    return studentMarksMapper.toDto(studentMarksRepository.getStudentMarksByCourseIdAndExamIdAndStandardId(courseId,examId, standardId));
                }
            }
        } else {
            return studentMarksMapper.toDto(studentMarksRepository.getStudentMarksByExamIdAndStandardId(examId, standardId));
        }
    }

    @Override
    public List<StudentMarksDTO> getAllMarksForAStudentInACourse(Long studentId, Long courseId, LocalDate startDate, LocalDate endDate) throws WitcurveException{
        log.debug("Request to get all marks for student {} in course {}", studentId, courseId);
        WitcurveUtil.correctDateFormat(startDate, endDate);
        List<StudentMarksDTO> result = new ArrayList<>();
        Optional<Student> student = studentRepository.findById(studentId);
        if (!student.isPresent()) {
            throw new WitcurveException("No student found with ID: " + studentId);
        }
        List<StudentStandard> studentStandards = studentStandardRepository.getByStudentId(studentId);
        List<BigInteger> ids = eventRepository.findMarksEventsByDateRangeForStudent(startDate, endDate, studentStandards.get(0).getStandard().getId(), Arrays.asList(courseId));
        List<Long> eventIds = WitcurveUtil.convertBigIntToLong(ids);
        Optional<Course> course = courseRepository.findById(courseId);
        if (!course.isPresent()) {
            throw new WitcurveException("No course found with ID: " + courseId);
        }
        result.addAll(studentMarksMapper.toDto(studentMarksRepository.getByCourseIdAndStudentId( studentId, courseId, startDate, endDate)));
        result.addAll(studentMarksMapper.toDto(studentMarksRepository.getByStudentIdAndEventIds(studentId, eventIds)));
        Collections.sort(result, new StudentMarksDTOAscComparator());
        return result;
    }

    @Override
    public void deleteStudentMarks(List<Long> studentMarksIds) throws WitcurveException {
        log.debug("Request to delete student Marks with id {}", studentMarksIds);
        studentMarksRepository.deleteStudentMarksByIds(studentMarksIds);
    }
    private List<StudentMarksDTO> validateAndFormatStudentMarks(List<StudentMarksDTO> studentMarksDTOs, Long eventId, Long rcdId, Long courseId) {
        List<StudentMarks> existingStudentMarksList;
        List<Long> requestStudentIds = new ArrayList<>();
        EventDTO eventDTO = null;
        CourseDTO courseDTO = null;
        ReportCardDesignDTO reportCardDesignDTO = null;
        if(rcdId != null && eventId == null) {
            log.debug("Request to save or update Student Marks: {} with rcd id : {} for course with id : {} ", studentMarksDTOs, rcdId, courseId);
            Optional<ReportCardDesign> reportCardDesign = reportCardDesignRepository.findById(rcdId);
            if(!reportCardDesign.isPresent()) {
                throw new WitcurveException("There is no report card design with given id");
            }
            if(!ALLOWED_FIELD_TYPES.contains(reportCardDesign.get().getFieldType()) ) {
                throw new WitcurveException("Invalid report card design, make sure it is of manual entry or main or non_scholastic field");
            }
            Optional<Course> course = courseRepository.findById(courseId);
            if(!course.isPresent() || !course.get().getActive() ) {
                throw new WitcurveException("This is not a valid course, only active non scholastic courses are allowed");
            }
            existingStudentMarksList = studentMarksRepository.getStudentMarksByRcdIdAndCourseId(rcdId, courseId);
            courseDTO = new CourseDTO();
            courseDTO.setId(courseId);
            reportCardDesignDTO = new ReportCardDesignDTO();
            reportCardDesignDTO.setId(rcdId);
            if(reportCardDesign.get().getFieldType().equals(ReportFieldType.NON_SCHOLASTIC)) {
                if(!course.get().getCourseType().equals(CourseType.NON_SCHOLASTIC)) {
                    throw new WitcurveException("Course should be a non scholastic type");
                }
            } else {
                if(!course.get().getCourseType().equals(CourseType.SCHOLASTIC)) {
                    throw new WitcurveException("Course should be a scholastic type");
                }
            }
        } else if(eventId != null && rcdId==null) {
            log.debug("Request to save or update Student Marks: {} with event id : {} ", studentMarksDTOs, eventId);
            Optional<Event> event = eventRepository.findById(eventId);
            if(!event.isPresent()) {
                throw new WitcurveException("Event doesn't exist with id "+ eventId);
            }
            existingStudentMarksList = studentMarksRepository.getStudentMarksByEventId(eventId);
            eventDTO = new EventDTO();
            eventDTO.setId(eventId);
        } else {
            throw new WitcurveException("You need to have only one of rcdId, eventId");
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
            studentMarksDTO.setCourseDTO(courseDTO);
            studentMarksDTO.setReportCardDesignDTO(reportCardDesignDTO);
            requestStudentIds.add(studentMarksDTO.getStudentId());
        }
        return studentMarksDTOs;
    }

    public class StudentMarksDTOAscComparator implements Comparator<StudentMarksDTO> {

        @Override
        public int compare(StudentMarksDTO o1, StudentMarksDTO o2) {
            if (o1.getEventOrExamDate().isAfter(o2.getEventOrExamDate())) {
                return 1;
            } else if (o1.getEventOrExamDate().isBefore(o2.getEventOrExamDate())) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
