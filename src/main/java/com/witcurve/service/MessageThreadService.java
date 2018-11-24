package com.witcurve.service;

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

    void approveMessageThread(Long threadId, Long staffId) throws WitcurveException;

    MessageThreadDTO replyMessage(MessageDTO messageDTO) throws WitcurveException;

    void readMessageService(Long messageId) throws WitcurveException;

    Page<MessageThreadDTO> getInboxMessageThreadsByUserId(Pageable pageable,
                                                          Long userId,
                                                          MessageType messageType,
                                                          Boolean approved,
                                                          Boolean read) throws WitcurveException;

    Map<MessageType, Integer> unReadCount(Long userId) throws WitcurveException;

    Page<MessageThreadDTO> getOutboxMessageThreadsByUserId(Pageable pageable,
                                                           Long userId,
                                                           MessageType messageType,
                                                           Boolean approved) throws WitcurveException;
}
