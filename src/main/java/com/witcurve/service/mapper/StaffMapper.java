package com.witcurve.service.mapper;

import com.witcurve.domain.Staff;
import com.witcurve.service.dto.StaffDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SchoolInfoMapper.class})
public interface StaffMapper extends EntityMapper<StaffDTO, Staff> {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.login")
    @Mapping(ignore = true, target = "active")
    @Mapping(ignore = true, target = "hasPassword")
    @Mapping(ignore = true, target = "subjects")
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
}

