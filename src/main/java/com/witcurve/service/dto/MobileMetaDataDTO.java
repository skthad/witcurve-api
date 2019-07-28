package com.witcurve.service.dto;

import com.witcurve.domain.enumeration.MobileOsType;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class MobileMetaDataDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private String latest;

    @NotNull
    private String minimum;

    @NotNull
    private String url;

    @NotNull
    private Boolean enabled;

    @NotNull
    private MobileOsType type;

    @NotNull
    private Long instituteId;

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

    public MobileOsType getType() {
        return type;
    }

    public void setType(MobileOsType type) {
        this.type = type;
    }

    public Long getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(Long instituteId) {
        this.instituteId = instituteId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MobileMetaDataDTO that = (MobileMetaDataDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "MobileMetaDataDTO{" +
            "id=" + id +
            ", latest='" + latest + '\'' +
            ", minimum='" + minimum + '\'' +
            ", url='" + url + '\'' +
            ", enabled=" + enabled +
            ", type='" + type + '\'' +
            ", instituteId=" + instituteId +
            '}';
    }
}
