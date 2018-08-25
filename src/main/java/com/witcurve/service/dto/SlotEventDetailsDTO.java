package com.witcurve.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class SlotEventDetailsDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private Long eventDTO;

    private Long scdDTO;

    private String bindingId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventDTO() {
        return eventDTO;
    }

    public void setEventDTO(Long eventDTO) {
        this.eventDTO = eventDTO;
    }

    public Long getScdDTO() {
        return scdDTO;
    }

    public void setScdDTO(Long scdDTO) {
        this.scdDTO = scdDTO;
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
            ", eventDTO=" + eventDTO +
            ", scdDTO=" + scdDTO +
            ", bindingId='" + bindingId + '\'' +
            '}';
    }
}
