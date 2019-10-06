package com.witcurve.repository;

import com.witcurve.domain.StudentRemarks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRemarksRepository extends JpaRepository<StudentRemarks, Long> {

    @Query("Select sr from StudentRemarks sr where sr.bindingId = ?1")
    List<StudentRemarks> findByBindingId(String bindingId);

    @Query("Select sr from StudentRemarks sr where sr.exam.id = ?1")
    List<StudentRemarks> findByExamId(Long examId);

    @Query("Select sr from StudentRemarks sr where sr.exam.id = ?1 and sr.student.id = ?2")
    StudentRemarks findByExamIdAndStudentId(Long examId, Long studentId);

    @Modifying
    @Query("delete from StudentRemarks sr where sr.id in ?1")
    void deleteByIds(List<Long> ids);



}
