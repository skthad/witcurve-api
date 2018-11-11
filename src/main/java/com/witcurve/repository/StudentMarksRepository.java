package com.witcurve.repository;

import com.witcurve.domain.StudentMarks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentMarksRepository extends JpaRepository<StudentMarks, Long> {

    @Query("select sm from StudentMarks sm where sm.test.id = ?1")
    List<StudentMarks> getByTestId(Long testId);

    @Query("select sm from StudentMarks sm where sm.student.id = ?1 and sm.test.id = ?2")
    List<StudentMarks> getByStudentAndTestId(Long studentId, Long testId);

}

