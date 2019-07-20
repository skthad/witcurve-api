package com.witcurve.service.dto;

import com.witcurve.domain.enumeration.MobileOsType;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class UserMobileEndPointDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    private MobileOsType type;

    @NotNull
    private String deviceToken;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public MobileOsType getType() {
        return type;
    }

    public void setType(MobileOsType type) {
        this.type = type;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserMobileEndPointDTO that = (UserMobileEndPointDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserMobileEndPointDTO{" +
            "id=" + id +
            '}';
    }
}
