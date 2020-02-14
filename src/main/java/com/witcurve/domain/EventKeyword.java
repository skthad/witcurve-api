package com.witcurve.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="event_keyword", uniqueConstraints = {
    @UniqueConstraint( name= "event_id_UK",
        columnNames = {"event_id", "keyword_name"})
})
public class EventKeyword implements Serializable{

    @Id
    @NotNull
    @Column(name="event_id",nullable = false)
    private Long eventId;

    @Id
    @NotNull
    @Column(name="keyword_name",nullable = false)
    private String keywordName;

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getKeywordName() {
        return keywordName;
    }

    public void setKeywordName(String keywordName) {
        this.keywordName = keywordName;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventKeyword that = (EventKeyword) o;
        return Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }

    @Override
    public String toString() {
        return "EventKeyword{" +
            ", eventId=" + eventId +
            ", keywordName='" + keywordName + '\'' +
            '}';
    }
}
