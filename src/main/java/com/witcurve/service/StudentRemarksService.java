package com.witcurve.service;

import com.witcurve.service.dto.StudentRemarksDTO;

import java.util.List;

public interface StudentRemarksService {

    List<StudentRemarksDTO> saveOrUpdate(List<StudentRemarksDTO> studentRemarksDTOs, Long examId, String bindingId);

    List<StudentRemarksDTO> getByBindingId(String bindingId);

    List<StudentRemarksDTO> getByExamId(Long examId);

    void deleteStudentMarks(List<Long> ids);


}
