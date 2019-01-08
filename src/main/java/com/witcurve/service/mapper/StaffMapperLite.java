package com.witcurve.service.mapper;

import com.witcurve.domain.School;
import com.witcurve.domain.SchoolInfo;
import com.witcurve.domain.Staff;
import com.witcurve.domain.User;
import com.witcurve.service.dto.SchoolDTO;
import com.witcurve.service.dto.SchoolInfoDTO;
import com.witcurve.service.dto.StaffDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {StandardMapper.class})
public interface StaffMapperLite extends EntityMapper<StaffDTO, Staff> {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.login")
    StaffDTO toDto(Staff staff);

    @Mapping(target = "user.id", source = "userId")
    Staff toEntity(StaffDTO staffDTO);

    default Staff fromId(Long id) {
        if(id == null) {
            return null;
        }
        Staff staff = new Staff();
        staff.setId(id);
        return  staff;
    }

    default User fromUserId(Long id) {
        if(id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return  user;
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

