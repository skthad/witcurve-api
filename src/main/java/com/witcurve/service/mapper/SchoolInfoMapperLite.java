package com.witcurve.service.mapper;

import com.witcurve.domain.School;
import com.witcurve.domain.SchoolInfo;
import com.witcurve.service.dto.SchoolDTO;
import com.witcurve.service.dto.SchoolInfoDTO;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring")
public interface SchoolInfoMapperLite extends EntityMapper<SchoolInfoDTO, SchoolInfo> {

    SchoolInfoDTO toDto(SchoolInfo schoolInfo);

    SchoolInfo toEntity(SchoolInfoDTO schoolInfoDTO);

    default School toSchool(SchoolDTO schoolDTO) {
        School school = new School();
        school.setId(schoolDTO.getId());

        return school;
    }

    default SchoolDTO toSchoolDTO(School school) {
        SchoolDTO schoolDTO = new SchoolDTO();
        schoolDTO.setId(school.getId());

        return schoolDTO;
    }

}
