package com.witcurve.repository;

import com.witcurve.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("select student from Student student where student.user.id = ?1")
    Student getStudentByUserId(Long userId);

    @Query("select student from Student student left join fetch student.user usr where student.schoolInfo.id = ?1 and lower(student.admissionId) = ?2")
    Student findBySchoolInfoIdAndAdmissionId(Long schoolInfoId, String admissionId);

    @Query("select student from Student student where student.schoolInfo.school.id = ?1")
    List<Student> findBySchoolId(Long schoolId);
}
