package com.witcurve.service.impl;

import com.witcurve.domain.ConfigSettings;
import com.witcurve.domain.Course;
import com.witcurve.domain.enumeration.ConfigType;
import com.witcurve.repository.*;
import com.witcurve.service.CourseService;
import com.witcurve.service.ReportCardService;
import com.witcurve.service.dto.CourseDTO;
import com.witcurve.service.util.HtmlToPdfUtil;
import com.witcurve.service.util.WitcurveUtil;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.vm.ReportCardVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReportCardServiceImpl implements ReportCardService {

    private final Logger log = LoggerFactory.getLogger(ReportCardServiceImpl.class);

    @Autowired
    ReportCardDesignRepository reportCardDesignRepository;

    @Autowired
    ConfigSettingsRepository configSettingsRepository;

    @Autowired
    ExamRepository examRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    StudentStandardRepository studentStandardRepository;

    @Autowired
    ExamCourseDetailsRepository examCourseDetailsRepository;

    @Autowired
    CourseService courseService;

    @Autowired
    StudentMarksRepository studentMarksRepository;

    public File getReportCardTemplatePdf(ReportCardVM reportCardVM, String templateUrl) {
        isValid(reportCardVM);
        HtmlToPdfUtil htmlToPdfUtil = new HtmlToPdfUtil();
        File inputFile = htmlToPdfUtil.getParsedReportCard(reportCardVM, templateUrl);
        return htmlToPdfUtil.htmlToPdf(inputFile);
    }

    public File getReportCardTemplateHtml(ReportCardVM reportCardVM, String templateUrl) {
        isValid(reportCardVM);
        HtmlToPdfUtil htmlToPdfUtil = new HtmlToPdfUtil();
        File inputFile = htmlToPdfUtil.getParsedReportCard(reportCardVM, templateUrl);
        String xml = htmlToPdfUtil.getReportHtmlXml(inputFile);
        try {
            File result = WitcurveUtil.createTempFile("result-template.html");
            FileWriter fw = new FileWriter(result);
            fw.write(xml);
            fw.close();
            return result;
        } catch (IOException e) {
            log.debug("There was problem while creating template : {}", e.getMessage());
            throw new WitcurveException("There was problem while creating template");
        }
    }

    public List<ReportCardVM> getReportCardDetailsForStandard(Long standardId, Long examId, String bindingId) {
        anyOne(examId, bindingId);
        List<ReportCardVM> result = new ArrayList<>();
        ReportCardVM reportCardVM = null;
        Map<String, String> colorForGrades;
        Map<String, String> definingGrades;
        Map<String, Object> marksAndGradesMap = null;
        Map<String, Object> marksAndGradesValueMap = new HashMap<>();
//        if(examId != null) {
//            Optional<Exam> exam = examRepository.findById(examId);
//            if(!exam.isPresent()) {
//                throw new WitcurveException("No Exam with given Id " + examId);
//            }
//            if(exam.get().getStatus().equals(ExamStatus.DRAFT)) {
//                throw new WitcurveException("Draft exams cannot have report card design");
//            }
//            String examName = exam.get().getName().toUpperCase();
//            String schoolName = exam.get().getSchoolInfo().getSchool().getName().toUpperCase();
//            List<ReportCardDesign> reportCardDesigns = reportCardDesignRepository.findByExam(examId);
//            colorForGrades = getGradeDetails(exam.get().getSchoolInfo().getSchool().getId(), ConfigType.GRADING_SCALE_COLOR);
////            List<ConfigSettings>
////            definingGrades = getGradeDetails();
//            List<StudentStandard> studentStandards = studentStandardRepository.getByStandardId(standardId);
//            for(StudentStandard  studentStandard : studentStandards)  {
//                reportCardVM = new ReportCardVM();
//                reportCardVM.setExamName(examName);
//                reportCardVM.setAdmissionId(studentStandard.getStudent().getAdmissionId());
//                reportCardVM.setStandard(studentStandard.getStandard().getGrade().toString());
//                reportCardVM.setStudentName(studentStandard.getStudent().getFirstName()+" "+studentStandard.getStudent().getLastName());
//                reportCardVM.setRollNo(studentStandard.getRollNo());
//                List<Course> courses = examCourseDetailsRepository.findCoursesByGradesAndExamId(Arrays.asList(studentStandard.getStandard().getGrade()), examId);
//                courses = getCoursesForStudentId(courses, studentStandard.getStudent().getId());
//                for(ReportCardDesign reportCardDesign : reportCardDesigns) {
//                    marksAndGradesValueMap = new HashMap<>();
//                    marksAndGradesValueMap.put("showGrade", reportCardDesign.getShowGradesOnly());
//                    marksAndGradesValueMap.put("showMarks", reportCardDesign.getShowMarksOnly());
//                    if(reportCardDesign.getFieldType().equals(ReportFieldType.MAIN)) {
//                        List<StudentMarks> studentMarks = studentMarksRepository.getByStudentIdForExam(examId, studentStandard.getStudent().getId(), Arrays.asList(Boolean.TRUE));
////                        updateMarksAndGrades(marksAndGradesMap, studentMarks, courses);
//                    } else if(reportCardDesign.getFieldType().equals(ReportFieldType.MANUAL_ENTRY)) {
//
//                    } else if(reportCardDesign.getFieldType().equals(ReportFieldType.REMARKS)) {
//                        reportCardVM.setShowRemarks(true);
//                        //add remarks
//                    } else if(reportCardDesign.getFieldType().equals(ReportFieldType.ATTENDANCE)) {
//                        reportCardVM.setShowAttendance(true);
//                        //calculate attendance
//                    } else {
//
//                    }
//                }
//            }
//        } else {
//            List<Event> events = eventRepository.findPeriodicEventsByBindingId(bindingId);
//            if(events.size() == 0) {
//                throw new WitcurveException("No Periodic Test exists with given bindingId " + bindingId);
//            }
//            String examName = events.get(0).getName().toUpperCase();
//            String schoolName = events.get(0).getSchoolInfo().getSchool().getName().toUpperCase();
//            List<ReportCardDesign> reportCardDesigns = reportCardDesignRepository.findByExam(examId);
//            colorForGrades = getGradeDetails(events.get(0).getSchoolInfo().getSchool().getId(), ConfigType.GRADING_SCALE_COLOR);
//            definingGrades = getGradeDetails(events.get(0).getSchoolInfo().getSchool().getId(), ConfigType.GRADING_SCALE);
//            List<StudentStandard> studentStandards = studentStandardRepository.getByStandardId(standardId);
//            for(StudentStandard  studentStandard : studentStandards)  {
//                reportCardVM = new ReportCardVM();
//                reportCardVM.setExamName(examName);
//                reportCardVM.setAdmissionId(studentStandard.getStudent().getAdmissionId());
//                reportCardVM.setStandard(studentStandard.getStandard().getGrade().toString());
//                reportCardVM.setStudentName(studentStandard.getStudent().getFirstName().toUpperCase()+" "+studentStandard.getStudent().getLastName());
//                reportCardVM.setRollNo(studentStandard.getRollNo());
//                List<Course> courses = eventRepository.findPeriodicEventCoursesByBindingId(bindingId);
//                courses = getCoursesForStudentId(courses, studentStandard.getStudent().getId());
//
//                for(ReportCardDesign reportCardDesign : reportCardDesigns) {
//                    marksAndGradesValueMap.put("showGrade", reportCardDesign.getShowGradesOnly());
//                    marksAndGradesValueMap.put("showMarks", reportCardDesign.getShowMarksOnly());
//                    if(reportCardDesign.getFieldType().equals(ReportFieldType.MAIN)) {
//
//                    } else if(reportCardDesign.getFieldType().equals(ReportFieldType.MANUAL_ENTRY)) {
//
//                    } else if(reportCardDesign.getFieldType().equals(ReportFieldType.REMARKS)) {
//
//                    } else if(reportCardDesign.getFieldType().equals(ReportFieldType.ATTENDANCE)) {
//
//                    } else {
//
//                    }
//                }
//            }
//
//        }
        return null;
    }

    private Map<String, String> getGradeDetails(Long schoolId, ConfigType configType) {
        Integer max = 100;
        Map<String, String> result = new HashMap<>();
        ConfigType[] configTypes = new ConfigType[0];
        configTypes[0] = configType;
        List<ConfigSettings> configSettingsList = configSettingsRepository.getConfigSettingsBySchoolIdAndTypes(schoolId, configTypes);
        for (ConfigSettings configSettings : configSettingsList) {
            if (configType.equals(ConfigType.GRADING_SCALE_COLOR)) {
                result.put(configSettings.getDisplayFieldName(), configSettings.getFieldValue());
            } else {
                Integer min = Integer.parseInt(configSettings.getFieldValue());
                if (min != null) {
                    result.put(configSettings.getDisplayFieldName(), min.toString() + "-" + max.toString());
                }
                max = min - 1;
            }
        }
        return result;
    }


    private List<Course> getCoursesForStudentId(List<Course> courses, Long studentId) {
        Map<Long, Course> examCoursesMap = courses.stream().collect(Collectors.toMap(Course::getId, course -> course));
        List<CourseDTO> validCourses = new ArrayList<>(); //courseService.getCourseByStudentId(studentId);
        List<Course> result = new ArrayList<>();
        for (CourseDTO courseDTO : validCourses) {
            if (examCoursesMap.keySet().contains(courseDTO.getId())) {
                result.add(examCoursesMap.get(courseDTO.getId()));
            }
        }
        return result;
    }

//    private void updateMarksAndGrades(Map<String, Object>marksAndGradesMap, List<StudentMarks> studentMarks, List<Course> courses, ) {
//
//    }


    private void anyOne(Long examId, String bindingId) {
        if (examId == null && bindingId == null) {
            throw new WitcurveException("Both examId and bindingId cannot be null");
        }
        if (examId != null && bindingId != null) {
            throw new WitcurveException("Both examId and bindingId cannot be not null");
        }
    }

    private void isValid(ReportCardVM reportCardVM) {
        if (!reportCardVM.getScholastic().getScholasticDetails().getOverall().getShowGrade() && !reportCardVM.getScholastic().getScholasticDetails().getOverall().getShowMarks()) {
            throw new WitcurveException("Both showMarks and showGrade can't be false at the same time");
        }
        if (reportCardVM.getScholastic().getScholasticDetails().getOverall().getShowGrade()) {
            if (reportCardVM.getScholastic().getScholasticDetails().getOverall().getGrade() == null) {
                throw new WitcurveException("Grades require to show grade");
            }
        }
        if (reportCardVM.getScholastic().getScholasticDetails().getOverall().getShowMarks()) {
            if (reportCardVM.getScholastic().getScholasticDetails().getOverall().getMarks() == null) {
                throw new WitcurveException("Marks require to show marks");
            }
        }
        List<ReportCardVM.ScholasticVM.ScholasticDetailsVM.ExamDetailsVM> listOfExamDetailsVM = reportCardVM.getScholastic().getScholasticDetails().getExamDetails();
        for (ReportCardVM.ScholasticVM.ScholasticDetailsVM.ExamDetailsVM examDetailsVM : listOfExamDetailsVM) {

            List<ReportCardVM.ScholasticVM.ScholasticDetailsVM.ExamDetailsVM.MarksAndGradeDetailsVM> listOfMarksAndGradeDetail = examDetailsVM.getMarksAndGradesDetails();
            for (ReportCardVM.ScholasticVM.ScholasticDetailsVM.ExamDetailsVM.MarksAndGradeDetailsVM marksAndGradeDetailsVM : listOfMarksAndGradeDetail) {
                if (!marksAndGradeDetailsVM.getShowGrade() && !marksAndGradeDetailsVM.getShowMarks()) {
                    throw new WitcurveException("Both showMarks and showGrade can't be false at same time");
                }
                if (marksAndGradeDetailsVM.getShowMarks()) {
                    if (marksAndGradeDetailsVM.getMarks() == null) {
                        throw new WitcurveException("Marks require to show marks");
                    }
                }
                if (marksAndGradeDetailsVM.getShowGrade()) {
                    if (marksAndGradeDetailsVM.getGrade() == null) {
                        throw new WitcurveException("Grades require to show grade");
                    }
                }
            }
        }
    }
}
