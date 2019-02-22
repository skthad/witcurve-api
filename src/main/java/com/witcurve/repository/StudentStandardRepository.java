package com.witcurve.repository;

import com.witcurve.domain.Student;
import com.witcurve.domain.StudentStandard;
import com.witcurve.domain.enumeration.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentStandardRepository extends JpaRepository<StudentStandard, Long> {

    @Query("select ss from StudentStandard ss where ss.student.id = ?1 and ss.active = true order by ss.standard.grade, ss.standard.section, ss.rollNo")
    List<StudentStandard> getByStudentId(Long studentId);

    @Query("select ss from StudentStandard ss where ss.student.id = ?1 and ss.standard.id = ?2")
    StudentStandard getByStudentIdAndStandardId(Long studentId, Long standardId);

    @Query("select ss from StudentStandard ss where ss.standard.id = ?1 and ss.active = true order by ss.rollNo")
    List<StudentStandard> getByStandardId(Long standardId);

    @Query("select ss from StudentStandard ss where ss.standard.id in ?1 and ss.active = true order by ss.standard.grade, ss.standard.section, ss.rollNo")
    List<StudentStandard> getByStandardsId(List<Long> standardIds);

    @Query("select ss from StudentStandard ss where ss.standard.schoolInfo.id = ?1 and ss.active = true order by ss.standard.grade, ss.standard.section, ss.rollNo")
    List<StudentStandard> getBySchoolInfoId(Long schoolInfoId);

    @Query("select ss from StudentStandard ss where ss.standard.schoolInfo.id = ?1 and ss.standard.grade = ?2 and ss.active = true order by ss.standard.grade, ss.standard.section, ss.rollNo")
    List<StudentStandard> getBySchoolInfoIdAndGrade(Long schoolInfoId, Grade grade);

    @Query("select ss.student.id from StudentStandard ss where ss.standard.id = ?1 and ss.active = true")
    List<Long> findStudentIdByStandardId(Long standardId);

    @Query("select ss.student from StudentStandard ss where ss.standard.id = ?1 and ss.active = true order by ss.rollNo")
    List<Student> getStudentsByStandardId(Long standardId);

    @Query("select ss.standard.id from StudentStandard ss where ss.student.id = ?1 and ss.active = true")
    Long getStandardIdByStudentId(Long studentId);

    @Query("select ss from StudentStandard ss where ss.standard.id = ?1 and ss.rollNo = ?2 and ss.active = true order by ss.rollNo")
    List<StudentStandard> getByStandardIdAndRollNo(Long standardId, String rollNo);

    @Query("select ss from StudentStandard ss where ss.standard.id = ?1 and ss.rollNo in ?2 and ss.active = true order by ss.rollNo")
    List<StudentStandard> getByStandardIdAndRollNos(Long standardId, List<String> rollNos);

    @Modifying
    @Query("update StudentStandard set active = false where student.id in ?1")
    void deactivateByStudentIds(List<Long> studentIds);
}
