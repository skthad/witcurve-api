package com.witcurve.service;

import com.witcurve.domain.enumeration.ApprovalStatus;
import com.witcurve.domain.enumeration.MessageType;
import com.witcurve.service.dto.MessageDTO;
import com.witcurve.service.dto.MessageThreadDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface MessageThreadService {

    MessageThreadDTO saveOrUpdate(MessageThreadDTO messageThreadDTO) throws WitcurveException;

    MessageThreadDTO getMessageThreadById(Long messageThreadId) throws WitcurveException;

    void approveOrRejectMessageThread(Long threadId, Long staffId, ApprovalStatus status) throws WitcurveException;

    MessageThreadDTO replyMessage(MessageDTO messageDTO) throws WitcurveException;

    void readMessageService(Long messageId) throws WitcurveException;

    Page<MessageThreadDTO> getInboxMessageThreadsByUserId(Pageable pageable,
                                                          Long userId,
                                                          MessageType messageType,
                                                          ApprovalStatus status,
                                                          Boolean read) throws WitcurveException;

    Map<MessageType, Integer> unReadCount(Long userId) throws WitcurveException;

    Page<MessageThreadDTO> getOutboxMessageThreadsByUserId(Pageable pageable,
                                                           Long userId,
                                                           MessageType messageType,
                                                           ApprovalStatus status) throws WitcurveException;

    void deleteMessage(Long messageId);

    void deleteMessageThread(Long messageThreadId);
}
