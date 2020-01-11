package com.witcurve.service.impl;

import com.witcurve.domain.Course;
import com.witcurve.domain.ReportCardDesign;
import com.witcurve.domain.enumeration.CourseType;
import com.witcurve.domain.enumeration.ReportFieldType;
import com.witcurve.repository.CourseRepository;
import com.witcurve.repository.ReportCardDesignRepository;
import com.witcurve.service.dto.CourseGradeDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseGradeServiceImpl {

    private final Logger log  = LoggerFactory.getLogger(CourseGradeServiceImpl.class);

    @Autowired
    ReportCardDesignRepository reportCardDesignRepository;

    @Autowired
    CourseRepository courseRepository;

    @Override
    List<CourseGradeDTO> saveOrUpdateCourseGrade(List<CourseGradeDTO> courseGradeDTOs, Long rcdId, Long courseId){
        log.debug("Request to save student grade");
        courseGradeDTOs = validateAndFormatStudentGrade(courseGradeDTOs, rcdId, courseId);



        return null;
    }
    private List<CourseGradeDTO> validateAndFormatStudentGrade(List<CourseGradeDTO> courseGradeDTOs, Long rcdId, Long courseId){
        Optional<ReportCardDesign> reportCardDesign = reportCardDesignRepository.findById(rcdId);
        if(!reportCardDesign.isPresent()){
            throw new WitcurveException("There is no report card design with given id");
        }
        if(!reportCardDesign.get().getFieldType().equals(ReportFieldType.NON_SCHOLASTIC)){
            throw new WitcurveException("Report card design type should be non scholastic type");
        }
        Optional<Course> course = courseRepository.findById(courseId);
        if(!course.isPresent() || !course.get().getActive() || !course.get().getCourseType().equals(CourseType.NON_SCHOLASTIC) ) {
            throw new WitcurveException("This is not a valid course, only active non scholastic courses are allowed");
        }

    }
}
