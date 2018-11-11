package com.witcurve.service.mapper;

import com.witcurve.domain.Message;
import com.witcurve.domain.User;
import com.witcurve.service.dto.MessageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MessageThreadMapper.class})
public interface MessageMapper extends EntityMapper<MessageDTO, Message> {

    @Mapping(source = "messageThread.id", target = "messageThreadId")
    @Mapping(source = "fromUser.id", target = "fromUserId")
    @Mapping(source = "toUser.id", target = "toUserId")
    MessageDTO toDto(Message message);

    @Mapping(source = "messageThreadId", target = "messageThread")
    @Mapping(source = "fromUserId", target = "fromUser")
    @Mapping(source = "toUserId", target = "toUser")
    Message toEntity(MessageDTO messageDTO);

    default Message fromId(Long id) {
        if(id == null) {
            return null;
        }
        Message message = new Message();
        message.setId(id);
        return message;
    }

    default User userFromUserId(Long userId) {
        if(userId == null) {
            return null;
        }
        User user = new User();
        user.setId(userId);
        return user;
    }

}
