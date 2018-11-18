package com.witcurve.service.impl;

import com.witcurve.domain.*;
import com.witcurve.domain.enumeration.MessageType;
import com.witcurve.repository.MessageRepository;
import com.witcurve.repository.MessageThreadRepository;
import com.witcurve.repository.StudentRepository;
import com.witcurve.repository.StudentStandardRepository;
import com.witcurve.service.MessageThreadService;
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

import java.time.Instant;
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
        Set<Message> messageSet = new HashSet<>();
        messageSet.add(message);
        messageThread.setMessages(messageSet);
        return messageThreadMapper.toDto(messageThread);

    }

    public MessageThreadDTO replyMessage(MessageDTO messageDTO) throws WitcurveException {
        log.debug("Request to save a reply message : {}", messageDTO);
        Message message = messageMapper.toEntity(messageDTO);
        messageRepository.save(message);
        Optional<MessageThread> messageThread = messageThreadRepository.findById(messageDTO.getMessageThreadId());
        if(!messageThread.isPresent()) {
            throw new WitcurveException("No Message Thread exists with given Id");
        }
        return messageThreadMapper.toDto(messageThread.get());
    }

    public MessageThreadDTO getMessageThreadById(Long messageThreadId) throws WitcurveException {
        log.debug("Request to find a message thread with id : {}", messageThreadId);
        Optional<MessageThread> messageThread = messageThreadRepository.findById(messageThreadId);
        if(!messageThread.isPresent()) {
            throw new WitcurveException("No Message Thread exists with given Id");
        }
        return messageThreadMapper.toDto(messageThread.get());
    }


    public void approveMessageThread(Long threadId, Long staffId) throws WitcurveException {
        log.debug("Approval for meeting request with id {}", threadId);
        Optional<MessageThread> messageThread = messageThreadRepository.findById(threadId);
        if (!messageThread.isPresent()) {
            throw new WitcurveException("No message thread with given id");
        }
        MessageThread toBeApproved = messageThread.get();
        if(messageThread.get().getMessageType().equals(MessageType.MEETING_REQUEST) || messageThread.get().getMessageType().equals(MessageType.LEAVE) )
        {
            toBeApproved.setApproved(true);
            if(messageThread.get().getMessageType().equals(MessageType.LEAVE)) {
                LeaveApplication leaveApplication = messageThread.get().getLeaveApplication();
                leaveApplication.setApproved(true);
                Staff staff = new Staff();
                staff.setId(staffId);
                leaveApplication.setApprovedBy(staff);
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
        message.get().setRead(true);
    }

    public Page<MessageThreadDTO> getInboxMessageThreadsByUserId(Pageable pageable,
                                                                 Long userId,
                                                                 MessageType messageType,
                                                                 Boolean approved,
                                                                 Boolean read) throws WitcurveException{
        log.debug("Get inbox list of inbox message threads for user with id : {} of " +
            "type : {} with approved : {} and read : {}", userId, messageType, approved, read);
        Page<MessageThread> messageThreads = null;
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
            if(approved == null && read == null) {
                messageThreads = messageThreadRepository.findInboxMessageThreadsOfSubjectNote(standardId, messageType, pageable);
            } else if(approved != null && read == null) {
                messageThreads = messageThreadRepository.
                    findInboxMessageThreadsOfSubjectNoteWithApproved(standardId, messageType, approved, pageable);
            } else if(approved == null && read !=null) {
                if(read) {
                    messageThreads = messageThreadRepository.
                        findReadInboxMessageThreadsOfSubjectNote(standardId, messageType, pageable);
                } else {
                    messageThreads = messageThreadRepository.
                        findUnReadInboxMessageThreadsOfSubjectNote(standardId, messageType, pageable);
                }
            } else {
                if(read) {
                    messageThreads = messageThreadRepository.
                        findReadInboxMessageThreadsOfSubjectNoteWithApproved(standardId, messageType, approved, pageable);
                } else {
                    messageThreads = messageThreadRepository.
                        findUnReadInboxMessageThreadsOfSubjectNoteWithApproved(standardId, messageType, approved, pageable);
                }
            }

        } else {
            if(approved == null && read == null) {
                messageThreads = messageThreadRepository.findInboxMessageThreads(userId, messageType, pageable);
            } else if(approved != null && read == null) {
                messageThreads = messageThreadRepository.
                    findInboxMessageThreadsWithApproved(userId, messageType, approved, pageable);
            } else if(approved == null && read !=null) {
                if(read) {
                    messageThreads = messageThreadRepository.findReadInboxMessageThreads(userId, messageType, pageable);
                } else {
                    messageThreads = messageThreadRepository.findUnReadInboxMessageThreads(userId, messageType, pageable);
                }
            } else {
                if(read) {
                    messageThreads = messageThreadRepository.
                        findReadInboxMessageThreadsWithApproved(userId, messageType, approved, pageable);
                } else {
                    messageThreads = messageThreadRepository.
                        findUnReadInboxMessageThreadsWithApproved(userId, messageType, approved, pageable);
                }
            }
        }

        return messageThreads.map(messageThreadMapper::toDto);
    }

    public Page<MessageThreadDTO> getOutboxMessageThreadsByUserId(Pageable pageable,
                                                                  Long userId,
                                                                  MessageType messageType,
                                                                  Boolean approved) throws WitcurveException {
        log.debug("Get inbox list of inbox message threads for user with id : {} of " +
            "type : {} with approved : {}", userId, messageType, approved);
        Page<MessageThread> messageThreads = null;
        if(messageType.equals(MessageType.SUBJECT_NOTE)) {
            Student student = studentRepository.getStudentByUserId(userId);
            if(student != null) {
                throw new WitcurveException("Subject Note doesn't exist for student user ");
            }
        }
        if(approved == null) {
            messageThreads = messageThreadRepository.findOutboxMessageThreads(userId, messageType, pageable);
        } else {
            messageThreads = messageThreadRepository.findOutboxMessageThreadsWithApproved(userId, messageType, approved, pageable);
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
        }
        if(messageType.equals(MessageType.PERSONAL)) {
            if(messageThreadDTO.getToUserId() == null || messageDTO.getToUserId() == null) {
                throw new WitcurveException("For Personal Type thread there should be toUserId");
            }
        }
        if(messageType.equals(MessageType.SUBJECT_NOTE)) {
            if(messageThreadDTO.getCourseTeacherDTO() == null) {
                throw new WitcurveException("For Subject Note Type thread there should be courseTeacherDTO");
            }
        }
    }

}
