package com.witcurve.service.impl;

import com.witcurve.domain.LeaveApplication;
import com.witcurve.domain.Message;
import com.witcurve.domain.MessageThread;
import com.witcurve.domain.Staff;
import com.witcurve.domain.enumeration.MessageType;
import com.witcurve.repository.MessageRepository;
import com.witcurve.repository.MessageThreadRepository;
import com.witcurve.service.MessageThreadService;
import com.witcurve.service.dto.MessageDTO;
import com.witcurve.service.dto.MessageThreadDTO;
import com.witcurve.service.mapper.MessageMapper;
import com.witcurve.service.mapper.MessageThreadMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    public MessageDTO saveMessage(MessageDTO messageDTO) throws WitcurveException {
        log.debug("Request to save the message  : {}", messageDTO);
        Message message = messageMapper.toEntity(messageDTO);
        message = messageRepository.save(message);
        return messageMapper.toDto(message);
    }

    public MessageThreadDTO getMessageThreadById(Long messageThreadId) throws WitcurveException {
        log.debug("Request to find a mesage thread with id : {}", messageThreadId);
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
