package com.witcurve.web.rest;

import com.witcurve.domain.*;
import com.witcurve.domain.enumeration.EventType;
import com.witcurve.domain.enumeration.Gender;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.domain.enumeration.UserType;
import com.witcurve.repository.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private StandardRepository classRepository;

    @Autowired
    private MasterSubjectRepository masterSubjectRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseTeacherRepository courseTeacherRepository;

    @Autowired
    private StudentStandardRepository studentStandardRepository;

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
    private EventRepository eventRepository;

    @Autowired
    private InstituteRepository instituteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private SchoolInfoRepository schoolInfoRepository;

    @RequestMapping(value = "/load-data/parent", method = RequestMethod.POST)
    public ResponseEntity loadDataForParent() {
        loadSchoolDataForParent();
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/load-data/teacher", method = RequestMethod.POST)
    public ResponseEntity loadDataForTeacher() {
        loadSchoolDataForTeacher();
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/load-user", method = RequestMethod.POST)
    public ResponseEntity loadUserRelationForStudentAndStaff() {
        List<Staff> staffList = staffRepository.findAll();
        for(Staff staff : staffList) {
            if(staff.getUser() == null) {
                User user = new User();
                user.setLogin(staff.getStaffId());
                user.setFirstName(staff.getFirstName());
                user.setLastName(staff.getLastName());
                user.setPassword(passwordEncoder.encode("witcurve"));
                user.setType(UserType.STAFF);
                Set<Authority> authorities = new HashSet<>();
                Authority authority = null;
                if(staffList.get(0).equals(staff)) {
                    authority = authorityRepository.findById("ROLE_ADMIN").get();
                } else if(staffList.get(1).equals(staff)) {
                    authority = authorityRepository.findById("ROLE_NON_TEACHING").get();
                } else {
                    authority = authorityRepository.findById("ROLE_TEACHING").get();
                }
                authorities.add(authority);
                user.setAuthorities(authorities);
                user = userRepository.save(user);
                staff.setSecondaryPhone(null);
                staff.setUser(user);
            }
        }

        List<Student> studentList = studentRepository.findAll();
        List<Long> ids = new ArrayList<>();
        for(Student student : studentList) {
            if(student.getUser() == null) {
                User user = new User();
                user.setFirstName(student.getFirstName());
                user.setLastName(student.getLastName());
                user.setLogin(student.getAdmissionId());
                user.setPassword(passwordEncoder.encode("witcurve"));
                user.setType(UserType.PARENT);
                Set<Authority> authorities = new HashSet<>();
                Authority authority = authorityRepository.findById("ROLE_GUARDIAN").get();
                authorities.add(authority);
                user.setAuthorities(authorities);
                user = userRepository.save(user);
                ids.add(user.getId());
                student.setUser(user);
            }
        }
        return ResponseEntity.ok().build();
    }

    private void loadSchoolDataForParent() {

        String affiliationId = RandomStringUtils.randomAlphanumeric(6);
        //school data

        Institute institute1 = new Institute();
        institute1.setName("Chirec");
        institute1 = instituteRepository.save(institute1);

        School school1 = new School();
        school1.setName("Witcurve");
        school1.setAddress1("Kondapur");
        school1.setAffiliationId(affiliationId);
        school1.setCity("Hyderabad");
        school1.setState("Telangana");
        school1.setCountry("India");
        school1.setDistrict("Ranga Reddy District");
        school1.setPincode("500084");
        school1.setPrimaryPhone("9999999999");
        school1.setPrimaryEmail("contact@witcurve.com");
        school1.setFax("9999999999");
        school1.setInstitute(institute1);

        school1= schoolRepository.save(school1);

        SchoolInfo schoolInfo = new SchoolInfo();
        schoolInfo.setSchool(school1);
        schoolInfo.setMedium("English");
        schoolInfo.setBoard("CBSE");

        schoolInfo = schoolInfoRepository.save(schoolInfo);

        // academic session

        AcademicSession academicSession1 = new AcademicSession();
        academicSession1.setSchoolInfo(schoolInfo);
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
        staff1.setPrimaryPhone("9876543210");
        staff1.setSchool(school1);
        staff1.setType("Teaching");
        staff1.setStaffId("STAFF_01");

        staff1 = staffRepository.save(staff1);

        Staff staff2 = new Staff();
        staff2.setAddress1("Hitech City");
        staff2.setFirstName("Dhiraj");
        staff2.setLastName("Kumar");
        staff2.setPrimaryPhone("9876543210");
        staff2.setSchool(school1);
        staff2.setType("Teaching");
        staff2.setStaffId("STAFF_02");

        staff2 = staffRepository.save(staff2);

        Staff staff3 = new Staff();
        staff3.setAddress1("Hitech City");
        staff3.setFirstName("Srujan Kumar");
        staff3.setLastName("Tad");
        staff3.setPrimaryPhone("9876543210");
        staff3.setSchool(school1);
        staff3.setType("Teaching");
        staff3.setStaffId("STAFF_03");

        staff3 = staffRepository.save(staff3);

        Staff staff4 = new Staff();
        staff4.setAddress1("Hitech City");
        staff4.setFirstName("Kishore Kumar");
        staff4.setLastName("SVR");
        staff4.setPrimaryPhone("9876543210");
        staff4.setSchool(school1);
        staff4.setType("Teaching");
        staff4.setStaffId("STAFF_04");

        staff4 = staffRepository.save(staff4);

        Staff staff5 = new Staff();
        staff5.setAddress1("Hitech City");
        staff5.setFirstName("Mahendra");
        staff5.setLastName("No Idea");
        staff5.setPrimaryPhone("9876543210");
        staff5.setSchool(school1);
        staff5.setType("Teaching");
        staff5.setStaffId("STAFF_05");

        staff5 = staffRepository.save(staff5);

        Staff staff6 = new Staff();
        staff6.setAddress1("Hitech City");
        staff6.setFirstName("Satya");
        staff6.setLastName("No Idea");
        staff6.setPrimaryPhone("9876543210");
        staff6.setSchool(school1);
        staff6.setType("Teaching");
        staff6.setStaffId("STAFF_06");

        staff6 = staffRepository.save(staff6);


        Staff staff7 = new Staff();
        staff7.setAddress1("Hitech City");
        staff7.setFirstName("Sai Chand");
        staff7.setLastName("Gandivasala");
        staff7.setPrimaryPhone("9876543210");
        staff7.setSchool(school1);
        staff7.setType("Teaching");
        staff7.setStaffId("STAFF_07");

        staff7 = staffRepository.save(staff7);

        Staff staff8 = new Staff();
        staff8.setAddress1("Hitech City");
        staff8.setFirstName("Kalyan");
        staff8.setLastName("Naik");
        staff8.setPrimaryPhone("9876543210");
        staff8.setSchool(school1);
        staff8.setType("Teaching");
        staff8.setStaffId("STAFF_08");

        staff8 = staffRepository.save(staff8);

        // class data

        Standard class1 = new Standard();
        class1.setGrade(Grade.III);
        class1.setTerm(term2);
        class1.setSection("A");
        class1.setClassTeacher(staff1);
        class1 = classRepository.save(class1);

        // courses

        MasterSubject masterSubject1 = new MasterSubject();
        masterSubject1.setName("Telugu1");
        masterSubject1 = masterSubjectRepository.save(masterSubject1);

        Course course1 = new Course();
        course1.setCourseName("Telugu1");
        course1.setMasterSubject(masterSubject1);
        course1.setDescription("Telugu1");
        course1.setSchool(school1);
        course1 = courseRepository.save(course1);

        MasterSubject masterSubject2 = new MasterSubject();
        masterSubject2.setName("Hindi1");
        masterSubject2 = masterSubjectRepository.save(masterSubject1);

        Course course2 = new Course();
        course2.setCourseName("Hindi1");
        course2.setMasterSubject(masterSubject2);
        course2.setDescription("Hindi1");
        course2.setSchool(school1);
        course2 = courseRepository.save(course2);

        MasterSubject masterSubject3 = new MasterSubject();
        masterSubject3.setName("English1");
        masterSubject3 = masterSubjectRepository.save(masterSubject3);

        Course course3 = new Course();
        course3.setCourseName("English1");
        course3.setMasterSubject(masterSubject3);
        course3.setDescription("English1");
        course3.setSchool(school1);
        course3 = courseRepository.save(course3);

        MasterSubject masterSubject4 = new MasterSubject();
        masterSubject4.setName("Maths1");
        masterSubject4 = masterSubjectRepository.save(masterSubject4);

        Course course4 = new Course();
        course4.setCourseName("Maths1");
        course4.setMasterSubject(masterSubject4);
        course4.setDescription("Maths1");
        course4.setSchool(school1);
        course4 = courseRepository.save(course4);

        MasterSubject masterSubject5 = new MasterSubject();
        masterSubject5.setName("Science1");
        masterSubject5 = masterSubjectRepository.save(masterSubject5);

        Course course5 = new Course();
        course5.setCourseName("Science1");
        course5.setMasterSubject(masterSubject5);
        course5.setDescription("Science1");
        course5.setSchool(school1);
        course5 = courseRepository.save(course5);

        MasterSubject masterSubject6 = new MasterSubject();
        masterSubject6.setName("Social Studies1");
        masterSubject6 = masterSubjectRepository.save(masterSubject6);

        Course course6 = new Course();
        course6.setCourseName("Social Studies1");
        course6.setMasterSubject(masterSubject6);
        course6.setDescription("Social Studies1");
        course6.setSchool(school1);
        course6 = courseRepository.save(course6);

        MasterSubject masterSubject7 = new MasterSubject();
        masterSubject7.setName("Drawing1");
        masterSubject7 = masterSubjectRepository.save(masterSubject7);

        Course course7 = new Course();
        course7.setCourseName("Drawing1");
        course7.setMasterSubject(masterSubject7);
        course7.setDescription("Drawing1");
        course7.setSchool(school1);
        course7 = courseRepository.save(course7);


        MasterSubject masterSubject8 = new MasterSubject();
        masterSubject8.setName("Physical Training1");
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
        student1.setSchoolInfo(schoolInfo);
        student1.setAdmissionId("STD_001");
        student1.setRegisteredMobileNumber("9876543210");
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

        StudentStandard studentStandard = new StudentStandard();
        studentStandard.setStandard(class1);
        studentStandard.setStudent(student1);
        studentStandard.setRollNo("1");
        studentStandard = studentStandardRepository.save(studentStandard);

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
        slotCourseDetails11.setCourseTeacher(courseTeacher1);
        slotCourseDetails11.setGsd(gsd1);
        slotCourseDetails11.setDayOfWeek(DayOfWeek.MONDAY);
        slotCourseDetails11 = slotCourseDetailsRepository.save(slotCourseDetails11);


        SlotCourseDetails slotCourseDetails12 = new SlotCourseDetails();
        slotCourseDetails12.setCourseTeacher(courseTeacher2);
        slotCourseDetails12.setGsd(gsd2);
        slotCourseDetails12.setDayOfWeek(DayOfWeek.MONDAY);
        slotCourseDetails12 = slotCourseDetailsRepository.save(slotCourseDetails12);

        SlotCourseDetails slotCourseDetails13 = new SlotCourseDetails();
        slotCourseDetails13.setCourseTeacher(courseTeacher3);
        slotCourseDetails13.setGsd(gsd3);
        slotCourseDetails13.setDayOfWeek(DayOfWeek.MONDAY);
        slotCourseDetails13 = slotCourseDetailsRepository.save(slotCourseDetails13);

        SlotCourseDetails slotCourseDetails14 = new SlotCourseDetails();
        slotCourseDetails14.setCourseTeacher(courseTeacher4);
        slotCourseDetails14.setGsd(gsd4);
        slotCourseDetails14.setDayOfWeek(DayOfWeek.MONDAY);
        slotCourseDetails14 = slotCourseDetailsRepository.save(slotCourseDetails14);

        SlotCourseDetails slotCourseDetails15 = new SlotCourseDetails();
        slotCourseDetails15.setCourseTeacher(courseTeacher5);
        slotCourseDetails15.setGsd(gsd5);
        slotCourseDetails15.setDayOfWeek(DayOfWeek.MONDAY);
        slotCourseDetails15 = slotCourseDetailsRepository.save(slotCourseDetails15);


        SlotCourseDetails slotCourseDetails16 = new SlotCourseDetails();
        slotCourseDetails16.setCourseTeacher(courseTeacher6);
        slotCourseDetails16.setGsd(gsd6);
        slotCourseDetails16.setDayOfWeek(DayOfWeek.MONDAY);
        slotCourseDetails16 = slotCourseDetailsRepository.save(slotCourseDetails16);

        SlotCourseDetails slotCourseDetails17 = new SlotCourseDetails();
        slotCourseDetails17.setCourseTeacher(courseTeacher7);
        slotCourseDetails17.setGsd(gsd7);
        slotCourseDetails17.setDayOfWeek(DayOfWeek.MONDAY);
        slotCourseDetails17 = slotCourseDetailsRepository.save(slotCourseDetails17);


        SlotCourseDetails slotCourseDetails18 = new SlotCourseDetails();
        slotCourseDetails18.setCourseTeacher(courseTeacher8);
        slotCourseDetails18.setGsd(gsd8);
        slotCourseDetails18.setDayOfWeek(DayOfWeek.MONDAY);
        slotCourseDetails18 = slotCourseDetailsRepository.save(slotCourseDetails18);

        /*TUESDAY*/
        SlotCourseDetails slotCourseDetails21 = new SlotCourseDetails();
        slotCourseDetails21.setCourseTeacher(courseTeacher1);
        slotCourseDetails21.setGsd(gsd1);
        slotCourseDetails21.setDayOfWeek(DayOfWeek.TUESDAY);
        slotCourseDetails21 = slotCourseDetailsRepository.save(slotCourseDetails21);


        SlotCourseDetails slotCourseDetails22 = new SlotCourseDetails();
        slotCourseDetails22.setCourseTeacher(courseTeacher2);
        slotCourseDetails22.setGsd(gsd2);
        slotCourseDetails22.setDayOfWeek(DayOfWeek.TUESDAY);
        slotCourseDetails22 = slotCourseDetailsRepository.save(slotCourseDetails22);

        SlotCourseDetails slotCourseDetails23 = new SlotCourseDetails();
        slotCourseDetails23.setCourseTeacher(courseTeacher3);
        slotCourseDetails23.setGsd(gsd3);
        slotCourseDetails23.setDayOfWeek(DayOfWeek.TUESDAY);
        slotCourseDetails23 = slotCourseDetailsRepository.save(slotCourseDetails23);

        SlotCourseDetails slotCourseDetails24 = new SlotCourseDetails();
        slotCourseDetails24.setCourseTeacher(courseTeacher4);
        slotCourseDetails24.setGsd(gsd4);
        slotCourseDetails24.setDayOfWeek(DayOfWeek.TUESDAY);
        slotCourseDetails24 = slotCourseDetailsRepository.save(slotCourseDetails24);

        SlotCourseDetails slotCourseDetails25 = new SlotCourseDetails();
        slotCourseDetails25.setCourseTeacher(courseTeacher5);
        slotCourseDetails25.setGsd(gsd5);
        slotCourseDetails25.setDayOfWeek(DayOfWeek.TUESDAY);
        slotCourseDetails25 = slotCourseDetailsRepository.save(slotCourseDetails25);


        SlotCourseDetails slotCourseDetails26 = new SlotCourseDetails();
        slotCourseDetails26.setCourseTeacher(courseTeacher6);
        slotCourseDetails26.setGsd(gsd6);
        slotCourseDetails26.setDayOfWeek(DayOfWeek.TUESDAY);
        slotCourseDetails26 = slotCourseDetailsRepository.save(slotCourseDetails26);

        SlotCourseDetails slotCourseDetails27 = new SlotCourseDetails();
        slotCourseDetails27.setCourseTeacher(courseTeacher7);
        slotCourseDetails27.setGsd(gsd7);
        slotCourseDetails27.setDayOfWeek(DayOfWeek.TUESDAY);
        slotCourseDetails27 = slotCourseDetailsRepository.save(slotCourseDetails27);


        SlotCourseDetails slotCourseDetails28 = new SlotCourseDetails();
        slotCourseDetails28.setCourseTeacher(courseTeacher8);
        slotCourseDetails28.setGsd(gsd8);
        slotCourseDetails28.setDayOfWeek(DayOfWeek.TUESDAY);
        slotCourseDetails28 = slotCourseDetailsRepository.save(slotCourseDetails28);


        /*WEDNESDAY*/
        SlotCourseDetails slotCourseDetails31 = new SlotCourseDetails();
        slotCourseDetails31.setCourseTeacher(courseTeacher1);
        slotCourseDetails31.setGsd(gsd1);
        slotCourseDetails31.setDayOfWeek(DayOfWeek.WEDNESDAY);
        slotCourseDetails31 = slotCourseDetailsRepository.save(slotCourseDetails31);


        SlotCourseDetails slotCourseDetails32 = new SlotCourseDetails();
        slotCourseDetails32.setCourseTeacher(courseTeacher2);
        slotCourseDetails32.setGsd(gsd2);
        slotCourseDetails32.setDayOfWeek(DayOfWeek.WEDNESDAY);
        slotCourseDetails32 = slotCourseDetailsRepository.save(slotCourseDetails32);

        SlotCourseDetails slotCourseDetails33 = new SlotCourseDetails();
        slotCourseDetails33.setCourseTeacher(courseTeacher3);
        slotCourseDetails33.setGsd(gsd3);
        slotCourseDetails33.setDayOfWeek(DayOfWeek.WEDNESDAY);
        slotCourseDetails33 = slotCourseDetailsRepository.save(slotCourseDetails33);

        SlotCourseDetails slotCourseDetails34 = new SlotCourseDetails();
        slotCourseDetails34.setCourseTeacher(courseTeacher4);
        slotCourseDetails34.setGsd(gsd4);
        slotCourseDetails34.setDayOfWeek(DayOfWeek.WEDNESDAY);
        slotCourseDetails34 = slotCourseDetailsRepository.save(slotCourseDetails34);

        SlotCourseDetails slotCourseDetails35 = new SlotCourseDetails();
        slotCourseDetails35.setCourseTeacher(courseTeacher5);
        slotCourseDetails35.setGsd(gsd5);
        slotCourseDetails35.setDayOfWeek(DayOfWeek.WEDNESDAY);
        slotCourseDetails35 = slotCourseDetailsRepository.save(slotCourseDetails35);


        SlotCourseDetails slotCourseDetails36 = new SlotCourseDetails();
        slotCourseDetails36.setCourseTeacher(courseTeacher6);
        slotCourseDetails36.setGsd(gsd6);
        slotCourseDetails36.setDayOfWeek(DayOfWeek.WEDNESDAY);
        slotCourseDetails36 = slotCourseDetailsRepository.save(slotCourseDetails36);

        SlotCourseDetails slotCourseDetails37 = new SlotCourseDetails();
        slotCourseDetails37.setCourseTeacher(courseTeacher7);
        slotCourseDetails37.setGsd(gsd7);
        slotCourseDetails37.setDayOfWeek(DayOfWeek.WEDNESDAY);
        slotCourseDetails37 = slotCourseDetailsRepository.save(slotCourseDetails37);


        SlotCourseDetails slotCourseDetails38 = new SlotCourseDetails();
        slotCourseDetails38.setCourseTeacher(courseTeacher8);
        slotCourseDetails38.setGsd(gsd8);
        slotCourseDetails38.setDayOfWeek(DayOfWeek.WEDNESDAY);
        slotCourseDetails38 = slotCourseDetailsRepository.save(slotCourseDetails38);

        /*THURSDAY*/
        SlotCourseDetails slotCourseDetails41 = new SlotCourseDetails();
        slotCourseDetails41.setCourseTeacher(courseTeacher1);
        slotCourseDetails41.setGsd(gsd1);
        slotCourseDetails41.setDayOfWeek(DayOfWeek.THURSDAY);
        slotCourseDetails41 = slotCourseDetailsRepository.save(slotCourseDetails41);


        SlotCourseDetails slotCourseDetails42 = new SlotCourseDetails();
        slotCourseDetails42.setCourseTeacher(courseTeacher2);
        slotCourseDetails42.setGsd(gsd2);
        slotCourseDetails42.setDayOfWeek(DayOfWeek.THURSDAY);
        slotCourseDetails42 = slotCourseDetailsRepository.save(slotCourseDetails42);

        SlotCourseDetails slotCourseDetails43 = new SlotCourseDetails();
        slotCourseDetails43.setCourseTeacher(courseTeacher3);
        slotCourseDetails43.setGsd(gsd3);
        slotCourseDetails43.setDayOfWeek(DayOfWeek.THURSDAY);
        slotCourseDetails43 = slotCourseDetailsRepository.save(slotCourseDetails43);

        SlotCourseDetails slotCourseDetails44 = new SlotCourseDetails();
        slotCourseDetails44.setCourseTeacher(courseTeacher4);
        slotCourseDetails44.setGsd(gsd4);
        slotCourseDetails44.setDayOfWeek(DayOfWeek.THURSDAY);
        slotCourseDetails44 = slotCourseDetailsRepository.save(slotCourseDetails44);

        SlotCourseDetails slotCourseDetails45 = new SlotCourseDetails();
        slotCourseDetails45.setCourseTeacher(courseTeacher5);
        slotCourseDetails45.setGsd(gsd5);
        slotCourseDetails45.setDayOfWeek(DayOfWeek.THURSDAY);
        slotCourseDetails45 = slotCourseDetailsRepository.save(slotCourseDetails45);


        SlotCourseDetails slotCourseDetails46 = new SlotCourseDetails();
        slotCourseDetails46.setCourseTeacher(courseTeacher6);
        slotCourseDetails46.setGsd(gsd6);
        slotCourseDetails46.setDayOfWeek(DayOfWeek.THURSDAY);
        slotCourseDetails46 = slotCourseDetailsRepository.save(slotCourseDetails46);

        SlotCourseDetails slotCourseDetails47 = new SlotCourseDetails();
        slotCourseDetails47.setCourseTeacher(courseTeacher7);
        slotCourseDetails47.setGsd(gsd7);
        slotCourseDetails47.setDayOfWeek(DayOfWeek.THURSDAY);
        slotCourseDetails47 = slotCourseDetailsRepository.save(slotCourseDetails47);


        SlotCourseDetails slotCourseDetails48 = new SlotCourseDetails();
        slotCourseDetails48.setCourseTeacher(courseTeacher8);
        slotCourseDetails48.setGsd(gsd8);
        slotCourseDetails48.setDayOfWeek(DayOfWeek.THURSDAY);
        slotCourseDetails48 = slotCourseDetailsRepository.save(slotCourseDetails48);

        /*FRIDAY*/
        SlotCourseDetails slotCourseDetails51 = new SlotCourseDetails();
        slotCourseDetails51.setCourseTeacher(courseTeacher1);
        slotCourseDetails51.setGsd(gsd1);
        slotCourseDetails51.setDayOfWeek(DayOfWeek.FRIDAY);
        slotCourseDetails51 = slotCourseDetailsRepository.save(slotCourseDetails51);


        SlotCourseDetails slotCourseDetails52 = new SlotCourseDetails();
        slotCourseDetails52.setCourseTeacher(courseTeacher2);
        slotCourseDetails52.setGsd(gsd2);
        slotCourseDetails52.setDayOfWeek(DayOfWeek.FRIDAY);
        slotCourseDetails52 = slotCourseDetailsRepository.save(slotCourseDetails52);

        SlotCourseDetails slotCourseDetails53 = new SlotCourseDetails();
        slotCourseDetails53.setCourseTeacher(courseTeacher3);
        slotCourseDetails53.setGsd(gsd3);
        slotCourseDetails53.setDayOfWeek(DayOfWeek.FRIDAY);
        slotCourseDetails53 = slotCourseDetailsRepository.save(slotCourseDetails53);

        SlotCourseDetails slotCourseDetails54 = new SlotCourseDetails();
        slotCourseDetails54.setCourseTeacher(courseTeacher4);
        slotCourseDetails54.setGsd(gsd4);
        slotCourseDetails54.setDayOfWeek(DayOfWeek.FRIDAY);
        slotCourseDetails54 = slotCourseDetailsRepository.save(slotCourseDetails54);

        SlotCourseDetails slotCourseDetails55 = new SlotCourseDetails();
        slotCourseDetails55.setCourseTeacher(courseTeacher5);
        slotCourseDetails55.setGsd(gsd5);
        slotCourseDetails55.setDayOfWeek(DayOfWeek.FRIDAY);
        slotCourseDetails55 = slotCourseDetailsRepository.save(slotCourseDetails55);


        SlotCourseDetails slotCourseDetails56 = new SlotCourseDetails();
        slotCourseDetails56.setCourseTeacher(courseTeacher6);
        slotCourseDetails56.setGsd(gsd6);
        slotCourseDetails56.setDayOfWeek(DayOfWeek.FRIDAY);
        slotCourseDetails56 = slotCourseDetailsRepository.save(slotCourseDetails56);

        SlotCourseDetails slotCourseDetails57 = new SlotCourseDetails();
        slotCourseDetails57.setCourseTeacher(courseTeacher7);
        slotCourseDetails57.setGsd(gsd7);
        slotCourseDetails57.setDayOfWeek(DayOfWeek.FRIDAY);
        slotCourseDetails57 = slotCourseDetailsRepository.save(slotCourseDetails57);

        SlotCourseDetails slotCourseDetails58 = new SlotCourseDetails();
        slotCourseDetails58.setCourseTeacher(courseTeacher8);
        slotCourseDetails58.setGsd(gsd8);
        slotCourseDetails58.setDayOfWeek(DayOfWeek.FRIDAY);
        slotCourseDetails58 = slotCourseDetailsRepository.save(slotCourseDetails58);

        //Events
        //Daily Updates for Monday
        Event event11 = new Event();
        event11.setName("Daily Update");
        event11.setDescription("This is a daily update for today's class");
        event11.setType(EventType.DAILY_UPDATE);
        event11.setDate(LocalDate.of(2018, 9, 17));
        event11.setStandard(class1);
        event11.setScd(slotCourseDetails11);
        event11 = eventRepository.save(event11);

        Event event12 = new Event();
        event12.setName("Daily Update");
        event12.setDescription("This is a daily update for today's class");
        event12.setType(EventType.DAILY_UPDATE);
        event12.setDate(LocalDate.of(2018, 9, 17));
        event12.setStandard(class1);
        event12.setScd(slotCourseDetails12);
        event12 = eventRepository.save(event12);

        Event event13 = new Event();
        event13.setName("Daily Update");
        event13.setDescription("This is a daily update for today's class");
        event13.setType(EventType.DAILY_UPDATE);
        event13.setDate(LocalDate.of(2018, 9, 17));
        event13.setStandard(class1);
        event13.setScd(slotCourseDetails13);
        event13 = eventRepository.save(event13);

        Event event14 = new Event();
        event14.setName("Daily Update");
        event14.setDescription("This is a daily update for today's class");
        event14.setType(EventType.DAILY_UPDATE);
        event14.setDate(LocalDate.of(2018, 9, 17));
        event14.setStandard(class1);
        event14.setScd(slotCourseDetails14);
        event14 = eventRepository.save(event14);

        Event event15 = new Event();
        event15.setName("Daily Update");
        event15.setDescription("This is a daily update for today's class");
        event15.setType(EventType.DAILY_UPDATE);
        event15.setDate(LocalDate.of(2018, 9, 17));
        event15.setStandard(class1);
        event15.setScd(slotCourseDetails15);
        event15 = eventRepository.save(event15);

        Event event16 = new Event();
        event16.setName("Daily Update");
        event16.setDescription("This is a daily update for today's class");
        event16.setType(EventType.DAILY_UPDATE);
        event16.setDate(LocalDate.of(2018, 9, 17));
        event16.setStandard(class1);
        event16.setScd(slotCourseDetails16);
        event16 = eventRepository.save(event16);

        Event event17 = new Event();
        event17.setName("Daily Update");
        event17.setDescription("This is a daily update for today's class");
        event17.setType(EventType.DAILY_UPDATE);
        event17.setDate(LocalDate.of(2018, 9, 17));
        event17.setStandard(class1);
        event17.setScd(slotCourseDetails17);
        event17 = eventRepository.save(event17);

        Event event18 = new Event();
        event18.setName("Daily Update");
        event18.setDescription("This is a daily update for today's class");
        event18.setType(EventType.DAILY_UPDATE);
        event18.setDate(LocalDate.of(2018, 9, 17));
        event18.setStandard(class1);
        event18.setScd(slotCourseDetails18);
        event18 = eventRepository.save(event18);

        //Daily Update for Tuesday
        Event event21 = new Event();
        event21.setName("Daily Update");
        event21.setDescription("This is a daily update for today's class");
        event21.setType(EventType.DAILY_UPDATE);
        event21.setDate(LocalDate.of(2018, 9, 18));
        event21.setStandard(class1);
        event21.setScd(slotCourseDetails21);
        event21 = eventRepository.save(event21);

        Event event22 = new Event();
        event22.setName("Daily Update");
        event22.setDescription("This is a daily update for today's class");
        event22.setType(EventType.DAILY_UPDATE);
        event22.setDate(LocalDate.of(2018, 9, 18));
        event22.setStandard(class1);
        event22.setScd(slotCourseDetails22);
        event22 = eventRepository.save(event22);

        Event event23 = new Event();
        event23.setName("Daily Update");
        event23.setDescription("This is a daily update for today's class");
        event23.setType(EventType.DAILY_UPDATE);
        event23.setDate(LocalDate.of(2018, 9, 18));
        event23.setStandard(class1);
        event23.setScd(slotCourseDetails23);
        event23 = eventRepository.save(event23);

        Event event24 = new Event();
        event24.setName("Daily Update");
        event24.setDescription("This is a daily update for today's class");
        event24.setType(EventType.DAILY_UPDATE);
        event24.setDate(LocalDate.of(2018, 9, 18));
        event24.setStandard(class1);
        event24.setScd(slotCourseDetails24);
        event24 = eventRepository.save(event24);

        Event event25 = new Event();
        event25.setName("Daily Update");
        event25.setDescription("This is a daily update for today's class");
        event25.setType(EventType.DAILY_UPDATE);
        event25.setDate(LocalDate.of(2018, 9, 18));
        event25.setStandard(class1);
        event25.setScd(slotCourseDetails25);
        event25 = eventRepository.save(event25);

        Event event26 = new Event();
        event26.setName("Daily Update");
        event26.setDescription("This is a daily update for today's class");
        event26.setType(EventType.DAILY_UPDATE);
        event26.setDate(LocalDate.of(2018, 9, 18));
        event26.setStandard(class1);
        event26.setScd(slotCourseDetails26);
        event26 = eventRepository.save(event26);

        Event event27 = new Event();
        event27.setName("Daily Update");
        event27.setDescription("This is a daily update for today's class");
        event27.setType(EventType.DAILY_UPDATE);
        event27.setDate(LocalDate.of(2018, 9, 18));
        event27.setStandard(class1);
        event27.setScd(slotCourseDetails27);
        event27 = eventRepository.save(event27);

        Event event28 = new Event();
        event28.setName("Daily Update");
        event28.setDescription("This is a daily update for today's class");
        event28.setType(EventType.DAILY_UPDATE);
        event28.setDate(LocalDate.of(2018, 9, 18));
        event28.setStandard(class1);
        event28.setScd(slotCourseDetails28);
        event28 = eventRepository.save(event28);

        //Daily Update for Wednesday
        Event event31 = new Event();
        event31.setName("Daily Update");
        event31.setDescription("This is a daily update for today's class");
        event31.setType(EventType.DAILY_UPDATE);
        event31.setDate(LocalDate.of(2018, 9, 19));
        event31.setStandard(class1);
        event31.setScd(slotCourseDetails31);
        event31 = eventRepository.save(event31);

        Event event32 = new Event();
        event32.setName("Daily Update");
        event32.setDescription("This is a daily update for today's class");
        event32.setType(EventType.DAILY_UPDATE);
        event32.setDate(LocalDate.of(2018, 9, 19));
        event32.setStandard(class1);
        event32.setScd(slotCourseDetails32);
        event32 = eventRepository.save(event32);

        Event event33 = new Event();
        event33.setName("Daily Update");
        event33.setDescription("This is a daily update for today's class");
        event33.setType(EventType.DAILY_UPDATE);
        event33.setDate(LocalDate.of(2018, 9, 19));
        event33.setStandard(class1);
        event33.setScd(slotCourseDetails33);
        event33 = eventRepository.save(event33);

        Event event34 = new Event();
        event34.setName("Daily Update");
        event34.setDescription("This is a daily update for today's class");
        event34.setType(EventType.DAILY_UPDATE);
        event34.setDate(LocalDate.of(2018, 9, 19));
        event34.setStandard(class1);
        event34.setScd(slotCourseDetails34);
        event34 = eventRepository.save(event34);

        Event event35 = new Event();
        event35.setName("Daily Update");
        event35.setDescription("This is a daily update for today's class");
        event35.setType(EventType.DAILY_UPDATE);
        event35.setDate(LocalDate.of(2018, 9, 19));
        event35.setStandard(class1);
        event35.setScd(slotCourseDetails35);
        event35 = eventRepository.save(event35);

        Event event36 = new Event();
        event36.setName("Daily Update");
        event36.setDescription("This is a daily update for today's class");
        event36.setType(EventType.DAILY_UPDATE);
        event36.setDate(LocalDate.of(2018, 9, 19));
        event36.setStandard(class1);
        event36.setScd(slotCourseDetails36);
        event36 = eventRepository.save(event36);

        Event event37 = new Event();
        event37.setName("Daily Update");
        event37.setDescription("This is a daily update for today's class");
        event37.setType(EventType.DAILY_UPDATE);
        event37.setDate(LocalDate.of(2018, 9, 19));
        event37.setStandard(class1);
        event37.setScd(slotCourseDetails37);
        event37 = eventRepository.save(event37);

        Event event38 = new Event();
        event38.setName("Daily Update");
        event38.setDescription("This is a daily update for today's class");
        event38.setType(EventType.DAILY_UPDATE);
        event38.setDate(LocalDate.of(2018, 9, 19));
        event38.setStandard(class1);
        event38.setScd(slotCourseDetails38);
        event38 = eventRepository.save(event38);

        //Daily Update for Thursday
        Event event41 = new Event();
        event41.setName("Daily Update");
        event41.setDescription("This is a daily update for today's class");
        event41.setType(EventType.DAILY_UPDATE);
        event41.setDate(LocalDate.of(2018, 9, 20));
        event41.setStandard(class1);
        event41.setScd(slotCourseDetails41);
        event41 = eventRepository.save(event41);

        Event event42 = new Event();
        event42.setName("Daily Update");
        event42.setDescription("This is a daily update for today's class");
        event42.setType(EventType.DAILY_UPDATE);
        event42.setDate(LocalDate.of(2018, 9, 20));
        event42.setStandard(class1);
        event42.setScd(slotCourseDetails42);
        event42= eventRepository.save(event42);

        Event event43 = new Event();
        event43.setName("Daily Update");
        event43.setDescription("This is a daily update for today's class");
        event43.setType(EventType.DAILY_UPDATE);
        event43.setDate(LocalDate.of(2018, 9, 20));
        event43.setStandard(class1);
        event43.setScd(slotCourseDetails43);
        event43 = eventRepository.save(event43);

        Event event44 = new Event();
        event44.setName("Daily Update");
        event44.setDescription("This is a daily update for today's class");
        event44.setType(EventType.DAILY_UPDATE);
        event44.setDate(LocalDate.of(2018, 9, 20));
        event44.setStandard(class1);
        event44.setScd(slotCourseDetails44);
        event44= eventRepository.save(event44);

        Event event45 = new Event();
        event45.setName("Daily Update");
        event45.setDescription("This is a daily update for today's class");
        event45.setType(EventType.DAILY_UPDATE);
        event45.setDate(LocalDate.of(2018, 9, 20));
        event45.setStandard(class1);
        event45.setScd(slotCourseDetails45);
        event45= eventRepository.save(event45);

        Event event46 = new Event();
        event46.setName("Daily Update");
        event46.setDescription("This is a daily update for today's class");
        event46.setType(EventType.DAILY_UPDATE);
        event46.setDate(LocalDate.of(2018, 9, 20));
        event46.setStandard(class1);
        event46.setScd(slotCourseDetails46);
        event46= eventRepository.save(event46);

        Event event47 = new Event();
        event47.setName("Daily Update");
        event47.setDescription("This is a daily update for today's class");
        event47.setType(EventType.DAILY_UPDATE);
        event47.setDate(LocalDate.of(2018, 9, 20));
        event47.setStandard(class1);
        event47.setScd(slotCourseDetails47);
        event47= eventRepository.save(event47);

        Event event48 = new Event();
        event48.setName("Daily Update");
        event48.setDescription("This is a daily update for today's class");
        event48.setType(EventType.DAILY_UPDATE);
        event48.setDate(LocalDate.of(2018, 9, 20));
        event48.setStandard(class1);
        event48.setScd(slotCourseDetails48);
        event48 = eventRepository.save(event48);

        //Daily Update for Friday
        Event event51 = new Event();
        event51.setName("Daily Update");
        event51.setDescription("This is a daily update for today's class");
        event51.setType(EventType.DAILY_UPDATE);
        event51.setDate(LocalDate.of(2018, 9, 21));
        event51.setStandard(class1);
        event51.setScd(slotCourseDetails51);
        event51 = eventRepository.save(event51);

        Event event52 = new Event();
        event52.setName("Daily Update");
        event52.setDescription("This is a daily update for today's class");
        event52.setType(EventType.DAILY_UPDATE);
        event52.setDate(LocalDate.of(2018, 9, 21));
        event52.setStandard(class1);
        event52.setScd(slotCourseDetails52);
        event52 = eventRepository.save(event52);

        Event event53 = new Event();
        event53.setName("Daily Update");
        event53.setDescription("This is a daily update for today's class");
        event53.setType(EventType.DAILY_UPDATE);
        event53.setDate(LocalDate.of(2018, 9, 21));
        event53.setStandard(class1);
        event53.setScd(slotCourseDetails53);
        event53 = eventRepository.save(event53);

        Event event54 = new Event();
        event54.setName("Daily Update");
        event54.setDescription("This is a daily update for today's class");
        event54.setType(EventType.DAILY_UPDATE);
        event54.setDate(LocalDate.of(2018, 9, 21));
        event54.setStandard(class1);
        event54.setScd(slotCourseDetails54);
        event54 = eventRepository.save(event54);

        Event event55 = new Event();
        event55.setName("Daily Update");
        event55.setDescription("This is a daily update for today's class");
        event55.setType(EventType.DAILY_UPDATE);
        event55.setDate(LocalDate.of(2018, 9, 21));
        event55.setStandard(class1);
        event55.setScd(slotCourseDetails55);
        event55 = eventRepository.save(event55);

        Event event56 = new Event();
        event56.setName("Daily Update");
        event56.setDescription("This is a daily update for today's class");
        event56.setType(EventType.DAILY_UPDATE);
        event56.setDate(LocalDate.of(2018, 9, 21));
        event56.setStandard(class1);
        event56.setScd(slotCourseDetails56);
        event56 = eventRepository.save(event56);

        Event event57 = new Event();
        event57.setName("Daily Update");
        event57.setDescription("This is a daily update for today's class");
        event57.setType(EventType.DAILY_UPDATE);
        event57.setDate(LocalDate.of(2018, 9, 21));
        event57.setStandard(class1);
        event57.setScd(slotCourseDetails57);
        event57 = eventRepository.save(event57);

        Event event58 = new Event();
        event58.setName("Daily Update");
        event58.setDescription("This is a daily update for today's class");
        event58.setType(EventType.DAILY_UPDATE);
        event58.setDate(LocalDate.of(2018, 9, 21));
        event58.setStandard(class1);
        event58.setScd(slotCourseDetails58);
        event58 = eventRepository.save(event58);


        //Test Events created on 10, 13 and dated for 12, 14
        Event testEvent11 = new Event();
        testEvent11.setName("Test Created");
        testEvent11.setDescription("Description about test");
        testEvent11.setType(EventType.TEST);
        testEvent11.setDate(LocalDate.of(2018, 9, 19));
        testEvent11.setStandard(class1);
        testEvent11.setCreatedDate(LocalDate.of(2018, 9, 17).atStartOfDay().toInstant(ZoneOffset.UTC));
        testEvent11.setScd(slotCourseDetails31);
        testEvent11 = eventRepository.save(testEvent11);

        Event testEvent12 = new Event();
        testEvent12.setName("Test Created");
        testEvent12.setDescription("Description about test");
        testEvent12.setType(EventType.TEST);
        testEvent12.setDate(LocalDate.of(2018, 9, 19));
        testEvent12.setStandard(class1);
        testEvent12.setCreatedDate(LocalDate.of(2018, 9, 17).atStartOfDay().toInstant(ZoneOffset.UTC));
        testEvent12.setScd(slotCourseDetails32);
        testEvent12 = eventRepository.save(testEvent12);

        Event testEvent13 = new Event();
        testEvent13.setName("Test Created");
        testEvent13.setDescription("Description about test");
        testEvent13.setType(EventType.TEST);
        testEvent13.setDate(LocalDate.of(2018, 9, 19));
        testEvent13.setStandard(class1);
        testEvent13.setCreatedDate(LocalDate.of(2018, 9, 17).atStartOfDay().toInstant(ZoneOffset.UTC));
        testEvent13.setScd(slotCourseDetails33);
        testEvent13 = eventRepository.save(testEvent13);

        Event testEvent14 = new Event();
        testEvent14.setName("Test Created");
        testEvent14.setDescription("Description about test");
        testEvent14.setType(EventType.TEST);
        testEvent14.setDate(LocalDate.of(2018, 9, 19));
        testEvent14.setStandard(class1);
        testEvent14.setCreatedDate(LocalDate.of(2018, 9, 17).atStartOfDay().toInstant(ZoneOffset.UTC));
        testEvent14.setScd(slotCourseDetails34);
        testEvent14 = eventRepository.save(testEvent14);

        Event testEvent15 = new Event();
        testEvent15.setName("Test Created");
        testEvent15.setDescription("Description about test");
        testEvent15.setType(EventType.TEST);
        testEvent15.setDate(LocalDate.of(2018, 9, 19));
        testEvent15.setStandard(class1);
        testEvent15.setCreatedDate(LocalDate.of(2018, 9, 17).atStartOfDay().toInstant(ZoneOffset.UTC));
        testEvent15.setScd(slotCourseDetails35);
        testEvent15 = eventRepository.save(testEvent15);

        Event testEvent16 = new Event();
        testEvent16.setName("Test Created");
        testEvent16.setDescription("Description about test");
        testEvent16.setType(EventType.TEST);
        testEvent16.setDate(LocalDate.of(2018, 9, 19));
        testEvent16.setStandard(class1);
        testEvent16.setCreatedDate(LocalDate.of(2018, 9, 17).atStartOfDay().toInstant(ZoneOffset.UTC));
        testEvent16.setScd(slotCourseDetails36);
        testEvent16 = eventRepository.save(testEvent16);

        Event testEvent17 = new Event();
        testEvent17.setName("Test Created");
        testEvent17.setDescription("Description about test");
        testEvent17.setType(EventType.TEST);
        testEvent17.setDate(LocalDate.of(2018, 9, 19));
        testEvent17.setStandard(class1);
        testEvent17.setCreatedDate(LocalDate.of(2018, 9, 17).atStartOfDay().toInstant(ZoneOffset.UTC));
        testEvent17.setScd(slotCourseDetails37);
        testEvent17 = eventRepository.save(testEvent17);

        Event testEvent18 = new Event();
        testEvent18.setName("Test Created");
        testEvent18.setDescription("Description about test");
        testEvent18.setType(EventType.TEST);
        testEvent18.setDate(LocalDate.of(2018, 9, 19));
        testEvent18.setStandard(class1);
        testEvent18.setCreatedDate(LocalDate.of(2018, 9, 17).atStartOfDay().toInstant(ZoneOffset.UTC));
        testEvent18.setScd(slotCourseDetails38);
        testEvent18 = eventRepository.save(testEvent18);

        Event testEvent41 = new Event();
        testEvent41.setName("Test Created");
        testEvent41.setDescription("Description about test");
        testEvent41.setType(EventType.TEST);
        testEvent41.setDate(LocalDate.of(2018, 9, 21));
        testEvent41.setStandard(class1);
        testEvent41.setCreatedDate(LocalDate.of(2018, 9, 20).atStartOfDay().toInstant(ZoneOffset.UTC));
        testEvent41.setScd(slotCourseDetails51);
        testEvent41 = eventRepository.save(testEvent41);

        Event testEvent42 = new Event();
        testEvent42.setName("Test Created");
        testEvent42.setDescription("Description about test");
        testEvent42.setType(EventType.TEST);
        testEvent42.setDate(LocalDate.of(2018, 9, 21));
        testEvent42.setStandard(class1);
        testEvent42.setCreatedDate(LocalDate.of(2018, 9, 20).atStartOfDay().toInstant(ZoneOffset.UTC));
        testEvent42.setScd(slotCourseDetails52);
        testEvent42 = eventRepository.save(testEvent42);

        Event testEvent43 = new Event();
        testEvent43.setName("Test Created");
        testEvent43.setDescription("Description about test");
        testEvent43.setType(EventType.TEST);
        testEvent43.setDate(LocalDate.of(2018, 9, 21));
        testEvent43.setStandard(class1);
        testEvent43.setCreatedDate(LocalDate.of(2018, 9, 20).atStartOfDay().toInstant(ZoneOffset.UTC));
        testEvent43.setScd(slotCourseDetails53);
        testEvent43 = eventRepository.save(testEvent43);

        Event testEvent44 = new Event();
        testEvent44.setName("Test Created");
        testEvent44.setDescription("Description about test");
        testEvent44.setType(EventType.TEST);
        testEvent44.setDate(LocalDate.of(2018, 9, 21));
        testEvent44.setStandard(class1);
        testEvent44.setCreatedDate(LocalDate.of(2018, 9, 20).atStartOfDay().toInstant(ZoneOffset.UTC));
        testEvent44.setScd(slotCourseDetails54);
        testEvent44 = eventRepository.save(testEvent44);

        Event testEvent45 = new Event();
        testEvent45.setName("Test Created");
        testEvent45.setDescription("Description about test");
        testEvent45.setType(EventType.TEST);
        testEvent45.setDate(LocalDate.of(2018, 9, 21));
        testEvent45.setStandard(class1);
        testEvent45.setCreatedDate(LocalDate.of(2018, 9, 20).atStartOfDay().toInstant(ZoneOffset.UTC));
        testEvent45.setScd(slotCourseDetails55);
        testEvent45 = eventRepository.save(testEvent45);

        Event testEvent46 = new Event();
        testEvent46.setName("Test Created");
        testEvent46.setDescription("Description about test");
        testEvent46.setType(EventType.TEST);
        testEvent46.setDate(LocalDate.of(2018, 9, 21));
        testEvent46.setStandard(class1);
        testEvent46.setCreatedDate(LocalDate.of(2018, 9, 20).atStartOfDay().toInstant(ZoneOffset.UTC));
        testEvent46.setScd(slotCourseDetails56);
        testEvent46 = eventRepository.save(testEvent46);

        Event testEvent47 = new Event();
        testEvent47.setName("Test Created");
        testEvent47.setDescription("Description about test");
        testEvent47.setType(EventType.TEST);
        testEvent47.setDate(LocalDate.of(2018, 9, 21));
        testEvent47.setStandard(class1);
        testEvent47.setCreatedDate(LocalDate.of(2018, 9, 20).atStartOfDay().toInstant(ZoneOffset.UTC));
        testEvent47.setScd(slotCourseDetails57);
        testEvent47 = eventRepository.save(testEvent47);

        Event testEvent48 = new Event();
        testEvent48.setName("Test Created");
        testEvent48.setDescription("Description about test");
        testEvent48.setType(EventType.TEST);
        testEvent48.setDate(LocalDate.of(2018, 9, 21));
        testEvent48.setStandard(class1);
        testEvent48.setCreatedDate(LocalDate.of(2018, 9, 20).atStartOfDay().toInstant(ZoneOffset.UTC));
        testEvent48.setScd(slotCourseDetails58);
        testEvent48 = eventRepository.save(testEvent48);

        //Assignment Events created on 10, 13 and dated for 12, 14
        Event assignmentEvent11 = new Event();
        assignmentEvent11.setName("Assignment Created");
        assignmentEvent11.setDescription("Description about assignment");
        assignmentEvent11.setType(EventType.ASSIGNMENT);
        assignmentEvent11.setDate(LocalDate.of(2018, 9, 19));
        assignmentEvent11.setStandard(class1);
        assignmentEvent11.setCreatedDate(LocalDate.of(2018, 9, 17).atStartOfDay().toInstant(ZoneOffset.UTC));
        assignmentEvent11.setScd(slotCourseDetails31);
        assignmentEvent11 = eventRepository.save(assignmentEvent11);

        Event assignmentEvent12 = new Event();
        assignmentEvent12.setName("Assignment Created");
        assignmentEvent12.setDescription("Description about assignment");
        assignmentEvent12.setType(EventType.ASSIGNMENT);
        assignmentEvent12.setDate(LocalDate.of(2018, 9, 19));
        assignmentEvent12.setStandard(class1);
        assignmentEvent12.setCreatedDate(LocalDate.of(2018, 9, 17).atStartOfDay().toInstant(ZoneOffset.UTC));
        assignmentEvent12.setScd(slotCourseDetails32);
        assignmentEvent12 = eventRepository.save(assignmentEvent12);

        Event assignmentEvent13 = new Event();
        assignmentEvent13.setName("Assignment Created");
        assignmentEvent13.setDescription("Description about assignment");
        assignmentEvent13.setType(EventType.ASSIGNMENT);
        assignmentEvent13.setDate(LocalDate.of(2018, 9, 19));
        assignmentEvent13.setStandard(class1);
        assignmentEvent13.setCreatedDate(LocalDate.of(2018, 9, 17).atStartOfDay().toInstant(ZoneOffset.UTC));
        assignmentEvent13.setScd(slotCourseDetails33);
        assignmentEvent13 = eventRepository.save(assignmentEvent13);

        Event assignmentEvent14 = new Event();
        assignmentEvent14.setName("Assignment Created");
        assignmentEvent14.setDescription("Description about assignment");
        assignmentEvent14.setType(EventType.ASSIGNMENT);
        assignmentEvent14.setDate(LocalDate.of(2018, 9, 19));
        assignmentEvent14.setStandard(class1);
        assignmentEvent14.setCreatedDate(LocalDate.of(2018, 9, 17).atStartOfDay().toInstant(ZoneOffset.UTC));
        assignmentEvent14.setScd(slotCourseDetails34);
        assignmentEvent14 = eventRepository.save(assignmentEvent14);

        Event assignmentEvent15 = new Event();
        assignmentEvent15.setName("Assignment Created");
        assignmentEvent15.setDescription("Description about assignment");
        assignmentEvent15.setType(EventType.ASSIGNMENT);
        assignmentEvent15.setDate(LocalDate.of(2018, 9, 19));
        assignmentEvent15.setStandard(class1);
        assignmentEvent15.setCreatedDate(LocalDate.of(2018, 9, 17).atStartOfDay().toInstant(ZoneOffset.UTC));
        assignmentEvent15.setScd(slotCourseDetails35);
        assignmentEvent15 = eventRepository.save(assignmentEvent15);

        Event assignmentEvent16 = new Event();
        assignmentEvent16.setName("Assignment Created");
        assignmentEvent16.setDescription("Description about assignment");
        assignmentEvent16.setType(EventType.ASSIGNMENT);
        assignmentEvent16.setDate(LocalDate.of(2018, 9, 19));
        assignmentEvent16.setStandard(class1);
        assignmentEvent16.setCreatedDate(LocalDate.of(2018, 9, 17).atStartOfDay().toInstant(ZoneOffset.UTC));
        assignmentEvent16.setScd(slotCourseDetails36);
        assignmentEvent16 = eventRepository.save(assignmentEvent16);

        Event assignmentEvent17 = new Event();
        assignmentEvent17.setName("Assignment Created");
        assignmentEvent17.setDescription("Description about assignment");
        assignmentEvent17.setType(EventType.ASSIGNMENT);
        assignmentEvent17.setDate(LocalDate.of(2018, 9, 19));
        assignmentEvent17.setStandard(class1);
        assignmentEvent17.setCreatedDate(LocalDate.of(2018, 9, 17).atStartOfDay().toInstant(ZoneOffset.UTC));
        assignmentEvent17.setScd(slotCourseDetails37);
        assignmentEvent17 = eventRepository.save(assignmentEvent17);

        Event assignmentEvent18 = new Event();
        assignmentEvent18.setName("Assignment Created");
        assignmentEvent18.setDescription("Description about assignment");
        assignmentEvent18.setType(EventType.ASSIGNMENT);
        assignmentEvent18.setDate(LocalDate.of(2018, 9, 19));
        assignmentEvent18.setStandard(class1);
        assignmentEvent18.setCreatedDate(LocalDate.of(2018, 9, 17).atStartOfDay().toInstant(ZoneOffset.UTC));
        assignmentEvent18.setScd(slotCourseDetails38);
        assignmentEvent18 = eventRepository.save(assignmentEvent18);


        Event assignmentEvent41 = new Event();
        assignmentEvent41.setName("Assignment Created");
        assignmentEvent41.setDescription("Description about assignment");
        assignmentEvent41.setType(EventType.ASSIGNMENT);
        assignmentEvent41.setDate(LocalDate.of(2018, 9, 21));
        assignmentEvent41.setStandard(class1);
        assignmentEvent41.setCreatedDate(LocalDate.of(2018, 9, 20).atStartOfDay().toInstant(ZoneOffset.UTC));
        assignmentEvent41.setScd(slotCourseDetails51);
        assignmentEvent41 = eventRepository.save(assignmentEvent41);

        Event assignmentEvent42 = new Event();
        assignmentEvent42.setName("Assignment Created");
        assignmentEvent42.setDescription("Description about assignment");
        assignmentEvent42.setType(EventType.ASSIGNMENT);
        assignmentEvent42.setDate(LocalDate.of(2018, 9, 21));
        assignmentEvent42.setStandard(class1);
        assignmentEvent42.setCreatedDate(LocalDate.of(2018, 9, 20).atStartOfDay().toInstant(ZoneOffset.UTC));
        assignmentEvent42.setScd(slotCourseDetails52);
        assignmentEvent42 = eventRepository.save(assignmentEvent42);

        Event assignmentEvent43 = new Event();
        assignmentEvent43.setName("Assignment Created");
        assignmentEvent43.setDescription("Description about assignment");
        assignmentEvent43.setType(EventType.ASSIGNMENT);
        assignmentEvent43.setDate(LocalDate.of(2018, 9, 21));
        assignmentEvent43.setStandard(class1);
        assignmentEvent43.setCreatedDate(LocalDate.of(2018, 9, 20).atStartOfDay().toInstant(ZoneOffset.UTC));
        assignmentEvent43.setScd(slotCourseDetails53);
        assignmentEvent43 = eventRepository.save(assignmentEvent43);


        Event assignmentEvent44 = new Event();
        assignmentEvent44.setName("Assignment Created");
        assignmentEvent44.setDescription("Description about assignment");
        assignmentEvent44.setType(EventType.ASSIGNMENT);
        assignmentEvent44.setDate(LocalDate.of(2018, 9, 21));
        assignmentEvent44.setStandard(class1);
        assignmentEvent44.setCreatedDate(LocalDate.of(2018, 9, 20).atStartOfDay().toInstant(ZoneOffset.UTC));
        assignmentEvent44.setScd(slotCourseDetails54);
        assignmentEvent44 = eventRepository.save(assignmentEvent44);

        Event assignmentEvent45 = new Event();
        assignmentEvent45.setName("Assignment Created");
        assignmentEvent45.setDescription("Description about assignment");
        assignmentEvent45.setType(EventType.ASSIGNMENT);
        assignmentEvent45.setDate(LocalDate.of(2018, 9, 21));
        assignmentEvent45.setStandard(class1);
        assignmentEvent45.setCreatedDate(LocalDate.of(2018, 9, 20).atStartOfDay().toInstant(ZoneOffset.UTC));
        assignmentEvent45.setScd(slotCourseDetails55);
        assignmentEvent45 = eventRepository.save(assignmentEvent45);

        Event assignmentEvent46 = new Event();
        assignmentEvent46.setName("Assignment Created");
        assignmentEvent46.setDescription("Description about assignment");
        assignmentEvent46.setType(EventType.ASSIGNMENT);
        assignmentEvent46.setDate(LocalDate.of(2018, 9, 21));
        assignmentEvent46.setStandard(class1);
        assignmentEvent46.setCreatedDate(LocalDate.of(2018, 9, 20).atStartOfDay().toInstant(ZoneOffset.UTC));
        assignmentEvent46.setScd(slotCourseDetails56);
        assignmentEvent46 = eventRepository.save(assignmentEvent46);

        Event assignmentEvent47 = new Event();
        assignmentEvent47.setName("Assignment Created");
        assignmentEvent47.setDescription("Description about assignment");
        assignmentEvent47.setType(EventType.ASSIGNMENT);
        assignmentEvent47.setDate(LocalDate.of(2018, 9, 21));
        assignmentEvent47.setStandard(class1);
        assignmentEvent47.setCreatedDate(LocalDate.of(2018, 9, 20).atStartOfDay().toInstant(ZoneOffset.UTC));
        assignmentEvent47.setScd(slotCourseDetails57);
        assignmentEvent47 = eventRepository.save(assignmentEvent47);

        Event assignmentEvent48 = new Event();
        assignmentEvent48.setName("Assignment Created");
        assignmentEvent48.setDescription("Description about assignment");
        assignmentEvent48.setType(EventType.ASSIGNMENT);
        assignmentEvent48.setDate(LocalDate.of(2018, 9, 21));
        assignmentEvent48.setStandard(class1);
        assignmentEvent48.setCreatedDate(LocalDate.of(2018, 9, 20).atStartOfDay().toInstant(ZoneOffset.UTC));
        assignmentEvent48.setScd(slotCourseDetails58);
        assignmentEvent48 = eventRepository.save(assignmentEvent48);

        //GSD for exam time table slots
        GeneralSlotDetails examGsd1 = new GeneralSlotDetails();
        examGsd1.setStandard(class1);
        examGsd1.setStart("0900");
        examGsd1.setDuration(180);
        examGsd1.setExam(exam1);
        examGsd1 = generalSlotDetailsRepository.save(examGsd1);

        GeneralSlotDetails examGsd2 = new GeneralSlotDetails();
        examGsd2.setStandard(class1);
        examGsd2.setStart("1300");
        examGsd2.setDuration(180);
        examGsd2.setExam(exam1);
        examGsd2 = generalSlotDetailsRepository.save(examGsd2);

        GeneralSlotDetails examGsd3 = new GeneralSlotDetails();
        examGsd3.setStandard(class1);
        examGsd3.setStart("1200");
        examGsd3.setDuration(60);
        examGsd3.setExam(exam1);
        examGsd3.setRecess(true);
        examGsd3 = generalSlotDetailsRepository.save(examGsd3);

        //scd for exam
        SlotCourseDetails examScd1 = new SlotCourseDetails();
        examScd1.setCourseTeacher(courseTeacher1);
        examScd1.setGsd(examGsd1);
        examScd1.setDayOfWeek(DayOfWeek.MONDAY);
        examScd1 = slotCourseDetailsRepository.save(examScd1);

        SlotCourseDetails examScd2 = new SlotCourseDetails();
        examScd2.setCourseTeacher(courseTeacher2);
        examScd2.setGsd(examGsd2);
        examScd2.setDayOfWeek(DayOfWeek.TUESDAY);
        examScd2 = slotCourseDetailsRepository.save(examScd2);

        SlotCourseDetails examScd3 = new SlotCourseDetails();
        examScd3.setCourseTeacher(courseTeacher3);
        examScd3.setGsd(examGsd1);
        examScd3.setDayOfWeek(DayOfWeek.WEDNESDAY);
        examScd3 = slotCourseDetailsRepository.save(examScd3);

        SlotCourseDetails examScd4 = new SlotCourseDetails();
        examScd4.setCourseTeacher(courseTeacher4);
        examScd4.setGsd(examGsd2);
        examScd4.setDayOfWeek(DayOfWeek.THURSDAY);
        examScd4 = slotCourseDetailsRepository.save(examScd4);

        SlotCourseDetails examScd5 = new SlotCourseDetails();
        examScd5.setCourseTeacher(courseTeacher5);
        examScd5.setGsd(examGsd1);
        examScd5.setDayOfWeek(DayOfWeek.FRIDAY);
        examScd5 = slotCourseDetailsRepository.save(examScd5);

        SlotCourseDetails examScd6 = new SlotCourseDetails();
        examScd6.setCourseTeacher(courseTeacher6);
        examScd6.setGsd(examGsd2);
        examScd6.setDayOfWeek(DayOfWeek.MONDAY);
        examScd6 = slotCourseDetailsRepository.save(examScd6);

        SlotCourseDetails examScd7 = new SlotCourseDetails();
        examScd7.setCourseTeacher(courseTeacher7);
        examScd7.setGsd(examGsd1);
        examScd7.setDayOfWeek(DayOfWeek.TUESDAY);
        examScd7 = slotCourseDetailsRepository.save(examScd7);

        SlotCourseDetails examScd8 = new SlotCourseDetails();
        examScd8.setCourseTeacher(courseTeacher8);
        examScd8.setGsd(examGsd2);
        examScd8.setDayOfWeek(DayOfWeek.WEDNESDAY);
        examScd8 = slotCourseDetailsRepository.save(examScd8);

        //Events for exam
        Event examEvent1 = new Event();
        examEvent1.setName("Exam Title");
        examEvent1.setDescription("Exam Syllabus");
        examEvent1.setType(EventType.EXAM);
        examEvent1.setDate(LocalDate.of(2018, 9, 3));
        examEvent1.setStandard(class1);
        examEvent1.setScd(examScd1);
        examEvent1 = eventRepository.save(examEvent1);

        Event examEvent2 = new Event();
        examEvent2.setName("Exam Title");
        examEvent2.setDescription("Exam Syllabus");
        examEvent2.setType(EventType.EXAM);
        examEvent2.setDate(LocalDate.of(2018, 9, 4));
        examEvent2.setStandard(class1);
        examEvent2.setScd(examScd2);
        examEvent2 = eventRepository.save(examEvent2);

        Event examEvent3 = new Event();
        examEvent3.setName("Exam Title");
        examEvent3.setDescription("Exam Syllabus");
        examEvent3.setType(EventType.EXAM);
        examEvent3.setDate(LocalDate.of(2018, 9, 5));
        examEvent3.setStandard(class1);
        examEvent3.setScd(examScd3);
        examEvent3 = eventRepository.save(examEvent3);

        Event examEvent4 = new Event();
        examEvent4.setName("Exam Title");
        examEvent4.setDescription("Exam Syllabus");
        examEvent4.setType(EventType.EXAM);
        examEvent4.setDate(LocalDate.of(2018, 9, 6));
        examEvent4.setStandard(class1);
        examEvent4.setScd(examScd4);
        examEvent4 = eventRepository.save(examEvent4);

        Event examEvent5 = new Event();
        examEvent5.setName("Exam Title");
        examEvent5.setDescription("Exam Syllabus");
        examEvent5.setType(EventType.EXAM);
        examEvent5.setDate(LocalDate.of(2018, 9, 7));
        examEvent5.setStandard(class1);
        examEvent5.setScd(examScd5);
        examEvent5 = eventRepository.save(examEvent5);

        Event examEvent6 = new Event();
        examEvent6.setName("Exam Title");
        examEvent6.setDescription("Exam Syllabus");
        examEvent6.setType(EventType.EXAM);
        examEvent6.setDate(LocalDate.of(2018, 9, 10));
        examEvent6.setStandard(class1);
        examEvent6.setScd(examScd6);
        examEvent6 = eventRepository.save(examEvent6);

        Event examEvent7 = new Event();
        examEvent7.setName("Exam Title");
        examEvent7.setDescription("Exam Syllabus");
        examEvent7.setType(EventType.EXAM);
        examEvent7.setDate(LocalDate.of(2018, 9, 11));
        examEvent7.setStandard(class1);
        examEvent7.setScd(examScd7);
        examEvent7 = eventRepository.save(examEvent7);

        Event examEvent8 = new Event();
        examEvent8.setName("Exam Title");
        examEvent8.setDescription("Exam Syllabus");
        examEvent8.setType(EventType.EXAM);
        examEvent8.setDate(LocalDate.of(2018, 9, 12));
        examEvent8.setStandard(class1);
        examEvent8.setScd(examScd8);
        examEvent8 = eventRepository.save(examEvent8);

        //Exam Events for Holidays
        Event holidayEvent1 = new Event();
        holidayEvent1.setName("Holiday 1");
        holidayEvent1.setDescription("This is a holiday, get your asses out of the school");
        holidayEvent1.setType(EventType.HOLIDAY);
        holidayEvent1.setDate(LocalDate.of(2018, 9, 24));
        holidayEvent1.setAcademicSession(academicSession1);
        holidayEvent1 = eventRepository.save(holidayEvent1);

        Event holidayEvent2 = new Event();
        holidayEvent2.setName("Holiday 2");
        holidayEvent2.setDescription("This is a holiday, get your asses out of the school");
        holidayEvent2.setType(EventType.HOLIDAY);
        holidayEvent2.setDate(LocalDate.of(2018, 9, 26));
        holidayEvent2.setAcademicSession(academicSession1);
        holidayEvent2 = eventRepository.save(holidayEvent2);

        Event holidayEvent3 = new Event();
        holidayEvent3.setName("Holiday 3");
        holidayEvent3.setDescription("This is a holiday, get your asses out of the school");
        holidayEvent3.setType(EventType.HOLIDAY);
        holidayEvent3.setDate(LocalDate.of(2018, 9, 28));
        holidayEvent3.setAcademicSession(academicSession1);
        holidayEvent3 = eventRepository.save(holidayEvent3);

        //Exam Events for Holidays
        Event leaveEvent1 = new Event();
        leaveEvent1.setName("Leave 1");
        leaveEvent1.setDescription("This is a leave, get your indiscipline asses out of the school");
        leaveEvent1.setType(EventType.ATTENDANCE);
        leaveEvent1.setDate(LocalDate.of(2018, 9, 25));
        leaveEvent1.setStudent(student1);
        leaveEvent1.setAcademicSession(academicSession1);
        leaveEvent1 = eventRepository.save(leaveEvent1);

        Event leaveEvent2 = new Event();
        leaveEvent2.setName("Leave 2");
        leaveEvent2.setDescription("This is a leave, get your indiscipline asses out of the school");
        leaveEvent2.setType(EventType.ATTENDANCE);
        leaveEvent2.setDate(LocalDate.of(2018, 9, 27));
        leaveEvent2.setStudent(student1);
        leaveEvent2.setAcademicSession(academicSession1);
        leaveEvent2 = eventRepository.save(leaveEvent2);

        Event leaveEvent3 = new Event();
        leaveEvent3.setName("Leave 3");
        leaveEvent3.setDescription("This is a leave, get your indiscipline asses out of the school");
        leaveEvent3.setType(EventType.ATTENDANCE);
        leaveEvent3.setDate(LocalDate.of(2018, 9, 29));
        leaveEvent3.setStudent(student1);
        leaveEvent3.setAcademicSession(academicSession1);
        leaveEvent3 = eventRepository.save(leaveEvent3);


    }

    private void loadSchoolDataForTeacher() {

        String affiliationId = RandomStringUtils.randomAlphanumeric(6);
        //school data

        Institute institute1 = new Institute();
        institute1.setName("Chirec");
        institute1 = instituteRepository.save(institute1);

        School school1 = new School();
        school1.setName("Witcurve");
        school1.setAddress1("Kondapur");
        school1.setAffiliationId(affiliationId);
        school1.setCity("Hyderabad");
        school1.setState("Telangana");
        school1.setCountry("India");
        school1.setDistrict("Ranga Reddy District");
        school1.setPincode("500084");
        school1.setPrimaryPhone("9999999999");
        school1.setPrimaryEmail("contact@witcurve.com");
        school1.setFax("9999999999");


        school1= schoolRepository.save(school1);

        SchoolInfo schoolInfo = new SchoolInfo();
        schoolInfo.setSchool(school1);
        schoolInfo.setMedium("English");
        schoolInfo.setBoard("CBSE");

        schoolInfo = schoolInfoRepository.save(schoolInfo);

        // academic session

        AcademicSession academicSession1 = new AcademicSession();
        academicSession1.setSchoolInfo(schoolInfo);
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
        staff1.setPrimaryPhone("9876543210");
        staff1.setSchool(school1);
        staff1.setType("Teaching");
        staff1.setStaffId("STAFF_01");

        staff1 = staffRepository.save(staff1);

        Staff staff2 = new Staff();
        staff2.setAddress1("Hitech City");
        staff2.setFirstName("Dhiraj");
        staff2.setLastName("Kumar");
        staff2.setPrimaryPhone("9876543210");
        staff2.setSchool(school1);
        staff2.setType("Teaching");
        staff2.setStaffId("STAFF_02");

        staff2 = staffRepository.save(staff2);

        Staff staff3 = new Staff();
        staff3.setAddress1("Hitech City");
        staff3.setFirstName("Srujan Kumar");
        staff3.setLastName("Tad");
        staff3.setPrimaryPhone("9876543210");
        staff3.setSchool(school1);
        staff3.setType("Teaching");
        staff3.setStaffId("STAFF_03");

        staff3 = staffRepository.save(staff3);

        Staff staff4 = new Staff();
        staff4.setAddress1("Hitech City");
        staff4.setFirstName("Kishore Kumar");
        staff4.setLastName("SVR");
        staff4.setPrimaryPhone("9876543210");
        staff4.setSchool(school1);
        staff4.setType("Teaching");
        staff4.setStaffId("STAFF_04");

        staff4 = staffRepository.save(staff4);

        Staff staff5 = new Staff();
        staff5.setAddress1("Hitech City");
        staff5.setFirstName("Mahendra");
        staff5.setLastName("No Idea");
        staff5.setPrimaryPhone("9876543210");
        staff5.setSchool(school1);
        staff5.setType("Teaching");
        staff5.setStaffId("STAFF_05");

        staff5 = staffRepository.save(staff5);

        Staff staff6 = new Staff();
        staff6.setAddress1("Hitech City");
        staff6.setFirstName("Satya");
        staff6.setLastName("No Idea");
        staff6.setPrimaryPhone("9876543210");
        staff6.setSchool(school1);
        staff6.setType("Teaching");
        staff6.setStaffId("STAFF_06");

        staff6 = staffRepository.save(staff6);


        Staff staff7 = new Staff();
        staff7.setAddress1("Hitech City");
        staff7.setFirstName("Sai Chand");
        staff7.setLastName("Gandivasala");
        staff7.setPrimaryPhone("9876543210");
        staff7.setSchool(school1);
        staff7.setType("Teaching");
        staff7.setStaffId("STAFF_07");

        staff7 = staffRepository.save(staff7);

        Staff staff8 = new Staff();
        staff8.setAddress1("Hitech City");
        staff8.setFirstName("Kalyan");
        staff8.setLastName("Naik");
        staff8.setPrimaryPhone("9876543210");
        staff8.setSchool(school1);
        staff8.setType("Teaching");
        staff8.setStaffId("STAFF_08");

        staff8 = staffRepository.save(staff8);

        // class data

        Standard class1 = new Standard();
        class1.setGrade(Grade.III);
        class1.setTerm(term2);
        class1.setSection("A");
        class1.setClassTeacher(staff1);
        class1 = classRepository.save(class1);

        // courses

        MasterSubject masterSubject1 = new MasterSubject();
        masterSubject1.setName("Telugu1");
        masterSubject1 = masterSubjectRepository.save(masterSubject1);

        Course course1 = new Course();
        course1.setCourseName("Telugu1");
        course1.setMasterSubject(masterSubject1);
        course1.setDescription("Telugu1");
        course1.setSchool(school1);
        course1 = courseRepository.save(course1);

        MasterSubject masterSubject2 = new MasterSubject();
        masterSubject2.setName("Hindi1");
        masterSubject2 = masterSubjectRepository.save(masterSubject1);

        Course course2 = new Course();
        course2.setCourseName("Hindi1");
        course2.setMasterSubject(masterSubject2);
        course2.setDescription("Hindi1");
        course2.setSchool(school1);
        course2 = courseRepository.save(course2);

        MasterSubject masterSubject3 = new MasterSubject();
        masterSubject3.setName("English1");
        masterSubject3 = masterSubjectRepository.save(masterSubject3);

        Course course3 = new Course();
        course3.setCourseName("English1");
        course3.setMasterSubject(masterSubject3);
        course3.setDescription("English1");
        course3.setSchool(school1);
        course3 = courseRepository.save(course3);

        MasterSubject masterSubject4 = new MasterSubject();
        masterSubject4.setName("Maths1");
        masterSubject4 = masterSubjectRepository.save(masterSubject4);

        Course course4 = new Course();
        course4.setCourseName("Maths1");
        course4.setMasterSubject(masterSubject4);
        course4.setDescription("Maths1");
        course4.setSchool(school1);
        course4 = courseRepository.save(course4);

        MasterSubject masterSubject5 = new MasterSubject();
        masterSubject5.setName("Science1");
        masterSubject5 = masterSubjectRepository.save(masterSubject5);

        Course course5 = new Course();
        course5.setCourseName("Science1");
        course5.setMasterSubject(masterSubject5);
        course5.setDescription("Science1");
        course5.setSchool(school1);
        course5 = courseRepository.save(course5);

        MasterSubject masterSubject6 = new MasterSubject();
        masterSubject6.setName("Social Studies1");
        masterSubject6 = masterSubjectRepository.save(masterSubject6);

        Course course6 = new Course();
        course6.setCourseName("Social Studies1");
        course6.setMasterSubject(masterSubject6);
        course6.setDescription("Social Studies1");
        course6.setSchool(school1);
        course6 = courseRepository.save(course6);

        MasterSubject masterSubject7 = new MasterSubject();
        masterSubject7.setName("Drawing1");
        masterSubject7 = masterSubjectRepository.save(masterSubject7);

        Course course7 = new Course();
        course7.setCourseName("Drawing1");
        course7.setMasterSubject(masterSubject7);
        course7.setDescription("Drawing1");
        course7.setSchool(school1);
        course7 = courseRepository.save(course7);


        MasterSubject masterSubject8 = new MasterSubject();
        masterSubject8.setName("Physical Training1");
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
        student1.setSchoolInfo(schoolInfo);
        student1.setAdmissionId("STD_001");
        student1.setRegisteredMobileNumber("9876543210");
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

        StudentStandard studentStandard = new StudentStandard();
        studentStandard.setStandard(class1);
        studentStandard.setStudent(student1);
        studentStandard.setRollNo("1");
        studentStandard = studentStandardRepository.save(studentStandard);

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
        slotCourseDetails11.setCourseTeacher(courseTeacher1);
        slotCourseDetails11.setGsd(gsd1);
        slotCourseDetails11.setDayOfWeek(DayOfWeek.MONDAY);
        slotCourseDetails11 = slotCourseDetailsRepository.save(slotCourseDetails11);


        SlotCourseDetails slotCourseDetails12 = new SlotCourseDetails();
        slotCourseDetails12.setCourseTeacher(courseTeacher2);
        slotCourseDetails12.setGsd(gsd2);
        slotCourseDetails12.setDayOfWeek(DayOfWeek.MONDAY);
        slotCourseDetails12 = slotCourseDetailsRepository.save(slotCourseDetails12);

        SlotCourseDetails slotCourseDetails13 = new SlotCourseDetails();
        slotCourseDetails13.setCourseTeacher(courseTeacher3);
        slotCourseDetails13.setGsd(gsd3);
        slotCourseDetails13.setDayOfWeek(DayOfWeek.MONDAY);
        slotCourseDetails13 = slotCourseDetailsRepository.save(slotCourseDetails13);

        SlotCourseDetails slotCourseDetails14 = new SlotCourseDetails();
        slotCourseDetails14.setCourseTeacher(courseTeacher4);
        slotCourseDetails14.setGsd(gsd4);
        slotCourseDetails14.setDayOfWeek(DayOfWeek.MONDAY);
        slotCourseDetails14 = slotCourseDetailsRepository.save(slotCourseDetails14);

        SlotCourseDetails slotCourseDetails15 = new SlotCourseDetails();
        slotCourseDetails15.setCourseTeacher(courseTeacher5);
        slotCourseDetails15.setGsd(gsd5);
        slotCourseDetails15.setDayOfWeek(DayOfWeek.MONDAY);
        slotCourseDetails15 = slotCourseDetailsRepository.save(slotCourseDetails15);


        SlotCourseDetails slotCourseDetails16 = new SlotCourseDetails();
        slotCourseDetails16.setCourseTeacher(courseTeacher6);
        slotCourseDetails16.setGsd(gsd6);
        slotCourseDetails16.setDayOfWeek(DayOfWeek.MONDAY);
        slotCourseDetails16 = slotCourseDetailsRepository.save(slotCourseDetails16);

        SlotCourseDetails slotCourseDetails17 = new SlotCourseDetails();
        slotCourseDetails17.setCourseTeacher(courseTeacher7);
        slotCourseDetails17.setGsd(gsd7);
        slotCourseDetails17.setDayOfWeek(DayOfWeek.MONDAY);
        slotCourseDetails17 = slotCourseDetailsRepository.save(slotCourseDetails17);


        SlotCourseDetails slotCourseDetails18 = new SlotCourseDetails();
        slotCourseDetails18.setCourseTeacher(courseTeacher8);
        slotCourseDetails18.setGsd(gsd8);
        slotCourseDetails18.setDayOfWeek(DayOfWeek.MONDAY);
        slotCourseDetails18 = slotCourseDetailsRepository.save(slotCourseDetails18);

        /*TUESDAY*/
        SlotCourseDetails slotCourseDetails21 = new SlotCourseDetails();
        slotCourseDetails21.setCourseTeacher(courseTeacher1);
        slotCourseDetails21.setGsd(gsd1);
        slotCourseDetails21.setDayOfWeek(DayOfWeek.TUESDAY);
        slotCourseDetails21 = slotCourseDetailsRepository.save(slotCourseDetails21);


        SlotCourseDetails slotCourseDetails22 = new SlotCourseDetails();
        slotCourseDetails22.setCourseTeacher(courseTeacher2);
        slotCourseDetails22.setGsd(gsd2);
        slotCourseDetails22.setDayOfWeek(DayOfWeek.TUESDAY);
        slotCourseDetails22 = slotCourseDetailsRepository.save(slotCourseDetails22);

        SlotCourseDetails slotCourseDetails23 = new SlotCourseDetails();
        slotCourseDetails23.setCourseTeacher(courseTeacher3);
        slotCourseDetails23.setGsd(gsd3);
        slotCourseDetails23.setDayOfWeek(DayOfWeek.TUESDAY);
        slotCourseDetails23 = slotCourseDetailsRepository.save(slotCourseDetails23);

        SlotCourseDetails slotCourseDetails24 = new SlotCourseDetails();
        slotCourseDetails24.setCourseTeacher(courseTeacher4);
        slotCourseDetails24.setGsd(gsd4);
        slotCourseDetails24.setDayOfWeek(DayOfWeek.TUESDAY);
        slotCourseDetails24 = slotCourseDetailsRepository.save(slotCourseDetails24);

        SlotCourseDetails slotCourseDetails25 = new SlotCourseDetails();
        slotCourseDetails25.setCourseTeacher(courseTeacher5);
        slotCourseDetails25.setGsd(gsd5);
        slotCourseDetails25.setDayOfWeek(DayOfWeek.TUESDAY);
        slotCourseDetails25 = slotCourseDetailsRepository.save(slotCourseDetails25);


        SlotCourseDetails slotCourseDetails26 = new SlotCourseDetails();
        slotCourseDetails26.setCourseTeacher(courseTeacher6);
        slotCourseDetails26.setGsd(gsd6);
        slotCourseDetails26.setDayOfWeek(DayOfWeek.TUESDAY);
        slotCourseDetails26 = slotCourseDetailsRepository.save(slotCourseDetails26);

        SlotCourseDetails slotCourseDetails27 = new SlotCourseDetails();
        slotCourseDetails27.setCourseTeacher(courseTeacher7);
        slotCourseDetails27.setGsd(gsd7);
        slotCourseDetails27.setDayOfWeek(DayOfWeek.TUESDAY);
        slotCourseDetails27 = slotCourseDetailsRepository.save(slotCourseDetails27);


        SlotCourseDetails slotCourseDetails28 = new SlotCourseDetails();
        slotCourseDetails28.setCourseTeacher(courseTeacher8);
        slotCourseDetails28.setGsd(gsd8);
        slotCourseDetails28.setDayOfWeek(DayOfWeek.TUESDAY);
        slotCourseDetails28 = slotCourseDetailsRepository.save(slotCourseDetails28);


        /*WEDNESDAY*/
        SlotCourseDetails slotCourseDetails31 = new SlotCourseDetails();
        slotCourseDetails31.setCourseTeacher(courseTeacher1);
        slotCourseDetails31.setGsd(gsd1);
        slotCourseDetails31.setDayOfWeek(DayOfWeek.WEDNESDAY);
        slotCourseDetails31 = slotCourseDetailsRepository.save(slotCourseDetails31);


        SlotCourseDetails slotCourseDetails32 = new SlotCourseDetails();
        slotCourseDetails32.setCourseTeacher(courseTeacher2);
        slotCourseDetails32.setGsd(gsd2);
        slotCourseDetails32.setDayOfWeek(DayOfWeek.WEDNESDAY);
        slotCourseDetails32 = slotCourseDetailsRepository.save(slotCourseDetails32);

        SlotCourseDetails slotCourseDetails33 = new SlotCourseDetails();
        slotCourseDetails33.setCourseTeacher(courseTeacher3);
        slotCourseDetails33.setGsd(gsd3);
        slotCourseDetails33.setDayOfWeek(DayOfWeek.WEDNESDAY);
        slotCourseDetails33 = slotCourseDetailsRepository.save(slotCourseDetails33);

        SlotCourseDetails slotCourseDetails34 = new SlotCourseDetails();
        slotCourseDetails34.setCourseTeacher(courseTeacher4);
        slotCourseDetails34.setGsd(gsd4);
        slotCourseDetails34.setDayOfWeek(DayOfWeek.WEDNESDAY);
        slotCourseDetails34 = slotCourseDetailsRepository.save(slotCourseDetails34);

        SlotCourseDetails slotCourseDetails35 = new SlotCourseDetails();
        slotCourseDetails35.setCourseTeacher(courseTeacher5);
        slotCourseDetails35.setGsd(gsd5);
        slotCourseDetails35.setDayOfWeek(DayOfWeek.WEDNESDAY);
        slotCourseDetails35 = slotCourseDetailsRepository.save(slotCourseDetails35);


        SlotCourseDetails slotCourseDetails36 = new SlotCourseDetails();
        slotCourseDetails36.setCourseTeacher(courseTeacher6);
        slotCourseDetails36.setGsd(gsd6);
        slotCourseDetails36.setDayOfWeek(DayOfWeek.WEDNESDAY);
        slotCourseDetails36 = slotCourseDetailsRepository.save(slotCourseDetails36);

        SlotCourseDetails slotCourseDetails37 = new SlotCourseDetails();
        slotCourseDetails37.setCourseTeacher(courseTeacher7);
        slotCourseDetails37.setGsd(gsd7);
        slotCourseDetails37.setDayOfWeek(DayOfWeek.WEDNESDAY);
        slotCourseDetails37 = slotCourseDetailsRepository.save(slotCourseDetails37);


        SlotCourseDetails slotCourseDetails38 = new SlotCourseDetails();
        slotCourseDetails38.setCourseTeacher(courseTeacher8);
        slotCourseDetails38.setGsd(gsd8);
        slotCourseDetails38.setDayOfWeek(DayOfWeek.WEDNESDAY);
        slotCourseDetails38 = slotCourseDetailsRepository.save(slotCourseDetails38);

        /*THURSDAY*/
        SlotCourseDetails slotCourseDetails41 = new SlotCourseDetails();
        slotCourseDetails41.setCourseTeacher(courseTeacher1);
        slotCourseDetails41.setGsd(gsd1);
        slotCourseDetails41.setDayOfWeek(DayOfWeek.THURSDAY);
        slotCourseDetails41 = slotCourseDetailsRepository.save(slotCourseDetails41);


        SlotCourseDetails slotCourseDetails42 = new SlotCourseDetails();
        slotCourseDetails42.setCourseTeacher(courseTeacher2);
        slotCourseDetails42.setGsd(gsd2);
        slotCourseDetails42.setDayOfWeek(DayOfWeek.THURSDAY);
        slotCourseDetails42 = slotCourseDetailsRepository.save(slotCourseDetails42);

        SlotCourseDetails slotCourseDetails43 = new SlotCourseDetails();
        slotCourseDetails43.setCourseTeacher(courseTeacher3);
        slotCourseDetails43.setGsd(gsd3);
        slotCourseDetails43.setDayOfWeek(DayOfWeek.THURSDAY);
        slotCourseDetails43 = slotCourseDetailsRepository.save(slotCourseDetails43);

        SlotCourseDetails slotCourseDetails44 = new SlotCourseDetails();
        slotCourseDetails44.setCourseTeacher(courseTeacher4);
        slotCourseDetails44.setGsd(gsd4);
        slotCourseDetails44.setDayOfWeek(DayOfWeek.THURSDAY);
        slotCourseDetails44 = slotCourseDetailsRepository.save(slotCourseDetails44);

        SlotCourseDetails slotCourseDetails45 = new SlotCourseDetails();
        slotCourseDetails45.setCourseTeacher(courseTeacher5);
        slotCourseDetails45.setGsd(gsd5);
        slotCourseDetails45.setDayOfWeek(DayOfWeek.THURSDAY);
        slotCourseDetails45 = slotCourseDetailsRepository.save(slotCourseDetails45);


        SlotCourseDetails slotCourseDetails46 = new SlotCourseDetails();
        slotCourseDetails46.setCourseTeacher(courseTeacher6);
        slotCourseDetails46.setGsd(gsd6);
        slotCourseDetails46.setDayOfWeek(DayOfWeek.THURSDAY);
        slotCourseDetails46 = slotCourseDetailsRepository.save(slotCourseDetails46);

        SlotCourseDetails slotCourseDetails47 = new SlotCourseDetails();
        slotCourseDetails47.setCourseTeacher(courseTeacher7);
        slotCourseDetails47.setGsd(gsd7);
        slotCourseDetails47.setDayOfWeek(DayOfWeek.THURSDAY);
        slotCourseDetails47 = slotCourseDetailsRepository.save(slotCourseDetails47);


        SlotCourseDetails slotCourseDetails48 = new SlotCourseDetails();
        slotCourseDetails48.setCourseTeacher(courseTeacher8);
        slotCourseDetails48.setGsd(gsd8);
        slotCourseDetails48.setDayOfWeek(DayOfWeek.THURSDAY);
        slotCourseDetails48 = slotCourseDetailsRepository.save(slotCourseDetails48);

        /*FRIDAY*/
        SlotCourseDetails slotCourseDetails51 = new SlotCourseDetails();
        slotCourseDetails51.setCourseTeacher(courseTeacher1);
        slotCourseDetails51.setGsd(gsd1);
        slotCourseDetails51.setDayOfWeek(DayOfWeek.FRIDAY);
        slotCourseDetails51 = slotCourseDetailsRepository.save(slotCourseDetails51);


        SlotCourseDetails slotCourseDetails52 = new SlotCourseDetails();
        slotCourseDetails52.setCourseTeacher(courseTeacher2);
        slotCourseDetails52.setGsd(gsd2);
        slotCourseDetails52.setDayOfWeek(DayOfWeek.FRIDAY);
        slotCourseDetails52 = slotCourseDetailsRepository.save(slotCourseDetails52);

        SlotCourseDetails slotCourseDetails53 = new SlotCourseDetails();
        slotCourseDetails53.setCourseTeacher(courseTeacher3);
        slotCourseDetails53.setGsd(gsd3);
        slotCourseDetails53.setDayOfWeek(DayOfWeek.FRIDAY);
        slotCourseDetails53 = slotCourseDetailsRepository.save(slotCourseDetails53);

        SlotCourseDetails slotCourseDetails54 = new SlotCourseDetails();
        slotCourseDetails54.setCourseTeacher(courseTeacher4);
        slotCourseDetails54.setGsd(gsd4);
        slotCourseDetails54.setDayOfWeek(DayOfWeek.FRIDAY);
        slotCourseDetails54 = slotCourseDetailsRepository.save(slotCourseDetails54);

        SlotCourseDetails slotCourseDetails55 = new SlotCourseDetails();
        slotCourseDetails55.setCourseTeacher(courseTeacher5);
        slotCourseDetails55.setGsd(gsd5);
        slotCourseDetails55.setDayOfWeek(DayOfWeek.FRIDAY);
        slotCourseDetails55 = slotCourseDetailsRepository.save(slotCourseDetails55);


        SlotCourseDetails slotCourseDetails56 = new SlotCourseDetails();
        slotCourseDetails56.setCourseTeacher(courseTeacher6);
        slotCourseDetails56.setGsd(gsd6);
        slotCourseDetails56.setDayOfWeek(DayOfWeek.FRIDAY);
        slotCourseDetails56 = slotCourseDetailsRepository.save(slotCourseDetails56);

        SlotCourseDetails slotCourseDetails57 = new SlotCourseDetails();
        slotCourseDetails57.setCourseTeacher(courseTeacher7);
        slotCourseDetails57.setGsd(gsd7);
        slotCourseDetails57.setDayOfWeek(DayOfWeek.FRIDAY);
        slotCourseDetails57 = slotCourseDetailsRepository.save(slotCourseDetails57);

        SlotCourseDetails slotCourseDetails58 = new SlotCourseDetails();
        slotCourseDetails58.setCourseTeacher(courseTeacher8);
        slotCourseDetails58.setGsd(gsd8);
        slotCourseDetails58.setDayOfWeek(DayOfWeek.FRIDAY);
        slotCourseDetails58 = slotCourseDetailsRepository.save(slotCourseDetails58);

    }

}
