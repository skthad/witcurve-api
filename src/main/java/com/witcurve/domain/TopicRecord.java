package com.witcurve.domain;

import com.witcurve.domain.enumeration.TopicType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "topic_record")
public class TopicRecord extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TopicType type;

    @NotNull
    @Column(nullable = false)
    private String topicEndPoint;

    @OneToOne
    @JoinColumn(unique = true)
    private SchoolInfo schoolInfo;

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

    public SchoolInfo getSchoolInfo() {
        return schoolInfo;
    }

    public void setSchoolInfo(SchoolInfo schoolInfo) {
        this.schoolInfo = schoolInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TopicRecord that = (TopicRecord) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TopicRecord{" +
            "id=" + id +
            '}';
    }
}
