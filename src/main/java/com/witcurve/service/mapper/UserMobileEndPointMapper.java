package com.witcurve.service.mapper;

import com.witcurve.domain.User;
import com.witcurve.domain.UserMobileEndPoint;
import com.witcurve.service.dto.UserMobileEndPointDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMobileEndPointMapper extends EntityMapper<UserMobileEndPointDTO, UserMobileEndPoint> {

    @Mapping(source = "user.id", target = "userId")
    UserMobileEndPointDTO toDto(UserMobileEndPoint userMobileEndPoint);

    @Mapping(source = "userId", target = "user")
    @Mapping(target = "endPoint", ignore = true)
    UserMobileEndPoint toEntity(UserMobileEndPointDTO userMobileEndPointDTO);

    default User userFromUserId(Long userId) {
        if(userId == null) {
            return null;
        }
        User user = new User();
        user.setId(userId);
        return user;
    }
}
