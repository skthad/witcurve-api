package com.witcurve.service;

import com.witcurve.service.dto.EventContentDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface EventContentService {

    List<EventContentDTO> saveOrUpdateForEvent(Long eventId, List<EventContentDTO> eventContentDTOs) throws WitcurveException;

    void deleteEventContentById(Long eventContentId) throws WitcurveException;

    void deleteEventContentByEventId(Long eventId) throws WitcurveException;
}

