package com.witcurve.repository;

import com.witcurve.domain.Student;
import com.witcurve.domain.StudentStandard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentStandardRepository extends JpaRepository<StudentStandard, Long> {

    @Query("select ss from StudentStandard ss where ss.student.id = ?1 and ss.active = true")
    List<StudentStandard> getByStudentId(Long studentId);

    @Query("select ss from StudentStandard ss where ss.standard.id = ?1 and ss.active = true")
    List<StudentStandard> getByStandardId(Long standardId);

    @Query("select ss.student from StudentStandard ss where ss.standard.id = ?1 and ss.active = true")
    List<Student> getStudentsByStandardId(Long standardId);
}
