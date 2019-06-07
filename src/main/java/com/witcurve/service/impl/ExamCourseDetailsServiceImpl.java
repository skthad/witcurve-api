package com.witcurve.service.impl;

import com.witcurve.domain.*;
import com.witcurve.domain.enumeration.ExamStatus;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.repository.*;
import com.witcurve.service.ExamCourseDetailsService;
import com.witcurve.service.StudentStandardService;
import com.witcurve.service.dto.ExamCourseDetailsDTO;
import com.witcurve.service.dto.StudentStandardDTO;
import com.witcurve.service.mapper.ExamCourseDetailsMapper;
import com.witcurve.service.mapper.ExamCourseDetailsMapperLite;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ExamCourseDetailsServiceImpl implements ExamCourseDetailsService {

    private final Logger log  = LoggerFactory.getLogger(ExamCourseDetailsServiceImpl.class);

    @Autowired
    private ExamCourseDetailsMapper examCourseDetailsMapper;

    @Autowired
    private ExamCourseDetailsMapperLite examCourseDetailsMapperLite;

    @Autowired
    private ExamCourseDetailsRepository examCourseDetailsRepository;

    @Autowired
    private CourseTeacherRepository courseTeacherRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private StudentStandardService studentStandardService;

    @Autowired
    private StandardRepository standardRepository;

    @Autowired
    private StudentMarksRepository studentMarksRepository;

    @Override
    public List<ExamCourseDetailsDTO> saveOrUpdate(List<ExamCourseDetailsDTO> examCourseDetailsDTOs, Long examId) throws WitcurveException {
        log.debug("Request to save or update examCourseDetailsDTO for examId: " + examId);

        Optional<Exam> exam = examRepository.findById(examId);
        if (!exam.isPresent()) {
            throw new WitcurveException("No exam found with id: " + examId);
        }
        for (ExamCourseDetailsDTO examCourseDetailsDTO : examCourseDetailsDTOs) {
            if (examCourseDetailsDTO.getDate().isBefore(exam.get().getStartDate())
                || examCourseDetailsDTO.getDate().isAfter(exam.get().getEndDate())) {
                throw new WitcurveException("Date cannot be out of boundary of the exam startDate and endDate");
            }
        }
        List<ExamCourseDetails> examCourseDetails = examCourseDetailsMapperLite.toEntity(examCourseDetailsDTOs);
        examCourseDetails = examCourseDetailsRepository.saveAll(examCourseDetails);
        return examCourseDetailsMapperLite.toDto(examCourseDetails);
    }

    @Override
    public ExamCourseDetailsDTO getExamCourseDetailsById(Long examCourseDetailsId) throws WitcurveException {
        log.debug("Request to get examCourseDetails by id : {}", examCourseDetailsId);
        ExamCourseDetails examCourseDetails = examCourseDetailsRepository.findById(examCourseDetailsId).get();
        if(examCourseDetails == null) {
            throw new WitcurveException("No ExamCourseDetails exists for given id");
        }
        return examCourseDetailsMapper.toDto(examCourseDetails);
    }

    @Override
    public List<ExamCourseDetailsDTO> getExamCourseDetailsByGradesAndExamId(List<Grade> grades, Long examId) {
        log.debug("Request to get list of examCourseDetails for given grades : {} and examId : {}", grades, examId);
        List<ExamCourseDetails> examCourseDetailsList = null;
        if(grades != null && !grades.isEmpty()) {
            examCourseDetailsList = examCourseDetailsRepository.findByGradesAndExamId(grades, examId);
        } else {
            examCourseDetailsList = examCourseDetailsRepository.findByExamId(examId);
        }
        List<ExamCourseDetailsDTO> result = examCourseDetailsMapper.toDto(examCourseDetailsList);
        for(ExamCourseDetailsDTO examCourseDetails : result) {
            List<Standard> standards = standardRepository.findByGradeAndSchoolInfoId(examCourseDetails.getGsd().getGrade(), examCourseDetails.getCourse().getSchoolInfoId());
            for(Standard standard : standards) {
                List<StudentMarks> studentMarks = studentMarksRepository.getStudentMarksByEcdIdAndStandardId(examCourseDetails.getId(), standard.getId(), Arrays.asList(true, false));
                if(studentMarks.size() ==0) {
                    examCourseDetails.setDoesAllStudentMarksExist(false);
                }
            }
        }
        return result;
    }

    @Override
    public List<ExamCourseDetailsDTO> getExamCourseDetailsForStudentOnDate(Long studentId, LocalDate date) throws WitcurveException{
        log.debug("Request to get list of examCourseDetails for given student with id : {} and on date : {}", studentId, date);
        StudentStandardDTO studentStandardDTO = studentStandardService.getByStudentId(studentId);
        Grade grade = studentStandardDTO.getStandard().getGrade();
        Long schoolInfoId = studentStandardDTO.getStandard().getSchoolInfo().getId();
        List<ExamCourseDetails> examCourseDetails = examCourseDetailsRepository.findByGradeBetweenDatesOrderByGsdStart(grade, date, date, schoolInfoId);
        return examCourseDetailsMapper.toDto(examCourseDetails);
    }

    @Override
    public List<ExamCourseDetailsDTO> getExamCourseDetailsOnAGivenMonthForStudent(Long studentId, Integer month, Integer year) throws WitcurveException {
        log.debug("Request to get list of examCourseDetails for given student with id : {} for month : {} and on year : {}", studentId, month, year);
        StudentStandardDTO studentStandardDTO = studentStandardService.getByStudentId(studentId);
        Grade grade = studentStandardDTO.getStandard().getGrade();
        Long schoolInfoId = studentStandardDTO.getStandard().getSchoolInfo().getId();
        LocalDate monthStart = LocalDate.of(year,month,1);
        LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);
        List<ExamCourseDetails> examCourseDetails = examCourseDetailsRepository.findByGradeBetweenDatesOrderByGsdStart(grade, monthStart, monthEnd, schoolInfoId);
        return examCourseDetailsMapper.toDto(examCourseDetails);
    }

    @Override
    public List<ExamCourseDetailsDTO> getUpcomingExamCourseDetailsForStudent(Long studentId, LocalDate date) throws WitcurveException {
        log.debug("Request to get list of a upcoming examCourseDetails for given student with id : {} and from date : {}", studentId, date);
        StudentStandardDTO studentStandardDTO = studentStandardService.getByStudentId(studentId);
        Grade grade = studentStandardDTO.getStandard().getGrade();
        Long schoolInfoId = studentStandardDTO.getStandard().getSchoolInfo().getId();
        LocalDate endDate = date.plusDays(6);
        List<ExamCourseDetails> examCourseDetails = examCourseDetailsRepository.findByGradeBetweenDatesOrderByGsdStart(grade, date, endDate, schoolInfoId);
        return examCourseDetailsMapper.toDto(examCourseDetails);
    }

    @Override
    public List<ExamCourseDetailsDTO> getDairyCourseDetailsForStudent(Long studentId, LocalDate date) throws WitcurveException {
        log.debug("Request to get list of a diary examCourseDetails for given student with id : {} and from date : {}", studentId, date);
        StudentStandardDTO studentStandardDTO = studentStandardService.getByStudentId(studentId);
        Grade grade = studentStandardDTO.getStandard().getGrade();
        Long schoolInfoId = studentStandardDTO.getStandard().getSchoolInfo().getId();
        LocalDate startDate = date.minusDays(6);
        List<ExamCourseDetails> examCourseDetails = examCourseDetailsRepository.findByGradeBetweenDatesOrderByGsdStart(grade, startDate, date, schoolInfoId);
        return examCourseDetailsMapper.toDto(examCourseDetails);

    }

    @Override
    public List<ExamCourseDetailsDTO> getExamCourseDetailsForStaffOnDate(Long staffId, LocalDate date) throws WitcurveException {
        log.debug("Request to get list of a week examCourseDetails for given staff with id : {} and from date : {}", staffId, date);
        List<ExamCourseDetails> result = new ArrayList<>();
        List<CourseTeacher> courseTeachers = courseTeacherRepository.findByTeacherId(staffId);
        if(courseTeachers.size()!=0) {
            Set<Long> couseIds = courseTeachers
                .stream()
                .map(CourseTeacher::getCourse)
                .map(s -> s.getId())
                .collect(Collectors.toSet());
            result = examCourseDetailsRepository.findByCoursesBetweenDateOrderByGsdStart(new ArrayList<>(couseIds), date, date);
        }
        return examCourseDetailsMapper.toDto(result);
    }

    @Override
    public List<ExamCourseDetailsDTO> getExamCourseDetailsOnAGivenMonthForStaff(Long staffId, Integer month, Integer year) throws WitcurveException {
        log.debug("Request to get list of examCourseDetails for given staff with id : {} for month : {} and on year : {}", staffId, month, year);
        LocalDate monthStart = LocalDate.of(year,month,1);
        LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);
        List<ExamCourseDetails> result = new ArrayList<>();
        List<CourseTeacher> courseTeachers = courseTeacherRepository.findByTeacherId(staffId);
        if(courseTeachers.size()!=0) {
            Set<Long> couseIds = courseTeachers
                .stream()
                .map(CourseTeacher::getCourse)
                .map(s -> s.getId())
                .collect(Collectors.toSet());
            result = examCourseDetailsRepository.findByCoursesBetweenDateOrderByGsdStart(new ArrayList<>(couseIds), monthStart, monthEnd);
        }
        return examCourseDetailsMapper.toDto(result);
    }

    @Override
    public List<ExamCourseDetailsDTO> getUpcomingExamCourseDetailsForStaff(Long staffId, LocalDate date) throws WitcurveException {
        log.debug("Request to get list of a upcoming examCourseDetails for given staff with id : {} and from date : {}", staffId, date);
        LocalDate endDate = date.plusDays(6);
        List<ExamCourseDetails> result = new ArrayList<>();
        List<CourseTeacher> courseTeachers = courseTeacherRepository.findByTeacherId(staffId);
        if(courseTeachers.size()!=0) {
            Set<Long> couseIds = courseTeachers
                .stream()
                .map(CourseTeacher::getCourse)
                .map(s -> s.getId())
                .collect(Collectors.toSet());
            result = examCourseDetailsRepository.findByCoursesBetweenDateOrderByGsdStart(new ArrayList<>(couseIds), date, endDate);
        }
        return examCourseDetailsMapper.toDto(result);
    }

    @Override
    public List<ExamCourseDetailsDTO> getDiaryExamCourseDetailsForStaff(Long staffId, LocalDate date) throws WitcurveException {
        log.debug("Request to get list of a diary examCourseDetails for given staff with id : {} and from date : {}", staffId, date);
        LocalDate startDate = date.minusDays(6);
        List<ExamCourseDetails> result = new ArrayList<>();
        List<CourseTeacher> courseTeachers = courseTeacherRepository.findByTeacherId(staffId);
        if(courseTeachers.size()!=0) {
            Set<Long> couseIds = courseTeachers
                .stream()
                .map(CourseTeacher::getCourse)
                .map(s -> s.getId())
                .collect(Collectors.toSet());
            result = examCourseDetailsRepository.findByCoursesBetweenDateOrderByGsdStart(new ArrayList<>(couseIds), startDate, date);
        }
        return examCourseDetailsMapper.toDto(result);
    }



    @Override
    public void deleteExamCourseDetails(List<Long> ecdIds) throws WitcurveException {
        log.debug("Request to delete examCourseDetails by id : {}", ecdIds);
        for(Long examCourseDetailsId : ecdIds) {
            Optional<ExamCourseDetails> examCourseDetails = examCourseDetailsRepository.findById(examCourseDetailsId);
            if(!examCourseDetails.isPresent()) {
                throw new WitcurveException("No ExamCourseDetails exists for given id");
            }
            if(!examCourseDetails.get().getGsd().getExam().getStatus().equals(ExamStatus.DRAFT)) {
                throw new WitcurveException("Only DRAFT exam course slots can be deleted");
            }
            examCourseDetailsRepository.delete(examCourseDetails.get());
        }
    }

}
