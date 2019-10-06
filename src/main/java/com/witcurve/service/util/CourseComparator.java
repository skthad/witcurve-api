package com.witcurve.service.util;

import com.witcurve.domain.Course;
import com.witcurve.domain.enumeration.CourseType;

import java.util.Comparator;

public class CourseComparator implements Comparator<Course> {

    @Override
    public int compare(Course o1, Course o2) {
        Boolean scolasticCourseO1 = o1.getCourseType().equals(CourseType.SCHOLASTIC);
        Boolean scolasticCourseO2 = o2.getCourseType().equals(CourseType.SCHOLASTIC);
        if(scolasticCourseO1 && !scolasticCourseO2) {
            return 1;
        } else if(!scolasticCourseO1 && scolasticCourseO2) {
            return -1;
        } else if(!scolasticCourseO1 && !scolasticCourseO2){
            return o1.getDisplayName().compareTo(o2.getDisplayName());
        } else {
            if((o1.getElective() && o2.getElective()) || (!o1.getElective() && !o2.getElective())) {
                return o1.getDisplayName().compareTo(o2.getDisplayName());
            } else if(o1.getElective() && !o2.getElective()) {
                return 1;
            } else {
                return -1;
            }
        }

    }

}
