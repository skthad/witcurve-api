package com.witcurve.service;

import com.witcurve.service.dto.EventContentDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface EventContentService {

    List<EventContentDTO> saveOrUpdateForEvent(Long id, List<EventContentDTO> eventContentDTOs, Boolean forExam) throws WitcurveException;

    void deleteEventContentById(Long eventContentId) throws WitcurveException;

    void deleteEventContentByEventId(Long eventId, Boolean forExam) throws WitcurveException;
}

