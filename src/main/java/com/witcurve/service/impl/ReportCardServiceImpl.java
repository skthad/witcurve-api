package com.witcurve.service.impl;

import com.witcurve.domain.*;
import com.witcurve.domain.enumeration.ConfigType;
import com.witcurve.domain.enumeration.CourseType;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.domain.enumeration.ReportFieldType;
import com.witcurve.repository.*;
import com.witcurve.service.CourseService;
import com.witcurve.service.EventService;
import com.witcurve.service.ReportCardService;
import com.witcurve.service.StudentMarksService;
import com.witcurve.service.dto.*;
import com.witcurve.service.mapper.ReportCardMapper;
import com.witcurve.service.util.*;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.vm.ReportCardVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

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
    StudentMarksService studentMarksService;

    @Autowired
    StudentRemarksRepository studentRemarksRepository;

    @Autowired
    ReportCardMapper reportCardMapper;

    @Autowired
    ReportCardRepository reportCardRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    StandardRepository standardRepository;

    @Autowired
    AcademicSessionRepository academicSessionRepository;

    @Autowired
    EventService eventService;

    @Autowired
    StudentCourseRepository studentCourseRepository;

    @Autowired
    AttributeValueRepository attributeValueRepository;



    private final List<ReportFieldType> SCHOLASTIC_FIELD_TYPE_LISTS = Arrays.asList(ReportFieldType.MAIN, ReportFieldType.MANUAL_ENTRY, ReportFieldType.PERIODIC_TEST, ReportFieldType.TOTAL);


    public File getReportCardTemplatePdf(ReportCardVM reportCardVM, String templateUrl, String nameOfFile) {
        isValid(reportCardVM);
        HtmlToPdfUtil htmlToPdfUtil = new HtmlToPdfUtil();
        File inputFile = htmlToPdfUtil.getParsedReportCard(reportCardVM, templateUrl);
        return htmlToPdfUtil.htmlToPdf(inputFile, nameOfFile);

    }

    public File getReportCardTemplateHtml(ReportCardVM reportCardVM, String templateUrl, String nameOfFile) {
        isValid(reportCardVM);
        HtmlToPdfUtil htmlToPdfUtil = new HtmlToPdfUtil();
        File inputFile = htmlToPdfUtil.getParsedReportCard(reportCardVM, templateUrl);
        String xml = htmlToPdfUtil.getReportHtmlXml(inputFile);
        try {
            String fileName;
            if(nameOfFile == null) {
                fileName  = "result-template.html";
            } else {
                fileName = nameOfFile+"_template.html";
            }
            File result = WitcurveUtil.createTempFile(fileName);
            FileWriter fw = new FileWriter(result);
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
        if (grade == null) {
            result = reportCardMapper.toDto(reportCardRepository.findByExamId(examId));
        } else {
            result.add(reportCardMapper.toDto(reportCardRepository.findByExamIdAndGrade(examId, grade)));
        }
        return result;
    }

    @Override
    public ReportCardDTO findOne(Long id) {
        log.debug("Request to get report card with id : {}", id);
        Optional<ReportCard> reportCard = reportCardRepository.findById(id);
        if (!reportCard.isPresent()) {
            throw new WitcurveException("No report card found with id : " + id);
        }
        return reportCardMapper.toDto(reportCard.get());
    }

    @Override
    public Map<String, String> getGradeDetailsByExamIdAndConfigType(Long examId, ConfigType configType) {
        Optional<Exam> exam = examRepository.findById(examId);
        return getGradeDetails(exam.get().getSchoolInfo().getSchool().getId(), configType);
    }

    @Override
    @Transactional(readOnly = true, noRollbackFor = WitcurveException.class)
    public Page<File> getReportCardPreviewForStandard(Long reportCardId, Long standardId, Pageable pageable, Boolean showHeader, String type) throws WitcurveException {
        log.debug("Get report card preview with report card id : {} and for standard with id : {}", reportCardId, standardId);
        Optional<Standard> standard = standardRepository.findById(standardId);
        if (!standard.isPresent()) {
            throw new WitcurveException("No standard found with id : " + standardId);
        }
        Optional<ReportCard> reportCard = reportCardRepository.findById(reportCardId);
        if(!reportCard.isPresent()) {
            throw new WitcurveException("There is no report card setting available with id : "+reportCardId);
        }
        if(!reportCard.get().getGrade().equals(standard.get().getGrade())) {
            throw new WitcurveException("Given report card with id : "+reportCardId+" is not valid for grade to which standard belongs to");
        }
        Page<StudentStandard> studentStandards = studentStandardRepository.getByStandardId(standardId, pageable);
        List<StudentStandard> studentStandardList = studentStandards.getContent();
        List<File> result = new ArrayList<>();
        Long schoolId = reportCard.get().getExam().getSchoolInfo().getSchool().getId();
        List<ConfigSettings> configSettings = configSettingsRepository.getConfigSettingsBySchoolIdAndTypes(schoolId, new ConfigType[]{ConfigType.GRADING_SCALE});
        String subDomainName = standard.get().getSchoolInfo().getSchool().getInstitute().getSubDomainName();
        Long id = standard.get().getSchoolInfo().getSchool().getInstitute().getId();
        String headerUrl = "http://"+subDomainName+".witcurve-app.com/assets/images/header-logo/"+id+"-header-logo.png";
        List<ConfigSettings> schoolPrimaryColorSettings  = configSettingsRepository.getConfigSettingsBySchoolIdAndTypes(schoolId, new ConfigType[]{ConfigType.SCHOOL_PRIMARY_COLOR});
        String schoolPrimaryColor = "#00000";
        if(!configSettings.isEmpty()) {
            schoolPrimaryColor = schoolPrimaryColorSettings.get(0).getFieldValue();
        }
        for(StudentStandard studentStandard : studentStandardList) {
            ReportCardVM reportCardVM = new ReportCardVM();
            reportCardVM.setDateOfBirth(studentStandard.getStudent().getDateOfBirth().format(DateTimeFormatter.ofPattern(WitCurveConstants.DEFAULT_IMPORT_DATE_FORMAT)));
            reportCardVM.setShowHeader(showHeader);
            reportCardVM.setLogoLink(headerUrl);
            reportCardVM.setNote(reportCard.get().getNote());
            reportCardVM.setTitle(reportCard.get().getTitle());
            reportCardVM.setSchoolPrimaryColor(schoolPrimaryColor);
            reportCardVM.setAdmissionId(studentStandard.getStudent().getAdmissionId());
            if(studentStandard.getStudent().getMiddleName() == null || studentStandard.getStudent().getMiddleName().isEmpty()) {
                reportCardVM.setStudentName(studentStandard.getStudent().getFirstName()+" "+studentStandard.getStudent().getLastName());
            } else {
                reportCardVM.setStudentName(studentStandard.getStudent().getFirstName()+" "+studentStandard.getStudent().getMiddleName()+" "+studentStandard.getStudent().getLastName());
            }
            reportCardVM.setStandard(standard.get().getGrade().toString()+"-"+standard.get().getSection());
            setAttendance(reportCard.get(), reportCardVM, studentStandard.getStudent());
            setScholasticDetails(reportCard.get(), reportCardVM, studentStandard, configSettings);
            setNonScholasticDetails(reportCard.get(), reportCardVM, studentStandard, configSettings);
            setAttributeDetails(reportCard.get(), reportCardVM, studentStandard);
            if(reportCard.get().getShowRemarks()) {
                StudentRemarks studentRemarks = studentRemarksRepository.
                    findByExamIdAndStudentId(reportCard.get().getExam().getId(), studentStandard.getStudent().getId());
                if(studentRemarks != null) {
                    reportCardVM.setRemarks(studentRemarks.getRemarks());
                } else {
                    reportCardVM.setRemarks("");
                }
            }
            log.info("\n\n\n report card vn : {} \n\n\n", reportCardVM);
            String nameOfFile = showHeader ? reportCardVM.getStudentName()+"_"+reportCardVM.getAdmissionId() : reportCardVM.getStudentName()+"_"+reportCardVM.getAdmissionId()+"_without_header";
            if(type.equalsIgnoreCase("pdf")) {
                result.add(getReportCardTemplatePdf(reportCardVM, WitCurveConstants.EXAM_PERIODIC_REPPORT_CARD_TEMPLATE, nameOfFile));
            } else {
                result.add(getReportCardTemplateHtml(reportCardVM, WitCurveConstants.EXAM_PERIODIC_REPPORT_CARD_TEMPLATE, nameOfFile));
            }
        }
        return new PageImpl<>(result, pageable, studentStandards.getTotalElements());
    }

    private void setAttendance(ReportCard reportCard, ReportCardVM reportCardVM, Student student) {
        if(reportCard.getShowAttendance()) {
            LocalDate startDate = reportCard.getExam().getStartDate();
            AcademicSession nearestAcademicSession = academicSessionRepository.nearestActiveSessionToDate(student.getSchoolInfo().getId(), startDate);
            if(nearestAcademicSession == null) {
                throw new WitcurveException("There is no current active academic session");
            }
            LocalDate sessionStartDate = nearestAcademicSession.getStartDate();
            Integer count = eventRepository.findPresentCountForStudent(sessionStartDate, startDate, student.getId());
            Long totalCalendarDays = DAYS.between(sessionStartDate, startDate) + 1;
            List<EventDTO> allHolidays = eventService.findHolidaysInSchoolInfo(student.getSchoolInfo().getId(), sessionStartDate, startDate);
            long noOfHolidays = allHolidays.size();
            long noOfSundays =  WeekdayUtil.getNoOfWeekDayBetweenDates
                (sessionStartDate, startDate, DayOfWeek.SUNDAY);
            for (EventDTO holiday : allHolidays) {
                if (holiday.getDate().isAfter(startDate)) {
                    noOfHolidays--;
                }
            }
            Long totalWorkingDays = totalCalendarDays-noOfHolidays-noOfSundays;
            reportCardVM.setAttendance(count.toString()+"/"+totalWorkingDays.toString());
        }
    }

    private void setScholasticDetails(ReportCard reportCard, ReportCardVM reportCardVM, StudentStandard studentStandard,  List<ConfigSettings> configSettings) {
        if(reportCard.getScholasticCourses() != null && !reportCard.getScholasticCourses().isEmpty()
            && reportCard.getScholasticDetails() != null && !reportCard.getScholasticDetails().isEmpty()) {
            List<ConfigSettings> gradeColorConfigSettings = configSettingsRepository.getConfigSettingsBySchoolIdAndTypes(studentStandard.getStudent().getSchoolInfo().getSchool().getId(), new ConfigType[]{ConfigType.GRADING_SCALE_COLOR});
            reportCardVM.setDefiningGrade(getGradeDetailsWithConfigSettings(configSettings));
            reportCardVM.setColorForGrades(getGradeDetailsWithConfigSettings(gradeColorConfigSettings));
            List<Course> courses = new ArrayList<>(reportCard.getScholasticCourses());
            Collections.sort(courses, new CourseComparator());
            List<String> subjectArray = new ArrayList<>();
            ReportCardVM.ScholasticVM scholasticVM= new ReportCardVM.ScholasticVM();
            ReportCardVM.ScholasticVM.ScholasticDetailsVM scholasticDetailsVM = new ReportCardVM.ScholasticVM.ScholasticDetailsVM();
            ReportCardVM.ScholasticVM.ScholasticDetailsVM.ExamDetailsVM examDetailsVM = new ReportCardVM.ScholasticVM.ScholasticDetailsVM.ExamDetailsVM();
            ReportCardVM.ScholasticVM.ScholasticDetailsVM.OverallVM overallVM = null;
            Map<Integer, Map<CourseDTO, Double>> totalMarksMap = new LinkedHashMap<>();
            Map<Integer, Double> totalMap = new LinkedHashMap<>();
            Integer titleChangeCounter =0;
            //set subjet list
            for(Course course : courses) {
                //check if course belongs to this student
                StudentCourse studentCourse = studentCourseRepository.getStudentCourseByStudentStandardIdAndCourseId(studentStandard.getId(), course.getId());
                if(studentCourse != null && studentCourse.getActive()) {
                    subjectArray.add(course.getDisplayName());
                }
            }
            //set exam details for each scholastic report details
            for(ScholasticReportDetails scholasticReportDetails: reportCard.getScholasticDetails()) {
                if(examDetailsVM.getExamName() == null) {
                    //set title for first time
                    titleChangeCounter++;
                    examDetailsVM.setExamName(scholasticReportDetails.getHeader());
                } else {
                    if(!examDetailsVM.getExamName().equals(scholasticReportDetails.getHeader())) {

                        //create new when title changes and add the old one
                        titleChangeCounter++;
                        scholasticDetailsVM.addExamDetails(examDetailsVM);
                        examDetailsVM = new ReportCardVM.ScholasticVM.ScholasticDetailsVM.ExamDetailsVM();
                        examDetailsVM.setExamName(scholasticReportDetails.getHeader());
                    }
                }
                Map<String, String> marksMap = new HashMap<>();
                Map<String, String> gradeMap = new HashMap<>();
                ReportCardVM.ScholasticVM.ScholasticDetailsVM.ExamDetailsVM.MarksAndGradeDetailsVM marksAndGradeDetailsVM
                    = new ReportCardVM.ScholasticVM.ScholasticDetailsVM.ExamDetailsVM.MarksAndGradeDetailsVM();

                List<StudentMarksDTO> studentMarksList = studentMarksService.getStudentMarksByRcdIdAndStudentId(scholasticReportDetails.getReportCardDesign(),
                    studentStandard.getStudent().getId());
                Double multiplyingFactor = scholasticReportDetails.getMarksNormalisation()/scholasticReportDetails.getReportCardDesign().getMarks();
                for(StudentMarksDTO studentMarks : studentMarksList) {
                    Double finalMarks = Math.round(studentMarks.getMarks()*multiplyingFactor * 10)/10.0;
                    marksMap.put(studentMarks.getCourseDTO().getDisplayName(), WitcurveUtil.formatDouble(finalMarks));
                    if(scholasticReportDetails.getShowGrades()) {
                        gradeMap.put(studentMarks.getCourseDTO().getDisplayName(), getGrade(configSettings, finalMarks, scholasticReportDetails.getMarksNormalisation()));
                    }
                    if(totalMarksMap.get(titleChangeCounter) == null) {
                      totalMarksMap.put(titleChangeCounter, new LinkedHashMap<>());
                    }
                    Map<CourseDTO, Double> courseMap = totalMarksMap.get(titleChangeCounter);
                    Double previousMarks = courseMap.get(studentMarks.getCourseDTO());
                    if( previousMarks == null) {
                        courseMap.put(studentMarks.getCourseDTO(), finalMarks);
                    } else {
                        if(scholasticReportDetails.getReportCardDesign().getFieldType().equals(ReportFieldType.TOTAL)) {
                            //as soon as total appear, we no longer add, we replace with total marks
                            courseMap.put(studentMarks.getCourseDTO(), finalMarks);
                        } else {
                            courseMap.put(studentMarks.getCourseDTO(), finalMarks+previousMarks);
                        }
                    }
                    totalMarksMap.put(titleChangeCounter, courseMap);
                }
                Double previousTotalMarks = totalMap.get(titleChangeCounter);
                if( previousTotalMarks == null) {
                    totalMap.put(titleChangeCounter, scholasticReportDetails.getMarksNormalisation());
                } else {
                    if(scholasticReportDetails.getReportCardDesign().getFieldType().equals(ReportFieldType.TOTAL)) {
                        //as soon as total appear, we no longer add, we replace with total marks
                        totalMap.put(titleChangeCounter, scholasticReportDetails.getMarksNormalisation());
                    } else {
                        totalMap.put(titleChangeCounter, scholasticReportDetails.getMarksNormalisation()+previousTotalMarks);
                    }
                }
                String formattedMarks = WitcurveUtil.formatDouble(scholasticReportDetails.getMarksNormalisation());
                marksAndGradeDetailsVM.setName(scholasticReportDetails.getReportCardDesign().getName()+"("+formattedMarks+")");
                marksAndGradeDetailsVM.setShortForm(scholasticReportDetails.getReportCardDesign().getShortForm()+"("+formattedMarks+")");
                marksAndGradeDetailsVM.setShowMarks(scholasticReportDetails.getShowMarks());
                marksAndGradeDetailsVM.setShowGrade(scholasticReportDetails.getShowGrades());
                marksAndGradeDetailsVM.setMarks(marksMap);
                marksAndGradeDetailsVM.setGrade(gradeMap);
                examDetailsVM.addMarksAndGradeDetails(marksAndGradeDetailsVM);
            }
            scholasticDetailsVM.addExamDetails(examDetailsVM);
            // overall details
            if(reportCard.getShowOverallGrade() || reportCard.getShowOverallMarks()) {
                overallVM = new ReportCardVM.ScholasticVM.ScholasticDetailsVM.OverallVM();
                overallVM.setShowGrade(reportCard.getShowOverallGrade());
                overallVM.setShowMarks(reportCard.getShowOverallMarks());

                Double overallFullMarks = 0.00;
                for(Map.Entry<Integer, Double> overallFullEntry : totalMap.entrySet()) {
                    overallFullMarks += overallFullEntry.getValue();
                }
                Map<CourseDTO, Double> overAllCourseMap = new HashMap<>();
                for(Map.Entry<Integer, Map<CourseDTO, Double>> titleMapEntry : totalMarksMap.entrySet()) {
                    for(Map.Entry<CourseDTO, Double> courseMapEntry : titleMapEntry.getValue().entrySet()) {
                        Double overallCourseMap = overAllCourseMap.get(courseMapEntry.getKey());
                        if(overallCourseMap == null) {
                            overAllCourseMap.put(courseMapEntry.getKey(), courseMapEntry.getValue());
                        } else {
                            overAllCourseMap.put(courseMapEntry.getKey(), courseMapEntry.getValue()+overallCourseMap);
                        }
                    }
                }
                Map<String, String> overallMarks = new HashMap<>();
                Map<String, String> overallGrade = new HashMap<>();
                Double overallFinalMarks = 0.0;
                Double overallTotalMarks = 0.0;
                for(Map.Entry<CourseDTO, Double> courseEntry : overAllCourseMap.entrySet()) {
                    Double finalMarks = (double) Math.round(courseEntry.getValue());
                    overallFinalMarks += finalMarks;
                    overallTotalMarks += overallFullMarks;
                    overallMarks.put(courseEntry.getKey().getDisplayName(), WitcurveUtil.formatDouble(finalMarks));
                    if(reportCard.getShowOverallGrade()) {
                        overallGrade.put(courseEntry.getKey().getDisplayName(), getGrade(configSettings, finalMarks, overallFullMarks));
                    }
                }
                overallVM.setGrade(overallGrade);
                overallVM.setMarks(overallMarks);
                overallVM.setOverAllMarks(WitcurveUtil.formatDouble(overallFinalMarks));
                if(reportCard.getShowOverallGrade()) {
                    overallVM.setOverAllGrade(getGrade(configSettings, overallFinalMarks, overallTotalMarks));
                }

                scholasticDetailsVM.setOverall(overallVM);
            }

            scholasticVM.setSubjectsArray(subjectArray);
            scholasticVM.setScholasticDetails(scholasticDetailsVM);
            reportCardVM.setScholastic(scholasticVM);
        }
    }

    private void setNonScholasticDetails(ReportCard reportCard, ReportCardVM reportCardVM, StudentStandard studentStandard, List<ConfigSettings> configSettings) {
        if(reportCard.getNonScholasticCourses() != null && !reportCard.getNonScholasticCourses().isEmpty()
            && reportCard.getNonScholasticReportDetails() != null && !reportCard.getNonScholasticReportDetails().isEmpty()) {
            if(reportCardVM.getColorForGrades() == null || reportCardVM.getDefiningGrade() == null) {
                List<ConfigSettings> gradeColorConfigSettings = configSettingsRepository.getConfigSettingsBySchoolIdAndTypes(studentStandard.getStudent().getSchoolInfo().getSchool().getId(), new ConfigType[]{ConfigType.GRADING_SCALE_COLOR});
                reportCardVM.setDefiningGrade(getGradeDetailsWithConfigSettings(configSettings));
                reportCardVM.setColorForGrades(getGradeDetailsWithConfigSettings(gradeColorConfigSettings));
            }
            List<Course> courses = new ArrayList<>(reportCard.getNonScholasticCourses());
            Collections.sort(courses, new CourseComparator());
            List<String> subjectArray = new ArrayList<>();
            ReportCardVM.NonScholasticVM nonScholasticVM = new ReportCardVM.NonScholasticVM();
            nonScholasticVM.setTitleName("Subjects");
            //set subject list
            for(Course course : courses) {
                //check if course belongs to this student
                StudentCourse studentCourse = studentCourseRepository.getStudentCourseByStudentStandardIdAndCourseId(studentStandard.getId(), course.getId());
                if(studentCourse != null && studentCourse.getActive()) {
                    subjectArray.add(course.getDisplayName());
                }
            }
            //set attribute details vm for each scholastic report details
            for(NonScholasticReportDetails nonScholasticReportDetails: reportCard.getNonScholasticReportDetails()) {
                ReportCardVM.AttributeDetailVM attributeDetailVM = new ReportCardVM.AttributeDetailVM();
                attributeDetailVM.setColumnName(nonScholasticReportDetails.getHeader());
                List<StudentMarksDTO> studentMarksDTOS = studentMarksService.
                    getStudentMarksByRcdIdAndStudentId(nonScholasticReportDetails.getReportCardDesign(), studentStandard.getStudent().getId());
                Map<String, String> gradeMap = new HashMap<>();
                for(StudentMarksDTO studentMarksDTO : studentMarksDTOS) {
                    Double roundedMarks = (double)Math.round(studentMarksDTO.getMarks());
                    gradeMap.put(studentMarksDTO.getCourseDTO().getDisplayName(), getGrade(configSettings, roundedMarks, nonScholasticReportDetails.getReportCardDesign().getMarks()));
                }
                attributeDetailVM.setValues(gradeMap);
                nonScholasticVM.addNonScholasticDetails(attributeDetailVM);
            }
            nonScholasticVM.setSubjectsArray(subjectArray);
            reportCardVM.setNonScholastic(nonScholasticVM);
        }
    }

    private void setAttributeDetails(ReportCard reportCard, ReportCardVM reportCardVM, StudentStandard studentStandard) {
        if(reportCard.getShowAttributes()) {
            List<ReportCardDesign> reportCardDesigns = reportCardDesignRepository.findByFieldTypeAndExamAndGrade(ReportFieldType.ATTRIBUTES, reportCard.getExam().getId(), reportCard.getGrade());
            if(!reportCardDesigns.isEmpty() && reportCardDesigns.get(0).getSelected() ) {
                ReportCardVM.AttributeVM attributeVM = null;
                ReportCardVM.AttributeDetailVM attributeDetailVM = null;
                int columnCount = 0;
                Boolean freshTitle = true;
                List<Attribute> attributes = reportCardDesigns.get(0).getAttributes();
                List<String> fieldList = null;
                List<String> boldFieldList = null;
                List<ReportCardVM.AttributeDetailVM> attributeDetailVMList = null;
                List<ReportCardVM.AttributeVM> attributeVMList = null;
                String previousTitle = null, previousColumn = null, previousField = null;
                Map<String, String> fieldValue = new HashMap<>();
                for(Attribute attribute : attributes) {
                    if(previousTitle == null || !previousTitle.equals(attribute.getTitle())) {
                        freshTitle = true;
                        previousColumn=null;
                        if(attributeVM != null) {
                            attributeVM.addAttributeDetails(attributeDetailVM);
                            reportCardVM.addAttributes(attributeVM);
                        }
                        previousTitle = attribute.getTitle();
                        attributeVM = new ReportCardVM.AttributeVM();
                        attributeVM.setTitleName(attribute.getTitle());
                        fieldList = new ArrayList<>();
                        boldFieldList = new ArrayList<>();
                        columnCount =0;
                    } else {
                        freshTitle = false;
                    }
                    if((previousColumn == null || !previousColumn.equals(attribute.getColumn()))) {
                        ++columnCount;
                        if(attributeDetailVM != null && !freshTitle) {
                            attributeVM.addAttributeDetails(attributeDetailVM);
                        }
                        previousColumn = attribute.getColumn();
                        fieldValue = new HashMap<>();
                        attributeDetailVM = new ReportCardVM.AttributeDetailVM();
                        attributeDetailVM.setColumnName(attribute.getColumn());

                    }
                    if(previousField == null || !previousField.equals(attribute.getField())) {
                        if(columnCount ==1) {
                            previousField = attribute.getField();
                            fieldList.add(attribute.getField());
                            if(attribute.getHighlight()) {
                                boldFieldList.add(attribute.getField());
                            }
                            attributeVM.setFieldArray(fieldList);
                            attributeVM.setBoldColumnsArray(boldFieldList);
                        }
                    }
                    AttributeValue attributeValue = attributeValueRepository.findByStudentIdAndAttributeId(studentStandard.getStudent().getId(), attribute.getId());
                    if(attributeValue != null) {
                        fieldValue.put(attribute.getField(), attributeValue.getValue());
                    }
                    attributeDetailVM.setValues(fieldValue);
                }
                attributeVM.addAttributeDetails(attributeDetailVM);
                reportCardVM.addAttributes(attributeVM);
            }
        }
    }

    private String getGrade(List<ConfigSettings> configSettingsList, Double marks, Double totalMarks) throws WitcurveException {
        Double percent = (marks/totalMarks) * 100;
        String result = null;
        for(ConfigSettings configSettings : configSettingsList) {
            Double min = Double.parseDouble(configSettings.getFieldValue());
            if(min == null) {
                throw new WitcurveException("There is a problem with generating grade");
            } else {
                if(percent >= min) {
                    result = configSettings.getDisplayFieldName();
                    break;
                }
            }
        }
        if(result != null) {
            return result;
        } else {
            throw new WitcurveException("There is a problem with generating grade");
        }
    }

    private Map<String, String> getGradeDetails(Long schoolId, ConfigType configType) {
        ConfigType[] configTypes = new ConfigType[1];
        configTypes[0] = configType;
        List<ConfigSettings> configSettingsList = configSettingsRepository.getConfigSettingsBySchoolIdAndTypes(schoolId, configTypes);
        return getGradeDetailsWithConfigSettings(configSettingsList);

    }

    private Map<String, String> getGradeDetailsWithConfigSettings(List<ConfigSettings> configSettingsList) {
        Integer max = 100;
        Map<String, String> result = new LinkedHashMap<>();
        for (ConfigSettings configSettings : configSettingsList) {
            if (configSettings.getConfigType().equals(ConfigType.GRADING_SCALE_COLOR)) {
                result.put(configSettings.getDisplayFieldName(), configSettings.getFieldValue());
            } else {
                Integer min = Integer.parseInt(configSettings.getFieldValue());
                if (min != null) {
                    if(min ==0) {
                        result.put(configSettings.getDisplayFieldName(), max.toString()+" & Below");
                    } else {
                        result.put(configSettings.getDisplayFieldName(), min.toString() + "-" + max.toString());
                    }

                }
                max = min - 1;
            }
        }
        return result;
    }

    private void isValidReportCard(ReportCardDTO reportCardDTO) {
        if (reportCardDTO.getScholasticCourses() != null && !reportCardDTO.getScholasticCourses().isEmpty()) {
            for (CourseDTO courseDTO : reportCardDTO.getScholasticCourses()) {
                Optional<Course> course = courseRepository.findById(courseDTO.getId());
                if (!course.isPresent()) {
                    throw new WitcurveException("No course found with id : " + courseDTO.getId());
                }
                if (!course.get().getCourseType().equals(CourseType.SCHOLASTIC)) {
                    throw new WitcurveException("There is a non scholastic course in scholastic course list");
                }
            }
        }
        if (reportCardDTO.getNonScholasticCourses() != null && !reportCardDTO.getNonScholasticCourses().isEmpty()) {
            for (CourseDTO courseDTO : reportCardDTO.getNonScholasticCourses()) {
                Optional<Course> course = courseRepository.findById(courseDTO.getId());
                if (!course.isPresent()) {
                    throw new WitcurveException("No course found with id : " + courseDTO.getId());
                }
                if (!course.get().getCourseType().equals(CourseType.NON_SCHOLASTIC)) {
                    throw new WitcurveException("There is a scholastic course in non scholastic course list");
                }
            }
        }
        if (reportCardDTO.getNonScholasticDetails() != null && !reportCardDTO.getNonScholasticDetails().isEmpty()) {
            for (NonScholasticReportDetailsDTO nonScholasticReportDetailsDTO : reportCardDTO.getNonScholasticDetails()) {
                Optional<ReportCardDesign> reportCardDesign = reportCardDesignRepository.findById(nonScholasticReportDetailsDTO.getReportCardDesignId());
                if (!reportCardDesign.isPresent()) {
                    throw new WitcurveException("No report card design found with id : " + nonScholasticReportDetailsDTO.getReportCardDesignId());
                }
                if (!reportCardDesign.get().getFieldType().equals(ReportFieldType.NON_SCHOLASTIC)) {
                    throw new WitcurveException("Only non scholastic report field type allowed in non scholastic rcsds");
                }
            }
        }
        if (reportCardDTO.getScholasticDetails() != null && reportCardDTO.getScholasticDetails().isEmpty()) {
            for (ScholasticReportDetailsDTO scholasticReportDetailsDTO : reportCardDTO.getScholasticDetails()) {
                Optional<ReportCardDesign> reportCardDesign = reportCardDesignRepository.findById(scholasticReportDetailsDTO.getReportCardDesignId());
                if (!reportCardDesign.isPresent()) {
                    throw new WitcurveException("No report card design found with id : " + scholasticReportDetailsDTO.getReportCardDesignId());
                }
                if (!SCHOLASTIC_FIELD_TYPE_LISTS.contains(reportCardDesign.get().getFieldType())) {
                    throw new WitcurveException("Only main, manual entry, peridoic test and total field types report card designs are allowed in scholastic details");
                }
                if (!scholasticReportDetailsDTO.getShowGrades() && !scholasticReportDetailsDTO.getShowMarks()) {
                    throw new WitcurveException("Both grades and marks cannot be false, atleast one of them has to be true");
                }
            }
        }

    }

    private void isValid(ReportCardVM reportCardVM) {

        if(reportCardVM.getScholastic() != null) {
            List<ReportCardVM.ScholasticVM.ScholasticDetailsVM.ExamDetailsVM> listOfExamDetailsVM = reportCardVM.getScholastic().getScholasticDetails().getExamDetails();
            if( listOfExamDetailsVM!= null && !listOfExamDetailsVM.isEmpty()) {
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
            }
            ReportCardVM.ScholasticVM.ScholasticDetailsVM.OverallVM overallVM = reportCardVM.getScholastic().getScholasticDetails().getOverall();
            if(overallVM != null) {
                if (overallVM.getShowGrade()) {
                    if (reportCardVM.getScholastic().getScholasticDetails().getOverall().getGrade() == null) {
                        throw new WitcurveException("Grades require to show grade");
                    }
                }
                if (overallVM.getShowMarks()) {
                    if (reportCardVM.getScholastic().getScholasticDetails().getOverall().getMarks() == null) {
                        throw new WitcurveException("Marks require to show marks");
                    }
                }
                if (overallVM.getShowGrade() && overallVM.getOverAllGrade() == null ||
                    reportCardVM.getScholastic().getScholasticDetails().getOverall().getShowGrade() && reportCardVM.getScholastic().getScholasticDetails().getOverall().getOverAllGrade().isEmpty()) {
                    throw new WitcurveException("ShowGrade is true than OverAllGrade can't be null or empty");
                }
                if (overallVM.getShowMarks() && overallVM.getOverAllMarks() == null ||
                    reportCardVM.getScholastic().getScholasticDetails().getOverall().getShowMarks() && reportCardVM.getScholastic().getScholasticDetails().getOverall().getOverAllMarks().isEmpty()) {
                    throw new WitcurveException("ShowMarks is true than OverAllMarks can't be null or empty");
                }
            }
        }
    }


}
