package com.witcurve.service.impl;

import com.witcurve.domain.*;
import com.witcurve.domain.enumeration.ApprovalStatus;
import com.witcurve.domain.enumeration.MessageType;
import com.witcurve.domain.enumeration.UserType;
import com.witcurve.repository.*;
import com.witcurve.service.MessageThreadService;
import com.witcurve.service.SnsService;
import com.witcurve.service.dto.MessageDTO;
import com.witcurve.service.dto.MessageThreadDTO;
import com.witcurve.service.mapper.MessageMapper;
import com.witcurve.service.mapper.MessageThreadMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class MessageThreadServiceImpl implements MessageThreadService {

    private final Logger log = LoggerFactory.getLogger(MessageThreadServiceImpl.class);

    @Autowired
    MessageThreadRepository messageThreadRepository;

    @Autowired
    MessageThreadMapper messageThreadMapper;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    MessageMapper messageMapper;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    StudentStandardRepository studentStandardRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SnsService snsService;

    public MessageThreadDTO saveOrUpdate(MessageThreadDTO messageThreadDTO) throws WitcurveException {
        log.debug("Request to save or update message thread : {}", messageThreadDTO);
        isValidMessageThread(messageThreadDTO);
        List<MessageDTO> messageDTOs = new ArrayList<>(messageThreadDTO.getMessageDTOs());
        MessageDTO messageDTO = messageDTOs.get(0);
        messageThreadDTO.setMessageDTOs(null);
        MessageThread messageThread = messageThreadMapper.toEntity(messageThreadDTO);
        messageThread = messageThreadRepository.save(messageThread);
        messageDTO.setMessageThreadId(messageThread.getId());
        Message message = messageMapper.toEntity(messageDTO);
        message = messageRepository.save(message);
        messageThread.setFromUserLastMessageDate(message.getCreatedDate());
        Set<Message> messageSet = new HashSet<>();
        messageSet.add(message);
        messageThread.setMessages(messageSet);
        log.info("message is generated");
        snsService.sendPushNotification(messageMapper.toDto(message), messageThreadDTO);
        return messageThreadMapper.toDto(messageThread);

    }

    public MessageThreadDTO replyMessage(MessageDTO messageDTO) throws WitcurveException {
        log.debug("Request to save a reply message : {}", messageDTO);

        Message message = messageMapper.toEntity(messageDTO);
        message = messageRepository.save(message);
        Optional<MessageThread> messageThread = messageThreadRepository.findById(messageDTO.getMessageThreadId());
        if(!messageThread.isPresent()) {
            throw new WitcurveException("No Message Thread exists with given Id");
        }
        messageThread.get().addMessage(message);
        if(messageThread.get().getToUser().equals(message.getToUser())) {
            messageThread.get().setFromUserLastMessageDate(message.getCreatedDate());
            messageThread.get().setToUserUnreadCount(messageThread.get().getToUserUnreadCount()+1);
        } else {
            messageThread.get().setToUserLastMessageDate(message.getCreatedDate());
            messageThread.get().setFromUserUnreadCount(messageThread.get().getFromUserUnreadCount()+1);
        }
        MessageThreadDTO messageThreadDTO = messageThreadMapper.toDto(messageThread.get());
        snsService.sendPushNotification(messageMapper.toDto(message), messageThreadDTO);
        return messageThreadDTO;
    }

    public MessageThreadDTO getMessageThreadById(Long messageThreadId) throws WitcurveException {
        log.debug("Request to find a message thread with id : {}", messageThreadId);
        Optional<MessageThread> messageThread = messageThreadRepository.findById(messageThreadId);
        if(!messageThread.isPresent()) {
            throw new WitcurveException("No Message Thread exists with given Id");
        }
        return messageThreadMapper.toDto(messageThread.get());
    }

    public  void approveOrRejectMessageThread(Long threadId, Long staffId, ApprovalStatus status) throws WitcurveException {
        log.debug("Change status of meeting request with id : {} of status : {}", threadId, status);
        Optional<MessageThread> messageThread = messageThreadRepository.findById(threadId);
        if (!messageThread.isPresent()) {
            throw new WitcurveException("No message thread with given id");
        }
        MessageThread toBeApproved = messageThread.get();
        if(messageThread.get().getMessageType().equals(MessageType.MEETING_REQUEST) || messageThread.get().getMessageType().equals(MessageType.LEAVE) )
        {
            toBeApproved.setStatus(status);
            snsService.sendPushNotificationOnStatusOfMeetingRequestChange(messageThreadMapper.toDto(toBeApproved));
            if(messageThread.get().getMessageType().equals(MessageType.LEAVE)) {
                if(ApprovalStatus.APPROVED.equals(status)) {
                    LeaveApplication leaveApplication = messageThread.get().getLeaveApplication();
                    leaveApplication.setStatus(status);
                    Staff staff = new Staff();
                    staff.setId(staffId);
                    leaveApplication.setApprovedBy(staff);
                }
            }
        } else
        {
            throw new WitcurveException("Thread approval is valid only for leave and meeting request");
        }
    }

    public void readMessageService(Long messageId) throws WitcurveException {
        log.debug("Read message is set for the message id {}", messageId);
        Optional<Message> message= messageRepository.findById(messageId);
        if(!message.isPresent()) {
            throw new WitcurveException("No Message exists with given Id");
        }
        if(!message.get().getRead()) {
            message.get().setRead(true);
            Optional<MessageThread> messageThreadOptional = messageThreadRepository.findById(message.get().getMessageThread().getId());
            if(!messageThreadOptional.isPresent()) {
                throw new WitcurveException("No Message Thread exists with given Id");
            }
            if(messageThreadOptional.get().getToUser().equals(message.get().getToUser())) {
                messageThreadOptional.get().setToUserUnreadCount(messageThreadOptional.get().getToUserUnreadCount()-1<0 ? 0 : messageThreadOptional.get().getToUserUnreadCount()-1);
            } else {
                messageThreadOptional.get().setFromUserUnreadCount(messageThreadOptional.get().getFromUserUnreadCount()-1<0 ? 0 : messageThreadOptional.get().getFromUserUnreadCount()-1);
            }
        }
    }

    public Page<MessageThreadDTO> getInboxMessageThreadsByUserId(Pageable pageable,
                                                                 Boolean superAdminMessage,
                                                                 Boolean boardAdminMessage,
                                                                 Long userId,
                                                                 MessageType messageType,
                                                                 ApprovalStatus status,
                                                                 Boolean read) throws WitcurveException{
        log.debug("Get inbox list of inbox message threads for user with id : {} of " +
            "type : {} with status : {} and read : {}", userId, messageType, status, read);
        Page<MessageThread> messageThreads = null;
        if(boardAdminMessage) {
            if(read == null) {
                messageThreads = messageThreadRepository.findBoardAdminInboxMessageThreads(userId, messageType, pageable);
            } else {
                messageThreads = messageThreadRepository.findBoardAdminInboxMessageThreadsWithRead(userId, messageType, read, pageable);
            }
        } else if (superAdminMessage) {
            if(read == null) {
                messageThreads = messageThreadRepository.findSuperAdminInboxMessageThreads(userId, messageType, pageable);
            } else {
                messageThreads = messageThreadRepository.findSuperAdminInboxMessageThreadsWithRead(userId, messageType, read, pageable);
            }
        } else {
            if(messageType.equals(MessageType.SUBJECT_NOTE)) {
                Student student = studentRepository.getStudentByUserId(userId);
                if(student == null) {
                    throw new WitcurveException("No student exists for given user id to get subject note messages");
                }
                List<StudentStandard> studentStandards = studentStandardRepository.getByStudentId(student.getId());
                if(studentStandards.isEmpty()) {
                    throw new WitcurveException("There is no student standard with given student id : "+student.getId());
                }
                if (studentStandards.size() > 1) {
                    throw new WitcurveException("There are more than one active student standard with given student id : "+student.getId());
                }
                Long standardId = studentStandards.get(0).getStandard().getId();
                if(status == null && read==null) {
                    messageThreads = messageThreadRepository.findInboxMessageThreadsOfSubjectNote(standardId, messageType, pageable);
                } else if(status != null && read == null) {
                    messageThreads = messageThreadRepository.
                        findInboxMessageThreadsOfSubjectNoteWithStatus(standardId, messageType, status, pageable);
                } else {
                    throw new WitcurveException("Subject Note message doesn't support read filter");
                }
            } else {
                if(status == null && read==null) {
                    messageThreads = messageThreadRepository.findOtherInboxMessageThreads(userId, messageType, pageable);
                } else if(status != null && read == null) {
                    messageThreads = messageThreadRepository.
                        findOtherInboxMessageThreadsWithStatus(userId, messageType, status, pageable);
                } else if(status == null && read != null) {
                    messageThreads = messageThreadRepository.
                        findOtherInboxMessageThreadsWithRead(userId, messageType, read, pageable);
                } else {
                    messageThreads = messageThreadRepository.
                        findOtherInboxMessageThreadsWithStatusAndRead(userId, messageType, status, read, pageable);
                }
            }
        }
        return messageThreads.map(messageThreadMapper::toDto);
    }

    public Map<String, Integer> unReadCount(Long userId) throws WitcurveException {
        log.debug("Get inbox unread message threads counts for user with id : {}", userId);

        //refactor repo method to give details properly
        Map<String, Integer> result = new HashMap<>();
        Integer count = 0;
        Student student = studentRepository.getStudentByUserId(userId);
        if(student == null) {
            result.put(MessageType.SUBJECT_NOTE.toString(), null);
        } else {
            List<StudentStandard> studentStandards = studentStandardRepository.getByStudentId(student.getId());
            if(studentStandards.isEmpty()) {
                throw new WitcurveException("There is no student standard with given student id : "+student.getId());
            }
            if (studentStandards.size() > 1) {
                throw new WitcurveException("There are more than one active student standard with given student id : "+student.getId());
            }
            Long standardId = studentStandards.get(0).getStandard().getId();
            count = messageThreadRepository.findInboxMessageThreadsOfSubjectNoteCount(standardId, MessageType.SUBJECT_NOTE);
            result.put(MessageType.SUBJECT_NOTE.toString(), count);
        }
//        count = messageThreadRepository.findUnReadOtherInboxMessageThreadsCount(userId, MessageType.LEAVE);
//        result.put(MessageType.LEAVE, count);
        count = messageThreadRepository.findUnReadOtherInboxMessageThreadsCount(userId, MessageType.MEETING_REQUEST);
        result.put(MessageType.MEETING_REQUEST.toString(), count);
        count = messageThreadRepository.findUnReadOtherInboxMessageThreadsCount(userId, MessageType.PERSONAL);
        result.put(MessageType.PERSONAL.toString(), count);

        count = messageThreadRepository.findUnReadBoardAdminInboxMessageThreadsCount(userId, MessageType.PERSONAL);
        result.put("BOARD_ADMIN", count);

        count = messageThreadRepository.findUnReadSuperAdminInboxMessageThreadsCount(userId, MessageType.PERSONAL);
        result.put("SUPER_ADMIN", count);

        return result;
    }

    public Page<MessageThreadDTO> getOutboxMessageThreadsByUserId(Pageable pageable,
                                                                  Boolean superAdminMessage,
                                                                  Boolean boardAdminMessage,
                                                                  Long userId,
                                                                  MessageType messageType,
                                                                  ApprovalStatus status) throws WitcurveException {
        log.debug("Get inbox list of inbox message threads for user with id : {} of " +
            "type : {} with status : {}", userId, messageType, status);
        Page<MessageThread> messageThreads = null;
        if(boardAdminMessage) {
            messageThreads = messageThreadRepository.findBoardAdminOutboxMessageThreads(userId, messageType, pageable);
        } else if(superAdminMessage) {
            messageThreads = messageThreadRepository.findSuperAdminOutboxMessageThreads(userId, messageType, pageable);
        } else {
            if(messageType.equals(MessageType.SUBJECT_NOTE)) {
                Student student = studentRepository.getStudentByUserId(userId);
                if(student != null) {
                    throw new WitcurveException("Subject Note doesn't exist for student user ");
                }
            }
            if(status == null) {
                messageThreads = messageThreadRepository.findOutboxMessageThreads(userId, messageType, pageable);
            } else {
                messageThreads = messageThreadRepository.findOutboxMessageThreadsWithStatus(userId, messageType, status, pageable);
            }
        }
        return messageThreads.map(messageThreadMapper::toDto);
    }


    private void isValidMessageThread(MessageThreadDTO messageThreadDTO) throws WitcurveException{
        MessageType messageType = messageThreadDTO.getMessageType();
        List<MessageDTO> messageDTOList = new ArrayList<>(messageThreadDTO.getMessageDTOs());
        MessageDTO messageDTO = messageDTOList.get(0);
        if(messageType.equals(MessageType.LEAVE)) {
            if(messageThreadDTO.getToUserId() == null || messageThreadDTO.getLeaveApplicationDTO() == null
                || messageDTO.getToUserId() == null) {
                throw new WitcurveException("For Leave Type thread there should be toUserId and leaveApplicationDTO");
            }
        }
        if(messageType.equals(MessageType.MEETING_REQUEST)) {
            if(messageThreadDTO.getToUserId() == null || messageThreadDTO.getCourseTeacherDTO() == null
                || messageDTO.getToUserId() == null) {
                throw new WitcurveException("For Meeting Request Type thread there should be toUserId and courseTeacherDTO");
            }
            if(messageThreadDTO.getMeetingDate() == null || messageThreadDTO.getMeetingTime() == null) {
                throw new WitcurveException("For Meeting Request Type thread there should be meeting date and meeting time");
            }
            if(messageThreadDTO.getStatus() == null) {
                messageThreadDTO.setStatus(ApprovalStatus.PENDING);
            }
        }
        if(messageType.equals(MessageType.PERSONAL)) {
            if(messageThreadDTO.getToUserId() == null || messageDTO.getToUserId() == null) {
                throw new WitcurveException("For Personal Type thread there should be toUserId");
            }
            if(messageThreadDTO.getSuperAdminMessage()) {
                if(!(messageThreadDTO.getFromUserId() == 1L || messageThreadDTO.getToUserId() == 1L)) {
                    throw new WitcurveException("Please make sure super admin message is either from admin user or to admin user");
                }
            }
            if(messageThreadDTO.getSchoolBoardAdminMessage()) {
                Optional<User> toUser = userRepository.findById(messageThreadDTO.getToUserId());
                Optional<User> fromUser = userRepository.findById(messageThreadDTO.getFromUserId());
                if(!(toUser.get().getType().equals(UserType.SCHOOL_BOARD_MANAGER) || fromUser.get().getType().equals(UserType.SCHOOL_BOARD_MANAGER))) {
                    throw new WitcurveException("PLease make sure school board message should be either form school board user or to school board user ");
                }
            }
        }
        if(messageType.equals(MessageType.SUBJECT_NOTE)) {
            if(messageThreadDTO.getCourseTeacherDTO() == null) {
                throw new WitcurveException("For Subject Note Type thread there should be courseTeacherDTO");
            }
        }
    }

    public void deleteMessage(Long messageId) {
        log.debug("Request to delete message with id : {}",messageId);
        messageRepository.deleteById(messageId);
    }

    public void deleteMessageThread(Long messageThreadId) {
        log.debug("Request to delete message thread with id : {}",messageThreadId);
        messageRepository.deleteByMessageThreadId(messageThreadId);
        messageThreadRepository.deleteById(messageThreadId);
    }

}
