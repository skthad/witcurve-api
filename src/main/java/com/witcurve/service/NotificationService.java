package com.witcurve.service;

import com.witcurve.domain.Event;
import com.witcurve.domain.SchoolInfo;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface NotificationService {

    void sendAbsentNotification(SchoolInfo schoolInfo, Map<Long, Set<LocalDate>> studentIdAndDatesMap, Map<Long, Set<LocalDate>> staffIdAndDatesMap) throws UnsupportedEncodingException;

    void sendSchoolEventNotification(SchoolInfo schoolInfo, List<Event> schoolEvents) throws UnsupportedEncodingException;

    void sendLeaveApplicationSaveOrUpdateNotification(Long leaveApplication, Boolean update) throws UnsupportedEncodingException;

    void sendLeaveApplicationStatusNotification() throws UnsupportedEncodingException;

    void sendLeaveApplicationDeletionNotification() throws UnsupportedEncodingException;

}
