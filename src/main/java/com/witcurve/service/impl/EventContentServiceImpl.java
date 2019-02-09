package com.witcurve.service.impl;

import com.witcurve.domain.Event;
import com.witcurve.domain.EventContent;
import com.witcurve.repository.EventContentRepository;
import com.witcurve.repository.EventRepository;
import com.witcurve.service.EventContentService;
import com.witcurve.service.dto.EventContentDTO;
import com.witcurve.service.mapper.EventContentMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EventContentServiceImpl implements EventContentService {

    private final Logger log  = LoggerFactory.getLogger(EventContentServiceImpl.class);

    @Autowired
    EventContentRepository eventContentRepository;

    @Autowired
    EventContentMapper eventContentMapper;

    @Autowired
    EventRepository eventRepository;

    @Override
    public List<EventContentDTO> saveOrUpdateForEvent(Long eventId, List<EventContentDTO> eventContentDTOs) throws WitcurveException {
        log.debug("Request to save or update EventContents");
        Optional<Event> event = eventRepository.findById(eventId);
        if (!event.isPresent()) {
            throw new WitcurveException("No Event with given id " + eventId);
        }
        for (EventContentDTO eventContentDTO : eventContentDTOs) {
            if (!eventContentDTO.getEventId().equals(eventId)) {
                throw new WitcurveException("EventId provided does not match with eventId in one or more eventContents");
            }
        }
        List<EventContent> eventContents  = eventContentMapper.toEntity(eventContentDTOs);
        return eventContentMapper.toDto(eventContentRepository.saveAll(eventContents));
    }

    @Override
    public void deleteEventContentById(Long eventContentId) throws WitcurveException {
        log.debug("Request to delete EventContent with id {}", eventContentId);
        Optional<EventContent> eventContent = eventContentRepository.findById(eventContentId);
        if (!eventContent.isPresent()) {
            throw new WitcurveException("No EventContent with given id " + eventContentId);
        }
        eventContentRepository.delete(eventContent.get());
    }

    @Override
    public void deleteEventContentByEventId(Long eventId) {
        log.debug("Request to delete CourseContents for event with id {}", eventId);
        eventContentRepository.deleteByEventId(eventId);
    }

}
