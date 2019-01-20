package com.witcurve.service.mapper;

import com.witcurve.domain.AcademicSession;
import com.witcurve.domain.School;
import com.witcurve.domain.SchoolInfo;
import com.witcurve.service.dto.AcademicSessionDTO;
import com.witcurve.service.dto.SchoolDTO;
import com.witcurve.service.dto.SchoolInfoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AcademicSessionMapperLite extends EntityMapper<AcademicSessionDTO, AcademicSession>{

    @Mapping(target = "termsInSession", ignore = true)
    AcademicSessionDTO toDto(AcademicSession academicSession);

    AcademicSession toEntity(AcademicSessionDTO academicSessionDTO);

    default AcademicSession fromId(Long id) {
        if(id == null) {
            return  null;
        }
        AcademicSession academicSession = new AcademicSession();
        academicSession.setId(id);
        return academicSession;
    }

    default SchoolInfo toSchoolInfo(SchoolInfoDTO schoolInfoDTO) {
        SchoolInfo schoolInfo = new SchoolInfo();
        schoolInfo.setId(schoolInfoDTO.getId());

        School school = new School();
        school.setId(schoolInfoDTO.getSchool().getId());
        schoolInfo.setSchool(school);

        return schoolInfo;
    }

    default SchoolInfoDTO toSchoolInfoDTO(SchoolInfo schoolInfo) {
        SchoolInfoDTO schoolInfoDTO = new SchoolInfoDTO();
        schoolInfoDTO.setId(schoolInfo.getId());

        SchoolDTO school = new SchoolDTO();
        school.setId(schoolInfo.getSchool().getId());
        schoolInfoDTO.setSchool(school);

        return schoolInfoDTO;
    }


}
