package com.witcurve.service.mapper;

import com.witcurve.domain.MessageThread;
import com.witcurve.domain.User;
import com.witcurve.domain.enumeration.UserType;
import com.witcurve.service.dto.MessageThreadDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MessageMapper.class, LeaveApplicationMapper.class,
CourseTeacherMapper.class, GuardianMapper.class})
public interface MessageThreadMapper extends EntityMapper<MessageThreadDTO, MessageThread> {

    @Mapping(source = "messages", target = "messageDTOs")
    @Mapping(source = "fromUser.id", target = "fromUserId")
    @Mapping(source = "toUser.id", target = "toUserId")
    @Mapping(source = "courseTeacher", target = "courseTeacherDTO")
    @Mapping(source = "leaveApplication", target = "leaveApplicationDTO")
    @Mapping(target = "fromUserName", expression = "java(getUserName(messageThread.getFromUser()))")
    @Mapping(target = "toUserName", expression = "java(getUserName(messageThread.getToUser()))")
    @Mapping(target = "userType", expression = "java(getUserType(messageThread))")
    MessageThreadDTO toDto(MessageThread messageThread);

    @Mapping(source = "messageDTOs", target = "messages")
    @Mapping(source = "fromUserId", target = "fromUser")
    @Mapping(source = "toUserId", target = "toUser")
    @Mapping(source = "courseTeacherDTO", target = "courseTeacher")
    @Mapping(source = "leaveApplicationDTO", target = "leaveApplication")
    MessageThread toEntity(MessageThreadDTO messageThreadDTO);

    default MessageThread fromId(Long id) {
        if(id == null) {
            return null;
        }
        MessageThread messageThread = new MessageThread();
        messageThread.setId(id);
        return messageThread;
    }

    default String getUserName(User user) {
        if(user == null) {
            return null;
        } else {
            String firstName = user.getFirstName();
            String lastName = user.getLastName();
            if(firstName == null && lastName == null) {
                return  null;
            } else if(firstName != null && lastName == null) {
                return firstName;
            } else if(firstName == null && lastName != null) {
                return  lastName;
            } else {
                return firstName + " "+ lastName;
            }
        }
    }

    default String getUserType(MessageThread messageThread) {
        if(messageThread.getSchoolBoardAdminMessage()) {
            if(messageThread.getFromUser().getType() != null) {
                if (messageThread.getFromUser().getType().equals(UserType.TEACHING_STAFF)) {
                    return "Teacher";
                } else if (messageThread.getFromUser().getType().equals(UserType.PARENT)) {
                    return "Parent";
                }
            } else {
                return null;
            }
            if (messageThread.getToUser() != null) {
                if(messageThread.getToUser().getType() != null) {
                    if (messageThread.getToUser().getType().equals(UserType.TEACHING_STAFF)) {
                        return "Teacher";
                    } else if (messageThread.getToUser().getType().equals(UserType.PARENT)) {
                        return "Parent";
                    }
                }
            }
            return null;
        } else {
            return null;
        }

    }
}
