package com.witcurve.repository;

import com.witcurve.domain.Student;
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

    @Query("select distinct student.registeredMobileNumber from Student student where student.schoolInfo.id = ?1 and student.id in ?2")
    Set<String> getPhoneNumbersBySchoolInfoAndStudentIds(Long schoolInfoId, List<Long> studentIds);

    @Query("select distinct student.email from Student student where student.schoolInfo.id = ?1 and student.id in ?2 and student.email is not null")
    Set<String> getEmailsBySchoolInfoAndStudentIds(Long schoolInfoId, List<Long> studentIds);

    @Query("select student from Student student where student.schoolInfo.school.institute.id = ?1")
    List<Student> getStudentsByInstituteId(Long instituteId);

}
