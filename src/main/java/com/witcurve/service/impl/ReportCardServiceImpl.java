package com.witcurve.service.impl;

import com.witcurve.domain.ConfigSettings;
import com.witcurve.domain.Course;
import com.witcurve.domain.ReportCard;
import com.witcurve.domain.ReportCardDesign;
import com.witcurve.domain.enumeration.*;
import com.witcurve.repository.*;
import com.witcurve.service.CourseService;
import com.witcurve.service.ReportCardService;
import com.witcurve.service.dto.CourseDTO;
import com.witcurve.service.dto.ReportCardDTO;
import com.witcurve.service.dto.ReportCardDesignDTO;
import com.witcurve.service.dto.ScholasticReportDetailsDTO;
import com.witcurve.service.mapper.ReportCardMapper;
import com.witcurve.service.util.HtmlToPdfUtil;
import com.witcurve.service.util.WitcurveUtil;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.vm.ReportCardVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
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

    @Autowired
    ReportCardMapper reportCardMapper;

    @Autowired
    ReportCardRepository reportCardRepository;

    @Autowired
    CourseRepository courseRepository;

    private final List<ReportFieldType> SCHOLASTIC_FIELD_TYPE_LISTS = Arrays.asList(ReportFieldType.MAIN, ReportFieldType.MANUAL_ENTRY, ReportFieldType.PERIODIC_TEST, ReportFieldType.TOTAL);


    public File getReportCardTemplatePdf(ReportCardVM reportCardVM, String templateUrl)  {
        HtmlToPdfUtil htmlToPdfUtil = new HtmlToPdfUtil();
        File inputFile = htmlToPdfUtil.getParsedReportCard(reportCardVM, templateUrl);
        return htmlToPdfUtil.htmlToPdf(inputFile);

    }

    public File getReportCardTemplateHtml(ReportCardVM reportCardVM, String templateUrl)  {
        HtmlToPdfUtil htmlToPdfUtil = new HtmlToPdfUtil();
        File inputFile = htmlToPdfUtil.getParsedReportCard(reportCardVM, templateUrl);
        String xml = htmlToPdfUtil.getReportHtmlXml(inputFile);
        try {
            File result = WitcurveUtil.createTempFile("result-template.html");
            FileWriter fw=new FileWriter(result);
            fw.write(xml);
            fw.close();
            return result;
        } catch (IOException e) {
            log.debug("There was problem while creating template : {}", e.getMessage());
            throw new WitcurveException("There was problem while creating template");
        }
    }

    @Override
    public ReportCardDTO saveOrUpdate(ReportCardDTO reportCardDTO) {
        log.debug("Request to save or update report card : {}", reportCardDTO);
        isValidReportCard(reportCardDTO);
        ReportCard reportCard = reportCardMapper.toEntity(reportCardDTO);
        reportCard = reportCardRepository.save(reportCard);
        return reportCardMapper.toDto(reportCard);
    }

    @Override
    public List<ReportCardDTO> getReportCardByExam(Long examId, Grade grade) {
        log.debug("Request to get report cards for exam with id : {} and grade : {}", examId, grade);
        List<ReportCardDTO> result = new ArrayList<>();
        if(grade == null) {
            result = reportCardMapper.toDto(reportCardRepository.findByExamId(examId));
        }else {
            result.add(reportCardMapper.toDto(reportCardRepository.findByExamIdAndGrade(examId, grade)));
        }
        return result;
    }

    @Override
    public ReportCardDTO findOne(Long id) {
        log.debug("Request to get report card with id : {}", id);
        Optional<ReportCard> reportCard = reportCardRepository.findById(id);
        if(!reportCard.isPresent()) {
            throw new WitcurveException("No report card found with id : "+ id);
        }
        return reportCardMapper.toDto(reportCard.get());
    }



    private void isValidReportCard(ReportCardDTO reportCardDTO) {
        if(reportCardDTO.getScholasticCourses() != null && !reportCardDTO.getScholasticCourses().isEmpty()) {
            for(CourseDTO courseDTO : reportCardDTO.getScholasticCourses()) {
                Optional<Course> course = courseRepository.findById(courseDTO.getId());
                if(!course.isPresent()) {
                    throw new WitcurveException("No course found with id : "+courseDTO.getId());
                }
                if(!course.get().getCourseType().equals(CourseType.SCHOLASTIC)) {
                    throw new WitcurveException("There is a non scholastic course in scholastic course list");
                }
            }
        }
        if(reportCardDTO.getNonScholasticCourses() != null && !reportCardDTO.getNonScholasticCourses().isEmpty()) {
            for(CourseDTO courseDTO : reportCardDTO.getNonScholasticCourses()) {
                Optional<Course> course = courseRepository.findById(courseDTO.getId());
                if(!course.isPresent()) {
                    throw new WitcurveException("No course found with id : "+courseDTO.getId());
                }
                if(!course.get().getCourseType().equals(CourseType.NON_SCHOLASTIC)) {
                    throw new WitcurveException("There is a scholastic course in non scholastic course list");
                }
            }
        }
        if(reportCardDTO.getNonScholasticRcds() != null  && !reportCardDTO.getNonScholasticRcds().isEmpty()) {
            for(ReportCardDesignDTO reportCardDesignDTO : reportCardDTO.getNonScholasticRcds()) {
                Optional<ReportCardDesign> reportCardDesign = reportCardDesignRepository.findById(reportCardDesignDTO.getId());
                if(!reportCardDesign.isPresent()) {
                    throw new WitcurveException("No report card design found with id : "+ reportCardDesignDTO.getId());
                }
                if(!reportCardDesign.get().getFieldType().equals(ReportFieldType.NON_SCHOLASTIC)) {
                    throw new WitcurveException("Only non scholastic report field type allowed in non scholastic rcsds");
                }
            }
        }
        if(reportCardDTO.getScholasticDetails() != null && reportCardDTO.getScholasticDetails().isEmpty()) {
            for(ScholasticReportDetailsDTO scholasticReportDetailsDTO : reportCardDTO.getScholasticDetails()) {
                Optional<ReportCardDesign> reportCardDesign = reportCardDesignRepository.findById(scholasticReportDetailsDTO.getReportCardDesignId());
                if(!reportCardDesign.isPresent()) {
                    throw new WitcurveException("No report card design found with id : "+ scholasticReportDetailsDTO.getReportCardDesignId());
                }
                if(!SCHOLASTIC_FIELD_TYPE_LISTS.contains(reportCardDesign.get().getFieldType())) {
                    throw new WitcurveException("Only main, manual entry, peridoic test and total field types report card designs are allowed in scholastic details");
                }
                if(!scholasticReportDetailsDTO.getShowGrades()&& !scholasticReportDetailsDTO.getShowMarks()) {
                    throw new WitcurveException("Both grades and marks cannot be false, atleast one of them has to be true");
                }
            }

        }
    }

    private  Map<String, String> getGradeDetails(Long schoolId, ConfigType configType) {
        Integer max = 100;
        Map<String, String> result = new HashMap<>();
        ConfigType[] configTypes = new ConfigType[0];
        configTypes[0] = configType;
        List<ConfigSettings> configSettingsList = configSettingsRepository.getConfigSettingsBySchoolIdAndTypes(schoolId, configTypes);
        for(ConfigSettings configSettings : configSettingsList) {
            if(configType.equals(ConfigType.GRADING_SCALE_COLOR)) {
                result.put(configSettings.getDisplayFieldName(), configSettings.getFieldValue());
            } else {
                Integer min = Integer.parseInt(configSettings.getFieldValue());
                if(min != null) {
                    result.put(configSettings.getDisplayFieldName(), min.toString()+"-"+max.toString());
                }
                max= min-1;
            }
        }
        return result;
    }


    private List<Course> getCoursesForStudentId(List<Course> courses, Long studentId) {
        Map<Long, Course> examCoursesMap = courses.stream().collect(Collectors.toMap(Course::getId, course -> course));
        List<CourseDTO> validCourses= new ArrayList<>(); //courseService.getCourseByStudentId(studentId);
        List<Course> result = new ArrayList<>();
        for(CourseDTO courseDTO : validCourses) {
            if(examCoursesMap.keySet().contains(courseDTO.getId())) {
                result.add(examCoursesMap.get(courseDTO.getId()));
            }
        }
        return result;
    }

    private void isValid(ReportCardVM reportCardVM) {

        List<ReportCardVM.ScholasticVM.ScholasticDetailsVM.ExamDetailsVM> listOfExamDetailsVM = reportCardVM.getScholastic().getScholasticDetails().getExamDetails();
        for (ReportCardVM.ScholasticVM.ScholasticDetailsVM.ExamDetailsVM examDetailsVM : listOfExamDetailsVM) {


            List<ReportCardVM.ScholasticVM.ScholasticDetailsVM.ExamDetailsVM.MarksAndGradeDetailsVM> listOfMarksAndGradeDetail = examDetailsVM.getMarksAndGradesDetails();
            for (ReportCardVM.ScholasticVM.ScholasticDetailsVM.ExamDetailsVM.MarksAndGradeDetailsVM marksAndGradeDetailsVM : listOfMarksAndGradeDetail) {
                if (!marksAndGradeDetailsVM.getShowGrade() && !marksAndGradeDetailsVM.getShowMarks()) {
                    throw new WitcurveException("Both showMarks and showGrade can't be false at same time for MarksAndGradeDetails");
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
        if (reportCardVM.getScholastic().getScholasticDetails().getOverall().getShowGrade() && reportCardVM.getScholastic().getScholasticDetails().getOverall().getOverAllGrade() == null ||
            reportCardVM.getScholastic().getScholasticDetails().getOverall().getShowGrade() && reportCardVM.getScholastic().getScholasticDetails().getOverall().getOverAllGrade().isEmpty()) {
            throw new WitcurveException("ShowGrade is true than OverAllGrade can't be null or empty");
        }
        if (reportCardVM.getScholastic().getScholasticDetails().getOverall().getShowMarks() && reportCardVM.getScholastic().getScholasticDetails().getOverall().getOverAllMarks() == null ||
            reportCardVM.getScholastic().getScholasticDetails().getOverall().getShowMarks() && reportCardVM.getScholastic().getScholasticDetails().getOverall().getOverAllMarks().isEmpty()) {
            throw new WitcurveException("ShowMarks is true than OverAllMarks can't be null or empty");
        }
    }
}
