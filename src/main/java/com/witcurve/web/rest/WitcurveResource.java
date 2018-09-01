package com.witcurve.web.rest;

import com.witcurve.domain.*;
import com.witcurve.domain.Class;
import com.witcurve.domain.enumeration.EventType;
import com.witcurve.domain.enumeration.Gender;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneOffset;

/**
 * Util controller to load data
 */
@RestController
@RequestMapping("/api")
@Transactional
public class WitcurveResource {

    @Autowired
    private AcademicSessionRepository academicSessionRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private MasterSubjectRepository masterSubjectRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseTeacherRepository courseTeacherRepository;

    @Autowired
    private GuardianRepository guardianRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TermRepository termRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private GeneralSlotDetailsRepository generalSlotDetailsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SlotCourseDetailsRepository slotCourseDetailsRepository;

    @Autowired
    private SlotEventDetailsRepository slotEventDetailsRepository;

    @Autowired
    private EventRepository eventRepository;

    @RequestMapping(value = "/load-data", method = RequestMethod.POST)
    public ResponseEntity loadDate() {
        loadSchoolData();
        return ResponseEntity.ok().build();
    }

    private void loadSchoolData() {

        //school data
        School school1 = new School();
        school1.setName("Witcurve");
        school1.setAddress1("Kondapur");
        school1.setAffiliationId("affl_0001");
        school1.setCity("Hyderabad");
        school1.setState("Telangana");
        school1.setCountry("India");
        school1.setDistrict("Ranga Reddy District");
        school1.setPincode("500084");
        school1.setPrimaryPhone("9999999999");
        school1.setPrimaryEmail("contact@witcurve.com");
        school1.setFax("9999999999");


        school1= schoolRepository.save(school1);

        // academic session

        AcademicSession academicSession1 = new AcademicSession();
        academicSession1.setSchool(schoolRepository.findAll().get(0));
        academicSession1.setStartDate(LocalDate.of(2018, 4, 20));

        academicSession1 = academicSessionRepository.save(academicSession1);

        //terms

        Term term1 = new Term();
        term1.setSession(academicSession1);
        term1.setStartDate(LocalDate.of(2018, 4, 20));

        Term term2 = new Term();
        term2.setSession(academicSession1);
        term2.setStartDate(LocalDate.of(2018, 8, 1));

        termRepository.save(term1);
        term2 = termRepository.save(term2);

        //staff data

        Staff staff1 = new Staff();
        staff1.setAddress1("Kondpaur");
        staff1.setFirstName("Anuranjan");
        staff1.setLastName("Kumar");
        staff1.setSchool(school1);
        staff1.setType("Teaching");

        staff1 = staffRepository.save(staff1);

        Staff staff2 = new Staff();
        staff2.setAddress1("Hitech City");
        staff2.setFirstName("Dhiraj");
        staff2.setLastName("Kumar");
        staff2.setSchool(school1);
        staff2.setType("Teaching");

        staff2 = staffRepository.save(staff2);

        Staff staff3 = new Staff();
        staff3.setAddress1("Hitech City");
        staff3.setFirstName("Srujan Kumar");
        staff3.setLastName("Tad");
        staff3.setSchool(school1);
        staff3.setType("Teaching");

        staff3 = staffRepository.save(staff3);

        Staff staff4 = new Staff();
        staff4.setAddress1("Hitech City");
        staff4.setFirstName("Kishore Kumar");
        staff4.setLastName("SVR");
        staff4.setSchool(school1);
        staff4.setType("Teaching");

        staff4 = staffRepository.save(staff4);

        Staff staff5 = new Staff();
        staff5.setAddress1("Hitech City");
        staff5.setFirstName("Manohar");
        staff5.setLastName("No Idea");
        staff5.setSchool(school1);
        staff5.setType("Teaching");

        staff5 = staffRepository.save(staff5);

        Staff staff6 = new Staff();
        staff6.setAddress1("Hitech City");
        staff6.setFirstName("Satya");
        staff6.setLastName("No Idea");
        staff6.setSchool(school1);
        staff6.setType("Teaching");

        staff6 = staffRepository.save(staff6);


        Staff staff7 = new Staff();
        staff7.setAddress1("Hitech City");
        staff7.setFirstName("Sai Chand");
        staff7.setLastName("Gandivasala");
        staff7.setSchool(school1);
        staff7.setType("Teaching");

        staff7 = staffRepository.save(staff7);

        Staff staff8 = new Staff();
        staff8.setAddress1("Hitech City");
        staff8.setFirstName("Kalyan");
        staff8.setLastName("Naik");
        staff8.setSchool(school1);
        staff8.setType("Teaching");

        staff8 = staffRepository.save(staff8);

        // class data

        Class class1 = new Class();
        class1.setGrade(Grade.III);
        class1.setSchool(school1);
        class1.setTerm(term2);
        class1.setSection("A");
        class1.setClassTeacher(staff1);
        class1 = classRepository.save(class1);

        // courses

        MasterSubject masterSubject1 = new MasterSubject();
        masterSubject1.setName("Telugu");
        masterSubject1 = masterSubjectRepository.save(masterSubject1);

        Course course1 = new Course();
        course1.setCourseName("Telugu");
        course1.setMasterSubject(masterSubject1);
        course1.setDescription("Telugu");
        course1.setSchool(school1);
        course1 = courseRepository.save(course1);

        MasterSubject masterSubject2 = new MasterSubject();
        masterSubject2.setName("Hindi");
        masterSubject2 = masterSubjectRepository.save(masterSubject1);

        Course course2 = new Course();
        course2.setCourseName("Hindi");
        course2.setMasterSubject(masterSubject2);
        course2.setDescription("Hindi");
        course2.setSchool(school1);
        course2 = courseRepository.save(course2);

        MasterSubject masterSubject3 = new MasterSubject();
        masterSubject3.setName("English");
        masterSubject3 = masterSubjectRepository.save(masterSubject3);

        Course course3 = new Course();
        course3.setCourseName("English");
        course3.setMasterSubject(masterSubject3);
        course3.setDescription("English");
        course3.setSchool(school1);
        course3 = courseRepository.save(course3);

        MasterSubject masterSubject4 = new MasterSubject();
        masterSubject4.setName("Maths");
        masterSubject4 = masterSubjectRepository.save(masterSubject4);

        Course course4 = new Course();
        course4.setCourseName("Maths");
        course4.setMasterSubject(masterSubject4);
        course4.setDescription("Maths");
        course4.setSchool(school1);
        course4 = courseRepository.save(course4);

        MasterSubject masterSubject5 = new MasterSubject();
        masterSubject5.setName("Science");
        masterSubject5 = masterSubjectRepository.save(masterSubject5);

        Course course5 = new Course();
        course5.setCourseName("Science");
        course5.setMasterSubject(masterSubject5);
        course5.setDescription("Science");
        course5.setSchool(school1);
        course5 = courseRepository.save(course5);

        MasterSubject masterSubject6 = new MasterSubject();
        masterSubject6.setName("Social Studies");
        masterSubject6 = masterSubjectRepository.save(masterSubject6);

        Course course6 = new Course();
        course6.setCourseName("Social Studies");
        course6.setMasterSubject(masterSubject6);
        course6.setDescription("Social Students");
        course6.setSchool(school1);
        course6 = courseRepository.save(course6);

        MasterSubject masterSubject7 = new MasterSubject();
        masterSubject7.setName("Drawing");
        masterSubject7 = masterSubjectRepository.save(masterSubject7);

        Course course7 = new Course();
        course7.setCourseName("Drawing");
        course7.setMasterSubject(masterSubject7);
        course7.setDescription("Drawing");
        course7.setSchool(school1);
        course7 = courseRepository.save(course7);


        MasterSubject masterSubject8 = new MasterSubject();
        masterSubject8.setName("Physical Training");
        masterSubject8 = masterSubjectRepository.save(masterSubject8);

        Course course8 = new Course();
        course8.setCourseName("P.T.");
        course8.setMasterSubject(masterSubject8);
        course8.setDescription("Physical Training");
        course8.setSchool(school1);
        course8 = courseRepository.save(course8);

        //course teacher data

        CourseTeacher courseTeacher1 = new CourseTeacher();
        courseTeacher1.setCourse(course1);
        courseTeacher1.setTeacher(staff1);
        courseTeacher1.setStandard(class1);
        courseTeacher1 = courseTeacherRepository.save(courseTeacher1);

        CourseTeacher courseTeacher2 = new CourseTeacher();
        courseTeacher2.setCourse(course2);
        courseTeacher2.setTeacher(staff2);
        courseTeacher2.setStandard(class1);
        courseTeacher2 = courseTeacherRepository.save(courseTeacher2);

        CourseTeacher courseTeacher3 = new CourseTeacher();
        courseTeacher3.setCourse(course3);
        courseTeacher3.setTeacher(staff3);
        courseTeacher3.setStandard(class1);
        courseTeacher3 = courseTeacherRepository.save(courseTeacher3);

        CourseTeacher courseTeacher4 = new CourseTeacher();
        courseTeacher4.setCourse(course4);
        courseTeacher4.setTeacher(staff4);
        courseTeacher4.setStandard(class1);
        courseTeacher4 = courseTeacherRepository.save(courseTeacher4);

        CourseTeacher courseTeacher5 = new CourseTeacher();
        courseTeacher5.setCourse(course5);
        courseTeacher5.setTeacher(staff5);
        courseTeacher5.setStandard(class1);
        courseTeacher5 = courseTeacherRepository.save(courseTeacher5);

        CourseTeacher courseTeacher6 = new CourseTeacher();
        courseTeacher6.setCourse(course6);
        courseTeacher6.setTeacher(staff6);
        courseTeacher6.setStandard(class1);
        courseTeacher6 = courseTeacherRepository.save(courseTeacher6);

        CourseTeacher courseTeacher7 = new CourseTeacher();
        courseTeacher7.setCourse(course7);
        courseTeacher7.setTeacher(staff7);
        courseTeacher7.setStandard(class1);
        courseTeacher7 = courseTeacherRepository.save(courseTeacher7);

        CourseTeacher courseTeacher8 = new CourseTeacher();
        courseTeacher8.setCourse(course8);
        courseTeacher8.setTeacher(staff8);
        courseTeacher8.setStandard(class1);
        courseTeacher8 = courseTeacherRepository.save(courseTeacher8);

        // student data
        Student student1 = new Student();
        student1.setFirstName("Sai");
        student1.setMiddleName("Chand");
        student1.setLastName("Gandivasal");
        student1.setDateOfBirth(LocalDate.of(1994, 6, 25));
        student1.setNationality("Indian");
        student1.setCity("Hyderabad");
        student1.setAddress1("Nampally");
        student1.setGender(Gender.MALE);
        student1.setPincode("500001");
        student1.setState("Telangana");
        student1.setStandard(class1);
        student1.setSchool(school1);
        studentRepository.save(student1);

        Guardian g1 = new Guardian();
        g1.setFirstName("Prasad");
        g1.setLastName("Rao");
        g1.setType("Father");
        g1.setAnnualIncome(90000L);
        g1.setEmailId("prasad@prasad.com");
        g1.setMiddleName("Krishna");
        g1.setMobileNo("8888888888");
        g1.setStudent(student1);

        guardianRepository.save(g1);

        //exam data
        Exam exam1 = new Exam();
        exam1.setAcademicSession(academicSession1);
        exam1.setExamType("Quarterly Examination");
        exam1.setName("Quarterly Examination 2018-2019");
        exam1.setStartDate(LocalDate.of(2018, 8 , 6));
        exam1.setEndDate(LocalDate.of(2018,8,11));
        exam1 = examRepository.save(exam1);

        //gsd data
        GeneralSlotDetails gsd1 = new GeneralSlotDetails();
        gsd1.setStandard(class1);
        gsd1.setStart("0900");
        gsd1.setDuration(40);
        gsd1 = generalSlotDetailsRepository.save(gsd1);

        GeneralSlotDetails gsd2 = new GeneralSlotDetails();
        gsd2.setStandard(class1);
        gsd2.setStart("0940");
        gsd2.setDuration(40);
        gsd2 = generalSlotDetailsRepository.save(gsd2);

        GeneralSlotDetails gsd3 = new GeneralSlotDetails();
        gsd3.setStandard(class1);
        gsd3.setStart("1040");
        gsd3.setDuration(40);
        gsd3 = generalSlotDetailsRepository.save(gsd3);

        GeneralSlotDetails gsd4 = new GeneralSlotDetails();
        gsd4.setStandard(class1);
        gsd4.setStart("1120");
        gsd4.setDuration(40);
        gsd4 = generalSlotDetailsRepository.save(gsd4);

        GeneralSlotDetails gsd5 = new GeneralSlotDetails();
        gsd5.setStandard(class1);
        gsd5.setStart("1300");
        gsd5.setDuration(40);
        gsd5 = generalSlotDetailsRepository.save(gsd5);

        GeneralSlotDetails gsd6 = new GeneralSlotDetails();
        gsd6.setStandard(class1);
        gsd6.setStart("1340");
        gsd6.setDuration(40);
        gsd6 = generalSlotDetailsRepository.save(gsd6);

        GeneralSlotDetails gsd7 = new GeneralSlotDetails();
        gsd7.setStandard(class1);
        gsd7.setStart("1420");
        gsd7.setDuration(40);
        gsd7 = generalSlotDetailsRepository.save(gsd7);

        GeneralSlotDetails gsd8 = new GeneralSlotDetails();
        gsd8.setStandard(class1);
        gsd8.setStart("1500");
        gsd8.setDuration(40);
        gsd8 = generalSlotDetailsRepository.save(gsd8);

        GeneralSlotDetails gsd9 = new GeneralSlotDetails();
        gsd9.setStandard(class1);
        gsd9.setStart("1020");
        gsd9.setDuration(20);
        gsd9.setRecess(true);
        gsd9 = generalSlotDetailsRepository.save(gsd9);


        GeneralSlotDetails gsd10 = new GeneralSlotDetails();
        gsd10.setStandard(class1);
        gsd10.setStart("1200");
        gsd10.setDuration(60);
        gsd10.setRecess(true);
        gsd10 = generalSlotDetailsRepository.save(gsd10);


        //SCD records

        /*MONDAY*/
        SlotCourseDetails slotCourseDetails11 = new SlotCourseDetails();
        slotCourseDetails11.setCourse(course1);
        slotCourseDetails11.setGsd(gsd1);
        slotCourseDetails11.setDayOfWeek(DayOfWeek.MONDAY);
        slotCourseDetails11 = slotCourseDetailsRepository.save(slotCourseDetails11);


        SlotCourseDetails slotCourseDetails12 = new SlotCourseDetails();
        slotCourseDetails12.setCourse(course2);
        slotCourseDetails12.setGsd(gsd2);
        slotCourseDetails12.setDayOfWeek(DayOfWeek.MONDAY);
        slotCourseDetails12 = slotCourseDetailsRepository.save(slotCourseDetails12);

        SlotCourseDetails slotCourseDetails13 = new SlotCourseDetails();
        slotCourseDetails13.setCourse(course3);
        slotCourseDetails13.setGsd(gsd3);
        slotCourseDetails13.setDayOfWeek(DayOfWeek.MONDAY);
        slotCourseDetails13 = slotCourseDetailsRepository.save(slotCourseDetails13);

        SlotCourseDetails slotCourseDetails14 = new SlotCourseDetails();
        slotCourseDetails14.setCourse(course4);
        slotCourseDetails14.setGsd(gsd4);
        slotCourseDetails14.setDayOfWeek(DayOfWeek.MONDAY);
        slotCourseDetails14 = slotCourseDetailsRepository.save(slotCourseDetails14);

        SlotCourseDetails slotCourseDetails15 = new SlotCourseDetails();
        slotCourseDetails15.setCourse(course5);
        slotCourseDetails15.setGsd(gsd5);
        slotCourseDetails15.setDayOfWeek(DayOfWeek.MONDAY);
        slotCourseDetails15 = slotCourseDetailsRepository.save(slotCourseDetails15);


        SlotCourseDetails slotCourseDetails16 = new SlotCourseDetails();
        slotCourseDetails16.setCourse(course6);
        slotCourseDetails16.setGsd(gsd6);
        slotCourseDetails16.setDayOfWeek(DayOfWeek.MONDAY);
        slotCourseDetails16 = slotCourseDetailsRepository.save(slotCourseDetails16);

        SlotCourseDetails slotCourseDetails17 = new SlotCourseDetails();
        slotCourseDetails17.setCourse(course7);
        slotCourseDetails17.setGsd(gsd7);
        slotCourseDetails17.setDayOfWeek(DayOfWeek.MONDAY);
        slotCourseDetails17 = slotCourseDetailsRepository.save(slotCourseDetails17);


        SlotCourseDetails slotCourseDetails18 = new SlotCourseDetails();
        slotCourseDetails18.setCourse(course8);
        slotCourseDetails18.setGsd(gsd8);
        slotCourseDetails18.setDayOfWeek(DayOfWeek.MONDAY);
        slotCourseDetails18 = slotCourseDetailsRepository.save(slotCourseDetails18);

        /*TUESDAY*/
        SlotCourseDetails slotCourseDetails21 = new SlotCourseDetails();
        slotCourseDetails21.setCourse(course1);
        slotCourseDetails21.setGsd(gsd1);
        slotCourseDetails21.setDayOfWeek(DayOfWeek.TUESDAY);
        slotCourseDetails21 = slotCourseDetailsRepository.save(slotCourseDetails21);


        SlotCourseDetails slotCourseDetails22 = new SlotCourseDetails();
        slotCourseDetails22.setCourse(course2);
        slotCourseDetails22.setGsd(gsd2);
        slotCourseDetails22.setDayOfWeek(DayOfWeek.TUESDAY);
        slotCourseDetails22 = slotCourseDetailsRepository.save(slotCourseDetails22);

        SlotCourseDetails slotCourseDetails23 = new SlotCourseDetails();
        slotCourseDetails23.setCourse(course3);
        slotCourseDetails23.setGsd(gsd3);
        slotCourseDetails23.setDayOfWeek(DayOfWeek.TUESDAY);
        slotCourseDetails23 = slotCourseDetailsRepository.save(slotCourseDetails23);

        SlotCourseDetails slotCourseDetails24 = new SlotCourseDetails();
        slotCourseDetails24.setCourse(course4);
        slotCourseDetails24.setGsd(gsd4);
        slotCourseDetails24.setDayOfWeek(DayOfWeek.TUESDAY);
        slotCourseDetails24 = slotCourseDetailsRepository.save(slotCourseDetails24);

        SlotCourseDetails slotCourseDetails25 = new SlotCourseDetails();
        slotCourseDetails25.setCourse(course5);
        slotCourseDetails25.setGsd(gsd5);
        slotCourseDetails25.setDayOfWeek(DayOfWeek.TUESDAY);
        slotCourseDetails25 = slotCourseDetailsRepository.save(slotCourseDetails25);


        SlotCourseDetails slotCourseDetails26 = new SlotCourseDetails();
        slotCourseDetails26.setCourse(course6);
        slotCourseDetails26.setGsd(gsd6);
        slotCourseDetails26.setDayOfWeek(DayOfWeek.TUESDAY);
        slotCourseDetails26 = slotCourseDetailsRepository.save(slotCourseDetails26);

        SlotCourseDetails slotCourseDetails27 = new SlotCourseDetails();
        slotCourseDetails27.setCourse(course7);
        slotCourseDetails27.setGsd(gsd7);
        slotCourseDetails27.setDayOfWeek(DayOfWeek.TUESDAY);
        slotCourseDetails27 = slotCourseDetailsRepository.save(slotCourseDetails27);


        SlotCourseDetails slotCourseDetails28 = new SlotCourseDetails();
        slotCourseDetails28.setCourse(course8);
        slotCourseDetails28.setGsd(gsd8);
        slotCourseDetails28.setDayOfWeek(DayOfWeek.TUESDAY);
        slotCourseDetails28 = slotCourseDetailsRepository.save(slotCourseDetails28);


        /*WEDNESDAY*/
        SlotCourseDetails slotCourseDetails31 = new SlotCourseDetails();
        slotCourseDetails31.setCourse(course1);
        slotCourseDetails31.setGsd(gsd1);
        slotCourseDetails31.setDayOfWeek(DayOfWeek.WEDNESDAY);
        slotCourseDetails31 = slotCourseDetailsRepository.save(slotCourseDetails31);


        SlotCourseDetails slotCourseDetails32 = new SlotCourseDetails();
        slotCourseDetails32.setCourse(course2);
        slotCourseDetails32.setGsd(gsd2);
        slotCourseDetails32.setDayOfWeek(DayOfWeek.WEDNESDAY);
        slotCourseDetails32 = slotCourseDetailsRepository.save(slotCourseDetails32);

        SlotCourseDetails slotCourseDetails33 = new SlotCourseDetails();
        slotCourseDetails33.setCourse(course3);
        slotCourseDetails33.setGsd(gsd3);
        slotCourseDetails33.setDayOfWeek(DayOfWeek.WEDNESDAY);
        slotCourseDetails33 = slotCourseDetailsRepository.save(slotCourseDetails33);

        SlotCourseDetails slotCourseDetails34 = new SlotCourseDetails();
        slotCourseDetails34.setCourse(course4);
        slotCourseDetails34.setGsd(gsd4);
        slotCourseDetails34.setDayOfWeek(DayOfWeek.WEDNESDAY);
        slotCourseDetails34 = slotCourseDetailsRepository.save(slotCourseDetails34);

        SlotCourseDetails slotCourseDetails35 = new SlotCourseDetails();
        slotCourseDetails35.setCourse(course5);
        slotCourseDetails35.setGsd(gsd5);
        slotCourseDetails35.setDayOfWeek(DayOfWeek.WEDNESDAY);
        slotCourseDetails35 = slotCourseDetailsRepository.save(slotCourseDetails35);


        SlotCourseDetails slotCourseDetails36 = new SlotCourseDetails();
        slotCourseDetails36.setCourse(course6);
        slotCourseDetails36.setGsd(gsd6);
        slotCourseDetails36.setDayOfWeek(DayOfWeek.WEDNESDAY);
        slotCourseDetails36 = slotCourseDetailsRepository.save(slotCourseDetails36);

        SlotCourseDetails slotCourseDetails37 = new SlotCourseDetails();
        slotCourseDetails37.setCourse(course7);
        slotCourseDetails37.setGsd(gsd7);
        slotCourseDetails37.setDayOfWeek(DayOfWeek.WEDNESDAY);
        slotCourseDetails37 = slotCourseDetailsRepository.save(slotCourseDetails37);


        SlotCourseDetails slotCourseDetails38 = new SlotCourseDetails();
        slotCourseDetails38.setCourse(course8);
        slotCourseDetails38.setGsd(gsd8);
        slotCourseDetails38.setDayOfWeek(DayOfWeek.WEDNESDAY);
        slotCourseDetails38 = slotCourseDetailsRepository.save(slotCourseDetails38);

        /*THURSDAY*/
        SlotCourseDetails slotCourseDetails41 = new SlotCourseDetails();
        slotCourseDetails41.setCourse(course1);
        slotCourseDetails41.setGsd(gsd1);
        slotCourseDetails41.setDayOfWeek(DayOfWeek.THURSDAY);
        slotCourseDetails41 = slotCourseDetailsRepository.save(slotCourseDetails41);


        SlotCourseDetails slotCourseDetails42 = new SlotCourseDetails();
        slotCourseDetails42.setCourse(course2);
        slotCourseDetails42.setGsd(gsd2);
        slotCourseDetails42.setDayOfWeek(DayOfWeek.THURSDAY);
        slotCourseDetails42 = slotCourseDetailsRepository.save(slotCourseDetails42);

        SlotCourseDetails slotCourseDetails43 = new SlotCourseDetails();
        slotCourseDetails43.setCourse(course3);
        slotCourseDetails43.setGsd(gsd3);
        slotCourseDetails43.setDayOfWeek(DayOfWeek.THURSDAY);
        slotCourseDetails43 = slotCourseDetailsRepository.save(slotCourseDetails43);

        SlotCourseDetails slotCourseDetails44 = new SlotCourseDetails();
        slotCourseDetails44.setCourse(course4);
        slotCourseDetails44.setGsd(gsd4);
        slotCourseDetails44.setDayOfWeek(DayOfWeek.THURSDAY);
        slotCourseDetails44 = slotCourseDetailsRepository.save(slotCourseDetails44);

        SlotCourseDetails slotCourseDetails45 = new SlotCourseDetails();
        slotCourseDetails45.setCourse(course5);
        slotCourseDetails45.setGsd(gsd5);
        slotCourseDetails45.setDayOfWeek(DayOfWeek.THURSDAY);
        slotCourseDetails45 = slotCourseDetailsRepository.save(slotCourseDetails45);


        SlotCourseDetails slotCourseDetails46 = new SlotCourseDetails();
        slotCourseDetails46.setCourse(course6);
        slotCourseDetails46.setGsd(gsd6);
        slotCourseDetails46.setDayOfWeek(DayOfWeek.THURSDAY);
        slotCourseDetails46 = slotCourseDetailsRepository.save(slotCourseDetails46);

        SlotCourseDetails slotCourseDetails47 = new SlotCourseDetails();
        slotCourseDetails47.setCourse(course7);
        slotCourseDetails47.setGsd(gsd7);
        slotCourseDetails47.setDayOfWeek(DayOfWeek.THURSDAY);
        slotCourseDetails47 = slotCourseDetailsRepository.save(slotCourseDetails47);


        SlotCourseDetails slotCourseDetails48 = new SlotCourseDetails();
        slotCourseDetails48.setCourse(course8);
        slotCourseDetails48.setGsd(gsd8);
        slotCourseDetails48.setDayOfWeek(DayOfWeek.THURSDAY);
        slotCourseDetails48 = slotCourseDetailsRepository.save(slotCourseDetails48);

        /*FRIDAY*/
        SlotCourseDetails slotCourseDetails51 = new SlotCourseDetails();
        slotCourseDetails51.setCourse(course1);
        slotCourseDetails51.setGsd(gsd1);
        slotCourseDetails51.setDayOfWeek(DayOfWeek.FRIDAY);
        slotCourseDetails51 = slotCourseDetailsRepository.save(slotCourseDetails51);


        SlotCourseDetails slotCourseDetails52 = new SlotCourseDetails();
        slotCourseDetails52.setCourse(course2);
        slotCourseDetails52.setGsd(gsd2);
        slotCourseDetails52.setDayOfWeek(DayOfWeek.FRIDAY);
        slotCourseDetails52 = slotCourseDetailsRepository.save(slotCourseDetails52);

        SlotCourseDetails slotCourseDetails53 = new SlotCourseDetails();
        slotCourseDetails53.setCourse(course3);
        slotCourseDetails53.setGsd(gsd3);
        slotCourseDetails53.setDayOfWeek(DayOfWeek.FRIDAY);
        slotCourseDetails53 = slotCourseDetailsRepository.save(slotCourseDetails53);

        SlotCourseDetails slotCourseDetails54 = new SlotCourseDetails();
        slotCourseDetails54.setCourse(course4);
        slotCourseDetails54.setGsd(gsd4);
        slotCourseDetails54.setDayOfWeek(DayOfWeek.FRIDAY);
        slotCourseDetails54 = slotCourseDetailsRepository.save(slotCourseDetails54);

        SlotCourseDetails slotCourseDetails55 = new SlotCourseDetails();
        slotCourseDetails55.setCourse(course5);
        slotCourseDetails55.setGsd(gsd5);
        slotCourseDetails55.setDayOfWeek(DayOfWeek.FRIDAY);
        slotCourseDetails55 = slotCourseDetailsRepository.save(slotCourseDetails55);


        SlotCourseDetails slotCourseDetails56 = new SlotCourseDetails();
        slotCourseDetails56.setCourse(course6);
        slotCourseDetails56.setGsd(gsd6);
        slotCourseDetails56.setDayOfWeek(DayOfWeek.FRIDAY);
        slotCourseDetails56 = slotCourseDetailsRepository.save(slotCourseDetails56);

        SlotCourseDetails slotCourseDetails57 = new SlotCourseDetails();
        slotCourseDetails57.setCourse(course7);
        slotCourseDetails57.setGsd(gsd7);
        slotCourseDetails57.setDayOfWeek(DayOfWeek.FRIDAY);
        slotCourseDetails57 = slotCourseDetailsRepository.save(slotCourseDetails57);


        SlotCourseDetails slotCourseDetails58 = new SlotCourseDetails();
        slotCourseDetails58.setCourse(course8);
        slotCourseDetails58.setGsd(gsd8);
        slotCourseDetails58.setDayOfWeek(DayOfWeek.FRIDAY);
        slotCourseDetails58 = slotCourseDetailsRepository.save(slotCourseDetails58);

        //Events
          //Daily Updates for Monday
        Event event11 = new Event();
        event11.setName("Daily Update");
        event11.setDescription("This is a daily update for today's class");
        event11.setType(EventType.DAILY_UPDATE);
        event11.setDate(LocalDate.of(2018, 9, 10));
        event11.setStandard(class1);
        event11 = eventRepository.save(event11);

        SlotEventDetails slotEventDetails11 = new SlotEventDetails();
        slotEventDetails11.setEvent(event11);
        slotEventDetails11.setScd(slotCourseDetails11);
        slotEventDetailsRepository.save(slotEventDetails11);

        Event event12 = new Event();
        event12.setName("Daily Update");
        event12.setDescription("This is a daily update for today's class");
        event12.setType(EventType.DAILY_UPDATE);
        event12.setDate(LocalDate.of(2018, 9, 10));
        event12.setStandard(class1);
        event12 = eventRepository.save(event12);

        SlotEventDetails slotEventDetails12 = new SlotEventDetails();
        slotEventDetails12.setEvent(event12);
        slotEventDetails12.setScd(slotCourseDetails12);
        slotEventDetailsRepository.save(slotEventDetails12);

        Event event13 = new Event();
        event13.setName("Daily Update");
        event13.setDescription("This is a daily update for today's class");
        event13.setType(EventType.DAILY_UPDATE);
        event13.setDate(LocalDate.of(2018, 9, 10));
        event13.setStandard(class1);
        event13 = eventRepository.save(event13);

        SlotEventDetails slotEventDetails13 = new SlotEventDetails();
        slotEventDetails13.setEvent(event13);
        slotEventDetails13.setScd(slotCourseDetails13);
        slotEventDetailsRepository.save(slotEventDetails13);

        Event event14 = new Event();
        event14.setName("Daily Update");
        event14.setDescription("This is a daily update for today's class");
        event14.setType(EventType.DAILY_UPDATE);
        event14.setDate(LocalDate.of(2018, 9, 10));
        event14.setStandard(class1);
        event14 = eventRepository.save(event14);

        SlotEventDetails slotEventDetails14 = new SlotEventDetails();
        slotEventDetails14.setEvent(event14);
        slotEventDetails14.setScd(slotCourseDetails14);
        slotEventDetailsRepository.save(slotEventDetails14);

        Event event15 = new Event();
        event15.setName("Daily Update");
        event15.setDescription("This is a daily update for today's class");
        event15.setType(EventType.DAILY_UPDATE);
        event15.setDate(LocalDate.of(2018, 9, 10));
        event15.setStandard(class1);
        event15 = eventRepository.save(event15);

        SlotEventDetails slotEventDetails15 = new SlotEventDetails();
        slotEventDetails15.setEvent(event15);
        slotEventDetails15.setScd(slotCourseDetails15);
        slotEventDetailsRepository.save(slotEventDetails15);

        Event event16 = new Event();
        event16.setName("Daily Update");
        event16.setDescription("This is a daily update for today's class");
        event16.setType(EventType.DAILY_UPDATE);
        event16.setDate(LocalDate.of(2018, 9, 10));
        event16.setStandard(class1);
        event16 = eventRepository.save(event16);

        SlotEventDetails slotEventDetails16 = new SlotEventDetails();
        slotEventDetails16.setEvent(event16);
        slotEventDetails16.setScd(slotCourseDetails16);
        slotEventDetailsRepository.save(slotEventDetails16);

        Event event17 = new Event();
        event17.setName("Daily Update");
        event17.setDescription("This is a daily update for today's class");
        event17.setType(EventType.DAILY_UPDATE);
        event17.setDate(LocalDate.of(2018, 9, 10));
        event17.setStandard(class1);
        event17 = eventRepository.save(event17);

        SlotEventDetails slotEventDetails17 = new SlotEventDetails();
        slotEventDetails17.setEvent(event17);
        slotEventDetails17.setScd(slotCourseDetails17);
        slotEventDetailsRepository.save(slotEventDetails17);

        Event event18 = new Event();
        event18.setName("Daily Update");
        event18.setDescription("This is a daily update for today's class");
        event18.setType(EventType.DAILY_UPDATE);
        event18.setDate(LocalDate.of(2018, 9, 10));
        event18.setStandard(class1);
        event18 = eventRepository.save(event18);

        SlotEventDetails slotEventDetails18 = new SlotEventDetails();
        slotEventDetails18.setEvent(event18);
        slotEventDetails18.setScd(slotCourseDetails18);
        slotEventDetailsRepository.save(slotEventDetails18);

        //Daily Update for Tuesday
        Event event21 = new Event();
        event21.setName("Daily Update");
        event21.setDescription("This is a daily update for today's class");
        event21.setType(EventType.DAILY_UPDATE);
        event21.setDate(LocalDate.of(2018, 9, 11));
        event21.setStandard(class1);
        event21 = eventRepository.save(event21);

        SlotEventDetails slotEventDetails21 = new SlotEventDetails();
        slotEventDetails21.setEvent(event21);
        slotEventDetails21.setScd(slotCourseDetails21);
        slotEventDetailsRepository.save(slotEventDetails21);

        Event event22 = new Event();
        event22.setName("Daily Update");
        event22.setDescription("This is a daily update for today's class");
        event22.setType(EventType.DAILY_UPDATE);
        event22.setDate(LocalDate.of(2018, 9, 11));
        event22.setStandard(class1);
        event22 = eventRepository.save(event22);

        SlotEventDetails slotEventDetails22 = new SlotEventDetails();
        slotEventDetails22.setEvent(event22);
        slotEventDetails22.setScd(slotCourseDetails22);
        slotEventDetailsRepository.save(slotEventDetails22);

        Event event23 = new Event();
        event23.setName("Daily Update");
        event23.setDescription("This is a daily update for today's class");
        event23.setType(EventType.DAILY_UPDATE);
        event23.setDate(LocalDate.of(2018, 9, 11));
        event23.setStandard(class1);
        event23 = eventRepository.save(event23);

        SlotEventDetails slotEventDetails23 = new SlotEventDetails();
        slotEventDetails23.setEvent(event23);
        slotEventDetails23.setScd(slotCourseDetails23);
        slotEventDetailsRepository.save(slotEventDetails23);

        Event event24 = new Event();
        event24.setName("Daily Update");
        event24.setDescription("This is a daily update for today's class");
        event24.setType(EventType.DAILY_UPDATE);
        event24.setDate(LocalDate.of(2018, 9, 11));
        event24.setStandard(class1);
        event24 = eventRepository.save(event24);

        SlotEventDetails slotEventDetails24 = new SlotEventDetails();
        slotEventDetails24.setEvent(event24);
        slotEventDetails24.setScd(slotCourseDetails24);
        slotEventDetailsRepository.save(slotEventDetails24);

        Event event25 = new Event();
        event25.setName("Daily Update");
        event25.setDescription("This is a daily update for today's class");
        event25.setType(EventType.DAILY_UPDATE);
        event25.setDate(LocalDate.of(2018, 9, 11));
        event25.setStandard(class1);
        event25 = eventRepository.save(event25);

        SlotEventDetails slotEventDetails25 = new SlotEventDetails();
        slotEventDetails25.setEvent(event25);
        slotEventDetails25.setScd(slotCourseDetails25);
        slotEventDetailsRepository.save(slotEventDetails25);

        Event event26 = new Event();
        event26.setName("Daily Update");
        event26.setDescription("This is a daily update for today's class");
        event26.setType(EventType.DAILY_UPDATE);
        event26.setDate(LocalDate.of(2018, 9, 11));
        event26.setStandard(class1);
        event26 = eventRepository.save(event26);

        SlotEventDetails slotEventDetails26 = new SlotEventDetails();
        slotEventDetails26.setEvent(event26);
        slotEventDetails26.setScd(slotCourseDetails26);
        slotEventDetailsRepository.save(slotEventDetails26);

        Event event27 = new Event();
        event27.setName("Daily Update");
        event27.setDescription("This is a daily update for today's class");
        event27.setType(EventType.DAILY_UPDATE);
        event27.setDate(LocalDate.of(2018, 9, 11));
        event27.setStandard(class1);
        event27 = eventRepository.save(event27);

        SlotEventDetails slotEventDetails27 = new SlotEventDetails();
        slotEventDetails27.setEvent(event27);
        slotEventDetails27.setScd(slotCourseDetails27);
        slotEventDetailsRepository.save(slotEventDetails27);

        Event event28 = new Event();
        event28.setName("Daily Update");
        event28.setDescription("This is a daily update for today's class");
        event28.setType(EventType.DAILY_UPDATE);
        event28.setDate(LocalDate.of(2018, 9, 11));
        event28.setStandard(class1);
        event28 = eventRepository.save(event28);

        SlotEventDetails slotEventDetails28 = new SlotEventDetails();
        slotEventDetails28.setEvent(event28);
        slotEventDetails28.setScd(slotCourseDetails28);
        slotEventDetailsRepository.save(slotEventDetails28);

        //Daily Update for Wednesday
        Event event31 = new Event();
        event31.setName("Daily Update");
        event31.setDescription("This is a daily update for today's class");
        event31.setType(EventType.DAILY_UPDATE);
        event31.setDate(LocalDate.of(2018, 9, 12));
        event31.setStandard(class1);
        event31 = eventRepository.save(event31);

        SlotEventDetails slotEventDetails31 = new SlotEventDetails();
        slotEventDetails31.setEvent(event31);
        slotEventDetails31.setScd(slotCourseDetails31);
        slotEventDetailsRepository.save(slotEventDetails31);

        Event event32 = new Event();
        event32.setName("Daily Update");
        event32.setDescription("This is a daily update for today's class");
        event32.setType(EventType.DAILY_UPDATE);
        event32.setDate(LocalDate.of(2018, 9, 12));
        event32.setStandard(class1);
        event32 = eventRepository.save(event32);

        SlotEventDetails slotEventDetails32 = new SlotEventDetails();
        slotEventDetails32.setEvent(event32);
        slotEventDetails32.setScd(slotCourseDetails32);
        slotEventDetailsRepository.save(slotEventDetails32);

        Event event33 = new Event();
        event33.setName("Daily Update");
        event33.setDescription("This is a daily update for today's class");
        event33.setType(EventType.DAILY_UPDATE);
        event33.setDate(LocalDate.of(2018, 9, 12));
        event33.setStandard(class1);
        event33 = eventRepository.save(event33);

        SlotEventDetails slotEventDetails33 = new SlotEventDetails();
        slotEventDetails33.setEvent(event33);
        slotEventDetails33.setScd(slotCourseDetails33);
        slotEventDetailsRepository.save(slotEventDetails33);

        Event event34 = new Event();
        event34.setName("Daily Update");
        event34.setDescription("This is a daily update for today's class");
        event34.setType(EventType.DAILY_UPDATE);
        event34.setDate(LocalDate.of(2018, 9, 12));
        event34.setStandard(class1);
        event34 = eventRepository.save(event34);

        SlotEventDetails slotEventDetails34 = new SlotEventDetails();
        slotEventDetails34.setEvent(event34);
        slotEventDetails34.setScd(slotCourseDetails34);
        slotEventDetailsRepository.save(slotEventDetails34);

        Event event35 = new Event();
        event35.setName("Daily Update");
        event35.setDescription("This is a daily update for today's class");
        event35.setType(EventType.DAILY_UPDATE);
        event35.setDate(LocalDate.of(2018, 9, 12));
        event35.setStandard(class1);
        event35 = eventRepository.save(event35);

        SlotEventDetails slotEventDetails35 = new SlotEventDetails();
        slotEventDetails35.setEvent(event35);
        slotEventDetails35.setScd(slotCourseDetails35);
        slotEventDetailsRepository.save(slotEventDetails35);

        Event event36 = new Event();
        event36.setName("Daily Update");
        event36.setDescription("This is a daily update for today's class");
        event36.setType(EventType.DAILY_UPDATE);
        event36.setDate(LocalDate.of(2018, 9, 12));
        event36.setStandard(class1);
        event36 = eventRepository.save(event36);

        SlotEventDetails slotEventDetails36 = new SlotEventDetails();
        slotEventDetails36.setEvent(event36);
        slotEventDetails36.setScd(slotCourseDetails36);
        slotEventDetailsRepository.save(slotEventDetails36);

        Event event37 = new Event();
        event37.setName("Daily Update");
        event37.setDescription("This is a daily update for today's class");
        event37.setType(EventType.DAILY_UPDATE);
        event37.setDate(LocalDate.of(2018, 9, 12));
        event37.setStandard(class1);
        event37 = eventRepository.save(event37);

        SlotEventDetails slotEventDetails37 = new SlotEventDetails();
        slotEventDetails37.setEvent(event37);
        slotEventDetails37.setScd(slotCourseDetails37);
        slotEventDetailsRepository.save(slotEventDetails37);

        Event event38 = new Event();
        event38.setName("Daily Update");
        event38.setDescription("This is a daily update for today's class");
        event38.setType(EventType.DAILY_UPDATE);
        event38.setDate(LocalDate.of(2018, 9, 12));
        event38.setStandard(class1);
        event38 = eventRepository.save(event38);

        SlotEventDetails slotEventDetails38 = new SlotEventDetails();
        slotEventDetails38.setEvent(event38);
        slotEventDetails38.setScd(slotCourseDetails38);
        slotEventDetailsRepository.save(slotEventDetails38);

        //Daily Update for Thursday
        Event event41 = new Event();
        event41.setName("Daily Update");
        event41.setDescription("This is a daily update for today's class");
        event41.setType(EventType.DAILY_UPDATE);
        event41.setDate(LocalDate.of(2018, 9, 13));
        event41.setStandard(class1);
        event41 = eventRepository.save(event41);

        SlotEventDetails slotEventDetails41 = new SlotEventDetails();
        slotEventDetails41.setEvent(event41);
        slotEventDetails41.setScd(slotCourseDetails41);
        slotEventDetailsRepository.save(slotEventDetails41);

        Event event42 = new Event();
        event42.setName("Daily Update");
        event42.setDescription("This is a daily update for today's class");
        event42.setType(EventType.DAILY_UPDATE);
        event42.setDate(LocalDate.of(2018, 9, 13));
        event42.setStandard(class1);
        event42= eventRepository.save(event42);

        SlotEventDetails slotEventDetails42 = new SlotEventDetails();
        slotEventDetails42.setEvent(event42);
        slotEventDetails42.setScd(slotCourseDetails42);
        slotEventDetailsRepository.save(slotEventDetails42);

        Event event43 = new Event();
        event43.setName("Daily Update");
        event43.setDescription("This is a daily update for today's class");
        event43.setType(EventType.DAILY_UPDATE);
        event43.setDate(LocalDate.of(2018, 9, 13));
        event43.setStandard(class1);
        event43 = eventRepository.save(event43);

        SlotEventDetails slotEventDetails43 = new SlotEventDetails();
        slotEventDetails43.setEvent(event43);
        slotEventDetails43.setScd(slotCourseDetails43);
        slotEventDetailsRepository.save(slotEventDetails43);

        Event event44 = new Event();
        event44.setName("Daily Update");
        event44.setDescription("This is a daily update for today's class");
        event44.setType(EventType.DAILY_UPDATE);
        event44.setDate(LocalDate.of(2018, 9, 13));
        event44.setStandard(class1);
        event44= eventRepository.save(event44);

        SlotEventDetails slotEventDetails44 = new SlotEventDetails();
        slotEventDetails44.setEvent(event44);
        slotEventDetails44.setScd(slotCourseDetails44);
        slotEventDetailsRepository.save(slotEventDetails44);

        Event event45 = new Event();
        event45.setName("Daily Update");
        event45.setDescription("This is a daily update for today's class");
        event45.setType(EventType.DAILY_UPDATE);
        event45.setDate(LocalDate.of(2018, 9, 13));
        event45.setStandard(class1);
        event45= eventRepository.save(event45);

        SlotEventDetails slotEventDetails45 = new SlotEventDetails();
        slotEventDetails45.setEvent(event45);
        slotEventDetails45.setScd(slotCourseDetails45);
        slotEventDetailsRepository.save(slotEventDetails45);

        Event event46 = new Event();
        event46.setName("Daily Update");
        event46.setDescription("This is a daily update for today's class");
        event46.setType(EventType.DAILY_UPDATE);
        event46.setDate(LocalDate.of(2018, 9, 13));
        event46.setStandard(class1);
        event46= eventRepository.save(event46);

        SlotEventDetails slotEventDetails46 = new SlotEventDetails();
        slotEventDetails46.setEvent(event46);
        slotEventDetails46.setScd(slotCourseDetails46);
        slotEventDetailsRepository.save(slotEventDetails46);

        Event event47 = new Event();
        event47.setName("Daily Update");
        event47.setDescription("This is a daily update for today's class");
        event47.setType(EventType.DAILY_UPDATE);
        event47.setDate(LocalDate.of(2018, 9, 13));
        event47.setStandard(class1);
        event47= eventRepository.save(event47);

        SlotEventDetails slotEventDetails47 = new SlotEventDetails();
        slotEventDetails47.setEvent(event47);
        slotEventDetails47.setScd(slotCourseDetails47);
        slotEventDetailsRepository.save(slotEventDetails47);

        Event event48 = new Event();
        event48.setName("Daily Update");
        event48.setDescription("This is a daily update for today's class");
        event48.setType(EventType.DAILY_UPDATE);
        event48.setDate(LocalDate.of(2018, 9, 13));
        event48.setStandard(class1);
        event48 = eventRepository.save(event48);

        SlotEventDetails slotEventDetails48 = new SlotEventDetails();
        slotEventDetails48.setEvent(event48);
        slotEventDetails48.setScd(slotCourseDetails48);
        slotEventDetailsRepository.save(slotEventDetails48);

        //Daily Update for Friday
        Event event51 = new Event();
        event51.setName("Daily Update");
        event51.setDescription("This is a daily update for today's class");
        event51.setType(EventType.DAILY_UPDATE);
        event51.setDate(LocalDate.of(2018, 9, 14));
        event51.setStandard(class1);
        event51 = eventRepository.save(event51);

        SlotEventDetails slotEventDetails51 = new SlotEventDetails();
        slotEventDetails51.setEvent(event51);
        slotEventDetails51.setScd(slotCourseDetails51);
        slotEventDetailsRepository.save(slotEventDetails51);

        Event event52 = new Event();
        event52.setName("Daily Update");
        event52.setDescription("This is a daily update for today's class");
        event52.setType(EventType.DAILY_UPDATE);
        event52.setDate(LocalDate.of(2018, 9, 14));
        event52.setStandard(class1);
        event52 = eventRepository.save(event52);

        SlotEventDetails slotEventDetails52 = new SlotEventDetails();
        slotEventDetails52.setEvent(event52);
        slotEventDetails52.setScd(slotCourseDetails52);
        slotEventDetailsRepository.save(slotEventDetails52);

        Event event53 = new Event();
        event53.setName("Daily Update");
        event53.setDescription("This is a daily update for today's class");
        event53.setType(EventType.DAILY_UPDATE);
        event53.setDate(LocalDate.of(2018, 9, 14));
        event53.setStandard(class1);
        event53 = eventRepository.save(event53);

        SlotEventDetails slotEventDetails53 = new SlotEventDetails();
        slotEventDetails53.setEvent(event53);
        slotEventDetails53.setScd(slotCourseDetails53);
        slotEventDetailsRepository.save(slotEventDetails53);

        Event event54 = new Event();
        event54.setName("Daily Update");
        event54.setDescription("This is a daily update for today's class");
        event54.setType(EventType.DAILY_UPDATE);
        event54.setDate(LocalDate.of(2018, 9, 14));
        event54.setStandard(class1);
        event54 = eventRepository.save(event54);

        SlotEventDetails slotEventDetails54 = new SlotEventDetails();
        slotEventDetails54.setEvent(event54);
        slotEventDetails54.setScd(slotCourseDetails54);
        slotEventDetailsRepository.save(slotEventDetails54);

        Event event55 = new Event();
        event55.setName("Daily Update");
        event55.setDescription("This is a daily update for today's class");
        event55.setType(EventType.DAILY_UPDATE);
        event55.setDate(LocalDate.of(2018, 9, 14));
        event55.setStandard(class1);
        event55 = eventRepository.save(event55);

        SlotEventDetails slotEventDetails55 = new SlotEventDetails();
        slotEventDetails55.setEvent(event55);
        slotEventDetails55.setScd(slotCourseDetails55);
        slotEventDetailsRepository.save(slotEventDetails55);

        Event event56 = new Event();
        event56.setName("Daily Update");
        event56.setDescription("This is a daily update for today's class");
        event56.setType(EventType.DAILY_UPDATE);
        event56.setDate(LocalDate.of(2018, 9, 14));
        event56.setStandard(class1);
        event56 = eventRepository.save(event56);

        SlotEventDetails slotEventDetails56 = new SlotEventDetails();
        slotEventDetails56.setEvent(event56);
        slotEventDetails56.setScd(slotCourseDetails56);
        slotEventDetailsRepository.save(slotEventDetails56);

        Event event57 = new Event();
        event57.setName("Daily Update");
        event57.setDescription("This is a daily update for today's class");
        event57.setType(EventType.DAILY_UPDATE);
        event57.setDate(LocalDate.of(2018, 9, 14));
        event57.setStandard(class1);
        event57 = eventRepository.save(event57);

        SlotEventDetails slotEventDetails57 = new SlotEventDetails();
        slotEventDetails57.setEvent(event57);
        slotEventDetails57.setScd(slotCourseDetails57);
        slotEventDetailsRepository.save(slotEventDetails57);

        Event event58 = new Event();
        event58.setName("Daily Update");
        event58.setDescription("This is a daily update for today's class");
        event58.setType(EventType.DAILY_UPDATE);
        event58.setDate(LocalDate.of(2018, 9, 14));
        event58.setStandard(class1);
        event58 = eventRepository.save(event58);

        SlotEventDetails slotEventDetails58 = new SlotEventDetails();
        slotEventDetails58.setEvent(event58);
        slotEventDetails58.setScd(slotCourseDetails58);
        slotEventDetailsRepository.save(slotEventDetails58);


        //Test Events created on 10, 13 and dated for 12, 14
        Event testEvent11 = new Event();
        testEvent11.setName("Test Created");
        testEvent11.setDescription("Description about test");
        testEvent11.setType(EventType.TEST);
        testEvent11.setDate(LocalDate.of(2018, 9, 12));
        testEvent11.setStandard(class1);
        testEvent11.setCreatedDate(LocalDate.of(2018, 9, 10).atStartOfDay().toInstant(ZoneOffset.UTC));
        testEvent11 = eventRepository.save(testEvent11);

        SlotEventDetails slotTestEventDetails11 = new SlotEventDetails();
        slotTestEventDetails11.setEvent(testEvent11);
        slotTestEventDetails11.setScd(slotCourseDetails11);
        slotEventDetailsRepository.save(slotTestEventDetails11);

        Event testEvent12 = new Event();
        testEvent12.setName("Test Created");
        testEvent12.setDescription("Description about test");
        testEvent12.setType(EventType.TEST);
        testEvent12.setDate(LocalDate.of(2018, 9, 12));
        testEvent12.setStandard(class1);
        testEvent12.setCreatedDate(LocalDate.of(2018, 9, 10).atStartOfDay().toInstant(ZoneOffset.UTC));
        testEvent12 = eventRepository.save(testEvent12);

        SlotEventDetails slotTestEventDetails12 = new SlotEventDetails();
        slotTestEventDetails12.setEvent(testEvent12);
        slotTestEventDetails12.setScd(slotCourseDetails12);
        slotEventDetailsRepository.save(slotTestEventDetails12);

        Event testEvent13 = new Event();
        testEvent13.setName("Test Created");
        testEvent13.setDescription("Description about test");
        testEvent13.setType(EventType.TEST);
        testEvent13.setDate(LocalDate.of(2018, 9, 12));
        testEvent13.setStandard(class1);
        testEvent13.setCreatedDate(LocalDate.of(2018, 9, 10).atStartOfDay().toInstant(ZoneOffset.UTC));
        testEvent13 = eventRepository.save(testEvent13);

        SlotEventDetails slotTestEventDetails13 = new SlotEventDetails();
        slotTestEventDetails13.setEvent(testEvent13);
        slotTestEventDetails13.setScd(slotCourseDetails13);
        slotEventDetailsRepository.save(slotTestEventDetails13);

        Event testEvent14 = new Event();
        testEvent14.setName("Test Created");
        testEvent14.setDescription("Description about test");
        testEvent14.setType(EventType.TEST);
        testEvent14.setDate(LocalDate.of(2018, 9, 12));
        testEvent14.setStandard(class1);
        testEvent14.setCreatedDate(LocalDate.of(2018, 9, 10).atStartOfDay().toInstant(ZoneOffset.UTC));
        testEvent14 = eventRepository.save(testEvent14);

        SlotEventDetails slotTestEventDetails14 = new SlotEventDetails();
        slotTestEventDetails14.setEvent(testEvent14);
        slotTestEventDetails14.setScd(slotCourseDetails14);
        slotEventDetailsRepository.save(slotTestEventDetails14);

        Event testEvent15 = new Event();
        testEvent15.setName("Test Created");
        testEvent15.setDescription("Description about test");
        testEvent15.setType(EventType.TEST);
        testEvent15.setDate(LocalDate.of(2018, 9, 12));
        testEvent15.setStandard(class1);
        testEvent15.setCreatedDate(LocalDate.of(2018, 9, 10).atStartOfDay().toInstant(ZoneOffset.UTC));
        testEvent15 = eventRepository.save(testEvent15);

        SlotEventDetails slotTestEventDetails15 = new SlotEventDetails();
        slotTestEventDetails15.setEvent(testEvent15);
        slotTestEventDetails15.setScd(slotCourseDetails15);
        slotEventDetailsRepository.save(slotTestEventDetails15);

        Event testEvent16 = new Event();
        testEvent16.setName("Test Created");
        testEvent16.setDescription("Description about test");
        testEvent16.setType(EventType.TEST);
        testEvent16.setDate(LocalDate.of(2018, 9, 12));
        testEvent16.setStandard(class1);
        testEvent16.setCreatedDate(LocalDate.of(2018, 9, 10).atStartOfDay().toInstant(ZoneOffset.UTC));
        testEvent16 = eventRepository.save(testEvent16);

        SlotEventDetails slotTestEventDetails16 = new SlotEventDetails();
        slotTestEventDetails16.setEvent(testEvent16);
        slotTestEventDetails16.setScd(slotCourseDetails16);
        slotEventDetailsRepository.save(slotTestEventDetails16);

        Event testEvent17 = new Event();
        testEvent17.setName("Test Created");
        testEvent17.setDescription("Description about test");
        testEvent17.setType(EventType.TEST);
        testEvent17.setDate(LocalDate.of(2018, 9, 12));
        testEvent17.setStandard(class1);
        testEvent17.setCreatedDate(LocalDate.of(2018, 9, 10).atStartOfDay().toInstant(ZoneOffset.UTC));
        testEvent17 = eventRepository.save(testEvent17);

        SlotEventDetails slotTestEventDetails17 = new SlotEventDetails();
        slotTestEventDetails17.setEvent(testEvent17);
        slotTestEventDetails17.setScd(slotCourseDetails17);
        slotEventDetailsRepository.save(slotTestEventDetails17);

        Event testEvent18 = new Event();
        testEvent18.setName("Test Created");
        testEvent18.setDescription("Description about test");
        testEvent18.setType(EventType.TEST);
        testEvent18.setDate(LocalDate.of(2018, 9, 12));
        testEvent18.setStandard(class1);
        testEvent18.setCreatedDate(LocalDate.of(2018, 9, 10).atStartOfDay().toInstant(ZoneOffset.UTC));
        testEvent18 = eventRepository.save(testEvent18);

        SlotEventDetails slotTestEventDetails18 = new SlotEventDetails();
        slotTestEventDetails18.setEvent(testEvent18);
        slotTestEventDetails18.setScd(slotCourseDetails18);
        slotEventDetailsRepository.save(slotTestEventDetails18);


        Event testEvent41 = new Event();
        testEvent41.setName("Test Created");
        testEvent41.setDescription("Description about test");
        testEvent41.setType(EventType.TEST);
        testEvent41.setDate(LocalDate.of(2018, 9, 14));
        testEvent41.setStandard(class1);
        testEvent41.setCreatedDate(LocalDate.of(2018, 9, 13).atStartOfDay().toInstant(ZoneOffset.UTC));
        testEvent41 = eventRepository.save(testEvent41);

        SlotEventDetails slotTestEventDetails41 = new SlotEventDetails();
        slotTestEventDetails41.setEvent(testEvent41);
        slotTestEventDetails41.setScd(slotCourseDetails41);
        slotEventDetailsRepository.save(slotTestEventDetails41);

        Event testEvent42 = new Event();
        testEvent42.setName("Test Created");
        testEvent42.setDescription("Description about test");
        testEvent42.setType(EventType.TEST);
        testEvent42.setDate(LocalDate.of(2018, 9, 14));
        testEvent42.setStandard(class1);
        testEvent42.setCreatedDate(LocalDate.of(2018, 9, 13).atStartOfDay().toInstant(ZoneOffset.UTC));
        testEvent42 = eventRepository.save(testEvent42);

        SlotEventDetails slotTestEventDetails42 = new SlotEventDetails();
        slotTestEventDetails42.setEvent(testEvent42);
        slotTestEventDetails42.setScd(slotCourseDetails42);
        slotEventDetailsRepository.save(slotTestEventDetails42);

        Event testEvent43 = new Event();
        testEvent43.setName("Test Created");
        testEvent43.setDescription("Description about test");
        testEvent43.setType(EventType.TEST);
        testEvent43.setDate(LocalDate.of(2018, 9, 14));
        testEvent43.setStandard(class1);
        testEvent43.setCreatedDate(LocalDate.of(2018, 9, 13).atStartOfDay().toInstant(ZoneOffset.UTC));
        testEvent43 = eventRepository.save(testEvent43);

        SlotEventDetails slotTestEventDetails43 = new SlotEventDetails();
        slotTestEventDetails43.setEvent(testEvent43);
        slotTestEventDetails43.setScd(slotCourseDetails43);
        slotEventDetailsRepository.save(slotTestEventDetails43);


        Event testEvent44 = new Event();
        testEvent44.setName("Test Created");
        testEvent44.setDescription("Description about test");
        testEvent44.setType(EventType.TEST);
        testEvent44.setDate(LocalDate.of(2018, 9, 14));
        testEvent44.setStandard(class1);
        testEvent44.setCreatedDate(LocalDate.of(2018, 9, 13).atStartOfDay().toInstant(ZoneOffset.UTC));
        testEvent44 = eventRepository.save(testEvent44);

        SlotEventDetails slotTestEventDetails44 = new SlotEventDetails();
        slotTestEventDetails44.setEvent(testEvent44);
        slotTestEventDetails44.setScd(slotCourseDetails44);
        slotEventDetailsRepository.save(slotTestEventDetails44);

        Event testEvent45 = new Event();
        testEvent45.setName("Test Created");
        testEvent45.setDescription("Description about test");
        testEvent45.setType(EventType.TEST);
        testEvent45.setDate(LocalDate.of(2018, 9, 14));
        testEvent45.setStandard(class1);
        testEvent45.setCreatedDate(LocalDate.of(2018, 9, 13).atStartOfDay().toInstant(ZoneOffset.UTC));
        testEvent45 = eventRepository.save(testEvent45);

        SlotEventDetails slotTestEventDetails45 = new SlotEventDetails();
        slotTestEventDetails45.setEvent(testEvent45);
        slotTestEventDetails45.setScd(slotCourseDetails45);
        slotEventDetailsRepository.save(slotTestEventDetails45);

        Event testEvent46 = new Event();
        testEvent46.setName("Test Created");
        testEvent46.setDescription("Description about test");
        testEvent46.setType(EventType.TEST);
        testEvent46.setDate(LocalDate.of(2018, 9, 14));
        testEvent46.setStandard(class1);
        testEvent46.setCreatedDate(LocalDate.of(2018, 9, 13).atStartOfDay().toInstant(ZoneOffset.UTC));
        testEvent46 = eventRepository.save(testEvent46);

        SlotEventDetails slotTestEventDetails46 = new SlotEventDetails();
        slotTestEventDetails46.setEvent(testEvent46);
        slotTestEventDetails46.setScd(slotCourseDetails46);
        slotEventDetailsRepository.save(slotTestEventDetails46);


        Event testEvent47 = new Event();
        testEvent47.setName("Test Created");
        testEvent47.setDescription("Description about test");
        testEvent47.setType(EventType.TEST);
        testEvent47.setDate(LocalDate.of(2018, 9, 14));
        testEvent47.setStandard(class1);
        testEvent47.setCreatedDate(LocalDate.of(2018, 9, 13).atStartOfDay().toInstant(ZoneOffset.UTC));
        testEvent47 = eventRepository.save(testEvent47);

        SlotEventDetails slotTestEventDetails47 = new SlotEventDetails();
        slotTestEventDetails47.setEvent(testEvent47);
        slotTestEventDetails47.setScd(slotCourseDetails47);
        slotEventDetailsRepository.save(slotTestEventDetails47);


        Event testEvent48 = new Event();
        testEvent48.setName("Test Created");
        testEvent48.setDescription("Description about test");
        testEvent48.setType(EventType.TEST);
        testEvent48.setDate(LocalDate.of(2018, 9, 14));
        testEvent48.setStandard(class1);
        testEvent48.setCreatedDate(LocalDate.of(2018, 9, 13).atStartOfDay().toInstant(ZoneOffset.UTC));
        testEvent48 = eventRepository.save(testEvent48);

        SlotEventDetails slotTestEventDetails48 = new SlotEventDetails();
        slotTestEventDetails48.setEvent(testEvent48);
        slotTestEventDetails48.setScd(slotCourseDetails48);
        slotEventDetailsRepository.save(slotTestEventDetails48);



        //Assignment Events created on 10, 13 and dated for 12, 14
        Event assignmentEvent11 = new Event();
        assignmentEvent11.setName("Assignment Created");
        assignmentEvent11.setDescription("Description about assignment");
        assignmentEvent11.setType(EventType.ASSIGNMENT);
        assignmentEvent11.setDate(LocalDate.of(2018, 9, 12));
        assignmentEvent11.setStandard(class1);
        assignmentEvent11.setCreatedDate(LocalDate.of(2018, 9, 10).atStartOfDay().toInstant(ZoneOffset.UTC));
        assignmentEvent11 = eventRepository.save(assignmentEvent11);

        SlotEventDetails slotAssignmentEventDetails11 = new SlotEventDetails();
        slotAssignmentEventDetails11.setEvent(assignmentEvent11);
        slotAssignmentEventDetails11.setScd(slotCourseDetails11);
        slotEventDetailsRepository.save(slotAssignmentEventDetails11);

        Event assignmentEvent12 = new Event();
        assignmentEvent12.setName("Assignment Created");
        assignmentEvent12.setDescription("Description about assignment");
        assignmentEvent12.setType(EventType.ASSIGNMENT);
        assignmentEvent12.setDate(LocalDate.of(2018, 9, 12));
        assignmentEvent12.setStandard(class1);
        assignmentEvent12.setCreatedDate(LocalDate.of(2018, 9, 10).atStartOfDay().toInstant(ZoneOffset.UTC));
        assignmentEvent12 = eventRepository.save(assignmentEvent12);

        SlotEventDetails slotAssignmentEventDetails12 = new SlotEventDetails();
        slotAssignmentEventDetails12.setEvent(assignmentEvent12);
        slotAssignmentEventDetails12.setScd(slotCourseDetails12);
        slotEventDetailsRepository.save(slotAssignmentEventDetails12);

        Event assignmentEvent13 = new Event();
        assignmentEvent13.setName("Assignment Created");
        assignmentEvent13.setDescription("Description about assignment");
        assignmentEvent13.setType(EventType.ASSIGNMENT);
        assignmentEvent13.setDate(LocalDate.of(2018, 9, 12));
        assignmentEvent13.setStandard(class1);
        assignmentEvent13.setCreatedDate(LocalDate.of(2018, 9, 10).atStartOfDay().toInstant(ZoneOffset.UTC));
        assignmentEvent13 = eventRepository.save(assignmentEvent13);

        SlotEventDetails slotAssignmentEventDetails13 = new SlotEventDetails();
        slotAssignmentEventDetails13.setEvent(assignmentEvent13);
        slotAssignmentEventDetails13.setScd(slotCourseDetails13);
        slotEventDetailsRepository.save(slotAssignmentEventDetails13);

        Event assignmentEvent14 = new Event();
        assignmentEvent14.setName("Assignment Created");
        assignmentEvent14.setDescription("Description about assignment");
        assignmentEvent14.setType(EventType.ASSIGNMENT);
        assignmentEvent14.setDate(LocalDate.of(2018, 9, 12));
        assignmentEvent14.setStandard(class1);
        assignmentEvent14.setCreatedDate(LocalDate.of(2018, 9, 10).atStartOfDay().toInstant(ZoneOffset.UTC));
        assignmentEvent14 = eventRepository.save(assignmentEvent14);

        SlotEventDetails slotAssignmentEventDetails14 = new SlotEventDetails();
        slotAssignmentEventDetails14.setEvent(assignmentEvent14);
        slotAssignmentEventDetails14.setScd(slotCourseDetails14);
        slotEventDetailsRepository.save(slotAssignmentEventDetails14);

        Event assignmentEvent15 = new Event();
        assignmentEvent15.setName("Assignment Created");
        assignmentEvent15.setDescription("Description about assignment");
        assignmentEvent15.setType(EventType.ASSIGNMENT);
        assignmentEvent15.setDate(LocalDate.of(2018, 9, 12));
        assignmentEvent15.setStandard(class1);
        assignmentEvent15.setCreatedDate(LocalDate.of(2018, 9, 10).atStartOfDay().toInstant(ZoneOffset.UTC));
        assignmentEvent15 = eventRepository.save(assignmentEvent15);

        SlotEventDetails slotAssignmentEventDetails15 = new SlotEventDetails();
        slotAssignmentEventDetails15.setEvent(assignmentEvent15);
        slotAssignmentEventDetails15.setScd(slotCourseDetails15);
        slotEventDetailsRepository.save(slotAssignmentEventDetails15);

        Event assignmentEvent16 = new Event();
        assignmentEvent16.setName("Assignment Created");
        assignmentEvent16.setDescription("Description about assignment");
        assignmentEvent16.setType(EventType.ASSIGNMENT);
        assignmentEvent16.setDate(LocalDate.of(2018, 9, 12));
        assignmentEvent16.setStandard(class1);
        assignmentEvent16.setCreatedDate(LocalDate.of(2018, 9, 10).atStartOfDay().toInstant(ZoneOffset.UTC));
        assignmentEvent16 = eventRepository.save(assignmentEvent16);

        SlotEventDetails slotAssignmentEventDetails16 = new SlotEventDetails();
        slotAssignmentEventDetails16.setEvent(assignmentEvent16);
        slotAssignmentEventDetails16.setScd(slotCourseDetails16);
        slotEventDetailsRepository.save(slotAssignmentEventDetails16);

        Event assignmentEvent17 = new Event();
        assignmentEvent17.setName("Assignment Created");
        assignmentEvent17.setDescription("Description about assignment");
        assignmentEvent17.setType(EventType.ASSIGNMENT);
        assignmentEvent17.setDate(LocalDate.of(2018, 9, 12));
        assignmentEvent17.setStandard(class1);
        assignmentEvent17.setCreatedDate(LocalDate.of(2018, 9, 10).atStartOfDay().toInstant(ZoneOffset.UTC));
        assignmentEvent17 = eventRepository.save(assignmentEvent17);

        SlotEventDetails slotAssignmentEventDetails17 = new SlotEventDetails();
        slotAssignmentEventDetails17.setEvent(assignmentEvent17);
        slotAssignmentEventDetails17.setScd(slotCourseDetails17);
        slotEventDetailsRepository.save(slotAssignmentEventDetails17);

        Event assignmentEvent18 = new Event();
        assignmentEvent18.setName("Assignment Created");
        assignmentEvent18.setDescription("Description about assignment");
        assignmentEvent18.setType(EventType.ASSIGNMENT);
        assignmentEvent18.setDate(LocalDate.of(2018, 9, 12));
        assignmentEvent18.setStandard(class1);
        assignmentEvent18.setCreatedDate(LocalDate.of(2018, 9, 10).atStartOfDay().toInstant(ZoneOffset.UTC));
        assignmentEvent18 = eventRepository.save(assignmentEvent18);

        SlotEventDetails slotAssignmentEventDetails18 = new SlotEventDetails();
        slotAssignmentEventDetails18.setEvent(assignmentEvent18);
        slotAssignmentEventDetails18.setScd(slotCourseDetails18);
        slotEventDetailsRepository.save(slotAssignmentEventDetails18);


        Event assignmentEvent41 = new Event();
        assignmentEvent41.setName("Assignment Created");
        assignmentEvent41.setDescription("Description about assignment");
        assignmentEvent41.setType(EventType.ASSIGNMENT);
        assignmentEvent41.setDate(LocalDate.of(2018, 9, 14));
        assignmentEvent41.setStandard(class1);
        assignmentEvent41.setCreatedDate(LocalDate.of(2018, 9, 13).atStartOfDay().toInstant(ZoneOffset.UTC));
        assignmentEvent41 = eventRepository.save(assignmentEvent41);

        SlotEventDetails slotAssignmentEventDetails41 = new SlotEventDetails();
        slotAssignmentEventDetails41.setEvent(assignmentEvent41);
        slotAssignmentEventDetails41.setScd(slotCourseDetails41);
        slotEventDetailsRepository.save(slotAssignmentEventDetails41);

        Event assignmentEvent42 = new Event();
        assignmentEvent42.setName("Assignment Created");
        assignmentEvent42.setDescription("Description about assignment");
        assignmentEvent42.setType(EventType.ASSIGNMENT);
        assignmentEvent42.setDate(LocalDate.of(2018, 9, 14));
        assignmentEvent42.setStandard(class1);
        assignmentEvent42.setCreatedDate(LocalDate.of(2018, 9, 13).atStartOfDay().toInstant(ZoneOffset.UTC));
        assignmentEvent42 = eventRepository.save(assignmentEvent42);

        SlotEventDetails slotAssignmentEventDetails42 = new SlotEventDetails();
        slotAssignmentEventDetails42.setEvent(assignmentEvent42);
        slotAssignmentEventDetails42.setScd(slotCourseDetails42);
        slotEventDetailsRepository.save(slotAssignmentEventDetails42);

        Event assignmentEvent43 = new Event();
        assignmentEvent43.setName("Assignment Created");
        assignmentEvent43.setDescription("Description about assignment");
        assignmentEvent43.setType(EventType.ASSIGNMENT);
        assignmentEvent43.setDate(LocalDate.of(2018, 9, 14));
        assignmentEvent43.setStandard(class1);
        assignmentEvent43.setCreatedDate(LocalDate.of(2018, 9, 13).atStartOfDay().toInstant(ZoneOffset.UTC));
        assignmentEvent43 = eventRepository.save(assignmentEvent43);

        SlotEventDetails slotAssignmentEventDetails43 = new SlotEventDetails();
        slotAssignmentEventDetails43.setEvent(assignmentEvent43);
        slotAssignmentEventDetails43.setScd(slotCourseDetails43);
        slotEventDetailsRepository.save(slotAssignmentEventDetails43);


        Event assignmentEvent44 = new Event();
        assignmentEvent44.setName("Assignment Created");
        assignmentEvent44.setDescription("Description about assignment");
        assignmentEvent44.setType(EventType.ASSIGNMENT);
        assignmentEvent44.setDate(LocalDate.of(2018, 9, 14));
        assignmentEvent44.setStandard(class1);
        assignmentEvent44.setCreatedDate(LocalDate.of(2018, 9, 13).atStartOfDay().toInstant(ZoneOffset.UTC));
        assignmentEvent44 = eventRepository.save(assignmentEvent44);

        SlotEventDetails slotAssignmentEventDetails44 = new SlotEventDetails();
        slotAssignmentEventDetails44.setEvent(assignmentEvent44);
        slotAssignmentEventDetails44.setScd(slotCourseDetails44);
        slotEventDetailsRepository.save(slotAssignmentEventDetails44);

        Event assignmentEvent45 = new Event();
        assignmentEvent45.setName("Assignment Created");
        assignmentEvent45.setDescription("Description about assignment");
        assignmentEvent45.setType(EventType.ASSIGNMENT);
        assignmentEvent45.setDate(LocalDate.of(2018, 9, 14));
        assignmentEvent45.setStandard(class1);
        assignmentEvent45.setCreatedDate(LocalDate.of(2018, 9, 13).atStartOfDay().toInstant(ZoneOffset.UTC));
        assignmentEvent45 = eventRepository.save(assignmentEvent45);

        SlotEventDetails slotAssignmentEventDetails45 = new SlotEventDetails();
        slotAssignmentEventDetails45.setEvent(assignmentEvent45);
        slotAssignmentEventDetails45.setScd(slotCourseDetails45);
        slotEventDetailsRepository.save(slotAssignmentEventDetails45);

        Event assignmentEvent46 = new Event();
        assignmentEvent46.setName("Assignment Created");
        assignmentEvent46.setDescription("Description about assignment");
        assignmentEvent46.setType(EventType.ASSIGNMENT);
        assignmentEvent46.setDate(LocalDate.of(2018, 9, 14));
        assignmentEvent46.setStandard(class1);
        assignmentEvent46.setCreatedDate(LocalDate.of(2018, 9, 13).atStartOfDay().toInstant(ZoneOffset.UTC));
        assignmentEvent46 = eventRepository.save(assignmentEvent46);

        SlotEventDetails slotAssignmentEventDetails46 = new SlotEventDetails();
        slotAssignmentEventDetails46.setEvent(assignmentEvent46);
        slotAssignmentEventDetails46.setScd(slotCourseDetails46);
        slotEventDetailsRepository.save(slotAssignmentEventDetails46);


        Event assignmentEvent47 = new Event();
        assignmentEvent47.setName("Assignment Created");
        assignmentEvent47.setDescription("Description about assignment");
        assignmentEvent47.setType(EventType.ASSIGNMENT);
        assignmentEvent47.setDate(LocalDate.of(2018, 9, 14));
        assignmentEvent47.setStandard(class1);
        assignmentEvent47.setCreatedDate(LocalDate.of(2018, 9, 13).atStartOfDay().toInstant(ZoneOffset.UTC));
        assignmentEvent47 = eventRepository.save(assignmentEvent47);

        SlotEventDetails slotAssignmentEventDetails47 = new SlotEventDetails();
        slotAssignmentEventDetails47.setEvent(assignmentEvent47);
        slotAssignmentEventDetails47.setScd(slotCourseDetails47);
        slotEventDetailsRepository.save(slotAssignmentEventDetails47);


        Event assignmentEvent48 = new Event();
        assignmentEvent48.setName("Assignment Created");
        assignmentEvent48.setDescription("Description about assignment");
        assignmentEvent48.setType(EventType.ASSIGNMENT);
        assignmentEvent48.setDate(LocalDate.of(2018, 9, 14));
        assignmentEvent48.setStandard(class1);
        assignmentEvent48.setCreatedDate(LocalDate.of(2018, 9, 13).atStartOfDay().toInstant(ZoneOffset.UTC));
        assignmentEvent48 = eventRepository.save(assignmentEvent48);

        SlotEventDetails slotAssignmentEventDetails48 = new SlotEventDetails();
        slotAssignmentEventDetails48.setEvent(assignmentEvent48);
        slotAssignmentEventDetails48.setScd(slotCourseDetails48);
        slotEventDetailsRepository.save(slotAssignmentEventDetails48);





    }

}
