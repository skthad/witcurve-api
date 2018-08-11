package com.witcurve.service.impl;

import com.witcurve.domain.Holiday;
import com.witcurve.repository.HolidaysRepository;
import com.witcurve.service.HolidayService;
import com.witcurve.service.dto.HolidayDTO;
import com.witcurve.service.mapper.HolidayMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HolidayServiceImpl implements HolidayService {

    private final Logger log  = LoggerFactory.getLogger(HolidayServiceImpl.class);

    @Autowired
    HolidaysRepository holidaysRepository;

    @Autowired
    HolidayMapper holidayMapper;


    @Override
    public HolidayDTO saveOrUpdate(HolidayDTO holidayDTO) {
        log.debug("Request to save or update holiday : {}", holidayDTO);
        Holiday holiday = holidayMapper.holidayDTOToHoliday(holidayDTO);
        holiday = holidaysRepository.save(holiday);
        return holidayMapper.holidayToHolidayDTO(holiday);
    }

    @Override
    public HolidayDTO getHolidayById(Long holidayId) throws WitcurveException {
        log.debug("Request to get holiday with id : {}", holidayId);
        Holiday holiday = holidaysRepository.findById(holidayId).get();

        if (holiday == null) {
           throw new WitcurveException("No Holiday with given id");
        }
        return holidayMapper.holidayToHolidayDTO(holiday);
    }

    @Override
    public void deleteHoliday(Long holidayId) throws WitcurveException {
        log.debug("Request to delete holiday with id : {}", holidayId);
        Holiday holiday = holidaysRepository.findById(holidayId).get();

        if (holiday == null){
            throw new WitcurveException("No holiday with given id");
        }

        holidaysRepository.delete(holiday);
    }
}
