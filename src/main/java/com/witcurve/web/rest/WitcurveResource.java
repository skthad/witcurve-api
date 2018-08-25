package com.witcurve.web.rest;

import com.witcurve.domain.*;
import com.witcurve.domain.Class;
import com.witcurve.domain.enumeration.Gender;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * Util controller to load data
 */
@RestController
@RequestMapping("/api")
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

    @RequestMapping(value = "/load-data", method = RequestMethod.GET)
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
        staff1.setFirstName("Anuranjan Kumar");
        staff1.setSchool(school1);
        staff1.setType("Teaching");

        staff1 = staffRepository.save(staff1);

        Staff staff2 = new Staff();
        staff2.setAddress1("Hitech City");
        staff2.setFirstName("Dhiraj Kumar");
        staff2.setSchool(school1);
        staff2.setType("Teaching");

        staff2 = staffRepository.save(staff2);

        Staff staff3 = new Staff();
        staff3.setAddress1("Hitech City");
        staff3.setFirstName("Srujan Kumar");
        staff3.setSchool(school1);
        staff3.setType("Teaching");

        staff3 = staffRepository.save(staff3);

        Staff staff4 = new Staff();
        staff4.setAddress1("Hitech City");
        staff4.setFirstName("Kishore Kumar");
        staff4.setSchool(school1);
        staff4.setType("Teaching");

        staff4 = staffRepository.save(staff4);

        Staff staff5 = new Staff();
        staff5.setAddress1("Hitech City");
        staff5.setFirstName("Manohar");
        staff5.setSchool(school1);
        staff5.setType("Teaching");

        staff5 = staffRepository.save(staff5);

        Staff staff6 = new Staff();
        staff6.setAddress1("Hitech City");
        staff6.setFirstName("Satya");
        staff6.setSchool(school1);
        staff6.setType("Teaching");

        staff6 = staffRepository.save(staff2);

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
        course3.setMasterSubject(masterSubject1);
        course3.setDescription("English");
        course3.setSchool(school1);
        course3 = courseRepository.save(course3);

        MasterSubject masterSubject4 = new MasterSubject();
        masterSubject4.setName("Maths");
        masterSubject4 = masterSubjectRepository.save(masterSubject4);

        Course course4 = new Course();
        course4.setCourseName("Maths");
        course4.setMasterSubject(masterSubject1);
        course4.setDescription("Maths");
        course4.setSchool(school1);
        course4 = courseRepository.save(course4);

        MasterSubject masterSubject5 = new MasterSubject();
        masterSubject5.setName("Science");
        masterSubject5 = masterSubjectRepository.save(masterSubject5);

        Course course5 = new Course();
        course5.setCourseName("Science");
        course5.setMasterSubject(masterSubject1);
        course5.setDescription("Science");
        course5.setSchool(school1);
        course5 = courseRepository.save(course5);

        MasterSubject masterSubject6 = new MasterSubject();
        masterSubject6.setName("Social Studies");
        masterSubject6 = masterSubjectRepository.save(masterSubject6);

        Course course6 = new Course();
        course6.setCourseName("Social Studies");
        course6.setMasterSubject(masterSubject1);
        course6.setDescription("Social Students");
        course6.setSchool(school1);
        course6 = courseRepository.save(course6);

        //course teacher data

        CourseTeacher courseTeacher1 = new CourseTeacher();
        courseTeacher1.setCourse(course1);
        courseTeacher1.setTeacher(staff1);
        courseTeacher1 = courseTeacherRepository.save(courseTeacher1);

        CourseTeacher courseTeacher2 = new CourseTeacher();
        courseTeacher2.setCourse(course2);
        courseTeacher2.setTeacher(staff2);
        courseTeacher2 = courseTeacherRepository.save(courseTeacher2);

        CourseTeacher courseTeacher3 = new CourseTeacher();
        courseTeacher3.setCourse(course3);
        courseTeacher3.setTeacher(staff3);
        courseTeacher3 = courseTeacherRepository.save(courseTeacher3);

        CourseTeacher courseTeacher4 = new CourseTeacher();
        courseTeacher4.setCourse(course4);
        courseTeacher4.setTeacher(staff4);
        courseTeacher4 = courseTeacherRepository.save(courseTeacher4);

        CourseTeacher courseTeacher5 = new CourseTeacher();
        courseTeacher5.setCourse(course5);
        courseTeacher5.setTeacher(staff5);
        courseTeacher5 = courseTeacherRepository.save(courseTeacher5);

        CourseTeacher courseTeacher6 = new CourseTeacher();
        courseTeacher6.setCourse(course6);
        courseTeacher6.setTeacher(staff6);
        courseTeacher6 = courseTeacherRepository.save(courseTeacher6);

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
        gsd3.setStart("1020");
        gsd3.setDuration(20);
        gsd3.setRecess(true);
        gsd3 = generalSlotDetailsRepository.save(gsd3);

        GeneralSlotDetails gsd4 = new GeneralSlotDetails();
        gsd4.setStandard(class1);
        gsd4.setStart("1040");
        gsd4.setDuration(40);
        gsd4 = generalSlotDetailsRepository.save(gsd4);

        GeneralSlotDetails gsd5 = new GeneralSlotDetails();
        gsd5.setStandard(class1);
        gsd5.setStart("1120");
        gsd5.setDuration(40);
        gsd5 = generalSlotDetailsRepository.save(gsd5);

        GeneralSlotDetails gsd6 = new GeneralSlotDetails();
        gsd6.setStandard(class1);
        gsd6.setStart("1200");
        gsd6.setDuration(60);
        gsd6.setRecess(true);
        gsd6 = generalSlotDetailsRepository.save(gsd6);

        GeneralSlotDetails gsd7 = new GeneralSlotDetails();
        gsd7.setStandard(class1);
        gsd7.setStart("1300");
        gsd7.setDuration(40);
        gsd7 = generalSlotDetailsRepository.save(gsd7);

        GeneralSlotDetails gsd8 = new GeneralSlotDetails();
        gsd8.setStandard(class1);
        gsd8.setStart("1340");
        gsd8.setDuration(40);
        gsd8 = generalSlotDetailsRepository.save(gsd8);

        GeneralSlotDetails gsd9 = new GeneralSlotDetails();
        gsd9.setStandard(class1);
        gsd9.setStart("1420");
        gsd9.setDuration(40);
        gsd9 = generalSlotDetailsRepository.save(gsd9);

        GeneralSlotDetails gsd10 = new GeneralSlotDetails();
        gsd10.setStandard(class1);
        gsd10.setStart("1500");
        gsd10.setDuration(40);
        gsd10 = generalSlotDetailsRepository.save(gsd10);


    }

}
