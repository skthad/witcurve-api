package com.witcurve.service;

import com.witcurve.service.dto.MessageDTO;
import com.witcurve.service.dto.MessageThreadDTO;
import com.witcurve.web.rest.errors.WitcurveException;

public interface MessageThreadService {

    MessageThreadDTO saveOrUpdate(MessageThreadDTO messageThreadDTO) throws WitcurveException;

    MessageThreadDTO getMessageThreadById(Long messageThreadId) throws WitcurveException;

    MessageThreadDTO getMeetingApprover(Long threadId) throws WitcurveException;

    MessageDTO getMessageById(Long messageId) throws WitcurveException;
}
