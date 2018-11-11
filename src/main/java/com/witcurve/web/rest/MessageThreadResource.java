package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.Message;
import com.witcurve.domain.MessageThread;
import com.witcurve.service.MessageThreadService;
import com.witcurve.service.dto.MessageDTO;
import com.witcurve.service.dto.MessageThreadDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

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
     * reply message
     * @param messageDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/message-thread/reply-message")
    @Timed
    public ResponseEntity<MessageThreadDTO> replyMessage(@PathVariable("messageDTO") MessageDTO messageDTO) throws WitcurveException,URISyntaxException {
        log.debug("Request to reply Messages : {}"+messageDTO);
        if(messageDTO.getMessageThreadId()== null)
        {
            throw new WitcurveException("cannot reply to a message without a thread id");
        }
        MessageThreadDTO messageThreadDTO = messageThreadService.getMessageThreadyById(messageDTO.getMessageThreadId());;
        messageThreadDTO.setMessageDTOs(messageDTO);
        return ResponseEntity.created(new URI("/api/message-thread/reply-message" + result.getId()))
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
        MessageThreadDTO result = messageThreadService.getMessageThreadyById(messageThreadId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}
