package com.witcurve.service;

import com.witcurve.domain.StudentStandard;
import com.witcurve.service.dto.StudentDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StudentService {

    StudentDTO create(StudentDTO studentDTO);

    StudentDTO update(StudentDTO studentDTO) throws WitcurveException;

    StudentDTO getStudentById(Long studentId) throws WitcurveException;

    StudentDTO getStudentByUserId(Long userId) throws WitcurveException;

    List<StudentDTO> getStudentsByStandardIdAndCourseId(Long standardId, Long courseId) throws WitcurveException;

    List<StudentDTO> getUnAllocatedStudentsBySchoolInfoId(Long schoolInfoId);

    List<StudentDTO> getInActiveStudentsBySchoolInfoId(Long schoolInfoId);

    StudentDTO getStudentByUsername(String username) throws WitcurveException;

    void deactivate(Long studentId);

    void activate(Long studentId);

    void mapStudentsInNonElectiveCourses(StudentStandard studentStandard);

    void mapStudentsInNonElectiveCourses(List<StudentStandard> studentStandards, Long standardId);

    void mapUnmapStudentAndCourse(Long studentStandardId, Long courseId, Boolean map);

    void mapOneTime(Long schoolInfoId);

    StudentDTO addProfilePhoto(Long studentId, MultipartFile file);
}
