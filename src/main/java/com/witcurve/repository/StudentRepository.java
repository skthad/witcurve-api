package com.witcurve.repository;

import com.witcurve.domain.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("select student from Student student where student.user.id = ?1")
    Student getStudentByUserId(Long userId);

    @Query("select student from Student student where student.schoolInfo.id = ?1 and student.user.id in (select id from User where activated = true) and lower(student.admissionId) = ?2")
    Student findBySchoolInfoIdAndAdmissionId(Long schoolInfoId, String admissionId);

    @Query("select s from Student s where s.schoolInfo.id = ?1 and s.user.id in (select id from User where activated = true) and s.id not in (select distinct ss.student.id from StudentStandard ss where ss.student.schoolInfo.id = ?1 and ss.active = true)")
    List<Student> getUnallocatedStudentsBySchoolInfoId(Long schoolInfoId);

    @Query("select s from Student s where s.schoolInfo.id = ?1 and s.user.activated=false")
    List<Student> getInactiveStudentsBySchoolInfoId(Long schoolInfoId);

    @Query("select distinct student.registeredMobileNumber from Student student where student.id in ?1")
    Set<String> getPhoneNumbersByStudentIds(Set<Long> studentIds);

    @Query("select distinct student.registeredMobileNumber from Student student where student.schoolInfo.id = ?1 and student.id in ?2")
    Set<String> getPhoneNumbersBySchoolInfoAndStudentIds(Long schoolInfoId, List<Long> studentIds);

    @Query("select distinct student.email from Student student where student.schoolInfo.id = ?1 and student.id in ?2 and student.email is not null")
    Set<String> getEmailsBySchoolInfoAndStudentIds(Long schoolInfoId, List<Long> studentIds);

    @Query("select student from Student student where student.schoolInfo.school.institute.id = ?1")
    List<Student> getStudentsByInstituteId(Long instituteId);

    @Query("select ss.student from StudentStandard ss where ss.standard.id = ?1 and ss.active = true order by ss.rollNo")
    List<Student> getStudentsByStandardId(Long standardId);

    @Query("select ss.student from StudentStandard ss where ss.standard.id = ?1 and ss.active = true order by ss.rollNo")
    Page<Student> getStudentsByStandardId(Long standardId, Pageable pageable);

    @Query("select sc.studentStandard.student from StudentCourse sc where sc.studentStandard.standard.id = ?1 and sc.course.id=?2 and sc.active=true order by sc.studentStandard.rollNo")
    List<Student> getStudentsByStandardIdAndCourseId(Long standardId, Long courseId);

    @Query("select student from Student student where student.schoolInfo.school.institute.id = ?1 and student.admissionId = ?2 ")
    Student getByInstituteIdAndAdmissionId(Long instituteId, String admissionId);
}
