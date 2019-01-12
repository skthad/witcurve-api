package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.enumeration.ApprovalStatus;
import com.witcurve.domain.enumeration.MessageType;
import com.witcurve.service.MessageThreadService;
import com.witcurve.service.dto.MessageDTO;
import com.witcurve.service.dto.MessageThreadDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.util.HeaderUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MessageThreadResource {

    private final Logger log = LoggerFactory.getLogger(MessageThreadResource.class);

    @Autowired
    MessageThreadService messageThreadService;

    /**
     * creates a MessageThread
     * @param messageThreadDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/message-thread")
    @Timed
    public ResponseEntity<MessageThreadDTO> createMessageThread(@RequestBody @Valid MessageThreadDTO messageThreadDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request Save MessageThread : {}"+messageThreadDTO);
        if (messageThreadDTO.getId() != null) {
            throw new WitcurveException("New MessageThread can't already have an id");
        }
        List<MessageDTO> messageDTOList = new ArrayList<>(messageThreadDTO.getMessageDTOs());
        if(messageDTOList.size() !=1) {
            throw new WitcurveException("New MessageThread should have only one message in this body");
        }
        if(messageDTOList.get(0).getId() != null) {
            throw new WitcurveException("New MessageThread can't already have an message with id");
        }
        MessageThreadDTO result = messageThreadService.saveOrUpdate(messageThreadDTO);
        return ResponseEntity.created(new URI("/api/message-thread/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("messageThread", result.getId().toString()))
            .body(result);
    }

    /**
     * get messageThread by id
     * @param messageThreadId
     * @return
     * @throws WitcurveException
     */
    @GetMapping("/message-thread/{messageThreadId}")
    @Timed
    public ResponseEntity<MessageThreadDTO> getMessageThreadById(@PathVariable("messageThreadId") Long messageThreadId) throws WitcurveException {
        log.debug("Request to get MessageThread with id {}", messageThreadId);
        MessageThreadDTO result = messageThreadService.getMessageThreadById(messageThreadId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * reply message
     * @param messageDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/message-thread/reply-message")
    @Timed
    public ResponseEntity<MessageThreadDTO> replyMessage(@RequestBody @Valid MessageDTO messageDTO) throws WitcurveException,URISyntaxException {
        log.debug("Request to reply Messages : {}"+messageDTO);
        if(messageDTO.getMessageThreadId()== null)
        {
            throw new WitcurveException("cannot reply to a message without a thread id");
        }
        if(messageThreadService.getMessageThreadById(messageDTO.getMessageThreadId()).getMessageType().equals(MessageType.SUBJECT_NOTE))
        {
            throw new WitcurveException("Cannot reply to a message with type SUBJECT_NOTE");
        }
        MessageThreadDTO result = messageThreadService.replyMessage(messageDTO);
        return ResponseEntity.created(new URI("/api/message-thread/reply-message" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("messageThread", result.getId().toString()))
            .body(result);
    }

    /**
     * meeting approval
     * @param threadId
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PatchMapping("/message-thread/thread-approval/{messageThreadId}")
    @Timed
    public ResponseEntity<Void> approveOrRejectStatus(@PathVariable("messageThreadId") Long threadId, @RequestParam(value = "staffId", required = false) Long staffId, @RequestParam(value = "status") ApprovalStatus status) throws WitcurveException,URISyntaxException {
        log.debug("Request to approve thread with id : {}" + threadId);
        messageThreadService.approveOrRejectMessageThread(threadId, staffId, status);
        return ResponseEntity.ok(null);
    }

    /**
     * read message
     * @param messageId
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PatchMapping("/message-thread/read/message/{messageId}")
    @Timed
    public ResponseEntity<Void> readMessage(@PathVariable("messageId") Long messageId) throws WitcurveException,URISyntaxException {
        log.debug("The message is read with message id : {}" + messageId);
        messageThreadService.readMessageService(messageId);
        return ResponseEntity.ok(null);
    }


    /**
     * get Inbox messageThreads for a user
     * @param userId
     * @return
     * @throws WitcurveException
     */
    @GetMapping("/message-thread/user/{userId}/inbox")
    @Timed
    public ResponseEntity<Page<MessageThreadDTO>> getInboxMessagesForUser(@ApiParam Pageable pageable,
                                                                          @PathVariable("userId") Long userId,
                                                                          @RequestParam MessageType type,
                                                                          @RequestParam(required = false)ApprovalStatus status,
                                                                          @RequestParam(required = false)Boolean read) throws WitcurveException {
        log.debug("Request to get MessageThreads for user with id {} of type : {} with status : {} and read : {}", userId, type, status, read);
        Page<MessageThreadDTO> result = messageThreadService.getInboxMessageThreadsByUserId(pageable, userId, type, status, read);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get Inbox messageThreads for a user
     * @param userId
     * @return
     * @throws WitcurveException
     */
    @GetMapping("/message-thread/user/{userId}/inbox/count")
    @Timed
    public ResponseEntity<Map<MessageType, Integer>> getUnReadInboxCount(@PathVariable("userId") Long userId) throws WitcurveException {
        log.debug("Request to get unread MessageThreads count for user with id {}", userId);
        Map<MessageType, Integer> result = messageThreadService.unReadCount(userId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get Outbox messageThreads for a user
     * @param userId
     * @return
     * @throws WitcurveException
     */
    @GetMapping("/message-thread/user/{userId}/outbox")
    @Timed
    public ResponseEntity<Page<MessageThreadDTO>> getOutboxMessagesForUser(@ApiParam Pageable pageable,
                                                                           @PathVariable("userId") Long userId,
                                                                          @RequestParam MessageType type,
                                                                          @RequestParam(required = false)ApprovalStatus status) throws WitcurveException {
        log.debug("Request to get MessageThreads for user with id {} of type : {} with status : {}", userId, type, status);
        Page<MessageThreadDTO> result = messageThreadService.getOutboxMessageThreadsByUserId(pageable, userId, type, status);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
