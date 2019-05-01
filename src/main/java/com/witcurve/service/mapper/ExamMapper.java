package com.witcurve.service.mapper;

import com.witcurve.domain.Exam;
import com.witcurve.domain.GeneralSlotDetails;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.service.dto.ExamDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

@Mapper(componentModel = "spring", uses = {SchoolInfoMapper.class})
public interface ExamMapper extends EntityMapper<ExamDTO, Exam> {

    @Mapping(source = "schoolInfoId", target = "schoolInfo")
    Exam toEntity(ExamDTO examDTO);

    @Mapping(target = "schoolInfoId", source = "schoolInfo.id")
    @Mapping(target = "grades", expression = "java(getGrades(exam.getGeneralSlotDetails()))")
    ExamDTO toDto(Exam exam);


    default Exam fromId(Long id) {
        if(id == null) {
            return null;
        }
        Exam exam = new Exam();
        exam.setId(id);
        return exam;
    }

    default Set<Grade> getGrades(Set<GeneralSlotDetails> examGsds) {
        if(examGsds == null || examGsds.size()==0) {
            return null;
        }
        SortedSet<Grade> grades = new TreeSet<>();
        for(GeneralSlotDetails generalSlotDetails : examGsds) {
            grades.add(generalSlotDetails.getGrade());
        }
        return grades;
     }
}
