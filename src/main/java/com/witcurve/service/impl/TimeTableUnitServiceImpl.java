package com.witcurve.service.impl;

import com.witcurve.domain.Student;
import com.witcurve.domain.TimeTableUnit;
import com.witcurve.repository.StudentRepository;
import com.witcurve.repository.TimetableUnitRepository;
import com.witcurve.service.TimeTableUnitService;
import com.witcurve.service.dto.TimeTableUnitDTO;
import com.witcurve.service.mapper.TimetableUnitMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeTableUnitServiceImpl implements TimeTableUnitService {

    private final Logger log = LoggerFactory.getLogger(TimeTableUnitServiceImpl.class);

    @Autowired
    TimetableUnitRepository timetableUnitRepository;

    @Autowired
    TimetableUnitMapper timetableUnitMapper;

    @Autowired
    StudentRepository studentRepository;

    @Override
    public TimeTableUnitDTO saveOrUpdate(TimeTableUnitDTO timetableUnitDTO) {
        log.debug("Request to save or update time table unit : {}", timetableUnitDTO);
        TimeTableUnit timeTableUnit = timetableUnitMapper.timetableUnitDTOToTimetableUnit(timetableUnitDTO);
        timeTableUnit = timetableUnitRepository.save(timeTableUnit);
        return timetableUnitMapper.timeTableUnitToTimeTableUnitDTO(timeTableUnit);
    }

    @Override
    public TimeTableUnitDTO getTimetableUnitById(Long timetableUnitId) throws WitcurveException {
        log.debug("Request to get time table unit with id : {}", timetableUnitId);
        TimeTableUnit timeTableUnit = timetableUnitRepository.findById(timetableUnitId).get();

        if (timeTableUnit ==  null) {
            throw new WitcurveException("No timetable unit with given id");
        }
        return timetableUnitMapper.timeTableUnitToTimeTableUnitDTO(timeTableUnit);
    }

    @Override
    public void deleteTimetableUnitById(Long timeTableUnitId) throws WitcurveException {
        log.debug("Request to delete time table unit with id : {}", timeTableUnitId);
        TimeTableUnit timeTableUnit = timetableUnitRepository.findById(timeTableUnitId).get();

        if (timeTableUnit ==  null) {
            throw new WitcurveException("No timetable unit with given id");
        }
        timetableUnitRepository.delete(timeTableUnit);
    }

    @Override
    public List<TimeTableUnitDTO> getAllTimeTableUnitsByStudentId(Long studentId) throws WitcurveException {
        Student student = studentRepository.findById(studentId).get();
        if (student == null) {
            throw new WitcurveException("No student exists with given id");
        }
        List<TimeTableUnit> timeTableUnitsForStudent = timetableUnitRepository.findByClassId(student.getStandard().getId());
        return timetableUnitMapper.timeTableUnitsToTimeTableUnitDTOs(timeTableUnitsForStudent);
    }


}
