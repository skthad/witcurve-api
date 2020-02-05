package com.witcurve.service.impl;

import com.witcurve.domain.*;
import com.witcurve.domain.enumeration.CourseType;
import com.witcurve.domain.enumeration.ReportFieldType;
import com.witcurve.repository.*;
import com.witcurve.service.AnalyticsService;
import com.witcurve.service.StudentMarksService;
import com.witcurve.service.StudentService;
import com.witcurve.service.dto.SectionPerformanceDTO;
import com.witcurve.service.dto.StudentMarksDTO;
import com.witcurve.service.dto.StudentPerformanceDTO;
import com.witcurve.service.dto.SubjectPerformanceDTO;
import com.witcurve.service.util.CourseComparator;
import com.witcurve.service.util.WitcurveUtil;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.vm.StudentPerformanceDashboardVM;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class AnalyticsServiceImpl implements AnalyticsService {

    private final Logger log = LoggerFactory.getLogger(AnalyticsServiceImpl.class);

    @Autowired
    private StudentMarksService studentMarksService;

    @Autowired
    private StudentService studentService;

    @Autowired
    AcademicSessionRepository academicSessionRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    StudentStandardRepository studentStandardRepository;

    @Autowired
    ExamCourseDetailsRepository examCourseDetailsRepository;

    @Autowired
    StudentMarksRepository studentMarksRepository;

    @Autowired
    ReportCardDesignRepository reportCardDesignRepository;

    @Override
    public List<SectionPerformanceDTO> getSectionPerformance(Long examId) {
        Map<String, Map<String, Double>> sectionSubjectTotalMap = new HashMap<>();
        Map<String, Map<String, Integer>> sectionSubjectCountMap = new HashMap<>();
        Map<String, Integer> subjectFullMarksMap = new HashMap<>();
        Map<String, Long> sectionStandardIdMap = new HashMap<>();

        List<SectionPerformanceDTO> sectionPerformanceList = new ArrayList<>();

        List<StudentMarksDTO> studentMarksDTOList = studentMarksService.getStudentMarksByExamId(examId, null, null, null);

        for (StudentMarksDTO studentMarksDTO : studentMarksDTOList) {
            String masterSubject = studentMarksDTO.getCourseDTO().getMasterSubject();
            String sectionName = studentMarksDTO.getSection();

            if (StringUtils.isNotEmpty(sectionName)) {
                if (sectionSubjectCountMap.get(sectionName) == null) {
                    sectionSubjectTotalMap.put(sectionName, new HashMap<>());
                    sectionSubjectCountMap.put(sectionName, new HashMap<>());
                    sectionStandardIdMap.put(sectionName, studentMarksDTO.getStandardId());
                }

                Map<String, Double> subjectTotalMap = sectionSubjectTotalMap.get(sectionName);
                Map<String, Integer> subjectCountMap = sectionSubjectCountMap.get(sectionName);

                if (subjectCountMap.get(masterSubject) == null) {
                    subjectTotalMap.put(masterSubject, 0.0);
                    subjectCountMap.put(masterSubject, 0);
//                    todo fix this after using rcd remark
//                    subjectFullMarksMap.put(masterSubject, studentMarksDTO.getExamCourseDetailsDTO().getFullMarks());
                }

                Double subjectTotal = subjectTotalMap.get(masterSubject);
                Integer subjectCount = subjectCountMap.get(masterSubject);

                subjectCount++;
                subjectTotal += studentMarksDTO.getMarks();

                subjectTotalMap.put(masterSubject, subjectTotal);
                subjectCountMap.put(masterSubject, subjectCount);
            }
        }

        for (String sectionName : sectionSubjectCountMap.keySet()) {
            Double totalScore = 0.0;

            SectionPerformanceDTO sectionPerformanceDTO = new SectionPerformanceDTO();

            Map<String, Double> subjectTotalMap = sectionSubjectTotalMap.get(sectionName);
            Map<String, Integer> subjectCountMap = sectionSubjectCountMap.get(sectionName);

            for (String subjectName : subjectTotalMap.keySet()) {
                Integer studentCount = subjectCountMap.get(subjectName);
                Integer fullMarks = subjectFullMarksMap.get(subjectName);
                Double averagePercentage = WitcurveUtil.roundToTwoDecimal((subjectTotalMap.get(subjectName) * 100) / (studentCount * fullMarks));

                SubjectPerformanceDTO subjectPerformanceDTO = new SubjectPerformanceDTO();
                subjectPerformanceDTO.setSubjectName(subjectName);
                subjectPerformanceDTO.setAverageScore(averagePercentage);
                subjectPerformanceDTO.setStudentCount(studentCount);

                sectionPerformanceDTO.getSubjectPerformances().add(subjectPerformanceDTO);

                totalScore += averagePercentage;
            }

            sectionPerformanceDTO.setSectionName(sectionName);
            sectionPerformanceDTO.setAverageScore(WitcurveUtil.roundToTwoDecimal(totalScore / sectionPerformanceDTO.getSubjectPerformances().size()));
            sectionPerformanceDTO.setStudentCount(studentService.getStudentsByStandardIdAndCourseId(sectionStandardIdMap.get(sectionName), null).size());

            sectionPerformanceList.add(sectionPerformanceDTO);
        }
        return sectionPerformanceList;
    }

    @Override
    public StudentPerformanceDTO getStudentPerformanceByStudentId(Long studentId) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student == null) {
            throw new WitcurveException("No Student present with given studentId: " + studentId);
        }
        AcademicSession academicSession = academicSessionRepository.nearestSessionToDate(student.get().getSchoolInfo().getId(), LocalDate.now());

        List<StudentStandard> studentStandard = studentStandardRepository.getByStudentId(studentId);
        if (studentStandard.isEmpty()) {
            throw new WitcurveException("No StudentStandard active record found");
        }
        List<Exam> exams = reportCardDesignRepository.findExamByGradeSchoolInfoAndFieldType(studentStandard.get(0).getStandard().getGrade(), student.get().getSchoolInfo().getId(), academicSession.getStartDate(), LocalDate.now());
        StudentPerformanceDTO studentPerformanceDTO = new StudentPerformanceDTO();
        List<StudentPerformanceDTO.ExamMarksDTO> listOfExamMarks = new ArrayList<>();

        for (Exam exam : exams) {

            List<ReportCardDesign> reportCardDesigns = reportCardDesignRepository.findByExamAndGrade(exam.getId(), studentStandard.get(0).getStandard().getGrade());

            StudentPerformanceDTO.ExamMarksDTO examMarksDTO = new StudentPerformanceDTO().new ExamMarksDTO();
            List<StudentPerformanceDTO.ExamMarksDTO.CourseMarksDTO> listOfCourseMarks = new ArrayList<>();
            examMarksDTO.setExamName(exam.getName());

            List<Course> listOfCourse = examCourseDetailsRepository.findCoursesByGradesAndExamId(Arrays.asList(studentStandard.get(0).getStandard().getGrade()), exam.getId());
            Collections.sort(listOfCourse, new CourseComparator());

            for (Course course : listOfCourse) {
                StudentPerformanceDTO.ExamMarksDTO.CourseMarksDTO courseMarks = new StudentPerformanceDTO().new ExamMarksDTO().new CourseMarksDTO();

                if (course.getCourseType().equals(CourseType.SCHOLASTIC)) {
                    Double studentMarks = findStudentMarks(studentStandard.get(0), course, reportCardDesigns);
                    Double classAvg = findClassAvg(course, reportCardDesigns);
                    Double sectionAvg = findSectionAverage(studentStandard.get(0), course, reportCardDesigns);

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

    @Override
    public List<StudentPerformanceDashboardVM> getStudentDashboardDetailsBySchoolInfoId(Long schoolInfoId) {
        log.debug("Request to get student analytics dashboard details for school info with id : {}", schoolInfoId);
        List<StudentPerformanceDashboardVM> result = studentStandardRepository.findStudentDashBoardDetailsBySchoolInfoId(schoolInfoId);
        return result;
    }

    private Double findStudentMarks(StudentStandard studentStandard, Course course, List<ReportCardDesign> reportCardDesigns) {
        Double totalMarks = null;
        for (ReportCardDesign reportCardDesign : reportCardDesigns) {
            if (reportCardDesign.getFieldType().equals(ReportFieldType.MAIN) || reportCardDesign.getFieldType().equals(ReportFieldType.MANUAL_ENTRY)) {
                Double marks = studentMarksRepository.getStudentMarksByRcdIdAndCourseIdAndStudentId(reportCardDesign.getId(), course.getId(), studentStandard.getStudent().getId());
                if (marks != null) {
                    if (totalMarks == null)
                        totalMarks = marks;
                    else
                        totalMarks = totalMarks + marks;
                }
            }
        }
        return WitcurveUtil.roundToTwoDecimal(totalMarks);
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
            return WitcurveUtil.roundToTwoDecimal(average);
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
