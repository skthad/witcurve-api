package com.witcurve.repository;

import com.witcurve.domain.CourseTeacher;
import com.witcurve.domain.MasterSubject;
import com.witcurve.domain.Standard;
import com.witcurve.domain.enumeration.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseTeacherRepository extends JpaRepository<CourseTeacher, Long> {

    @Query("select ct from CourseTeacher ct where ct.teacher.id = ?1")
    List<CourseTeacher> findAllByTeacherId(Long teacherId);

    @Query("select ct from CourseTeacher ct where ct.teacher.id = ?1 and ct.active = true")
    List<CourseTeacher> findByTeacherId(Long teacherId);

    @Query("select distinct ct.standard from CourseTeacher ct where ct.teacher.id = ?1 and ct.active = true")
    List<Standard> findStandardsByTeacherId(Long teacherId);

    @Query("select ct from CourseTeacher ct where ct.standard.id = ?1 and ct.active = true")
    List<CourseTeacher> findByStandardId(Long standardId);

    @Query("select distinct ct.teacher.id from CourseTeacher ct where ct.standard.id = ?1 and ct.active = true and ct.teacher.id != ?2")
    List<Long> findAvailableTeacherByStandardId(Long standardId, Long teacherId);

    @Query("select ct from CourseTeacher ct where ct.teacher.id = ?1 " +
        "and ct.course.masterSubject = ?2 and ct.standard.grade = ?3 and ct.active = true")
    List<CourseTeacher> findByStaffAndSubjectAndGrade(Long staffId, MasterSubject subject, Grade grade);

    @Query("select distinct ct.teacher.id from CourseTeacher ct where ct.course.eligibleForSubstitute = true " +
        "and ct.teacher.id != ?1 and ct.teacher.schoolInfo.id = ?2")
    List<Long> findEligibleForSubstituteBySchoolInfoId(Long staffId, Long schoolInfoId);

}
