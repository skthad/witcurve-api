package com.witcurve.service.mapper;

import com.witcurve.domain.Message;
import com.witcurve.domain.MessageThread;
import com.witcurve.service.dto.MessageThreadDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring", uses = {MessageMapper.class, LeaveApplicationMapper.class,
CourseTeacherMapper.class, GuardianMapper.class})
public interface MessageThreadMapper extends EntityMapper<MessageThreadDTO, MessageThread> {

    @Mapping(source = "messages", target = "messageDTOs")
    @Mapping(source = "fromUser.id", target = "fromUserId")
    @Mapping(source = "toUser.id", target = "toUserId")
    @Mapping(source = "courseTeacher", target = "courseTeacherDTO")
    @Mapping(source = "leaveApplication", target = "leaveApplicationDTO")
    @Mapping(source = "guardian.id", target = "guardianId")
    @Mapping(target = "read", expression = "java(isThreadFullyRead(messageThread))")
    MessageThreadDTO toDto(MessageThread messageThread);

    @Mapping(source = "messageDTOs", target = "messages")
    @Mapping(source = "fromUserId", target = "fromUser")
    @Mapping(source = "toUserId", target = "toUser")
    @Mapping(source = "courseTeacherDTO", target = "courseTeacher")
    @Mapping(source = "leaveApplicationDTO", target = "leaveApplication")
    @Mapping(source = "guardianId", target = "guardian")
    MessageThread toEntity(MessageThreadDTO messageThreadDTO);

    default MessageThread fromId(Long id) {
        if(id == null) {
            return null;
        }
        MessageThread messageThread = new MessageThread();
        messageThread.setId(id);
        return messageThread;
    }

    default Boolean isThreadFullyRead(MessageThread messageThread) {
        Boolean result = true;
        Set<Message> messageSet = messageThread.getMessages();
        for(Message message : messageSet) {
            if(!message.getRead()) {
                result = false;
                break;
            }
        }
        return result;
    }
}
