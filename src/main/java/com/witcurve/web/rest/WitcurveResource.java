package com.witcurve.web.rest;

import com.witcurve.domain.*;
import com.witcurve.domain.Class;
import com.witcurve.domain.enumeration.Gender;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.repository.*;
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

    private AcademicSessionRepository academicSessionRepository;

    private ClassRepository classRepository;

    private MasterSubjectRepository masterSubjectRepository;

    private CourseRepository courseRepository;

    private CourseTeacherRepository courseTeacherRepository;

    private GuardianRepository guardianRepository;

    private SchoolRepository schoolRepository;

    private StaffRepository staffRepository;

    private StudentRepository studentRepository;

    private TermRepository termRepository;


    private UserRepository userRepository;

    public WitcurveResource(AcademicSessionRepository academicSessionRepository, ClassRepository classRepository,
                            MasterSubjectRepository masterSubjectRepository, CourseRepository courseRepository,
                            CourseTeacherRepository courseTeacherRepository, GuardianRepository guardianRepository,
                            SchoolRepository schoolRepository,
                            StaffRepository staffRepository, StudentRepository studentRepository,
                            TermRepository termRepository,
                            UserRepository userRepository) {
        this.academicSessionRepository = academicSessionRepository;
        this.classRepository = classRepository;
        this.masterSubjectRepository = masterSubjectRepository;
        this.courseRepository = courseRepository;
        this.courseTeacherRepository = courseTeacherRepository;
        this.guardianRepository = guardianRepository;
        this.schoolRepository = schoolRepository;
        this.staffRepository = staffRepository;
        this.studentRepository = studentRepository;
        this.termRepository = termRepository;
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/load-data", method = RequestMethod.GET)
    public ResponseEntity loadDate() {
        loadSchoolData();
        return ResponseEntity.ok().build();
    }

    private void loadSchoolData() {

        //school data
        School school1 = new School();
        school1.setName("Oxford Schools");
        school1.setAddress1("Madhapur");
        school1.setAffiliationId("Affiliation_1111");
        school1.setCity("Hyderabad");
        school1.setState("Telangana");
        school1.setCountry("India");
        school1.setDistrict("Ranga Reddy District");
        school1.setPincode("500084");
        school1.setPrimaryPhone("9999999999");
        school1.setFax("9999999999");

        School school2 = new School();
        school2.setName("Cambridge Schools");
        school2.setAddress1("Gacchibowli");
        school2.setAffiliationId("Affiliation_2222");
        school2.setCity("Hyderabad");
        school2.setState("Telangana");
        school2.setCountry("India");
        school2.setDistrict("Ranga Reddy District");
        school2.setPincode("500084");
        school2.setPrimaryPhone("8888888888");
        school2.setFax("8888888888");

        school1= schoolRepository.save(school1);
        school2 = schoolRepository.save(school2);

        // academic session

        AcademicSession academicSession1 = new AcademicSession();
        academicSession1.setSchool(schoolRepository.findAll().get(0));
        academicSession1.setStartDate(LocalDate.of(2018, 1, 1));

        AcademicSession academicSession2 = new AcademicSession();
        academicSession2.setSchool(schoolRepository.findAll().get(0));
        academicSession2.setStartDate(LocalDate.of(2017, 1, 1));

        AcademicSession academicSession3 = new AcademicSession();
        academicSession3.setSchool(schoolRepository.findAll().get(1));
        academicSession3.setStartDate(LocalDate.of(2018, 1, 1));

        AcademicSession academicSession4 = new AcademicSession();
        academicSession4.setSchool(schoolRepository.findAll().get(1));
        academicSession4.setStartDate(LocalDate.of(2017, 1, 1));

        academicSession1 = academicSessionRepository.save(academicSession1);
        academicSession2 = academicSessionRepository.save(academicSession2);
        academicSession3 = academicSessionRepository.save(academicSession3);
        academicSession4 = academicSessionRepository.save(academicSession4);

        //terms

        Term term1 = new Term();
        term1.setSession(academicSession1);
        term1.setStartDate(LocalDate.of(2018, 2, 1));

        Term term2 = new Term();
        term2.setSession(academicSession1);
        term2.setStartDate(LocalDate.of(2018, 6, 1));

        term1 = termRepository.save(term1);
        term2 = termRepository.save(term2);

        //staff data

        Staff staff1 = new Staff();
        staff1.setAddress1("Madhapur");
        staff1.setFirstName("Suresh Kumar");
        staff1.setSchool(school1);
        staff1.setType("Teaching");

        staff1 = staffRepository.save(staff1);

        Staff staff2 = new Staff();
        staff2.setAddress1("Hitech City");
        staff2.setFirstName("Balaji Kirhsnan");
        staff2.setSchool(school1);
        staff2.setType("Teaching");

        staff2 = staffRepository.save(staff2);

        // class data

        Class class1 = new Class();
        class1.setGrade(Grade.XI);
        class1.setSchool(school1);
        class1.setTerm(term2);
        class1.setSection("A");
        class1.setClassTeacher(staff1);
        class1 = classRepository.save(class1);

        Class class2 = new Class();
        class2.setGrade(Grade.XII);
        class2.setSchool(school1);
        class2.setTerm(term2);
        class2.setSection("A");
        class2.setClassTeacher(staff1);
        class2 = classRepository.save(class2);

        // courses

        MasterSubject masterSubject = new MasterSubject();
        masterSubject.setName("Mathematics");
        masterSubject = masterSubjectRepository.save(masterSubject);

        Course course = new Course();
        course.setCourseName("Alg");
        course.setMasterSubject(masterSubject);
        course.setDescription("Algebra");
        course.setSchool(school1);
        course = courseRepository.save(course);





        // student data
        Student student1 = new Student();
        student1.setFirstName("kalyan");
        student1.setDateOfBirth(LocalDate.of(1999, 6, 25));
        student1.setNationality("Indian");
        student1.setCity("Hyderabad");
        student1.setAddress1("Nampally");
        student1.setGender(Gender.MALE);
        student1.setPincode("500001");
        student1.setState("Telangana");
        student1.setStandard(class1);
        student1.setSchool(schoolRepository.findAll().get(0));

        Student student2 = new Student();
        student2.setFirstName("Surya");
        student2.setDateOfBirth(LocalDate.of(1999, 6, 25));
        student2.setNationality("Indian");
        student2.setCity("Hyderabad");
        student2.setAddress1("Nampally");
        student2.setGender(Gender.MALE);
        student2.setPincode("500001");
        student2.setState("Telangana");
        student2.setStandard(class1);
        student2.setSchool(schoolRepository.findAll().get(0));

        studentRepository.save(student1);
        studentRepository.save(student2);

        Guardian g1 = new Guardian();
        g1.setFirstName("Prasad");
        g1.setType("Father");
        g1.setAnnualIncome(90000L);
        g1.setEmailId("prasad@prasad.com");
        g1.setMiddleName("Krishna");
        g1.setMobileNo("8888888888");
        g1.setStudent(studentRepository.findAll().get(0));

        Guardian g2 = new Guardian();
        g2.setFirstName("Mohan");
        g2.setType("Father");
        g2.setAnnualIncome(90000L);
        g2.setEmailId("mohan@mohan.com");
        g2.setMiddleName("Rao");
        g2.setMobileNo("8888888887");
        g2.setStudent(studentRepository.findAll().get(1));

        guardianRepository.save(g1);
        guardianRepository.save(g2);
    }

}
