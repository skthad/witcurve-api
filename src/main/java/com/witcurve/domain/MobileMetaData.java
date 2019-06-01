package com.witcurve.domain;

import com.witcurve.domain.enumeration.MobileMetaDataStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="mobile_meta_data", uniqueConstraints = {
    @UniqueConstraint(name = "type_institute_UK",
        columnNames = {"type", "institute_id"})})
public class MobileMetaData extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String latest;

    @NotNull
    @Column(nullable = false)
    private String minimum;

    @NotNull
    @Column(nullable = false)
    private String url;

    @NotNull
    @Column(nullable = false)
    private Boolean enabled;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MobileMetaDataStatus type;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Institute institute;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLatest() {
        return latest;
    }

    public void setLatest(String latest) {
        this.latest = latest;
    }

    public String getMinimum() {
        return minimum;
    }

    public void setMinimum(String minimum) {
        this.minimum = minimum;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public MobileMetaDataStatus getType() {
        return type;
    }

    public void setType(MobileMetaDataStatus type) {
        this.type = type;
    }

    public Institute getInstitute() {
        return institute;
    }

    public void setInstitute(Institute institute) {
        this.institute = institute;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MobileMetaData that = (MobileMetaData) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "MobileMetaData{" +
            "id=" + id +
            ", latest='" + latest + '\'' +
            ", minimum='" + minimum + '\'' +
            ", url='" + url + '\'' +
            ", enabled=" + enabled +
            ", type='" + type + '\'' +
            ", institute=" + institute +
            '}';
    }
}
