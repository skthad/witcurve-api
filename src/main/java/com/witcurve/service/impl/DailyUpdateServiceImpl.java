package com.witcurve.service.impl;

import com.witcurve.domain.DailyUpdate;
import com.witcurve.repository.DailyUpdateRepository;
import com.witcurve.service.DailyUpdateService;
import com.witcurve.service.dto.DailyUpdateDTO;
import com.witcurve.service.mapper.DailyUpdateMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DailyUpdateServiceImpl implements DailyUpdateService {

    private final Logger log  = LoggerFactory.getLogger(DailyUpdateServiceImpl.class);

    @Autowired
    DailyUpdateRepository dailyUpdateRepository;

    @Autowired
    DailyUpdateMapper dailyUpdateMapper;


    @Override
    public DailyUpdateDTO saveOrUpdate(DailyUpdateDTO dailyUpdateDTO) {
        log.debug("Request to save or update daily update : {}", dailyUpdateDTO);
        DailyUpdate dailyUpdate = dailyUpdateMapper.dailyDTOToDailyUpdate(dailyUpdateDTO);
        dailyUpdate =  dailyUpdateRepository.save(dailyUpdate);
        return dailyUpdateMapper.dailyUpdateToDailyUpdateDTO(dailyUpdate);
    }

    @Override
    public DailyUpdateDTO getDailyUpdateById(Long id) throws WitcurveException {
        log.debug("Get daily update with id : {}", id);
        DailyUpdate dailyUpdate = dailyUpdateRepository.findById(id).get();

        if (dailyUpdate == null) {
            throw new WitcurveException("No Daily update with given id");
        }
        return dailyUpdateMapper.dailyUpdateToDailyUpdateDTO(dailyUpdate);
    }

    @Override
    public void deleteDailyUpdate(Long dailyUpdateId) throws WitcurveException {
        log.debug("Delete daily update with id : {}", dailyUpdateId);
        DailyUpdate dailyUpdate = dailyUpdateRepository.findById(dailyUpdateId).get();

        if (dailyUpdate == null){
            throw new WitcurveException("No Daily update with given id");
        }
        dailyUpdateRepository.delete(dailyUpdate);
    }
}
