package com.witcurve.service.impl;

import com.witcurve.domain.*;
import com.witcurve.domain.enumeration.CourseType;
import com.witcurve.domain.enumeration.ReportFieldType;
import com.witcurve.repository.*;
import com.witcurve.service.StudentPerformanceService;
import com.witcurve.service.dto.StudentPerformanceDTO;
import com.witcurve.service.util.CourseComparator;
import com.witcurve.web.rest.errors.WitcurveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class StudentPerformanceServiceImpl implements StudentPerformanceService {

    @Autowired
    AcademicSessionRepository academicSessionRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    ExamRepository examRepository;

    @Autowired
    StudentStandardRepository studentStandardRepository;

    @Autowired
    ExamCourseDetailsRepository examCourseDetailsRepository;

    @Autowired
    StudentMarksRepository studentMarksRepository;

    @Autowired
    ReportCardDesignRepository reportCardDesignRepository;

    @Override
    public StudentPerformanceDTO getStudentPerformanceByStudentId(Long studentId) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student == null) {
            throw new WitcurveException("no Student present with given studentId: " + studentId);
        }
        AcademicSession academicSession = academicSessionRepository.nearestActiveSessionToDate(student.get().getSchoolInfo().getId(), LocalDate.now());

        List<StudentStandard> studentStandard = studentStandardRepository.getByStudentId(studentId);
        if (studentStandard.isEmpty()) {
            throw new WitcurveException("no StudentStandard active record found");
        }
        List<Exam> exams = examRepository.findByAcademicsSessionGradeAndReportCardDesign(student.get().getSchoolInfo().getId(), academicSession.getStartDate(), LocalDate.now(), studentStandard.get(0).getStandard().getGrade());
        StudentPerformanceDTO studentPerformanceDTO = new StudentPerformanceDTO();
        List<StudentPerformanceDTO.ExamMarksDTO> listOfExamMarks = new ArrayList<>();

        for (Exam exam : exams) {

            StudentPerformanceDTO.ExamMarksDTO examMarksDTO = new StudentPerformanceDTO.ExamMarksDTO();
            List<StudentPerformanceDTO.ExamMarksDTO.CourseMarksDTO> listOfCourseMarks = new ArrayList<>();
            examMarksDTO.setExamName(exam.getName());

            List<Course> listOfCourse = examCourseDetailsRepository.findCoursesByGradesAndExamId(Arrays.asList(studentStandard.get(0).getStandard().getGrade()), exam.getId());
            Collections.sort(listOfCourse, new CourseComparator());
            for (Course course : listOfCourse) {
                StudentPerformanceDTO.ExamMarksDTO.CourseMarksDTO courseMarks = new StudentPerformanceDTO.ExamMarksDTO.CourseMarksDTO();
                if (course.getCourseType().equals(CourseType.SCHOLASTIC)) {
                    //studentMarks
                    List<ReportCardDesign> reportCardDesign = reportCardDesignRepository.findByExamAndGrade(exam.getId(), studentStandard.get(0).getStandard().getGrade());
                    Double studentMarks = findStudentMarks(studentStandard.get(0), course, reportCardDesign);
                    Double classAvg = findClassAvg(course, reportCardDesign);
                    Double sectionAvg = findSectionAverage(studentStandard.get(0), course, reportCardDesign);

                    courseMarks.setSubjectName(course.getDisplayName());
                    courseMarks.setMarks(studentMarks);
                    courseMarks.setClassAvg(classAvg);
                    courseMarks.setSectionAvg(sectionAvg);
                    listOfCourseMarks.add(courseMarks);
                }
            }
            examMarksDTO.setCourseMarks(listOfCourseMarks);
            listOfExamMarks.add(examMarksDTO);
            studentPerformanceDTO.setExams(listOfExamMarks);
        }

        return studentPerformanceDTO;
    }

    private Double findStudentMarks(StudentStandard studentStandard, Course course, List<ReportCardDesign> reportCardDesigns) {
        Double totalMarks = 0.0;
        for (ReportCardDesign reportCardDesign : reportCardDesigns) {
            if (reportCardDesign.getFieldType().equals(ReportFieldType.MAIN) || reportCardDesign.getFieldType().equals(ReportFieldType.MANUAL_ENTRY)) {
                Double marks = studentMarksRepository.getStudentMarksByRcdIdAndCourseIdAndStudentId(reportCardDesign.getId(), course.getId(), studentStandard.getStudent().getId());
                if (marks != null)
                    totalMarks = totalMarks + marks;
            }
        }
        if (totalMarks != 0.0) {
            return totalMarks;
        }
        return null;
    }

    private Double findClassAvg(Course course, List<ReportCardDesign> reportCardDesigns) {
        List<Double> listOfAvg = new ArrayList<>();
        List<Integer> listOfCounts = new ArrayList<>();

        for (ReportCardDesign reportCardDesign : reportCardDesigns) {
            if (reportCardDesign.getFieldType().equals(ReportFieldType.MAIN) || reportCardDesign.getFieldType().equals(ReportFieldType.MANUAL_ENTRY)) {
                Double avg = studentMarksRepository.getAvg(course.getId(), reportCardDesign.getId(), reportCardDesign.getGrade());
                Integer count = studentMarksRepository.getCount(course.getId(), reportCardDesign.getId(), reportCardDesign.getGrade());
                if (count != 0) {
                    listOfCounts.add(count);
                    listOfAvg.add(avg);
                }
            }
        }
        return getAverage(listOfCounts, listOfAvg);
    }

    private Double getAverage(List<Integer> listOfCount, List<Double> listOfAverage) {
        if (listOfCount.size() != 0) {
            Integer maxCount = Collections.max(listOfCount);
            Double average = 0.0;
            for (int i = 0; i < listOfCount.size(); i++) {
                average = average + listOfCount.get(i) * listOfAverage.get(i) / maxCount;
            }
            return average;
        }
        return null;
    }

    private Double findSectionAverage(StudentStandard studentStandard, Course course, List<ReportCardDesign> reportCardDesigns) {
        List<Double> listOfAvg = new ArrayList<>();
        List<Integer> listOfCounts = new ArrayList<>();
        for (ReportCardDesign reportCardDesign : reportCardDesigns) {
            if (reportCardDesign.getFieldType().equals(ReportFieldType.MAIN) || reportCardDesign.getFieldType().equals(ReportFieldType.MANUAL_ENTRY)) {
                Double avg = studentMarksRepository.getAvgOfStandard(course.getId(), reportCardDesign.getId(), studentStandard.getStandard().getId());
                Integer count = studentMarksRepository.getCountOfStandard(course.getId(), reportCardDesign.getId(), studentStandard.getStandard().getId());
                if (count != 0) {
                    listOfCounts.add(count);
                    listOfAvg.add(avg);
                }
            }
        }
        return getAverage(listOfCounts, listOfAvg);
    }
}
