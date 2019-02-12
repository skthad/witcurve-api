package com.witcurve.service.mapper;

import com.witcurve.domain.Staff;
import com.witcurve.domain.User;
import com.witcurve.service.dto.StaffDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {StandardMapper.class})
public interface StaffMapperLite extends EntityMapper<StaffDTO, Staff> {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.login")
    @Mapping(target = "active", source = "user.activated")
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
}

