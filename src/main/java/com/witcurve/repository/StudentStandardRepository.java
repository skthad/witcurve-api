package com.witcurve.repository;

import com.witcurve.domain.StudentStandard;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.web.rest.vm.StandardStudentCountVM;
import com.witcurve.web.rest.vm.StudentPerformanceDashboardVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public interface StudentStandardRepository extends JpaRepository<StudentStandard, Long> {

    @Query("select ss from StudentStandard ss where ss.student.id = ?1 and ss.active = true order by ss.standard.grade, ss.standard.section, ss.rollNo")
    List<StudentStandard> getByStudentId(Long studentId);

    @Query("select ss from StudentStandard ss where ss.student.id = ?1 and ss.standard.id = ?2 and ss.session.id = ?3")
    StudentStandard getByStudentIdAndStandardIdAndSessionId(Long studentId, Long standardId, Long sessionId);

    @Query("select ss from StudentStandard ss where ss.standard.id = ?1 and ss.active = true order by ss.rollNo")
    List<StudentStandard> getByStandardId(Long standardId);

    @Query("select ss from StudentStandard ss where ss.standard.id = ?1 and ss.active = true order by ss.rollNo")
    Page<StudentStandard> getByStandardId(Long standardId, Pageable pageable);

    @Query("select sc.studentStandard from StudentCourse sc where sc.studentStandard.standard.id = ?1 and sc.course.id=?2 and sc.active=true order by sc.studentStandard.rollNo")
    List<StudentStandard> getByStandardIdAndCourseId(Long standardId, Long courseId);

    @Query("select ss from StudentStandard ss where ss.standard.id in ?1 and ss.active = true order by ss.standard.grade, ss.standard.section, ss.rollNo")
    List<StudentStandard> getByStandardsId(List<Long> standardIds);

    @Query("select ss from StudentStandard ss where ss.standard.schoolInfo.id = ?1 and ss.active = true order by ss.standard.grade, ss.standard.section, ss.rollNo")
    List<StudentStandard> getBySchoolInfoId(Long schoolInfoId);

    @Query("select distinct ss.student.registeredMobileNumber from StudentStandard ss where ss.standard.schoolInfo.id = ?1 and ss.active = true")
    Set<String> getActiveStudentPhoneNumbersBySchoolInfoId(Long schoolInfoId);

    @Query("select distinct ss.student.email from StudentStandard ss where ss.standard.schoolInfo.id = ?1 and ss.active = true and ss.student.email is not null")
    Set<String> getActiveStudentEmailsBySchoolInfoId(Long schoolInfoId);

    @Query("select distinct ss.student.registeredMobileNumber from StudentStandard ss where ss.standard.schoolInfo.id = ?1 and ss.standard.grade in ?2 and ss.active = true")
    Set<String> getActiveStudentPhoneNumbersBySchoolInfoIdAndGradeList(Long schoolInfoId, List<Grade> grades);

    @Query("select distinct ss.student.email from StudentStandard ss where ss.standard.schoolInfo.id = ?1 and ss.standard.grade in ?2 and ss.active = tru and ss.student.email is not null")
    Set<String> getActiveStudentEmailsBySchoolInfoIdAndGradeList(Long schoolInfoId, List<Grade> grades);

    @Query("select distinct ss.student.registeredMobileNumber from StudentStandard ss where ss.standard.schoolInfo.id = ?1 and ss.standard.id in ?2 and ss.active = true")
    Set<String> getActiveStudentPhoneNumbersBySchoolInfoIdAndStandardIds(Long schoolInfoId, List<Long> standardIds);

    @Query("select distinct ss.student.email from StudentStandard ss where ss.standard.schoolInfo.id = ?1 and ss.standard.id in ?2 and ss.active = true and ss.student.email is not null")
    Set<String> getActiveStudentEmailsBySchoolInfoIdAndStandardIds(Long schoolInfoId, List<Long> standardIds);

    @Query("select ss from StudentStandard ss where ss.standard.schoolInfo.id = ?1 and ss.standard.grade = ?2 and ss.active = true order by ss.standard.grade, ss.standard.section, ss.rollNo")
    List<StudentStandard> getBySchoolInfoIdAndGrade(Long schoolInfoId, Grade grade);

    @Query("select ss.student.id from StudentStandard ss where ss.standard.id = ?1 and ss.active = true")
    List<Long> findStudentIdByStandardId(Long standardId);

    @Query("select ss.standard.id from StudentStandard ss where ss.student.id = ?1 and ss.active = true")
    Long getStandardIdByStudentId(Long studentId);

    @Query("select ss from StudentStandard ss where ss.standard.id = ?1 and ss.rollNo = ?2 and ss.active = true order by ss.rollNo")
    List<StudentStandard> getByStandardIdAndRollNo(Long standardId, String rollNo);

    @Query("select ss from StudentStandard ss where ss.standard.id = ?1 and ss.rollNo in ?2 and ss.active = true order by ss.rollNo")
    List<StudentStandard> getByStandardIdAndRollNos(Long standardId, List<String> rollNos);

    @Query("select ss from StudentStandard ss where ss.standard.schoolInfo.id = ?1 and ss.student.id in ?2 and ss.active = true order by ss.rollNo")
    List<StudentStandard> getByStudentIdsAndSchoolInfoId(Long schoolInfoId, List<Long> studentIds);

    @Modifying
    @Query("update StudentStandard set active = false where student.id in ?1")
    void deactivateByStudentIds(List<Long> studentIds);

    @Query("select ss.standard.id from StudentStandard ss where ss.student.user.id=?1 and ss.active = true")
    Long getStandardIdByUserId(Long userId);

    @Query("select ss from StudentStandard ss where ss.session.id = ?1 and ss.standard.id = ?2 and ss.rollNo = ?3 and ss.active = true")
    StudentStandard getBySessionIdAndStandardIdAndRollNo(Long sessionId,Long standardId,String rollNo);

    @Query("select ss.id from StudentStandard ss where ss.session.id= ?1 and ss.active=true and ss.standard.grade= ?2 and ss.active=true")
    List<Long> getBySessionIdAndGrade(Long sessionId,Grade grade);

    @Query("select new com.witcurve.web.rest.vm.StudentPerformanceDashboardVM(ss.standard.id, ss.student.gender, count(ss.id)) from StudentStandard ss where ss.active=true and ss.student.schoolInfo.id=?1 group by ss.standard.id, ss.student.gender order by ss.standard.grade asc, ss.standard.section asc")
    List<StudentPerformanceDashboardVM> findStudentDashBoardDetailsBySchoolInfoId(Long schoolInfoId);

    @Query("select new com.witcurve.web.rest.vm.StandardStudentCountVM(ss.standard.id, count(ss.id))  from StudentStandard ss where ss.active=true and ss.student.schoolInfo.id=?1 group by ss.standard.id order by ss.standard.grade asc, ss.standard.section asc")
    List<StandardStudentCountVM> findStandardStudentCount(Long schoolInfoId);

    @Query("select new com.witcurve.web.rest.vm.StandardStudentCountVM(ss.standard.id, count(ss.id))  from StudentStandard ss where ss.active=true and ss.student.schoolInfo.id=?1 and ss.standard.id in ?2 group by ss.standard.id order by ss.standard.grade asc, ss.standard.section asc")
    List<StandardStudentCountVM> findStandardStudentCountByStandardIds(Long schoolInfoId, List<Long> standardIds);

}
