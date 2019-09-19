package com.witcurve.repository;

import com.witcurve.domain.CourseTeacher;
import com.witcurve.domain.MasterSubject;
import com.witcurve.domain.Standard;
import com.witcurve.domain.enumeration.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface CourseTeacherRepository extends JpaRepository<CourseTeacher, Long> {

    @Query("select ct from CourseTeacher ct where ct.teacher.id = ?1")
    List<CourseTeacher> findAllByTeacherId(Long teacherId);

    @Query("select ct from CourseTeacher ct where ct.teacher.id = ?1 and ct.active = true")
    List<CourseTeacher> findByTeacherId(Long teacherId);

    @Query("select ct from CourseTeacher ct where ct.course.id = ?1 and ct.active = true")
    List<CourseTeacher> findByCourseId(Long courseId);

    @Query("select distinct ct.standard from CourseTeacher ct where ct.teacher.id = ?1 and ct.active = true")
    List<Standard> findStandardsByTeacherId(Long teacherId);

    @Query("select ct from CourseTeacher ct where ct.standard.id = ?1 order by ct.course.masterSubject.name asc, ct.course.courseCode asc")
    List<CourseTeacher> findByStandardId(Long standardId);

    @Query("select ct from CourseTeacher ct where ct.standard.id = ?1 and ct.active = true order by ct.course.masterSubject.name asc, ct.course.courseCode asc")
    List<CourseTeacher> findActiveCourseTeachersByStandardId(Long standardId);

    @Query("select ct from CourseTeacher ct where ct.standard.id = ?1 and ct.course.id in ?2 and ct.active = true order by ct.course.masterSubject.name asc, ct.course.courseCode asc")
    List<CourseTeacher> findActiveCourseTeachersByStandardIdAndCoursIds(Long standardId, List<Long> courseIds);

    @Query("select distinct ct.teacher.email from CourseTeacher ct where ct.standard.id = ?1 and ct.active = true")
    Set<String> findActiveCourseTeacherEmailIdsByStandardId(Long standardId);

    @Query("select distinct ct.teacher.primaryPhone from CourseTeacher ct where ct.standard.id = ?1 and ct.active = true")
    Set<String> findActiveCourseTeacherPhoneNumbersByStandardId(Long standardId);

    @Query("select ct from CourseTeacher ct where ct.standard.id = ?1 and ct.active = false order by ct.course.masterSubject.name asc, ct.course.courseCode asc")
    List<CourseTeacher> findInActiveCourseTeachersByStandardId(Long standardId);

    @Query("select distinct ct.teacher.id from CourseTeacher ct where ct.standard.id = ?1 and ct.active = true and ct.teacher.id <> ?2 " +
        "and ct.teacher.id in (select distinct e.staff.id from Event e where e.type = 'ATTENDANCE' and e.date = ?3 and e.attendanceType <> 'ABSENT') order by ct.teacher.employeeId")
    List<Long> findAvailableTeacherByStandardId(Long standardId, Long teacherId, LocalDate date);

    @Query("select ct from CourseTeacher ct where ct.teacher.id = ?1 " +
        "and ct.course.masterSubject = ?2 and ct.standard.grade = ?3 and ct.active = true")
    List<CourseTeacher> findByStaffAndSubjectAndGrade(Long staffId, MasterSubject subject, Grade grade);

    @Query("select distinct ct.teacher.id from CourseTeacher ct where ct.course.courseType = 'NON_SCHOLASTIC' " +
        "and ct.teacher.id <> ?1 and ct.teacher.schoolInfo.id = ?2 order by ct.teacher.employeeId")
    List<Long> findEligibleForSubstituteBySchoolInfoId(Long staffId, Long schoolInfoId);

    @Query("select ct from CourseTeacher ct where ct.standard.grade = ?1 and ct.standard.schoolInfo.id=?2 and ct.active = true order by ct.course.courseCode asc")
    List<CourseTeacher> findActiveCourseTeacherByGradeAndSchoolInfoId(Grade grade, Long schoolInfoId);

}
