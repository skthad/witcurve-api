package com.witcurve.service.mapper;

import com.witcurve.domain.SchoolInfo;
import com.witcurve.service.dto.SchoolInfoDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses={SchoolMapper.class})
public interface SchoolInfoMapper {

    SchoolInfoDTO schoolInfoToSchoolInfoDTO(SchoolInfo schoolInfo);

    SchoolInfo schoolInfoDTOToSchoolInfo(SchoolInfoDTO schoolInfoDTO);

    List<SchoolInfoDTO> schoolInfosToSchoolInfoDTOs(List<SchoolInfo> schoolInfos);

    List<SchoolInfo> schoolInfoDTOsToSchoolInfos(List<SchoolInfoDTO> schoolInfoDTOS);


}
