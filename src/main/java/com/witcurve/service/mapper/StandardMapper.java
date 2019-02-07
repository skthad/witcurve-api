package com.witcurve.service.mapper;

import com.witcurve.domain.School;
import com.witcurve.domain.SchoolInfo;
import com.witcurve.domain.Standard;
import com.witcurve.service.dto.SchoolDTO;
import com.witcurve.service.dto.SchoolInfoDTO;
import com.witcurve.service.dto.StandardDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {StaffMapper.class, TermMapper.class})
public interface StandardMapper extends EntityMapper<StandardDTO, Standard> {

    @Mapping(target = "classTeacherId", source = "classTeacher.id")
    StandardDTO toDto(Standard standard);

    @Mapping(target = "classTeacher", source = "classTeacherId")
    Standard toEntity(StandardDTO standardDTO);

    default Standard fromId(Long id) {
        if(id == null) {
            return null;
        }
        Standard standard =  new Standard();
        standard.setId(id);
        return standard;

    }

    default SchoolInfo toSchoolInfo(SchoolInfoDTO schoolInfoDTO) {
        if (schoolInfoDTO == null) {
            return null;
        }
        SchoolInfo schoolInfo = new SchoolInfo();
        schoolInfo.setId(schoolInfoDTO.getId());

        if (schoolInfoDTO.getSchool() != null) {
            School school = new School();
            school.setId(schoolInfoDTO.getSchool().getId());
            schoolInfo.setSchool(school);
        }

        return schoolInfo;
    }

    default SchoolInfoDTO toSchoolInfoDTO(SchoolInfo schoolInfo) {
        if (schoolInfo == null) {
            return null;
        }
        SchoolInfoDTO schoolInfoDTO = new SchoolInfoDTO();
        schoolInfoDTO.setId(schoolInfo.getId());

        if (schoolInfo.getSchool() != null) {
            SchoolDTO school = new SchoolDTO();
            school.setId(schoolInfo.getSchool().getId());
            schoolInfoDTO.setSchool(school);
        }

        return schoolInfoDTO;
    }

}
