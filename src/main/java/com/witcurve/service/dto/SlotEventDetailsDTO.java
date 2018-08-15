package com.witcurve.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class SlotEventDetailsDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private Long eventId;

    @NotNull
    private Long scdId;

    private String bindingId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    public Long getEventId() {
        return eventId;
    }

    public void setEventId(@NotNull Long eventId) {
        this.eventId = eventId;
    }

    @NotNull
    public Long getScdId() {
        return scdId;
    }

    public void setScdId(@NotNull Long scdId) {
        this.scdId = scdId;
    }

    public String getBindingId() {
        return bindingId;
    }

    public void setBindingId(String bindingId) {
        this.bindingId = bindingId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SlotEventDetailsDTO that = (SlotEventDetailsDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SlotEventDetailsDTO{" +
            "id=" + id +
            ", eventId=" + eventId +
            ", scdId=" + scdId +
            ", bindingId='" + bindingId + '\'' +
            '}';
    }
}
