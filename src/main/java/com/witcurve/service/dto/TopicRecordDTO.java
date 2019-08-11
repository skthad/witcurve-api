package com.witcurve.service.dto;

import com.witcurve.domain.enumeration.TopicType;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class TopicRecordDTO  extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private TopicType type;

    @NotNull
    private String topicEndPoint;

    private Long schoolInfoId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TopicType getType() {
        return type;
    }

    public void setType(TopicType type) {
        this.type = type;
    }

    public String getTopicEndPoint() {
        return topicEndPoint;
    }

    public void setTopicEndPoint(String topicEndPoint) {
        this.topicEndPoint = topicEndPoint;
    }

    public Long getSchoolInfoId() {
        return schoolInfoId;
    }

    public void setSchoolInfoId(Long schoolInfoId) {
        this.schoolInfoId = schoolInfoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TopicRecordDTO that = (TopicRecordDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TopicRecordDTO{" +
            "id=" + id +
            '}';
    }
}

