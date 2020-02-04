package com.witcurve.service.impl;

import com.witcurve.domain.*;
import com.witcurve.domain.enumeration.CourseType;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.domain.enumeration.ViewType;
import com.witcurve.repository.*;
import com.witcurve.service.*;
import com.witcurve.service.dto.*;
import com.witcurve.service.mapper.CourseMapper;
import com.witcurve.service.util.CourseComparator;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    private final Logger log = LoggerFactory.getLogger(CourseServiceImpl.class);

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    CourseContentService courseContentService;

    @Autowired
    EventService eventService;

    @Autowired
    StandardRepository standardRepository;

    @Autowired
    AcademicSessionService academicSessionService;

    @Autowired
    CourseTeacherRepository courseTeacherRepository;

    @Autowired
    SchoolInfoRepository schoolInfoRepository;

    @Autowired
    StudentCourseService studentCourseService;

    @Autowired
    StudentCourseRepository studentCourseRepository;

    @Autowired
    AcademicSessionRepository academicSessionRepository;

    @Autowired
    StudentStandardRepository studentStandardRepository;

    @Autowired
    ExamCourseDetailsRepository examCourseDetailsRepository;

    @Autowired
    ExamRepository examRepository;

    @Autowired
    ReportCardDesignRepository reportCardDesignRepository;

    @Override
    public CourseDTO saveOrUpdate(CourseDTO courseDTO) {
        log.debug("Request to save or update Course: {}", courseDTO);
        Boolean mandatory = null;
        List<Course> existingCourses = courseRepository.findBySchoolInfoAndGradeAndCourseCode(courseDTO.getSchoolInfoId(),
            courseDTO.getGrade(), courseDTO.getCourseCode());
        if (courseDTO.getId() == null) {
            for (Course existingCourse : existingCourses) {
                if (existingCourse.getActive()) {
                    throw new WitcurveException("There already exists a subject code with given subject code details for this grade");
                }
            }
        } else {
            Optional<Course> courseWithId = courseRepository.findById(courseDTO.getId());
            if (!courseWithId.isPresent()) {
                throw new WitcurveException("No Course with given id " + courseDTO.getId());
            }
            mandatory = courseWithId.get().getMandatory();
            for (Course existingCourse : existingCourses) {
                if (existingCourse.getActive() && !existingCourse.getId().equals(courseDTO.getId())) {
                    throw new WitcurveException("There already exists a subject code with given subject code details for this grade");
                }

            }
        }
        if (courseDTO.getCourseType().equals(CourseType.SCHOLASTIC)) {
            if (courseDTO.getElective() && courseDTO.getMandatory() || !courseDTO.getMandatory() && !courseDTO.getElective()) {
                throw new WitcurveException("Both elective and mandatory can't be true or false at same time");
            }
        } else if (courseDTO.getCourseType().equals(CourseType.NON_SCHOLASTIC)) {
            courseDTO.setElective(false);
            courseDTO.setMandatory(false);
        }
        Course course = courseMapper.toEntity(courseDTO);
        course = courseRepository.save(course);
        if (mandatory != null) {
            if (mandatory && !course.getMandatory()) {
                List<Long> studentCourseIds = studentCourseRepository.getByCourseId(course.getId());
                studentCourseService.deactivateStudentCourseByIds(studentCourseIds);
            }
            if (!mandatory && course.getMandatory()) {
                createOrUpdateStudentCourse(course);
            }
        } else {
            if (course.getMandatory()) {
                createOrUpdateStudentCourse(course);
            }
        }
        return courseMapper.toDto(course);
    }

    @Override
    public CourseDTO getCourseById(Long courseId) throws WitcurveException {
        log.debug("Request to get course with id : {}", courseId);
        Optional<Course> course = courseRepository.findById(courseId);
        if (!course.isPresent()) {
            throw new WitcurveException("No Course with given id " + courseId);
        }
        return courseMapper.toDto(course.get());
    }


    @Override
    public List<CourseDTO> getCourseBySchoolInfoAndGrade(Long schoolInfoId, Grade grade, CourseType courseType, Boolean elective, Boolean mandatory) throws WitcurveException {
        log.debug("Request to get courses in schoolInfo {} with grade : {}", schoolInfoId, grade);
        Optional<SchoolInfo> schoolInfo = schoolInfoRepository.findById(schoolInfoId);
        List<Course> courses = null;
        if (!schoolInfo.isPresent()) {
            throw new WitcurveException("No SchoolInfo with given id " + schoolInfoId);
        }
        if (courseType == null) {
            courses = courseRepository.findBySchoolInfoAndGrade(schoolInfoId, grade);
        } else {
            if (courseType.equals(CourseType.SCHOLASTIC)) {
                if (elective && !mandatory) {
                    courses = courseRepository.findElectiveCourse(schoolInfoId, grade);
                } else if (!elective && mandatory) {
                    courses = courseRepository.findMandatoryCourse(schoolInfoId, grade);
                } else {
                    courses = courseRepository.findBySchoolInfoAndGradeAndCourseType(schoolInfoId, grade, courseType);
                }
            } else {
                courses = courseRepository.findBySchoolInfoAndGradeAndCourseType(schoolInfoId, grade, courseType);
            }
        }
        return courseMapper.toDto(courses);
    }

    @Override
    public List<CourseDTO> getCourseByExamIdAndGradeAndCourseType(Long examId, Grade grade, CourseType courseType) {
        log.debug("Request to get courses for exam with id : {} and for grade : {} of course type {}", examId, grade, courseType);
        List<CourseDTO> result = new ArrayList<>();
        Optional<Exam> exam = examRepository.findById(examId);
        if (!exam.isPresent()) {
            throw  new WitcurveException("No Exam with given Id " + examId);
        }
        AcademicSession academicSession = academicSessionRepository.nearestSessionToDate(exam.get().getSchoolInfo().getId(), exam.get().getEndDate());
        if(academicSession == null) {
            throw new WitcurveException("No nearest active session found");
        }
        if(courseType.equals(CourseType.SCHOLASTIC)) {
            List<Course> courses = examCourseDetailsRepository.findScholasticCourse(grade, exam.get().getSchoolInfo().getId(), academicSession.getStartDate(), exam.get().getEndDate());
            Collections.sort(courses, new CourseComparator());
            result = courseMapper.toDto(courses);
        } else {
            Set<Course> courses = new HashSet<>();
            List<ReportCardDesign> rcds = reportCardDesignRepository.getNonScholasticReportCardDesigns(grade, exam.get().getSchoolInfo().getId(), academicSession.getStartDate(), exam.get().getEndDate().plusDays(1));
            for(ReportCardDesign reportCardDesign : rcds) {
                courses.addAll(reportCardDesign.getCourses());
            }
            List<Course> courseList = new ArrayList<>(courses);
            Collections.sort(courseList, new CourseComparator());
            result = courseMapper.toDto(courseList);
        }
        return result;
    }

    @Override
    public List<CourseDTO> getCourseByStudentId(Long studentId) {
        List<Course> courses = studentCourseRepository.getByStudentId(studentId);
        return courseMapper.toDto(courses);
    }

    @Override
    public void deleteCourse(Long courseId) throws WitcurveException {
        log.debug("Request to delete course with id {}", courseId);
        Optional<Course> course = courseRepository.findById(courseId);
        if (!course.isPresent()) {
            throw new WitcurveException("No Course with given id " + courseId);
        }
        List<CourseTeacher> courseTeachers = courseTeacherRepository.findByCourseId(courseId);
        if (courseTeachers.size() != 0) {
            throw new WitcurveException("There are some faculty assigned to this course, please deactivate them and try again");
        }
        course.get().setActive(false);
        List<Long> studentCourseIds = studentCourseRepository.getByCourseId(courseId);
        studentCourseService.deactivateStudentCourseByIds(studentCourseIds);
    }

    @Override
    public CourseTrackDTO getCourseTrackById(Long courseId, Long staffId) throws WitcurveException {
        CourseDTO courseDTO = getCourseById(courseId);
        List<CourseContentDTO> courseContentDTOS = courseContentService.getCourseContentsByCourseId(courseId, Boolean.TRUE);

        AcademicSessionDTO currentSession = academicSessionService.getCurrentSessionByDate(courseDTO.getSchoolInfoId(), LocalDate.now());
        List<Standard> standards = standardRepository.findByGradeAndSchoolInfoId(courseDTO.getGrade(), courseDTO.getSchoolInfoId());

        Map<String, Map<String, Long>> sectionTopicCountMap = new HashMap<>();
        for (Standard standard : standards) {
            sectionTopicCountMap.computeIfAbsent(standard.getSection(), k -> new HashMap<>());
            Map<String, Long> topicCountMap = sectionTopicCountMap.get(standard.getSection());

            List<EventDTO> dailyUpdates = eventService.findAllTestAndAssignmentAndDailyUpdateByStandardAndCourse(Pageable.unpaged(), currentSession.getStartDate(),
                currentSession.getStartDate().plusYears(1).minusDays(1), ViewType.DAILY_UPDATE, standard.getId(), courseDTO.getId(), staffId).getContent();

            for (CourseContentDTO courseContentDTO : courseContentDTOS) {
                Long count = dailyUpdates.stream()
                    .filter(eventDTO -> !CollectionUtils.isEmpty(eventDTO.getCourseContentIds()) && eventDTO.getCourseContentIds().contains(courseContentDTO.getId()))
                    .count();
                topicCountMap.put(courseContentDTO.getIndex(), count);
            }
        }

        return new CourseTrackDTO(courseDTO, courseContentDTOS, sectionTopicCountMap);
    }

    private void createOrUpdateStudentCourse(Course course) {
        AcademicSession academicSession = academicSessionRepository.nearestSessionToDate(course.getSchoolInfo().getId(), LocalDate.now());
        List<Long> listOfIds = studentStandardRepository.getBySessionIdAndGrade(academicSession.getId(), course.getGrade());
        List<StudentCourseDTO> studentCourseDTOs = new ArrayList<>();
        if (!listOfIds.isEmpty()) {
            for (Long id : listOfIds) {
                StudentCourseDTO studentCourseDTO = new StudentCourseDTO();
                studentCourseDTO.setStudentStandardId(id);
                studentCourseDTO.setCourseId(course.getId());
                studentCourseDTOs.add(studentCourseDTO);
            }
            studentCourseService.saveOrUpdate(studentCourseDTOs);
        }

    }
}
