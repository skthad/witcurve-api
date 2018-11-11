package com.witcurve.service;

import com.witcurve.service.dto.MessageThreadDTO;
import com.witcurve.web.rest.errors.WitcurveException;

public interface MessageThreadService {

    MessageThreadDTO saveOrUpdate(MessageThreadDTO messageThreadDTO) throws WitcurveException;

    MessageThreadDTO getMessageThreadyById(Long messageThreadId) throws WitcurveException;
}
