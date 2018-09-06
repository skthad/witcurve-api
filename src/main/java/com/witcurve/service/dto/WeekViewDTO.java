package com.witcurve.service.dto;

import java.io.Serializable;
import java.util.List;

public class WeekViewDTO implements Serializable {

    private List<GeneralSlotDetailsDTO> gsdList;

    private List<SlotCourseDetailsDTO> scdList;

    private List<EventDTO> eventList;

    public List<GeneralSlotDetailsDTO> getGsdList() {
        return gsdList;
    }

    public void setGsdList(List<GeneralSlotDetailsDTO> gsdList) {
        this.gsdList = gsdList;
    }

    public List<SlotCourseDetailsDTO> getScdList() {
        return scdList;
    }

    public void setScdList(List<SlotCourseDetailsDTO> scdList) {
        this.scdList = scdList;
    }

    public List<EventDTO> getEventList() {
        return eventList;
    }

    public void setEventList(List<EventDTO> eventList) {
        this.eventList = eventList;
    }

    @Override
    public String toString() {
        return "WeekViewDTO{" +
            "gsdList=" + gsdList +
            ", scdList=" + scdList +
            ", eventList=" + eventList +
            '}';
    }
}
