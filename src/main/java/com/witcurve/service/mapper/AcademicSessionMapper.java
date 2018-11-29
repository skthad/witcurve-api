package com.witcurve.service.mapper;

import com.witcurve.domain.AcademicSession;
import com.witcurve.service.dto.AcademicSessionDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {SchoolInfoMapper.class})
public interface AcademicSessionMapper extends EntityMapper<AcademicSessionDTO, AcademicSession>{

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


}
